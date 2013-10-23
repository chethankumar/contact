package com.chethan.contact;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.chethan.objects.CallHistory;
import com.chethan.services.ContactService;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class CalllogFragment extends Fragment {

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	public static ContactService contactService;
	private ArrayList<CallHistory> callHistories = new ArrayList<CallHistory>();
	private Vibrator myVib;
	private LinearLayout cardInCallLog;
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
	   
//	   	contactService.populateCallLogData();
	   	
	   	final Timer timer = new Timer();
	   	timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {

				    @Override
				    public void run() {
				    	if(contactService.getCallLogData() != null) {
				    		callHistories = contactService.getCallLogData();
				    		timer.cancel();
				    	}
				    }
				     
				});
			}
		}, 0, 2000);
	   	
//	   	callHistories = contactService.getCallLogData();
	   	
//------------------------------------
	   	boolean pauseOnScroll = false; // or true
	   	boolean pauseOnFling = true; // or false
	   	PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling);
	   	gridView.setOnScrollListener(listener);
	   	
	   	ImageView imgView = new ImageView(getActivity().getApplicationContext());
	   	for (CallHistory element : callHistories) {
	   		if(element.getPhoto()!=null)
	   			ImageLoader.getInstance().displayImage(element.getPhoto().toString(), imgView);
		}
	   	gridView.setAlwaysDrawnWithCacheEnabled(true);
	   	gridView.setDrawingCacheEnabled(true);
	   	gridView.setScrollingCacheEnabled(true);
	   	gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String url = "tel:"+callHistories.get(position).getPhoneNumber();
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
			public View getView(int position, View mygridView, ViewGroup container) {
				 
				 ViewHolder viewHolder;  
				 int width = Utils.getWidthForCard(getActivity());
				
				if(mygridView==null){
					mygridView = (LinearLayout)inflater.inflate(R.layout.log_tile, container, false);
					viewHolder = new ViewHolder();
					mygridView.setLayoutParams(new AbsListView.LayoutParams(width,Utils.getHeightForCard(getActivity())));
					viewHolder.photoImageView = (ImageView)mygridView.findViewById(R.id.log_photo);
//					viewHolder.photoImageView = (RoundedImageView)mygridView.findViewById(R.id.log_photo);
//					viewHolder.photoImageView = (CircularImageView)mygridView.findViewById(R.id.log_photo);
					viewHolder.photoImageView.getLayoutParams().height=width;//-Utils.getPx(getActivity(), 15);
					viewHolder.photoImageView.getLayoutParams().width=width;//-Utils.getPx(getActivity(), 15);
					viewHolder.nameOrNumber = (TextView)mygridView.findViewById(R.id.log_nameOrNumber);
					viewHolder.nameOrNumber.setTypeface(Utils.getSegoeTypeface(getActivity()));
					viewHolder.logDate = (TextView)mygridView.findViewById(R.id.log_date);
					viewHolder.logDate.setTypeface(Utils.getSegoeTypeface(getActivity()));
					viewHolder.logTime = (TextView)mygridView.findViewById(R.id.log_time);
					viewHolder.logTime.setTypeface(Utils.getSegoeTypeface(getActivity()));
					viewHolder.photoImageView.setImageResource(R.drawable.contact);
					viewHolder.cardForLogTile = (LinearLayout)mygridView.findViewById(R.id.log_tile_card_background);
					mygridView.setTag(viewHolder);
				}else{
					viewHolder=(ViewHolder)mygridView.getTag();
				}

				viewHolder.nameOrNumber.setText(callHistories.get(position).getName()==null ? callHistories.get(position).getPhoneNumber() : callHistories.get(position).getName());
				viewHolder.logDate.setText(Utils.getDate(callHistories.get(position).getDate()));
				viewHolder.logTime.setText(Utils.getTime(callHistories.get(position).getDate()));
					
				if(callHistories.get(position).getPhoto() != null) {
//					Picasso.with(getActivity()).load(callHistories.get(position).getPhoto()).into(viewHolder.photoImageView);
					ImageLoader.getInstance().displayImage(callHistories.get(position).getPhoto().toString(), viewHolder.photoImageView);
				}else {
					viewHolder.photoImageView.setImageResource(R.drawable.me6);
				}
				
				viewHolder.nameOrNumber.setTextColor(Color.WHITE);
				viewHolder.logDate.setTextColor(Color.WHITE);
				viewHolder.logTime.setTextColor(Color.WHITE);
				
						switch (callHistories.get(position).getType()) {
						case CallLog.Calls.OUTGOING_TYPE:
//							viewHolder.nameOrNumber.setTextColor(Color.parseColor("#007304"));
//							viewHolder.logDate.setTextColor(Color.parseColor("#007304"));
//							viewHolder.logTime.setTextColor(Color.parseColor("#007304"));
							viewHolder.cardForLogTile.setBackgroundResource(R.drawable.style_tile_green);
							break;
							
						case CallLog.Calls.INCOMING_TYPE:
//							viewHolder.nameOrNumber.setTextColor(Color.parseColor("#0A13DF"));
//							viewHolder.logDate.setTextColor(Color.parseColor("#0A13DF"));
//							viewHolder.logTime.setTextColor(Color.parseColor("#0A13DF"));
							viewHolder.cardForLogTile.setBackgroundResource(R.drawable.style_tile_blue);
							break;
							
						case CallLog.Calls.MISSED_TYPE:
//							viewHolder.nameOrNumber.setTextColor(Color.RED);
//							viewHolder.logDate.setTextColor(Color.RED);
//							viewHolder.logTime.setTextColor(Color.RED);
							viewHolder.cardForLogTile.setBackgroundResource(R.drawable.style_tile_red);
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
				return callHistories.size();
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
	   	
	   return v;
	 }
	 
	 @Override
	public void onResume() {
		super.onResume();
		if(contactService.getCallLogData() != null) {
    		callHistories = contactService.getCallLogData();
    	}
//		callHistories = contactService.getCallLogData();
	}

	static class ViewHolder{
//		 CircularImageView photoImageView;
//		 RoundedImageView photoImageView;
		 ImageView photoImageView;
		 TextView nameOrNumber;
		 TextView logDate;
		 TextView logTime;
		 LinearLayout cardForLogTile;
	 }
}

