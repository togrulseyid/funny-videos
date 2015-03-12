package com.togrulseyid.funnyvideos.models;

import java.io.Serializable;

public class CoreModel implements Serializable{

	/**
	 * Default Serializable Id
	 */
	private static final long serialVersionUID = 1L;
	private Integer coreId;
	private String message;
	private Integer messageId;
	private Integer appVersion;
	private String sysLang;
	private boolean ads;
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
	/**
	 * @return the ads
	 */
	public boolean isAds() {
		return ads;
	}
	/**
	 * @param ads the ads to set
	 */
	public void setAds(boolean ads) {
		this.ads = ads;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoreModel [coreId=" + coreId + ", message=" + message
				+ ", messageId=" + messageId + ", appVersion=" + appVersion
				+ ", sysLang=" + sysLang + ", ads=" + ads + "]";
	}


}