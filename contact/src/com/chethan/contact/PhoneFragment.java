package com.chethan.contact;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chethan.contact.CalllogFragment.ViewHolder;
import com.chethan.objects.SimpleContact;
import com.chethan.utils.RoundedImageView;
import com.chethan.utils.SearchUtil;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhoneFragment extends Fragment {

//	private TextView phone_0;
//	private TextView phone_1;
//	private TextView phone_2;
//	private TextView phone_3;
//	private TextView phone_4;
//	private TextView phone_5;
//	private TextView phone_6;
//	private TextView phone_7;
//	private TextView phone_8;
//	private TextView phone_9;
//	private TextView phone_star;
//	private TextView phone_hash;
	private GridView dialer_grid;
	private TextView phone_call;
	private TextView call_txt;
	private ImageView backspace;
	private Vibrator myVib;
	private GridView searchGridView;
	private ArrayList<SimpleContact> searchList = new ArrayList<SimpleContact>();
	private ArrayList<SimpleContact> contactList = new ArrayList<SimpleContact>();
	private ArrayList<SimpleContact> callHistories = new ArrayList<SimpleContact>();

	private StringBuilder searchString;

	public static final PhoneFragment newInstance() {
		PhoneFragment f = new PhoneFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.phone, container, false);
		myVib = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		initPhoneNumbers(v,inflater);
		searchGridView = (GridView) v.findViewById(R.id.SearchgridView);

		initAdapterOfSearchGridView(inflater);

		getActivity().registerReceiver(receiver, new IntentFilter("com.contacts.sendContactDetails"));
		return v;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			contactList = intent.getExtras().getParcelableArrayList("simpleContactList");
			callHistories = intent.getExtras().getParcelableArrayList("callHistorySimpleContactList");
			Log.i("contact", "contact size :: " + contactList.size());
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter("com.contacts.sendContactDetails"));
	}

	private void initAdapterOfSearchGridView(final LayoutInflater inflater) {

		searchGridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder viewHolder;
				int width = Utils.getWidthForCard(getActivity()) - Utils.getPx(getActivity(), 10);
				if (view == null) {
					view = (LinearLayout) inflater.inflate(R.layout.all_contacts_tile, parent, false);
					viewHolder = new ViewHolder();
					view.setLayoutParams(new AbsListView.LayoutParams(width, Utils.getHeightForCard(getActivity())));
					viewHolder.photoImageView = (RoundedImageView) view.findViewById(R.id.allContactPhoto);
					viewHolder.photoImageView.getLayoutParams().height = width;// -Utils.getPx(getActivity(),
																				// 15);
					viewHolder.photoImageView.getLayoutParams().width = width;// -Utils.getPx(getActivity(),
																				// 15);
					viewHolder.contactNameTextView = (TextView) view.findViewById(R.id.allContactName);
					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}

				viewHolder.contactNameTextView.setText(searchList.get(position).getName());
				// highlight matching text
				// highlightText(viewHolder.contactNameTextView);

				if (searchList.get(position).getPhoto() != null) {
					ImageLoader.getInstance().displayImage(searchList.get(position).getPhoto().toString(), viewHolder.photoImageView);
				} else {
					viewHolder.photoImageView.setImageResource(R.drawable.me6);
				}
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return searchList.get(position);
			}

			@Override
			public int getCount() {
				return searchList.size();
			}
		});

	}

	private void highlightText(TextView textView) {
		if (textView.getText() != null) {
			String textToHighlight = searchString.substring(0, 1);
			Spannable WordtoSpan = new SpannableString(textView.getText());
			if (WordtoSpan.toString().indexOf(textToHighlight) != -1) {
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1E29D8")), WordtoSpan.toString().indexOf(textToHighlight),
						WordtoSpan.toString().indexOf(textToHighlight) + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				WordtoSpan.setSpan(new android.text.style.BackgroundColorSpan(Color.parseColor("#CDCDEE")),
						WordtoSpan.toString().indexOf(textToHighlight), WordtoSpan.toString().indexOf(textToHighlight) + 1,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				textView.setText(WordtoSpan);
			}
		}
	}

	private void phoneNumberCommonAction() {
		if (phone_call.getText().length() > 0) {
			call_txt.setTextColor(Color.parseColor("#8FBE00"));
		} else {
			call_txt.setTextColor(Color.parseColor("#515151"));
		}

//		if (SearchUtil.search(contactList, phone_call.getText().toString()) != null) {
//			searchList.clear();
//			searchList = SearchUtil.search(contactList, phone_call.getText().toString());
//			searchList = SearchUtil.SortByPriority(searchList, callHistories);
//			((BaseAdapter) searchGridView.getAdapter()).notifyDataSetChanged();
//		} else {
//			searchList.clear();
//			((BaseAdapter) searchGridView.getAdapter()).notifyDataSetChanged();
//		}
	}

	public void initPhoneNumbers(View view, final LayoutInflater inflater) {
		// init views and setting typefaces for all the numbers

		dialer_grid = (GridView)view.findViewById(R.id.dialer_gridView);
		
		phone_call = (TextView) view.findViewById(R.id.phone_call);
		phone_call.setTypeface(Utils.getLightTypeface(getActivity()));

		call_txt = (TextView) view.findViewById(R.id.call_txt);
		call_txt.setTypeface(Utils.getLightTypeface(getActivity()));

		backspace = (ImageView) view.findViewById(R.id.backspace);
		backspace.setImageResource(R.drawable.backspace);
		
		final String[] characterStrings = {"","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz","","+",""};
		final String[] numberStrings = {"1","2","3","4","5","6","7","8","9","*","0","#"};
		
		dialer_grid.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				DialViewHolder viewHolder = null;
				if (convertView==null) {
					convertView = (FrameLayout)inflater.inflate(R.layout.dialer_button, parent,false);
					viewHolder = new DialViewHolder();
					viewHolder.charTextView = (TextView)convertView.findViewById(R.id.dialer_char);
					viewHolder.charTextView.setTypeface(Utils.getLightTypeface(getActivity()));
					viewHolder.numberTextView = (TextView)convertView.findViewById(R.id.dialer_number);
					viewHolder.numberTextView.setTypeface(Utils.getLightTypeface(getActivity()));
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (DialViewHolder) convertView.getTag();
				}
				
				viewHolder.charTextView.setText(characterStrings[position]);
				viewHolder.numberTextView.setText(numberStrings[position]);
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				return numberStrings[position];
			}
			
			@Override
			public int getCount() {
				return 12;
			}
		});
		
		dialer_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if (position==9) {
					phone_call.setText(phone_call.getText()+"*");
				}else if (position==11) {
					phone_call.setText(phone_call.getText()+"#");
				}else if (position==10) {
					phone_call.setText(phone_call.getText()+"0");
				}else {
					phone_call.setText(phone_call.getText()+""+(position+1));
				}
				
				phoneNumberCommonAction();
			}
		});
		
		dialer_grid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position==10) {
					phone_call.setText(phone_call.getText()+"+");
				}
				return true;
			}
		});

		call_txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (phone_call.getText().length() > 0) {
					String url = "tel:" + phone_call.getText();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
					startActivity(intent);
					myVib.vibrate(50);
				}
			}
		});

		backspace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (phone_call.getText().toString().length() > 1) {
					phone_call.setText(phone_call.getText().toString().subSequence(0, phone_call.getText().toString().length() - 1));
					myVib.vibrate(20);
				}else if (phone_call.getText().toString().length() == 1) {
					phone_call.setText("");
					myVib.vibrate(20);
					call_txt.setTextColor(Color.parseColor("#515151"));
				}
				phoneNumberCommonAction();
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

	}

	static class ViewHolder {
		RoundedImageView photoImageView;
		TextView contactNameTextView;
	}
	
	static class DialViewHolder{
		TextView charTextView;
		TextView numberTextView;
	}

}
