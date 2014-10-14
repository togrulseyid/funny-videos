package com.togrulseyid.funnyvideos.models;

public class GCMModel {

	private String gcm;
	private String message;
	private Integer messageId;
	private String sysLang;
	private Integer appVersion;

	/**
	 * @return the gcm
	 */
	public String getGcm() {
		return gcm;
	}

	/**
	 * @param gcm
	 *            the gcm to set
	 */
	public void setGcm(String gcm) {
		this.gcm = gcm;
	}

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
	 * @return the messageId
	 */
	public Integer getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the sysLang
	 */
	public String getSysLang() {
		return sysLang;
	}

	/**
	 * @param sysLang
	 *            the sysLang to set
	 */
	public void setSysLang(String sysLang) {
		this.sysLang = sysLang;
	}

	/**
	 * @return the appVersion
	 */
	public Integer getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion
	 *            the appVersion to set
	 */
	public void setAppVersion(Integer appVersion) {
		this.appVersion = appVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GCMModel [gcm=" + gcm + ", message=" + message + ", messageId="
				+ messageId + ", sysLang=" + sysLang + ", appVersion="
				+ appVersion + "]";
	}

}
