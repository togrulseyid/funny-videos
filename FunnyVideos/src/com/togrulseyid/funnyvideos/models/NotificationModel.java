package com.togrulseyid.funnyvideos.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message;
	private Integer data;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the data
	 */
	public Integer getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Integer data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NotificationModel [message=" + message + ", data=" + data + "]";
	}

}
