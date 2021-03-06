package com.togrulseyid.funnyvideos.models;

import java.io.Serializable;

public class LocalSettingModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean saveSession;
	private boolean notification;
	private boolean vibrate;

	public boolean isSaveSession() {
		return saveSession;
	}

	public void setSaveSession(boolean saveSession) {
		this.saveSession = saveSession;
	}

	public boolean isNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	public boolean isVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalSettingModel [saveSession=" + saveSession
				+ ", notification=" + notification + ", vibrate=" + vibrate
				+ "]";
	}

}
