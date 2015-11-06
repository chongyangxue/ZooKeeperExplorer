package com.zyuc.zkui.services.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zyuc.zkui.services.ServiceException;
import com.zyuc.zkui.services.ZkService;
import com.zyuc.zkui.utils.ToStringUtils;

/**
 * 
 * @author xuechongyang
 * 
 */
@Service
public class ZkServiceImpl implements BeanFactoryPostProcessor, ZkService, PriorityOrdered {

	private static final Logger log = Logger.getLogger(ZkServiceImpl.class);

	private ZooKeeper client;

	private final static Lock lock = new ReentrantLock();

	private static boolean connected = false;

	private static String root = "";

	private static int timeout = 1000;

	private static String nodes = "";

	private static List<Map<String, String>> users = null;
	
	@SuppressWarnings("unchecked")
	public void init() {
		String path = "conf/zk.json";
		try {
			InputStream in = new FileInputStream(path);
 			String json = ToStringUtils.Input2Str(in, null);
			Map<String, Object> zkMap = JSON.parseObject(json, Map.class);
			nodes = (String)zkMap.get("zk.url");
			timeout = (Integer) zkMap.get("zk.timeout");
			users = (List<Map<String, String>>) zkMap.get("zk.auth");
			root = (String) zkMap.get("zk.root");
			reconnect();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reconnect() throws IOException {
		lock.lock();
		try {
			if (connected) {
				log.info("zk already connected. init() do nothing and return.");
				return;
			}
			final AtomicInteger count = new AtomicInteger(1);
			client = new ZooKeeper(nodes, timeout, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (KeeperState.SyncConnected.equals(event.getState())) {
						count.set(0);
					} else if (KeeperState.Expired.equals(event.getState())) {
						connected = false;
						try {
							client.close();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						log.warn("Session of Zookeeper is Expired,is restarting");
						try {
							reconnect();
						} catch (IOException e) {
							e.printStackTrace();
						}
						log.warn("Zookeeper is restarted.");
					}
				}
			});
			for (Map<String, String> user : users) {
				client.addAuthInfo("digest", (user.get("user") + ":" + user.get("password")).getBytes());
			}

			while (true) {
				if (count.intValue() == 0) {
					connected = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// log.error(e.getMessage());
			connected = false;
			throw e;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean exists(String path) throws Exception {
		return client.exists(path, false) != null;
	}

	@Override
	public String create(String path, String data, String auth, CreateMode mode) throws ServiceException {

		if (!path.startsWith(root)) {
			path = root + path;
		}

		List<ACL> acl;
		if (auth.equals("CREATOR_ALL_ACL")) {
			acl = Ids.CREATOR_ALL_ACL;
		} else if (auth.equals("OPEN_ACL_UNSAFE")) {
			acl = Ids.OPEN_ACL_UNSAFE;
		} else {
			acl = Ids.READ_ACL_UNSAFE;
		}

		try {
			byte[] bytes = null;
			if(data != null)
				bytes = data.getBytes("UTF-8");
			String resultPath = client.create(path, bytes, acl, mode);
			return resultPath;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String data(String path) throws ServiceException {

		if (!path.startsWith(root)) {
			path = root + path;
		}

		try {
			return new String(client.getData(path, false, null), "utf-8");
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public void delete(String path) throws ServiceException {
		if (!path.startsWith(root)) {
			path = root + path;
		}
		List<String> children = getChildren(path);
		if (children != null && children.size() > 0) {
			for (String child : children) {
				delete(path + "/" + child);
			}
		}
		try {
			client.delete(path, -1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<String> getChildren(String path) {

		if (!path.startsWith(root)) {
			path = root + path;
		}

		try {
			return client.getChildren(path, false);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void update(String path, String data) throws ServiceException {

		if (!path.startsWith(root)) {
			path = root + path;
		}

		try {
			client.setData(path, data.getBytes("UTF-8"), -1);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Override
	public String getRoot(){
		return root;
	}


	@Override
	public Properties getProps(String path) {
		if (!path.startsWith(root)) {
			path = root + path;
		}
		Properties prop = new Properties();
		try {
			byte[] domains = client.getData(path, false, null);
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(domains));
			prop.load(dis);
			dis.close();
		} catch (Exception ex) {
			log.error(ex);
		}
		return prop;
	}

	@Override
	public int getOrder() {

		return 1;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		init();
	}

}
