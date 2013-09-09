package com.chethan.objects;

import java.util.ArrayList;
import java.util.Hashtable;

import android.net.Uri;

public class Person {

	private String id;
	private String name;
	private ArrayList<String> phoneNumbers = new ArrayList<String>();
	private Hashtable<String, String> emails = new Hashtable<String, String>();
	private Hashtable<String, String> organization = new Hashtable<String, String>();
	private String title;
	private String tagline;
	private String place;
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

	public ArrayList<String> getPhoneNumber() {
		return phoneNumbers;
	}

	public void setPhoneNumber(ArrayList<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Hashtable<String, String> getEmail() {
		return emails;
	}

	public void setEmail(Hashtable<String, String> email) {
		this.emails = email;
	}

	public Hashtable<String, String> getOrganization() {
		return organization;
	}

	public void setOrganization(Hashtable<String, String> organization) {
		this.organization = organization;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Uri getPhoto() {
		return photo;
	}

	public void setPhoto(Uri photo) {
		this.photo = photo;
	}

	public void addPhoneNumber(String number) {
		this.phoneNumbers.add(number);
	}

	public void addEmail(String type, String email) {
		if (!emails.containsKey(email)) {
			this.emails.put(email, type);// email is used as key rather than
											// value as it is unique
		}
	}
	
	public void addOrganization(String title, String org) {
		if (!organization.containsKey(org)) {
			organization.put(org, title);
		}
	}

	public void clearDetails() {
		this.id = null;
		this.name = null;
		this.phoneNumbers = new ArrayList<String>();
		this.emails = new Hashtable<String, String>();
		this.organization = null;
		this.title = null;
		this.tagline = null;
		this.place = null;
		this.photo = null;
	}
}
