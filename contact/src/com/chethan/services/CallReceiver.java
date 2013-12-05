package com.chethan.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {

	static boolean ring = false;
	static boolean callReceived = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("getting call", "when getting or making call");
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
			// If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
			if (ring == true && callReceived == false) {
				Log.i("missed call", "missed call");
				Toast.makeText(context, "It was A MISSED CALL from : ", Toast.LENGTH_LONG).show();
			}
		}
		Intent newServiceIntent = new Intent(context, ContactsService.class);
		context.startService(newServiceIntent);
	}

}
