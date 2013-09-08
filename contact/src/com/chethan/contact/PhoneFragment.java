package com.chethan.contact;

import com.chethan.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneFragment extends Fragment{

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private TextView phone_0;
	private TextView phone_1;
	private TextView phone_2;
	private TextView phone_3;
	private TextView phone_4;
	private TextView phone_5;
	private TextView phone_6;
	private TextView phone_7;
	private TextView phone_8;
	private TextView phone_9;
	private TextView phone_star;
	private TextView phone_hash;
	private TextView phone_call;
	private TextView call_txt;
	private ImageView backspace;
	private Vibrator myVib;
	
	public static final PhoneFragment newInstance(String message)
	 {
	   PhoneFragment f = new PhoneFragment();
	   Bundle bdl = new Bundle(1);
	   bdl.putString(EXTRA_MESSAGE, message);
	   f.setArguments(bdl);
	   return f;
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
	   String message = getArguments().getString(EXTRA_MESSAGE);
	   View v = inflater.inflate(R.layout.phone, container, false);
	   myVib = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
	   initPhoneNumbers(v);
	   
	   call_txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(phone_call.getText().length()>0){
					String url = "tel:"+phone_call.getText();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
					startActivity(intent);
					myVib.vibrate(50);
				}
			}
	   });
	   
	   phone_call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(phone_call.getText().length()>0){
					String url = "tel:"+phone_call.getText();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
					startActivity(intent);
					myVib.vibrate(50);
				}
			}
	   });
	   
	   phone_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"0");
				phoneNumberCommonAction();
			}
	   });
	   phone_0.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				phone_call.setText(phone_call.getText()+"+");
				phoneNumberCommonAction();
				return true;
			}
		});
	   phone_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"1");
				phoneNumberCommonAction();
			}
	   });
	   phone_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"2");
				phoneNumberCommonAction();
			}
	   });
	   phone_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"3");
				phoneNumberCommonAction();
			}
	   });
	   phone_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"4");
				phoneNumberCommonAction();
			}
	   });
	   phone_5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"5");
				phoneNumberCommonAction();
			}
	   });
	   phone_6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"6");
				phoneNumberCommonAction();
			}
	   });
	   phone_7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"7");
				phoneNumberCommonAction();
			}
	   });
	   phone_8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"8");
				phoneNumberCommonAction();
			}
	   });
	   phone_9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"9");
				phoneNumberCommonAction();
			}
	   });
	   phone_star.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"*");
				phoneNumberCommonAction();
			}
	   });
	   phone_hash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText()+"#");
				phoneNumberCommonAction();
			}
	   });
	   
	   backspace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(phone_call.getText().length()>0){
					phone_call.setText(phone_call.getText().subSequence(0, phone_call.getText().length()-1));
					myVib.vibrate(20);
				}else{
					call_txt.setTextColor(Color.parseColor("#515151"));
				}
			}
		});
	   
	   backspace.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
					phone_call.setText("");
					myVib.vibrate(30);
					call_txt.setTextColor(Color.parseColor("#515151"));
				return true;
			}
		});
	   
	   return v;
	 }
	 
	 private void phoneNumberCommonAction(){
		 myVib.vibrate(20);
		 if(call_txt.getText().length()>0){
			 call_txt.setTextColor(Color.parseColor("#8FBE00"));
		 }else{
			 call_txt.setTextColor(Color.parseColor("#515151"));
		 }
	 }
	 
	 public void initPhoneNumbers(View view){
		 phone_0 = (TextView)view.findViewById(R.id.phone_0);
		 phone_0.setTypeface(Utils.getLightTypeface(getActivity()));
		 
		 phone_1 = (TextView)view.findViewById(R.id.phone_1);
		 phone_1.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_2 = (TextView)view.findViewById(R.id.phone_2);
		 phone_2.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_3 = (TextView)view.findViewById(R.id.phone_3);
		 phone_3.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_4 = (TextView)view.findViewById(R.id.phone_4);
		 phone_4.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_5 = (TextView)view.findViewById(R.id.phone_5);
		 phone_5.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_6 = (TextView)view.findViewById(R.id.phone_6);
		 phone_6.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_7 = (TextView)view.findViewById(R.id.phone_7);
		 phone_7.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_8 = (TextView)view.findViewById(R.id.phone_8);
		 phone_8.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_9 = (TextView)view.findViewById(R.id.phone_9);
		 phone_9.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_star = (TextView)view.findViewById(R.id.phone_star);
		 phone_star.setTypeface(Utils.getLightTypeface(getActivity()));

		 phone_hash = (TextView)view.findViewById(R.id.phone_hash);
		 phone_hash.setTypeface(Utils.getLightTypeface(getActivity()));
		 
		 phone_call = (TextView)view.findViewById(R.id.phone_call);
		 phone_call.setTypeface(Utils.getLightTypeface(getActivity()));

		 call_txt = (TextView)view.findViewById(R.id.call_txt);
		 call_txt.setTypeface(Utils.getLightTypeface(getActivity()));
		 
		 backspace = (ImageView)view.findViewById(R.id.backspace);
		 backspace.setImageResource(R.drawable.backspace);
	 }
}
