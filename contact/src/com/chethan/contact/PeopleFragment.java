package com.chethan.contact;

import com.chethan.services.ContactService;
import com.chethan.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PeopleFragment extends Fragment {

	public static ContactService contactService;
	
	public static final PeopleFragment newInstance(ContactService service)
	 {
		PeopleFragment f = new PeopleFragment();
	   contactService=service;
	   return f;
	 }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.people_all, container, false);
		
		final View singleContact = (View)view.findViewById(R.id.single_contact);
		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getWidthForContact(getActivity()), android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
		singleContact.setLayoutParams(params);
		final TextView textView = (TextView)view.findViewById(R.id.alphabets);
		android.widget.LinearLayout.LayoutParams alphabets_params = new LinearLayout.LayoutParams(Utils.getWidthForAlphabets(getActivity()), android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
		textView.setLayoutParams(alphabets_params);
		
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_MOVE:
					if(arg1.getY()>(singleContact.getY()+singleContact.getHeight())){
						singleContact.setTranslationY(singleContact.getY()+10);
					}else if(arg1.getY()<(singleContact.getY())){
						singleContact.setTranslationY(singleContact.getY()-10);
					}
					else{
						singleContact.setTranslationY(arg1.getY()-singleContact.getHeight()/2);
					}
					break;

				default:
					break;
				}
				
				return true;
			}
		});
		
		return view;
	}
	
}
