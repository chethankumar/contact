package com.chethan.contact;

import java.sql.Date;

import com.chethan.utils.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

public class CalllogFragment extends Fragment {

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private Cursor c;
	public static final CalllogFragment newInstance(String message)
	 {
		CalllogFragment f = new CalllogFragment();
	   Bundle bdl = new Bundle(1);
	   bdl.putString(EXTRA_MESSAGE, message);
	   f.setArguments(bdl);
	   return f;
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
	   String message = getArguments().getString(EXTRA_MESSAGE);
	   View v = inflater.inflate(R.layout.call_log, container, false);
	   
	//   TextView textView = (TextView)v.findViewById(R.id.textView1);
	   GridLayout callLogGridLayout = (GridLayout)v.findViewById(R.id.call_log_grid);
	   
	   Uri allCalls = Uri.parse("content://call_log/calls");
		String strOrder = android.provider.CallLog.Calls.DATE + " DESC"; 
	    c = getActivity().managedQuery(allCalls, null, null, null, strOrder);

	   	int number = c.getColumnIndex(CallLog.Calls.NUMBER);
	   	int type = c.getColumnIndex(CallLog.Calls.TYPE);
		  int duration = c.getColumnIndex(CallLog.Calls.DURATION);// for duration
		  int date = c.getColumnIndex(CallLog.Calls.DATE);
	   
		  if(c.moveToFirst()){
			  do {
				  String phNumber = c.getString(number);
		            String callType = c.getString(type);
		            String callDate = c.getString(date);
		            Date callDayTime = new Date(Long.valueOf(callDate));
		            String callDuration = c.getString(duration);
		            String dir = null;
		            int dircode = Integer.parseInt(callType);
		            switch (dircode) {
		            case CallLog.Calls.OUTGOING_TYPE:
		                dir = "OUTGOING";
		                break;

		            case CallLog.Calls.INCOMING_TYPE:
		                dir = "INCOMING";
		                break;

		            case CallLog.Calls.MISSED_TYPE:
		                dir = "MISSED";
		                break;
		            }
//		            append(textView,"\nPhone Number:--- " + phNumber + " \nCall Type:--- "
//		                    + dir + " \nCall Date:--- " + callDayTime
//		                    + " \nCall duration in sec :--- " + callDuration);
//		            append(textView,"\n----------------------------------");
		            Utils.addCallLogTile((Activity)getActivity(), callLogGridLayout, inflater, phNumber, callDayTime, dircode);
			} while (c.moveToNext());
		  }
//		  c.close();
	   return v;
	 }
	 
	 @Override
	public void onResume() {
//		 Uri allCalls = Uri.parse("content://call_log/calls");
//			String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
//		 c = getActivity().managedQuery(allCalls, null, null, null, strOrder);
		super.onResume();
	}

	private void append(TextView textView,String string){
		 textView.setText(textView.getText()+"\n"+string);
	 }
}

