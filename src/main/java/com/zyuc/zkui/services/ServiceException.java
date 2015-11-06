package com.zyuc.zkui.services;

/**
 * 
 * @author xuechongyang
 * 
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -6725923101721017868L;

	public ServiceException(String msg) {
		super(msg);
	}
	
	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ServiceException() {
		super();
	}
}
