package com.zyuc.zkui.actions;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController{

	private static final String ERROR_PATH = "/error"; 
	
	@RequestMapping(value=ERROR_PATH)
	public String error() {
		return "redirect:/";
	}
	
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
}
