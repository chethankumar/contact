package com.chethan.contact;

import java.util.ArrayList;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chethan.objects.Person;
import com.chethan.objects.SimpleContact;
import com.chethan.utils.Preferences;
import com.chethan.utils.RoundedImageView;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twotoasters.jazzylistview.JazzyGridView;

public class AllContactsFragment extends Fragment {

	private Vibrator myVib;
	private ArrayList<SimpleContact> contactList = new ArrayList<SimpleContact>();
	private JazzyGridView gridView;
	private TextView alphabetTextView;

	public static final AllContactsFragment newInstance() {
		AllContactsFragment f = new AllContactsFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.all_contacts, container, false);
		v.setBackgroundResource(Preferences.getScreenBackground());
		myVib = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		gridView = (JazzyGridView) v.findViewById(R.id.allContactsGridView);
		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getWidthForContact(getActivity()),
				android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(params);
		gridView.setSmoothScrollbarEnabled(true);
		alphabetTextView = (TextView) v.findViewById(R.id.allContactsAlphabets);
		android.widget.LinearLayout.LayoutParams alphabets_params = new LinearLayout.LayoutParams(
				Utils.getWidthForAlphabets(getActivity()), android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
		alphabetTextView.setLayoutParams(alphabets_params);
		alphabetTextView.setTypeface(Utils.getSegoeTypeface(getActivity()));

		populateContactsGridView(inflater);
		getActivity().registerReceiver(receiver, new IntentFilter("com.contacts.sendContactDetails"));
		return v;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			contactList = intent.getExtras().getParcelableArrayList("simpleContactList");
			((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter("com.contacts.sendContactDetails"));
		((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
	}

	private void populateContactsGridView(final LayoutInflater inflater) {
		gridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder;
				int width = Utils.getWidthForCard(getActivity());

				if (convertView == null) {
					convertView = (LinearLayout) inflater.inflate(R.layout.all_contacts_tile, parent, false);
					viewHolder = new ViewHolder();
					convertView.setLayoutParams(new AbsListView.LayoutParams(width, Utils.getHeightForCard(getActivity())));
					viewHolder.photoImageView = (RoundedImageView) convertView.findViewById(R.id.allContactPhoto);
					viewHolder.photoImageView.getLayoutParams().height = width;
					viewHolder.photoImageView.getLayoutParams().width = width;
					viewHolder.name = (TextView) convertView.findViewById(R.id.allContactName);
					viewHolder.name.setTypeface(Utils.getSegoeTypeface(getActivity()));
					viewHolder.name.setTextColor(Color.parseColor("#484848"));
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}

				viewHolder.name.setText(contactList.get(position).getName());
				if (contactList.get(position).getPhoto() != null) {
					ImageLoader.getInstance().displayImage(contactList.get(position).getPhoto().toString(), viewHolder.photoImageView);
				} else {
					viewHolder.photoImageView.setImageResource(R.drawable.me6);
				}

				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return contactList.get(position);
			}

			@Override
			public int getCount() {
				return contactList.size();
			}
		});

		// tap on a contact tile to make a call
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				new GetAllDetailsOfAContact().execute(contactList.get(position).getId());
				// String url = "tel:" +
				// contactList.get(position).getPhoneNumber();
				// Intent intent = new Intent(Intent.ACTION_CALL,
				// Uri.parse(url));
				// startActivity(intent);
				myVib.vibrate(50);
			}
		});
	}

	static class ViewHolder {
		RoundedImageView photoImageView;
		TextView name;
	}

	class GetAllDetailsOfAContact extends AsyncTask<String, Integer, Person> {

		long start, end;

		@Override
		protected Person doInBackground(String... params) {
			start = new Date().getTime();
			ContentResolver cr = getActivity().getContentResolver();

			// using params[0] because i will only pass a single contactid.
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID + " = ?",
					new String[] { params[0] }, null);
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					Person person = new Person();
					String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

					if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						person.setId(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));
						person.setName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
								new String[] { cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)) }, null);
						while (pCur.moveToNext()) {
							person.addPhoneNumber(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
						}
						pCur.close();

						// get email and type

						Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
						while (emailCur.moveToNext()) {
							// This would allow you get several email addresses
							// if the email addresses were stored in an array
							String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

							System.out.println("Email " + email + " Email Type : " + emailType);
							person.addEmail(emailType, email);
						}
						emailCur.close();

						// Get note.......
						String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						String[] noteWhereParams = new String[] { id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
						Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
						if (noteCur.moveToFirst()) {
							String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
							System.out.println("Note " + note);
							person.setTagline(note);
						}
						noteCur.close();

						// Get Postal Address....

						String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						String[] addrWhereParams = new String[] { id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
						Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);
						while (addrCur.moveToNext()) {
							String poBox = addrCur.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
							String street = addrCur.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
							String city = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
							String state = addrCur.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
							String country = addrCur.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
							String type = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

							// Do something with these....
							person.setPlace(city);
						}
						addrCur.close();

						// Get Organizations.........

						String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						String[] orgWhereParams = new String[] { id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE };
						Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI, null, orgWhere, orgWhereParams, null);
						if (orgCur.moveToFirst()) {
							String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
							String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
							person.addOrganization(title, orgName);
						}
						orgCur.close();

						String image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
						if (image_uri != null) {
							person.setPhoto(Uri.parse(image_uri));
						}
					}
					end = new Date().getTime();
					return person;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Person result) {
			super.onPostExecute(result);
			Log.d("time", (end - start) + "");
		}

	}
}
