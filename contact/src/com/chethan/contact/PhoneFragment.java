package com.chethan.contact;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chethan.objects.SimpleContact;
import com.chethan.services.ContactService;
import com.chethan.utils.RoundedImageView;
import com.chethan.utils.SearchUtil;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhoneFragment extends Fragment {

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
	private static ContactService contactService;
	private GridView searchGridView;
	private ArrayList<SimpleContact> searchList = new ArrayList<SimpleContact>();
	private ArrayList<SimpleContact> contactList = new ArrayList<SimpleContact>();

	private StringBuilder searchString;

	public static final PhoneFragment newInstance(ContactService service) {
		PhoneFragment f = new PhoneFragment();
		contactService = service;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.phone, container, false);
		myVib = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		initPhoneNumbers(v);
		searchGridView = (GridView) v.findViewById(R.id.SearchgridView);

		contactList = contactService.getSimpleContactsList();
		initAdapterOfSearchGridView(inflater);

		return v;
	}

	private void initAdapterOfSearchGridView(final LayoutInflater inflater) {

		searchGridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder viewHolder;
				int width = Utils.getWidthForCard(getActivity()) - Utils.getPx(getActivity(), 10);
				if (view == null) {
					view = (LinearLayout) inflater.inflate(R.layout.me_contact, parent, false);
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
		});/*
			 * () {
			 * 
			 * @Override public void unregisterDataSetObserver(DataSetObserver
			 * arg0) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void registerDataSetObserver(DataSetObserver
			 * arg0) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public boolean isEmpty() { return false; }
			 * 
			 * @Override public boolean hasStableIds() { // TODO Auto-generated
			 * method stub return false; }
			 * 
			 * @Override public int getViewTypeCount() { return 1; }
			 * 
			 * @Override public View getView(int position, View view, ViewGroup
			 * container) { ViewHolder viewHolder; int width =
			 * Utils.getWidthForCard(getActivity()) - Utils.getPx(getActivity(),
			 * 10); if (view == null) { view = (LinearLayout)
			 * inflater.inflate(R.layout.me_contact, container, false);
			 * viewHolder = new ViewHolder(); view.setLayoutParams(new
			 * AbsListView.LayoutParams(width,
			 * Utils.getHeightForCard(getActivity())));
			 * viewHolder.photoImageView = (RoundedImageView)
			 * view.findViewById(R.id.allContactPhoto);
			 * viewHolder.photoImageView.getLayoutParams().height = width;//
			 * -Utils.getPx(getActivity(), // 15);
			 * viewHolder.photoImageView.getLayoutParams().width = width;//
			 * -Utils.getPx(getActivity(), // 15);
			 * viewHolder.contactNameTextView = (TextView)
			 * view.findViewById(R.id.allContactName); view.setTag(viewHolder);
			 * } else { viewHolder = (ViewHolder) view.getTag(); }
			 * 
			 * viewHolder.contactNameTextView.setText(searchList.get(position).
			 * getName()); // highlight matching text
			 * highlightText(viewHolder.contactNameTextView); if
			 * (searchList.get(position).getPhoto() != null) {
			 * ImageLoader.getInstance
			 * ().displayImage(searchList.get(position).getPhoto().toString(),
			 * viewHolder.photoImageView); } else {
			 * viewHolder.photoImageView.setImageResource(R.drawable.me6); }
			 * return view; }
			 * 
			 * @Override public int getItemViewType(int arg0) { // TODO
			 * Auto-generated method stub return 0; }
			 * 
			 * @Override public long getItemId(int arg0) { return arg0; }
			 * 
			 * @Override public Object getItem(int arg0) { return
			 * searchList.get(arg0); }
			 * 
			 * @Override public int getCount() { return searchList.size(); }
			 * 
			 * @Override public boolean isEnabled(int arg0) { return true; }
			 * 
			 * @Override public boolean areAllItemsEnabled() { return true; }
			 * });
			 */

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
		myVib.vibrate(20);
		if (call_txt.getText().length() > 0) {
			call_txt.setTextColor(Color.parseColor("#8FBE00"));
		} else {
			call_txt.setTextColor(Color.parseColor("#515151"));
		}

		if (SearchUtil.search(contactList, phone_call.getText().toString()) != null) {
			searchList.clear();
			searchList = SearchUtil.search(contactList, phone_call.getText().toString());
			searchList = SearchUtil.SortByPriority(searchList, contactService.getCallHistorySimpleContactList());
			((BaseAdapter) searchGridView.getAdapter()).notifyDataSetChanged();
		} else {
			searchList.clear();
			((BaseAdapter) searchGridView.getAdapter()).notifyDataSetChanged();
		}
	}

	public void initPhoneNumbers(View view) {
		// init views and setting typefaces for all the numbers
		phone_0 = (TextView) view.findViewById(R.id.phone_0);
		phone_0.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_1 = (TextView) view.findViewById(R.id.phone_1);
		phone_1.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_2 = (TextView) view.findViewById(R.id.phone_2);
		phone_2.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_3 = (TextView) view.findViewById(R.id.phone_3);
		phone_3.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_4 = (TextView) view.findViewById(R.id.phone_4);
		phone_4.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_5 = (TextView) view.findViewById(R.id.phone_5);
		phone_5.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_6 = (TextView) view.findViewById(R.id.phone_6);
		phone_6.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_7 = (TextView) view.findViewById(R.id.phone_7);
		phone_7.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_8 = (TextView) view.findViewById(R.id.phone_8);
		phone_8.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_9 = (TextView) view.findViewById(R.id.phone_9);
		phone_9.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_star = (TextView) view.findViewById(R.id.phone_star);
		phone_star.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_hash = (TextView) view.findViewById(R.id.phone_hash);
		phone_hash.setTypeface(Utils.getLightTypeface(getActivity()));

		phone_call = (TextView) view.findViewById(R.id.phone_call);
		phone_call.setTypeface(Utils.getLightTypeface(getActivity()));

		call_txt = (TextView) view.findViewById(R.id.call_txt);
		call_txt.setTypeface(Utils.getLightTypeface(getActivity()));

		backspace = (ImageView) view.findViewById(R.id.backspace);
		backspace.setImageResource(R.drawable.backspace);

		// Onclick listeners for all the numbers and call button
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

		// bad UX to have call and backspace adjacent to each other
		// phone_call.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// if(phone_call.getText().length()>0){
		// String url = "tel:"+phone_call.getText();
		// Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
		// startActivity(intent);
		// myVib.vibrate(50);
		// }
		// }
		// });

		phone_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "0");
				phoneNumberCommonAction();
			}
		});
		phone_0.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				phone_call.setText(phone_call.getText() + "+");
				phoneNumberCommonAction();
				return true;
			}
		});
		phone_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "1");
				phoneNumberCommonAction();
			}
		});
		phone_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "2");
				phoneNumberCommonAction();
			}
		});
		phone_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "3");
				phoneNumberCommonAction();
			}
		});
		phone_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "4");
				phoneNumberCommonAction();
			}
		});
		phone_5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "5");
				phoneNumberCommonAction();
			}
		});
		phone_6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "6");
				phoneNumberCommonAction();
			}
		});
		phone_7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "7");
				phoneNumberCommonAction();
			}
		});
		phone_8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "8");
				phoneNumberCommonAction();
			}
		});
		phone_9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "9");
				phoneNumberCommonAction();
			}
		});
		phone_star.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "*");
				phoneNumberCommonAction();
			}
		});
		phone_hash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_call.setText(phone_call.getText() + "#");
				phoneNumberCommonAction();
			}
		});

		backspace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (phone_call.getText().toString().length() > 0) {
					phone_call.setText(phone_call.getText().toString().subSequence(0, phone_call.getText().toString().length() - 1));
					myVib.vibrate(20);
				}
				if (phone_call.getText().toString().equals("") || phone_call.getText().toString().length() == 0) {
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

}
