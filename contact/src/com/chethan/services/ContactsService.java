package com.chethan.services;

import java.sql.Date;
import java.util.ArrayList;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import com.chethan.objects.CallHistory;
import com.chethan.objects.SimpleContact;

public class ContactsService extends Service {

	ArrayList<SimpleContact> simpleContactList = new ArrayList<SimpleContact>();
	ArrayList<CallHistory> callHistoryList = new ArrayList<CallHistory>();
	ArrayList<SimpleContact> callHistorySimpleContactList = new ArrayList<SimpleContact>();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("contact", "inside onstartCommand");
		new GetContactAndHistoryDetails().execute();
		return START_STICKY;
	}

	class GetContactAndHistoryDetails extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("contact", "inside doInBackground");
			simpleContactList.clear();
			callHistoryList.clear();
			callHistorySimpleContactList.clear();

			ContentResolver cr = getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					SimpleContact contact = new SimpleContact();

					if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0
							&& cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) != null
							&& cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) != "") {
						contact.setName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
						contact.setId(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));
						if (cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)) != null) {
							contact.setPhoto(Uri.parse(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))));
						}
						simpleContactList.add(contact);
					}
				}
			}
			// Collections.sort(simpleContactList);

			Uri allCalls = Uri.parse("content://call_log/calls");
			String strOrder = android.provider.CallLog.Calls.DATE + " DESC" + " limit " + 99;
			Cursor c = getContentResolver().query(allCalls, null, null, null, strOrder);

			int number = c.getColumnIndex(CallLog.Calls.NUMBER);
			int type = c.getColumnIndex(CallLog.Calls.TYPE);
			int date = c.getColumnIndex(CallLog.Calls.DATE);

			if (c.moveToFirst()) {
				do {
					CallHistory callHistory = new CallHistory();
					callHistory.setPhoneNumber(c.getString(number));
					callHistory.setDate(new Date(Long.valueOf(c.getString(date))));
					callHistory.setType(Integer.parseInt(c.getString(type)));
					callHistoryList.add(callHistory);
				} while (c.moveToNext());
			}

			// adding contact ids
			String contactId = new String();
			for (int i = 0; i < callHistoryList.size(); i++) {
				Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(callHistoryList.get(i).getPhoneNumber()));
				Cursor cFetch = getContentResolver().query(uri, new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID }, null, null,
						null);

				if (cFetch.moveToFirst()) {
					cFetch.moveToFirst();
					contactId = cFetch.getString(cFetch.getColumnIndex(PhoneLookup._ID));
					callHistoryList.get(i).setId(contactId);
				} else {
					callHistoryList.get(i).setId(null);
				}
			}

			// adding names and photo
			for (int i = 0; i < callHistoryList.size(); i++) {
				if (callHistoryList.get(i).getId() != null) {
					for (int j = 0; j < simpleContactList.size(); j++) {
						if (callHistoryList.get(i).getId().equalsIgnoreCase(simpleContactList.get(j).getId())) {
							callHistoryList.get(i).setName(simpleContactList.get(j).getName());
							callHistoryList.get(i).setPhoto(simpleContactList.get(j).getPhoto());
							callHistorySimpleContactList.add(simpleContactList.get(j));
							break;
						}
					}
				}
			}
			Log.i("contact", "finished doInBackground");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("contact", "inside onPostExecute");
			super.onPostExecute(result);
			Intent intent = new Intent("com.contacts.sendContactDetails");
			intent.putParcelableArrayListExtra("simpleContactList", simpleContactList);
			intent.putParcelableArrayListExtra("callHistoryList", callHistoryList);
			intent.putParcelableArrayListExtra("callHistorySimpleContactList", callHistorySimpleContactList);
			Log.i("contact", "before sending broadcast");
			sendBroadcast(intent);
			Log.i("contact", "after sending broadcast");
		}
	}
}
