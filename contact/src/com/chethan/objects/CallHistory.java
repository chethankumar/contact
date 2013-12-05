package com.chethan.objects;

import java.sql.Date;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class CallHistory implements Parcelable {

	private String id;
	private String name;
	private String phoneNumber;
	private Date date;
	private int type;
	private Uri photo;

	public CallHistory() {
		super();
	}

	public CallHistory(Parcel source) {
		id = source.readString();
		name = source.readString();
		phoneNumber = source.readString();
		date = new Date(source.readLong());
		type = source.readInt();
		String photoString = source.readString();
		photo = photoString != null ? Uri.parse(photoString) : null;
	}

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

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(phoneNumber);
		dest.writeLong(date.getTime());
		dest.writeInt(type);
		dest.writeString(photo != null ? photo.toString() : "null");
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public CallHistory createFromParcel(Parcel source) {
			return new CallHistory(source);
		}

		@Override
		public CallHistory[] newArray(int size) {
			return new CallHistory[size];
		}

	};
}
