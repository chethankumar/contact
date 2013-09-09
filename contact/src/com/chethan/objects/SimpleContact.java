package com.chethan.objects;

import android.net.Uri;

public class SimpleContact {

	private String id;
	private String name;
	private Uri photo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Uri getPhoto() {
		return photo;
	}

	public void setPhoto(Uri photo) {
		this.photo = photo;
	}

}
