package com.togrulseyid.funnyvideos.models;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoListModel extends CoreModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<VideoModel> videos;

	public ArrayList<VideoModel> getVideos() {
		return videos;
	}

	public void setVideos(ArrayList<VideoModel> videos) {
		this.videos = videos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "VideoListModel [videos=" + videos + "]";
	}

}