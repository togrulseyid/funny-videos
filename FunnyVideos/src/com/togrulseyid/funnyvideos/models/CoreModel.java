package com.togrulseyid.funnyvideos.models;

public class CoreModel {

	private Integer startId;
	private Integer maxCount;
	private String message;
	private Integer messageId;
	private Integer appVersion;
	private Integer gcm_id;
	private String sysLang;

	public Integer getStartId() {
		return startId;
	}

	public void setStartId(Integer startId) {
		this.startId = startId;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer endId) {
		this.maxCount = endId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Integer appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return the gcm_id
	 */
	public Integer getGcm_id() {
		return gcm_id;
	}

	/**
	 * @param gcm_id
	 *            the gcm_id to set
	 */
	public void setGcm_id(Integer gcm_id) {
		this.gcm_id = gcm_id;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoreModel [startId=" + startId + ", maxCount=" + maxCount
				+ ", message=" + message + ", messageId=" + messageId
				+ ", appVersion=" + appVersion + ", gcm_id=" + gcm_id
				+ ", sysLang=" + sysLang + "]";
	}

}