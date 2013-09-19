package com.chethan.objects;

import java.util.Comparator;

import android.net.Uri;

public class SimpleContact implements Comparable<SimpleContact> {

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

	@Override
	public int compareTo(SimpleContact arg0) {
		if(arg0.name!=null && this.name!=null){
			String arg1 = arg0.getName();
			return 	this.name.compareTo(arg1);
		}
		return -1;
	}
	
}
