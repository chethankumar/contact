package com.chethan.contact;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chethan.utils.AccountUtils;
import com.chethan.utils.AccountUtils.UserProfile;
import com.chethan.utils.Preferences;
import com.chethan.utils.RoundedImageView;
import com.chethan.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MeFragment extends Fragment {

	private TextView name;
	private TextView tagline;

	ImageView profile_background;
	ImageView profile_foreground;
	RoundedImageView profilePicture;
	ImageView editProfile;

	String meid;

	public static final MeFragment newInstance() {
		MeFragment f = new MeFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.me, container, false);
		view.setBackgroundResource(Preferences.getScreenBackground());
		name = (TextView) view.findViewById(R.id.me_profile_name);
		tagline = (TextView) view.findViewById(R.id.me_profile_tagline);

		profile_background = (ImageView) view.findViewById(R.id.me_profile_background);
		profile_background.getLayoutParams().width = Utils.getWidthForProfileBackground(getActivity());
		profile_background.getLayoutParams().height = Utils.getWidthForProfileBackground(getActivity());
		// profile_background.setAlpha(110);

		profile_foreground = (ImageView) view.findViewById(R.id.me_profile_foreground);
		profile_foreground.getLayoutParams().width = Utils.getWidthForProfileBackground(getActivity());
		profile_foreground.getLayoutParams().height = Utils.getWidthForProfileBackground(getActivity());
		// profile_foreground.setAlpha(11);

		profilePicture = (RoundedImageView) view.findViewById(R.id.me_profile_circle);
		RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(Utils.getWidthForProfileCircle(getActivity()),
				Utils.getWidthForProfileCircle(getActivity()));
		relativeLayout.leftMargin = (Utils.getWidthForProfileBackground(getActivity()) - Utils.getWidthForProfileCircle(getActivity())) / 2;
		relativeLayout.topMargin = (Utils.getWidthForProfileBackground(getActivity()) - Utils.getWidthForProfileCircle(getActivity())) * 2 / 10;
		profilePicture.setLayoutParams(relativeLayout);
		// profilePicture.setBorderColor(Color.WHITE);

		name.setTypeface(Utils.getLightTypeface(getActivity()));
		tagline.setTypeface(Utils.getLightTypeface(getActivity()));

		editProfile = (ImageView) view.findViewById(R.id.me_profile_edit);

		populateExistingMeDetails();

		editProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW).setData(ContactsContract.Profile.CONTENT_URI));

			}
		});
		return view;

	}

	private void populateExistingMeDetails() {
		Cursor c = getActivity().getContentResolver().query((ContactsContract.Profile.CONTENT_URI), null, null, null, null);
		int count = c.getCount();
		String[] columnNames = c.getColumnNames();
		boolean b = c.moveToFirst();
		int position = c.getPosition();
		if (count == 1 && position == 0) {
			meid = c.getString(c.getColumnIndex(ContactsContract.Profile._ID));

			String displayName = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
			if (displayName != null && !displayName.contains("gmail.com")) {
				name.setText(displayName);
			} else {
				name.setText("your Good name please");
			}

			String phoUri = c.getString(c.getColumnIndex(ContactsContract.Profile.PHOTO_URI));
			if (phoUri != null) {
				ImageLoader.getInstance().displayImage(phoUri, profile_background);
				ImageLoader.getInstance().displayImage(phoUri, profilePicture);
			}

			// if
			// (c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE))
			// != null) {
			// tagline.setText((c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE))));
			// }

			UserProfile profile = AccountUtils.getUserProfile(getActivity());
			System.out.println("test");
		}
	}
}
