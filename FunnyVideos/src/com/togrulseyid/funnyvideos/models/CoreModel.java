package com.togrulseyid.funnyvideos.models;

public class CoreModel {

	private Integer coreId;
	private String message;
	private Integer messageId;
	private Integer appVersion;
	private String sysLang;
	/**
	 * @return the coreId
	 */
	public Integer getCoreId() {
		return coreId;
	}
	/**
	 * @param coreId the coreId to set
	 */
	public void setCoreId(Integer coreId) {
		this.coreId = coreId;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
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
	 * @param messageId the messageId to set
	 */
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the appVersion
	 */
	public Integer getAppVersion() {
		return appVersion;
	}
	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(Integer appVersion) {
		this.appVersion = appVersion;
	}
	/**
	 * @return the sysLang
	 */
	public String getSysLang() {
		return sysLang;
	}
	/**
	 * @param sysLang the sysLang to set
	 */
	public void setSysLang(String sysLang) {
		this.sysLang = sysLang;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoreModel [coreId=" + coreId + ", message=" + message
				+ ", messageId=" + messageId + ", appVersion=" + appVersion
				+ ", sysLang=" + sysLang + "]";
	}

}