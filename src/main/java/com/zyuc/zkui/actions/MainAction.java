package com.zyuc.zkui.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zyuc.zkui.model.TreeNode;
import com.zyuc.zkui.services.ServiceException;
import com.zyuc.zkui.services.ZkService;
import com.zyuc.zkui.utils.MsgUtils;

/**
 * 
 * @author xuechongyang
 * 
 */
@Controller
public class MainAction {
	Logger log = Logger.getLogger(MainAction.class);
	private static String ROOT = "";
	private int autoIncrementId = 0;

	@Autowired
	private ZkService zkService;
	
	@RequestMapping("/")
	public String index(ModelMap map) {
		String rootName = zkService.getRoot();
		if(!ROOT.equals(rootName)) {
			ROOT = rootName;
		}
		try{
			TreeNode root = new TreeNode(true);
			root.setId(0);
			root.setName(ROOT);
			root.setLeaf(false);
			autoIncrementId = 0;
			buildTree(root, ROOT);
			map.put("tree", root);
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return "/zookeeper/main";
	}

	protected TreeNode buildTree(TreeNode root, String parent) {
		List<String> children = zkService.getChildren(parent);
		if(autoIncrementId < 10) {
			root.setOpen(true);
		}
		if(children != null && children.size() > 0){
			root.setLeaf(false);
			if(parent.equals("/")) {
				parent = "";
			}
			for (String child : children) {
				TreeNode node = new TreeNode();
				node.setName(child);
				node.setDescription(parent + "/" + child);
				node.setId(autoIncrementId ++);
				node.setParent(root.getId());
				root.getChildren().add(node);
				buildTree(node, parent + "/" + child);
			}
		}else{
			root.setLeaf(true);
		}
		return root;
	}

	@RequestMapping("remove")
	public String remove(String path, HttpServletRequest req) {
		try {
			zkService.delete(path);
		} catch (Exception ex) {
			MsgUtils.push(ex, req);
		}
		return "/common/common-ack";
	}

	@RequestMapping("/edit")
	public String edit(String path, String data, HttpServletRequest req, ModelMap map) {
		map.put("path", path);
		if (RequestMethod.valueOf(req.getMethod()) == RequestMethod.GET) {
			try {
				data = zkService.data(path);
				map.put("data", data);
			} catch (ServiceException ex) {
				MsgUtils.push(ex, req);
			}
			return "/zookeeper/edit";
		} else {
			try {
				zkService.update(path, data);
				
			} catch (Exception ex) {
				MsgUtils.push(ex, req);
			}
			return "/common/common-ack";
		}
	}

	@RequestMapping("/add")
	public String create(String parent, String path, String data, String auth,
			String mode, HttpServletRequest req, ModelMap map) {
		
		if (RequestMethod.valueOf(req.getMethod()) == RequestMethod.GET) {
			map.put("parent", parent == null ? ROOT : parent);
			return "/zookeeper/add";
		} else {
			try {
				zkService.create(parent + "/" + path, data,	auth, CreateMode.valueOf(mode));
			} catch (Exception ex) {
				MsgUtils.push(ex, req);
			}
			return "/common/common-ack";
		}
	}
}
