package com.zyuc.zkui.services;

import java.util.List;
import java.util.Properties;

import org.apache.zookeeper.CreateMode;

/**
 * 
 * @author xuechongyang
 * 
 */
public interface ZkService {

	boolean exists(String path) throws Exception;

	/**
	 * get children under the path given
	 * 
	 * @param path
	 * @return
	 * @throws ServiceException
	 */
	List<String> getChildren(String path) throws ServiceException;

	/**
	 * delete cascade
	 * 
	 * @param path
	 * @throws ServiceException
	 */
	void delete(String path) throws ServiceException;

	void update(String path, String data) throws ServiceException;

	String create(String path, String data, String auth, CreateMode mode)
			throws ServiceException;

	String data(String path) throws ServiceException;

	Properties getProps(String path);

	String getRoot();

}
