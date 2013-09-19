package com.chethan.contact;

import java.util.ArrayList;

import com.chethan.objects.SimpleContact;
import com.chethan.services.ContactService;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.annotation.TargetApi;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.AndroidCharacter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PeopleFragment extends Fragment {

	public static ContactService contactService;
	private ArrayList<SimpleContact> contactList = new ArrayList<SimpleContact>();
	private int contactListPosition = 0;
	
	private TextView contact_2;
	private TextView contact_1;
	private TextView contact;
	private TextView contact1;
	private TextView contact2;
	private TextView alphabetTextView;
	
	private ImageView callButton;
	private ImageView fullContactPhoto;
	
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
		
		fullContactPhoto = (ImageView)view.findViewById(R.id.contactFullPhoto);
		RelativeLayout.LayoutParams fullContactPhotoLayoutParam = new RelativeLayout.LayoutParams(Utils.getWidthForContact(getActivity()),Utils.getWidthForContact(getActivity()));
		fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		fullContactPhoto.setLayoutParams(fullContactPhotoLayoutParam);
		fullContactPhoto.setAlpha(10);
		
		callButton = (ImageView)view.findViewById(R.id.singleContact_call);
		
		contact_2 = (TextView)view.findViewById(R.id.SingleContactName_2);
		contact_1 = (TextView)view.findViewById(R.id.SingleContactName_1);
		contact = (TextView)view.findViewById(R.id.SingleContactName);
		contact1 = (TextView)view.findViewById(R.id.SingleContactName1);
		contact2 = (TextView)view.findViewById(R.id.SingleContactName2);
		alphabetTextView = (TextView)view.findViewById(R.id.alphabets);
		
		alphabetTextView.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact1.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact2.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact_1.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact_2.setTypeface(Utils.getSegoeTypeface(getActivity()));
		
//		contactNameList = contactService.getContactNameList();
//		scroll();
		
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				float step = (Utils.getScreenHeight(getActivity())/contactList.size());
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_MOVE:
//					if(arg1.getY()>(singleContact.getY()+singleContact.getHeight())){
//						singleContact.setTranslationY(singleContact.getY()+step);
//						contactListPosition++;
//						scroll();
//					}else if(arg1.getY()<(singleContact.getY())){
//						singleContact.setTranslationY(singleContact.getY()-step);
//						contactListPosition--;
//						scroll();
//					}
//					else{
//						singleContact.setTranslationY(arg1.getY()-singleContact.getHeight()/2);
//						contactListPosition = (int)(arg1.getY()/step);
//						scroll();
//					}
					if(arg1.getY()<(singleContact.getY()+singleContact.getHeight())
							&& arg1.getY()>(singleContact.getY())){
						singleContact.setTranslationY(arg1.getY()-singleContact.getHeight()/2);
						contactListPosition = (int)(arg1.getY()/step);
						scroll();
					}
					break;

				case MotionEvent.ACTION_DOWN:
					if(arg1.getY()>(singleContact.getY()+singleContact.getHeight())){
						singleContact.setTranslationY(singleContact.getY()+step);
						contactListPosition++;
						scroll();
					}else if(arg1.getY()<(singleContact.getY())){
						singleContact.setTranslationY(singleContact.getY()-step);
						contactListPosition--;
						scroll();
					}
					break;
					
				default:
					break;
				}
				
				return true;
			}
		});
		
//		callButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Toast.makeText(getActivity(), "call clicked for "+contactNameList.get(contactListPosition), Toast.LENGTH_SHORT).show();
//			}
//		});
		
//		msgButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Toast.makeText(getActivity(), "msg clicked for "+contactNameList.get(contactListPosition), Toast.LENGTH_SHORT).show();
//			}
//		});
		
		return view;
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	
		if (isVisibleToUser) {
			contactList = contactService.getSimpleContactsList();
			scroll();
		}
		else {  
			
		}

	}
	
	private void scroll(){
		
		if(contactListPosition>=contactList.size())
			contactListPosition=contactList.size()-1;
		
		if(contactListPosition<0)
			contactListPosition=0;
		int position = contactListPosition;
	//	contactListPosition=position;
		
//		setContactFullPhotoPosition();	
//		setFullContactPhoto();
		
		if(position>2 && position<contactList.size()){
			contact_2.setText(contactList.get(position-2).getName());
		}else{
			contact_2.setText("");
		}
		if(position>1 && position<contactList.size()){
			contact_1.setText(contactList.get(position-1).getName());
		}else{
			contact_1.setText("");
		}
		if(position>=0 && position<contactList.size()){
			contact.setText(contactList.get(position).getName());
		}
		if(position+1<contactList.size()){
			contact1.setText(contactList.get(position+1).getName());
		}else{
			contact1.setText("");
		}
		if(position+2<contactList.size()){
			contact2.setText(contactList.get(position+2).getName());
		}else {
			contact2.setText("");
		}
		
		highlightAlphabets();
	}
	
	private void setContactFullPhotoPosition(){
		if(contactListPosition<(contactList.size()/2)){
			RelativeLayout.LayoutParams fullContactPhotoLayoutParam = new RelativeLayout.LayoutParams(Utils.getWidthForContact(getActivity()),Utils.getWidthForContact(getActivity()));
			fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			fullContactPhoto.setLayoutParams(fullContactPhotoLayoutParam);
		}else{
			RelativeLayout.LayoutParams fullContactPhotoLayoutParam = new RelativeLayout.LayoutParams(Utils.getWidthForContact(getActivity()),Utils.getWidthForContact(getActivity()));
			fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			fullContactPhoto.setLayoutParams(fullContactPhotoLayoutParam);
		}
	}
	
	private void setFullContactPhoto(){
		if(contactList.get(contactListPosition).getPhoto()!=null){
			ImageLoader.getInstance().displayImage(contactList.get(contactListPosition).getPhoto().toString(), fullContactPhoto);
		}else{
			fullContactPhoto.setImageResource(R.drawable.me4);
		}
		fullContactPhoto.setAlpha(105);
	}
	
	private void highlightAlphabets(){
		if(contactList.get(contactListPosition).getName()!=null){
			String textToHighlight = contactList.get(contactListPosition).getName().substring(0, 1);
			Spannable WordtoSpan = new SpannableString("A\nB\nC\nD\nE\nF\nG\nH\nI\nJ\nK\nL\nM\nN\nO\nP\nQ\nR\nS\nT\nU\nV\nW\nX\nY\nZ");        
			if(WordtoSpan.toString().indexOf(textToHighlight)!=-1){
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1E29D8")), WordtoSpan.toString().indexOf(textToHighlight), WordtoSpan.toString().indexOf(textToHighlight)+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				WordtoSpan.setSpan(new android.text.style.BackgroundColorSpan(Color.parseColor("#CDCDEE")), WordtoSpan.toString().indexOf(textToHighlight), WordtoSpan.toString().indexOf(textToHighlight)+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				alphabetTextView.setText(WordtoSpan);
			}
		}
	}
	
}
