package com.chethan.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class SimpleContact implements Comparable<SimpleContact>, Parcelable {

	private String id;
	private String name;
	private Uri photo;

	public SimpleContact(Parcel source) {
		id = source.readString();
		name = source.readString();
		String photoString = source.readString();
		photo = photoString != null ? Uri.parse(photoString) : null;
	}

	public SimpleContact() {
		super();
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

	public Uri getPhoto() {
		return photo;
	}

	public void setPhoto(Uri photo) {
		this.photo = photo;
	}

	@Override
	public int compareTo(SimpleContact arg0) {
		if (arg0.name != null && this.name != null) {
			String arg1 = arg0.getName();
			return this.name.compareTo(arg1);
		}
		return -1;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(photo != null ? photo.toString() : "null");
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public SimpleContact createFromParcel(Parcel source) {
			return new SimpleContact(source);
		}

		@Override
		public SimpleContact[] newArray(int size) {
			return new SimpleContact[size];
		}

	};

}
