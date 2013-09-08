package com.chethan.services;

import java.sql.Date;
import java.util.ArrayList;

import com.chethan.contact.MainActivity;

import android.R.string;
import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.Toast;

public class ContactService extends Service {

	public final IBinder mBinder = new LocalBinder();
	public final Messenger mMessenger = new Messenger(new IncomingHandler());

	private static Cursor c;
	private static ArrayList<String> nameNumber = new ArrayList<String>();
	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<Date> dates = new ArrayList<Date>();
	private static ArrayList<Integer> type = new ArrayList<Integer>();
	private static ArrayList<String> contactPhotoList = new ArrayList<String>();
	private static ArrayList<String> contactIdList = new ArrayList<String>();
	private static Activity activity = null;
	private static boolean isDataLoaded = false;
	
	class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            
        	Bundle data = msg.getData();        	
        	String dataString = data.getString("MyString");
        	activity = (Activity) data.get("activity");
        	Toast.makeText(getApplicationContext(), 
                     dataString, Toast.LENGTH_SHORT).show();
        }
     }
	
	@Override 
	public IBinder onBind(Intent intent) {
//		return mBinder;
		return	mMessenger.getBinder();
	}
	
	public class LocalBinder extends Binder {
        public ContactService getService() {
            return ContactService.this;
        }
    }

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Contact service", "Received start id " + startId + ": " + intent);
        Log.d("mylog", "inside onstartcommand. startid : "+startId);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
	
	public ArrayList<String> getNameNumber() {
		if(isDataLoaded)
			return nameNumber;
		else 
			return null;
	}
	public ArrayList<String> getNames() {
		if(isDataLoaded) 
			return names; 
		else 
			return null;
	}
	public ArrayList<Date> getDates() {
		if(isDataLoaded)
			return dates;
		else 
			return null;
	}
	public ArrayList<Integer> getType() {
		if(isDataLoaded)
			return type;
		else 
			return null;
	}
	public ArrayList<String> getContactPhotoList() {
		if(isDataLoaded)
			return contactPhotoList;
		else 
			return null;
	}
	public ArrayList<String> getContactIdList() {
		if(isDataLoaded)
			return contactIdList;
		else 
			return null;
	}

	public void fillData(Activity a){
		if (activity==null) {
			Log.d("mylog", "activity is null");
			activity = a;
		}else{
			Log.d("mylog", "activity is NOT null");
		}
		if(!isDataLoaded){
			Log.d("mylog", "isdataloaded is :"+isDataLoaded);
			Uri allCalls = Uri.parse("content://call_log/calls");
			String strOrder = android.provider.CallLog.Calls.DATE + " DESC"; 
			c = activity.managedQuery(allCalls, null, null, null, strOrder);
			
			int number = c.getColumnIndex(CallLog.Calls.NUMBER);
			int type = c.getColumnIndex(CallLog.Calls.TYPE);
			int date = c.getColumnIndex(CallLog.Calls.DATE);
			if(c.moveToFirst()){
				do {
					String phNumber = c.getString(number);
					String callType = c.getString(type);
					String callDate = c.getString(date);
					Date callDayTime = new Date(Long.valueOf(callDate));
					int dircode = Integer.parseInt(callType);
					nameNumber.add(phNumber);
					dates.add(callDayTime);
					this.type.add(dircode);
				} while (c.moveToNext());
			}
			fillContactIdFromPhoneNumber(activity);
			fillContactName(activity);
			fillContactPhotoDetails(activity);
		}
		 isDataLoaded=true;
	 }
	 
	 private void fillContactPhotoDetails(Activity activity){
		 String contactid;
		 for (int i = 0; i < contactIdList.size(); i++) {
			 contactid = contactIdList.get(i);
				Uri uri=null;
				if(contactid!=null){
					uri = getPhotoUri(activity,Long.parseLong(contactid));
				}
				if (uri != null) {
					contactPhotoList.add(uri.toString());
	         } 
				else {
					contactPhotoList.add(null);
	         }
		 }
	 }
	 
	 private void fillContactIdFromPhoneNumber(Activity activity) {
		 String contactId = "";
		 for (int i = 0; i < nameNumber.size(); i++) {
	         Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
	                 Uri.encode(nameNumber.get(i)));
	         Cursor cFetch = activity.getContentResolver().query(uri,
	                 new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
	                 null, null, null);
	         
	         if (cFetch.moveToFirst()) {
	             cFetch.moveToFirst();
	                 contactId = cFetch.getString(cFetch
	                         .getColumnIndex(PhoneLookup._ID));
	                 contactIdList.add(contactId);
	         }else{
	        	 contactIdList.add(null);
	         }
		 }
    }
	 
	 public void fillContactName(Activity activity){
		 ContentResolver contentResolver = activity.getContentResolver();
		 Cursor cursor;
		 for (int i = 0; i < contactIdList.size(); i++) {
			try{
				 cursor = contentResolver.query(
		                 ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		                 null,
		                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
		                 new String[]{contactIdList.get(i)}, null);
				 if (cursor != null) {
		             if (cursor.moveToFirst()) {
		             }
		         } 
				 names.add(cursor.getString(cursor.getColumnIndex((ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
	
			}catch (Exception e) {
	            e.printStackTrace();
	            names.add(null);
	        }
		 }
	 }

    public Uri getPhotoUri(Activity activity,long contactId) {
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            Cursor cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);

            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }
}
