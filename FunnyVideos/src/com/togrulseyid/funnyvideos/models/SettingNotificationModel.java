package com.togrulseyid.funnyvideos.models;

public class SettingNotificationModel extends GCMModel {

	private boolean ntf;
//
//	/**
//	 * @return the ntf
//	 */
//	public int getNtf() {
//		return ntf;
//	}
//
//	/**
//	 * @param ntf the ntf to set
//	 */
//	public void setNtf(int ntf) {
//		this.ntf = ntf;
//	}
//
//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		return "SettingNotificationModel [ntf=" + ntf + "]";
//	}

	/**
	 * @return the ntf
	 */
	public boolean isNtf() {
		return ntf;
	}

	/**
	 * @param ntf the ntf to set
	 */
	public void setNtf(boolean ntf) {
		this.ntf = ntf;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SettingNotificationModel [ntf=" + ntf + "]";
	}

}