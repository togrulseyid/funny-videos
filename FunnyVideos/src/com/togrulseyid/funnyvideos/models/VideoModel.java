package com.togrulseyid.funnyvideos.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VideoModel implements Serializable {

	private String title;
	private String src;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public String toString() {
		return "VideoModel [title=" + title + ", src=" + src + "]";
	}

}