package com.chethan.contact;

import java.util.ArrayList;

import com.chethan.objects.SimpleContact;
import com.chethan.services.ContactService;
import com.chethan.utils.RoundedImageView;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.annotation.TargetApi;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Color;
import android.graphics.ColorFilter;
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

public class MeFragment extends Fragment {

	public static ContactService contactService;
	
	public static final MeFragment newInstance(ContactService service)
	 {
		MeFragment f = new MeFragment();
	   contactService=service;
	   return f;
	 }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.me, container, false);
		
		ImageView profile_background = (ImageView)view.findViewById(R.id.me_profile_background);
		profile_background.getLayoutParams().width = Utils.getWidthForProfileBackground(getActivity());
		profile_background.getLayoutParams().height = Utils.getWidthForProfileBackground(getActivity());
		profile_background.setAlpha(110);
		
		ImageView profile_foreground = (ImageView)view.findViewById(R.id.me_profile_foreground);
		profile_foreground.getLayoutParams().width = Utils.getWidthForProfileBackground(getActivity());
		profile_foreground.getLayoutParams().height = Utils.getWidthForProfileBackground(getActivity());
		profile_foreground.setAlpha(11);
		
		RoundedImageView profilePicture = (RoundedImageView)view.findViewById(R.id.me_profile_circle);
		RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(Utils.getWidthForProfileCircle(getActivity()), Utils.getWidthForProfileCircle(getActivity()));
		relativeLayout.leftMargin = (Utils.getWidthForProfileBackground(getActivity())-Utils.getWidthForProfileCircle(getActivity()))/2;
		relativeLayout.topMargin = (Utils.getWidthForProfileBackground(getActivity())-Utils.getWidthForProfileCircle(getActivity()))*2/10;
		profilePicture.setLayoutParams(relativeLayout);
		profilePicture.setBorderColor(Color.WHITE);
		return view;
		
	}
	
}
