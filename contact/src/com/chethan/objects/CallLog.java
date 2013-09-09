package com.chethan.objects;

import java.sql.Date;

import android.net.Uri;

public class CallLog {

	private String id;
	private String name;
	private String phoneNumber;
	private Date date;
	private int type;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Uri getPhoto() {
		return photo;
	}

	public void setPhoto(Uri photo) {
		this.photo = photo;
	}

}
