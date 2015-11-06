package com.zyuc.zkui.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class MsgUtils {

	public static void push(Exception ex, HttpServletRequest req) {
		List<String> list = (List<String>) req.getAttribute(Constants.MSG.ACTION_ERROR);
		if (list == null) {
			list = new ArrayList<String>();
			req.setAttribute(Constants.MSG.ACTION_ERROR, list);
		}
		if (ex.getMessage() != null) {

			list.add(ex.getMessage());

		} else {
			list.add("请稍候重试！");
		}
	}

	public static void push(String ex, HttpServletRequest req) {
		List<String> list = (List<String>) req.getAttribute(Constants.MSG.ACTION_ERROR);
		if (list == null) {
			list = new ArrayList<String>();
			req.setAttribute(Constants.MSG.ACTION_ERROR, list);
		}
		list.add(ex);
	}

	public static void push(String field, String msg, HttpServletRequest req) {
		Map<String, String> map = (Map<String, String>) req.getAttribute(Constants.MSG.FIELDS_ERROR);
		if (map == null) {
			map = new HashMap<String, String>();
			req.setAttribute(Constants.MSG.FIELDS_ERROR, map);
		}
		map.put(field, msg);
	}
}
