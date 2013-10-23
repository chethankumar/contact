package com.chethan.contact;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chethan.objects.SimpleContact;
import com.chethan.services.ContactService;
import com.chethan.utils.RoundedImageView;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PeopleFragment extends Fragment {

	public static ContactService contactService;
	private ArrayList<SimpleContact> contactList = new ArrayList<SimpleContact>();
	private int contactListPosition = 0;
	
	private View ContactScroller;
	
	private TextView contact_3;
	private TextView contact_2;
	private TextView contact_1;
	private TextView contact;
	private TextView contact1;
	private TextView contact2;
	private TextView contact3;
	private TextView alphabetTextView;
	
	private ImageView callButton;
	private ImageView fullContactPhoto;
	private RoundedImageView contactPhoto;
	
	public static final PeopleFragment newInstance(ContactService service)
	 {
		PeopleFragment f = new PeopleFragment();
	   contactService=service;
	   return f;
	 }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.people_all, container, false);
		
		//init all views
		ContactScroller = (View)view.findViewById(R.id.single_contact);
		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getWidthForContact(getActivity()), android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
		ContactScroller.setLayoutParams(params);
		alphabetTextView = (TextView)view.findViewById(R.id.alphabets);
		android.widget.LinearLayout.LayoutParams alphabets_params = new LinearLayout.LayoutParams(Utils.getWidthForAlphabets(getActivity()), android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
		alphabetTextView.setLayoutParams(alphabets_params);
		
		fullContactPhoto = (ImageView)view.findViewById(R.id.contactFullPhoto);
		RelativeLayout.LayoutParams fullContactPhotoLayoutParam = new RelativeLayout.LayoutParams(Utils.getWidthForContact(getActivity()),Utils.getWidthForContact(getActivity()));
		fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		fullContactPhotoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		fullContactPhoto.setLayoutParams(fullContactPhotoLayoutParam);
		fullContactPhoto.setAlpha(10);
		
		callButton = (ImageView)view.findViewById(R.id.singleContact_call);
		contactPhoto = (RoundedImageView)view.findViewById(R.id.singleContact_photo);
		
		contact_3 = (TextView)view.findViewById(R.id.ContactScroller_name_3);
		contact_2 = (TextView)view.findViewById(R.id.ContactScroller_name_2);
		contact_1 = (TextView)view.findViewById(R.id.ContactScroller_name_1);
		contact = (TextView)view.findViewById(R.id.ContactScroller_name);
		contact1 = (TextView)view.findViewById(R.id.ContactScroller_name1);
		contact2 = (TextView)view.findViewById(R.id.ContactScroller_name2);
		contact3 = (TextView)view.findViewById(R.id.ContactScroller_name3);
		
		alphabetTextView.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact1.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact2.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact3.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact_1.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact_2.setTypeface(Utils.getSegoeTypeface(getActivity()));
		contact_3.setTypeface(Utils.getSegoeTypeface(getActivity()));
		
		//get the list of all contacts name and photo
		contactList = contactService.getSimpleContactsList();
		scroll();
		
		final int screenHeight = Utils.getScreenHeight(getActivity());
		final int bottomStub = Utils.getPx(getActivity(), 50);
		final int topStub = Utils.getPx(getActivity(), 40);
		final float bigstep = (screenHeight-bottomStub-topStub)/15;
		final int mid = (int) ((screenHeight-bottomStub-topStub)/2);
		
		//scrolling the contacts 
		view.setOnTouchListener(new OnTouchListener() {
			int before = 0;
			int after = 0;
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				final float meY = arg1.getY();
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_MOVE:
					if(meY<(ContactScroller.getY()+ContactScroller.getHeight())
							&& meY>(ContactScroller.getY())){
						if(meY > (screenHeight-bottomStub)) {
							contactListPosition+=4;
						}else if(meY < topStub) {
							contactListPosition-=4;
						}else if(meY < (screenHeight-bottomStub) && meY > topStub) {
							ContactScroller.setTranslationY(meY-ContactScroller.getHeight()/2);
							after=(int) ((meY-mid)/bigstep);
							if (before!=after) {
								contactListPosition = (int) (contactListPosition + after-before);
								before=after;
							}
							
						}
						scroll();
					}
					break;

				case MotionEvent.ACTION_DOWN:
					if(meY>(ContactScroller.getY()+ContactScroller.getHeight())){
						ContactScroller.setTranslationY(ContactScroller.getY()+bigstep);
						contactListPosition++;
						scroll();
					}else if(meY<(ContactScroller.getY())){
						ContactScroller.setTranslationY(ContactScroller.getY()-bigstep);
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
		
		//touch event for alphabets
		
		alphabetTextView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_MOVE:
					
					break;
				case MotionEvent.ACTION_DOWN:
					
					break;
				default:break;
				}
				return true;
			}
		});
		
		return view;
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	
		if (isVisibleToUser) {
//			contactList = contactService.getSimpleContactsList();
//			contactList = contactService.getDummyList();
//			scroll();
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
		
		if(position>3 && position<contactList.size()){
			contact_3.setText(contactList.get(position-3).getName());
		}else{
			contact_3.setText("");
		}
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
			if(contactList.get(position).getPhoto()!=null) {
				ImageLoader.getInstance().displayImage(contactList.get(position).getPhoto().toString(), contactPhoto);
			}else {
				contactPhoto.setImageResource(R.drawable.me4);
			}
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
		if(position+3<contactList.size()){
			contact3.setText(contactList.get(position+3).getName());
		}else {
			contact3.setText("");
		}
		
		highlightAlphabets();
	}
	
	private void highlightAlphabets(){
		if(contactList.get(contactListPosition).getName()!=null){
			String textToHighlight = contactList.get(contactListPosition).getName().substring(0, 1);
			Spannable WordtoSpan = new SpannableString("#\nA\nB\nC\nD\nE\nF\nG\nH\nI\nJ\nK\nL\nM\nN\nO\nP\nQ\nR\nS\nT\nU\nV\nW\nX\nY\nZ");        
			if(WordtoSpan.toString().indexOf(textToHighlight)!=-1){
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1E29D8")), WordtoSpan.toString().indexOf(textToHighlight), WordtoSpan.toString().indexOf(textToHighlight)+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				WordtoSpan.setSpan(new android.text.style.BackgroundColorSpan(Color.parseColor("#CDCDEE")), WordtoSpan.toString().indexOf(textToHighlight), WordtoSpan.toString().indexOf(textToHighlight)+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				alphabetTextView.setText(WordtoSpan);
			}
		}
	}
	
}
