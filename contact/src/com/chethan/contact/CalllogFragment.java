package com.chethan.contact;

import java.sql.Date;
import java.util.ArrayList;

import com.chethan.services.ContactService;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CalllogFragment extends Fragment {

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	public static ContactService contactService;
	private ArrayList<String> nameNumber = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<Date> dates = new ArrayList<Date>();
	private ArrayList<Integer> type = new ArrayList<Integer>();
	private ArrayList<String> contactPhotoList = new ArrayList<String>();
	private Vibrator myVib;
	public static final CalllogFragment newInstance(ContactService service)
	 {
		CalllogFragment f = new CalllogFragment();
	   contactService=service;
	   return f;
	 }

	 @Override
	 public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	   Bundle savedInstanceState) {
	   View v = inflater.inflate(R.layout.call_log, container, false);
	   
	   	GridView gridView = (GridView)v.findViewById(R.id.gridView);
	   	myVib = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
	   
	   	contactService.fillData();
	   	nameNumber = contactService.getNameNumber();
	   	names = contactService.getNames();
	   	dates = contactService.getDates();
	   	type = contactService.getType();
	   	contactPhotoList = contactService.getContactPhotoList();
//------------------------------------
	   	boolean pauseOnScroll = true; // or true
	   	boolean pauseOnFling = true; // or false
	   	PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling);
	   	gridView.setOnScrollListener(listener);
	   	
	   	gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String url = "tel:"+nameNumber.get(position).toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
				startActivity(intent);
				myVib.vibrate(50);
			}
		});
	   	
	   	gridView.setAdapter(new ListAdapter() {
			
			@Override
			public void unregisterDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void registerDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isEmpty() {
				return false;
			}
			
			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return 1;
			}
			
			@Override
			public View getView(int arg0, View mygridView, ViewGroup container) {
				 
				 ViewHolder viewHolder;  
				
				if(mygridView==null){
					int width = Utils.getWidthForCard(getActivity());
					mygridView = (LinearLayout)inflater.inflate(R.layout.log_tile, container, false);
					viewHolder = new ViewHolder();
					mygridView.setLayoutParams(new AbsListView.LayoutParams(Utils.getWidthForCard(getActivity()),Utils.getHeightForCard(getActivity())));
					viewHolder.photoImageView = (ImageView)mygridView.findViewById(R.id.log_photo);
					viewHolder.photoImageView.getLayoutParams().height=width-Utils.getPx(getActivity(), 10);
					viewHolder.photoImageView.getLayoutParams().width=width-Utils.getPx(getActivity(), 10);
					viewHolder.nameOrNumber = (TextView)mygridView.findViewById(R.id.log_nameOrNumber);
					viewHolder.nameOrNumber.setTypeface(Utils.getPhoneTypeface(getActivity()));
					viewHolder.logDate = (TextView)mygridView.findViewById(R.id.log_date);
					viewHolder.logDate.setTypeface(Utils.getSubtitleTypeface(getActivity()));
					viewHolder.logTime = (TextView)mygridView.findViewById(R.id.log_time);
					viewHolder.logTime.setTypeface(Utils.getSubtitleTypeface(getActivity()));
					viewHolder.photoImageView.setImageResource(R.drawable.contact);
					mygridView.setTag(viewHolder);
				}else{
					viewHolder=(ViewHolder)mygridView.getTag();
				}

				if (contactPhotoList.get(arg0) != null) {
					ImageLoader.getInstance().displayImage(contactPhotoList.get(arg0), viewHolder.photoImageView);
                } 
				else {
					//ImageLoader.getInstance().displayImage("drawable://" + R.drawable.contact, viewHolder.photoImageView);
					viewHolder.photoImageView.setImageResource(R.drawable.contact);
                }
				
				viewHolder.nameOrNumber.setText(names.get(arg0)==null?nameNumber.get(arg0):names.get(arg0));
				viewHolder.logDate.setText(Utils.getDate(dates.get(arg0)));
				viewHolder.logTime.setText(Utils.getTime(dates.get(arg0)));
						
						switch (CalllogFragment.this.type.get(arg0)) {
						case CallLog.Calls.OUTGOING_TYPE:
							viewHolder.nameOrNumber.setTextColor(Color.parseColor("#8FBE00"));
							viewHolder.logDate.setTextColor(Color.parseColor("#8FBE00"));
							viewHolder.logTime.setTextColor(Color.parseColor("#8FBE00"));
							break;
							
						case CallLog.Calls.INCOMING_TYPE:
							viewHolder.nameOrNumber.setTextColor(Color.parseColor("#00A8C6"));
							viewHolder.logDate.setTextColor(Color.parseColor("#00A8C6"));
							viewHolder.logTime.setTextColor(Color.parseColor("#00A8C6"));
							break;
							
						case CallLog.Calls.MISSED_TYPE:
							viewHolder.nameOrNumber.setTextColor(Color.RED);
							viewHolder.logDate.setTextColor(Color.RED);
							viewHolder.logTime.setTextColor(Color.RED);
							break;
						}
				
				
				return mygridView;
			}
			
			@Override
			public int getItemViewType(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				//return c.getCount();
				return nameNumber.size();
			}
			
			@Override
			public boolean isEnabled(int arg0) {
				return true;
			}
			
			@Override
			public boolean areAllItemsEnabled() {
				return true;
			}
		});
//------------------------------------	   
//		  if(c.moveToFirst()){
//			  do {
//				  String phNumber = c.getString(number);
//		            String callType = c.getString(type);
//		            String callDate = c.getString(date);
//		            Date callDayTime = new Date(Long.valueOf(callDate));
//		            int dircode = Integer.parseInt(callType);
//		            
////		            Utils.addCallLogTile((Activity)getActivity(), callLogGridLayout, inflater, phNumber, callDayTime, dircode);
//			
//			  } while (c.moveToNext());
//		  }
	   return v;
	 }
	 
//	 private void fillData(){
//		 Uri allCalls = Uri.parse("content://call_log/calls");
//			String strOrder = android.provider.CallLog.Calls.DATE + " DESC"; 
//		    c = getActivity().managedQuery(allCalls, null, null, null, strOrder);
//
//		 	int number = c.getColumnIndex(CallLog.Calls.NUMBER);
//		   	int type = c.getColumnIndex(CallLog.Calls.TYPE);
//		   	int date = c.getColumnIndex(CallLog.Calls.DATE);
//		 if(c.moveToFirst()){
//			  do {
//				  	String phNumber = c.getString(number);
//		            String callType = c.getString(type);
//		            String callDate = c.getString(date);
//		            Date callDayTime = new Date(Long.valueOf(callDate));
//		            int dircode = Integer.parseInt(callType);
//		            nameNumber.add(phNumber);
//		            dates.add(callDayTime);
//		            this.type.add(dircode);
//			  } while (c.moveToNext());
//		  }
//		 fillContactIdFromPhoneNumber();
//		 fillContactName();
//		 fillContactPhotoDetails();
//	 }
//	 
//	 private void fillContactPhotoDetails(){
//		 String contactid;
//		 for (int i = 0; i < contactIdList.size(); i++) {
//			 contactid = contactIdList.get(i);
//				Uri uri=null;
//				if(contactid!=null){
//					uri = getPhotoUri(Long.parseLong(contactid));
//				}
//				if (uri != null) {
//					contactPhotoList.add(uri.toString());
//	         } 
//				else {
//					contactPhotoList.add(null);
//	         }
//		 }
//	 }
//	 
//	 private void fillContactIdFromPhoneNumber() {
//		 String contactId = "";
//		 for (int i = 0; i < nameNumber.size(); i++) {
//	         Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
//	                 Uri.encode(nameNumber.get(i)));
//	         Cursor cFetch = getActivity().getContentResolver().query(uri,
//	                 new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
//	                 null, null, null);
//	         
//	         if (cFetch.moveToFirst()) {
//	             cFetch.moveToFirst();
//	                 contactId = cFetch.getString(cFetch
//	                         .getColumnIndex(PhoneLookup._ID));
//	                 contactIdList.add(contactId);
//	         }else{
//	        	 contactIdList.add(null);
//	         }
//		 }
//     }
//	 
//	 public void fillContactName(){
//		 ContentResolver contentResolver = getActivity().getContentResolver();
//		 Cursor cursor;
//		 for (int i = 0; i < contactIdList.size(); i++) {
//			try{
//				 cursor = contentResolver.query(
//		                 ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//		                 null,
//		                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
//		                 new String[]{contactIdList.get(i)}, null);
//				 if (cursor != null) {
//		             if (cursor.moveToFirst()) {
//		             }
//		         } 
//				 names.add(cursor.getString(cursor.getColumnIndex((ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
//	
//			}catch (Exception e) {
//	            e.printStackTrace();
//	            names.add(null);
//	        }
//		 }
//	 }
//
//     public Uri getPhotoUri(long contactId) {
//         ContentResolver contentResolver = getActivity().getContentResolver();
//         try {
//             Cursor cursor = contentResolver
//                     .query(ContactsContract.Data.CONTENT_URI,
//                             null,
//                             ContactsContract.Data.CONTACT_ID
//                                     + "="
//                                     + contactId
//                                     + " AND "
//
//                                     + ContactsContract.Data.MIMETYPE
//                                     + "='"
//                                     + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
//                                     + "'", null, null);
//
//             if (cursor != null) {
//                 if (!cursor.moveToFirst()) {
//                     return null; // no photo
//                 }
//             } else {
//                 return null; // error in cursor process
//             }
//
//         } catch (Exception e) {
//             e.printStackTrace();
//             return null;
//         }
//
//         Uri person = ContentUris.withAppendedId(
//                 ContactsContract.Contacts.CONTENT_URI, contactId);
//         return Uri.withAppendedPath(person,
//                 ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//     }
	 
	 @Override
	public void onResume() {
		super.onResume();
		nameNumber = contactService.getNameNumber();
	   	names = contactService.getNames();
	   	dates = contactService.getDates();
	   	type = contactService.getType();
	   	contactPhotoList = contactService.getContactPhotoList();
	}

	static class ViewHolder{
		 ImageView photoImageView;
		 TextView nameOrNumber;
		 TextView logDate;
		 TextView logTime;
	 }
}

