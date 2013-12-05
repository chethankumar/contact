package com.chethan.contact;

import java.util.ArrayList;

import android.app.Application;

import com.chethan.objects.CallHistory;
import com.chethan.objects.SimpleContact;

public class ContactApplication extends Application {

	private ArrayList<CallHistory> callHistories = new ArrayList<CallHistory>();
	private ArrayList<SimpleContact> simpleContactList = new ArrayList<SimpleContact>();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public ArrayList<CallHistory> getCallHistories() {
		return callHistories;
	}

	public void setCallHistories(ArrayList<CallHistory> callHistories) {
		this.callHistories = callHistories;
	}

	public ArrayList<SimpleContact> getSimpleContactList() {
		return simpleContactList;
	}

	public void setSimpleContactList(ArrayList<SimpleContact> simpleContactList) {
		this.simpleContactList = simpleContactList;
	}

}
