package com.togrulseyid.funnyvideos.models;

@SuppressWarnings("serial")
public class GCMModel extends CoreModel {

	private String gcm;
	private String imei;
//	private String email;
	/**
	 * @return the gcm
	 */
	public String getGcm() {
		return gcm;
	}
	/**
	 * @param gcm the gcm to set
	 */
	public void setGcm(String gcm) {
		this.gcm = gcm;
	}
	/**
	 * @return the iemi
	 */
	public String getImei() {
		return imei;
	}
	/**
	 * @param iemi the iemi to set
	 */
	public void setImei(String iemi) {
		this.imei = iemi;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GCMModel [gcm=" + gcm + ", iemi=" + imei + "]";
	}

}
