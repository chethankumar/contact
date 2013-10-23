package com.chethan.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.chethan.contact.R;

import android.R.integer;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.provider.CallLog;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class Utils {
	
	private static Point size = null;
	
	private static int getSizeX(Activity activity) {
		if(size==null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			Utils.size = size;
		}
		return size.x;
	}
	
	private static int getSizeY(Activity activity) {
		if(size==null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			Utils.size = size;
		}
		return size.y;
	}

	public static int getWidthForCard(Activity activity){
		JazzyViewPager viewPager = (JazzyViewPager)activity.findViewById(R.id.viewpager);
		int width = viewPager.getLayoutParams().width;
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
//		return (int) ((size.x/3.5)-10);
		return (int) ((getSizeX(activity)/3.5)-10);
	}
	
	public static int getWidthForProfileBackground(Activity activity){
		return getSizeX(activity)-getPx(activity, 10);
	}
	
	public static int getWidthForProfileCircle(Activity activity){
		return (int) (getSizeX(activity)*1.3/3);
	}
	
	public static int getWidthForContact(Activity activity){
		return (getSizeX(activity)*8)/10;
	}
	
	public static int getWidthForSingleContact(Activity activity){
		return (getSizeX(activity)*3)/10;
	}
	
	public static int getTopMarginForContact(Activity activity){
		return (getSizeY(activity)*8)/10;
	}
	
	public static int getWidthForAlphabets(Activity activity){
		return (int) ((getSizeX(activity)*1.4)/10);
	}
	
	public static int getScreenHeight(Activity activity){
		return getSizeY(activity) - getPx(activity, 75);
	}
	
	public static int getHeightForCard(Activity activity) {
		return (int) (getWidthForCard(activity)*4/2.8);
	}
	
	public static Typeface getTitleTypeface(Activity activity) {
		// return Typeface.createFromAsset(activity.getAssets(),"Ubuntu-L.ttf");
		// return Typeface.createFromAsset(activity.getAssets(),"Nunito-Regular.ttf");
		 return Typeface.createFromAsset(activity.getAssets(),"Ubuntu-M.ttf");
		// return Typeface.createFromAsset(activity.getAssets(),"Delevane.otf");
		// return Typeface.createFromAsset(activity.getAssets(),"Helvetica.ttf");
		// return Typeface.createFromAsset(activity.getAssets(),"Junction.otf");
		 //return Typeface.createFromAsset(activity.getAssets(),"Overlock-R.otf");
		// return Typeface.createFromAsset(activity.getAssets(),"HoneyScript-Light.ttf");
		// return Typeface.createFromAsset(activity.getAssets(),"HelveticaNeueLt.ttf");
	}
	
	public static Typeface getPhoneTypeface(Activity activity) {
		return Typeface.createFromAsset(activity.getAssets(),"Ubuntu-M.ttf");
	}
	
	public static Typeface getLightTypeface(Activity activity) {
		return Typeface.createFromAsset(activity.getAssets(),"segoeuil.ttf");
	}
	
	public static Typeface getSubtitleTypeface(Activity activity) {
		return Typeface.createFromAsset(activity.getAssets(),"Ubuntu-R.ttf");
	}
	
	public static Typeface getSegoeTypeface(Activity activity) {
		return Typeface.createFromAsset(activity.getAssets(),"segoeui.ttf");
	}
	
	public static Typeface getSegoeSemiBoldTypeface(Activity activity) {
		return Typeface.createFromAsset(activity.getAssets(),"seguisb.ttf");
	}
	
	public static int getPx(Activity activity,int dps){
		final float scale =activity.getBaseContext().getResources().getDisplayMetrics().density;
		return (int) (dps * scale + 0.5f);
	}
	
	public static void addCallLogTile(Activity activity,GridLayout gridLayout,LayoutInflater inflater, String nameNumber,Date date,int type) {
		final View view = inflater.inflate(R.layout.log_tile, null);

		int width = Utils.getWidthForCard(activity);
		ViewGroup.LayoutParams layoutParams =  new LayoutParams(Utils.getWidthForCard(activity),Utils.getHeightForCard(activity));
		view.setLayoutParams(layoutParams);
		ImageView photoImageView = (ImageView)view.findViewById(R.id.log_photo);
		photoImageView.getLayoutParams().height=width-getPx(activity, 15);
		photoImageView.getLayoutParams().width=width-getPx(activity, 15);
		photoImageView.setImageResource(R.drawable.me);
		TextView nameOrNumber = (TextView)view.findViewById(R.id.log_nameOrNumber);
		nameOrNumber.setText(nameNumber);
		nameOrNumber.setTypeface(getPhoneTypeface(activity));
		
		TextView logDate = (TextView)view.findViewById(R.id.log_date);
		logDate.setText(getDate(date));
		logDate.setTypeface(getSubtitleTypeface(activity));
		
		TextView logTime = (TextView)view.findViewById(R.id.log_time);
		logTime.setText(getTime(date));
		logTime.setTypeface(getSubtitleTypeface(activity));
		
		
		switch (type) {
        case CallLog.Calls.OUTGOING_TYPE:
        	nameOrNumber.setTextColor(Color.parseColor("#8FBE00"));
        	logDate.setTextColor(Color.parseColor("#8FBE00"));
        	logTime.setTextColor(Color.parseColor("#8FBE00"));
            break;

        case CallLog.Calls.INCOMING_TYPE:
        	nameOrNumber.setTextColor(Color.parseColor("#00A8C6"));
        	logDate.setTextColor(Color.parseColor("#00A8C6"));
        	logTime.setTextColor(Color.parseColor("#00A8C6"));
            break;

        case CallLog.Calls.MISSED_TYPE:
        	nameOrNumber.setTextColor(Color.RED);
        	logDate.setTextColor(Color.RED);
        	logTime.setTextColor(Color.RED);
            break;
        }
		gridLayout.addView(view);
	}
	
	public static String getDate(Date date){
		String monthString = "";
		switch(date.getMonth()){
			case 0:monthString="Jan";break;
			case 1:monthString="Feb";break;
			case 2:monthString="Mar";break;
			case 3:monthString="Apr";break;
			case 4:monthString="May";break;
			case 5:monthString="Jun";break;
			case 6:monthString="Jul";break;
			case 7:monthString="Aug";break;
			case 8:monthString="Sep";break;
			case 9:monthString="Oct";break;
			case 10:monthString="Nov";break;
			case 11:monthString="Dec";break;
		}
		return date.getDate()+"-"+monthString;
	}
	
	public static String getTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		return sdf.format(date);
	}
	
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

}
