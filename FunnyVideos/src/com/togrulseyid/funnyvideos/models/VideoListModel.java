package com.togrulseyid.funnyvideos.models;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class VideoListModel extends CoreModel implements Serializable {

	private Integer startId;
	private Integer maxCount;
	private Integer gcm_id;
	private ArrayList<VideoModel> videos;
	/**
	 * @return the startId
	 */
	public Integer getStartId() {
		return startId;
	}
	/**
	 * @param startId the startId to set
	 */
	public void setStartId(Integer startId) {
		this.startId = startId;
	}
	/**
	 * @return the maxCount
	 */
	public Integer getMaxCount() {
		return maxCount;
	}
	/**
	 * @param maxCount the maxCount to set
	 */
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	/**
	 * @return the gcm_id
	 */
	public Integer getGcm_id() {
		return gcm_id;
	}
	/**
	 * @param gcm_id the gcm_id to set
	 */
	public void setGcm_id(Integer gcm_id) {
		this.gcm_id = gcm_id;
	}
	/**
	 * @return the videos
	 */
	public ArrayList<VideoModel> getVideos() {
		return videos;
	}
	/**
	 * @param videos the videos to set
	 */
	public void setVideos(ArrayList<VideoModel> videos) {
		this.videos = videos;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoListModel [startId=" + startId + ", maxCount=" + maxCount
				+ ", gcm_id=" + gcm_id + ", videos=" + videos + "]";
	}

}