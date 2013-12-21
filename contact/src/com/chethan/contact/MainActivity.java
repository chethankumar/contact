package com.chethan.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.ViewGroup;

import com.chethan.services.ContactService;
import com.chethan.services.ContactsService;
import com.chethan.utils.JazzyViewPager;
import com.chethan.utils.JazzyViewPager.TransitionEffect;
import com.chethan.utils.PagerSlidingTabStrip;
import com.chethan.utils.Preferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.Parse;

public class MainActivity extends FragmentActivity {

	private JazzyViewPager pager;
	private MyPageAdapter pageAdapter;
	public static ContactService contactService = null;
	public boolean mIsBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Preferences.initPreferences(this);
		
		Parse.initialize(this, "52RxIGTkCts1unU6ERHP4Z5u5WLcaMjGWufohyKR", "Lmmu0szFdeqXSWUqCDDwgUEbxcUVqQaZTZOImhcd");
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
				// .displayer(new FadeInBitmapDisplayer(1500))
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(2)
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);

		customInitSequence();

		mIsBound = true;

		// ParseAnalytics.trackAppOpened(getIntent());
		// ------------------------------------------

		// final TextView tv = (TextView)findViewById(R.id.textview);
		// tv.setText("contacts");

		//
		// initTabs();
		//
		// List<Fragment> fragments = getFragments();
		//
		// pageAdapter = new MyPageAdapter(getSupportFragmentManager(),
		// fragments);
		//
		// ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
		// pager.setOffscreenPageLimit(4);
		// pager.setPageMargin(1);
		// pager.setAdapter(pageAdapter);

		// ParseObject testObject = new ParseObject("sampleClass");
		// testObject.put("phoneNumber", "9449205174");
		// testObject.saveInBackground();

		// ParseUser user = new ParseUser();
		// user.setUsername("9449205174");
		// user.setPassword("9449205174");
		// user.put("phonenumber", "9449205174");
		// user.put("firstname", "chethan");
		//
		// user.signUpInBackground(new SignUpCallback() {
		//
		// @Override
		// public void done(ParseException arg0) {
		// if(arg0==null){
		// tv.setText("signup success");
		// }else{
		// tv.setText("could not signup"+arg0.getMessage());
		// }
		// }
		// });

		Preferences.setTheme(Preferences.WHITE);
		Intent newServiceIntent = new Intent(getApplicationContext(), ContactsService.class);
		getApplicationContext().startService(newServiceIntent);
	}

	private void customInitSequence() {
		List<Fragment> fragments = getFragments();

		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);

		pager = (JazzyViewPager) findViewById(R.id.viewpager);
		pager.setTransitionEffect(TransitionEffect.CubeOut);
		pager.setOffscreenPageLimit(4);
		pager.setPageMargin(1);
		pager.setAdapter(pageAdapter);
		initTabs(); // it is important to have tabs after page adapter is set
	}

	private void initTabs() {
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.main_tabs);
		tabs.setIndicatorColor(Color.parseColor("#00B4FF"));
		tabs.setViewPager(pager);
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		fList.add(PhoneFragment.newInstance());
		fList.add(CalllogFragment.newInstance());
		// if (ThemeUtil.gridOrFancyScroller.equalsIgnoreCase("fancy")) {
		// fList.add(PeopleFragment.newInstance(contactService));
		// } else {
		fList.add(AllContactsFragment.newInstance());
		// }
		fList.add(MeFragment.newInstance());
		return fList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class MyPageAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Object obj = super.instantiateItem(container, position);
			pager.setObjectForPosition(obj, position);
			return obj;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Phone";
			case 1:
				return " Logs ";
			case 2:
				return "Contacts";
			case 3:
				return " Me ";
			default:
				return super.getPageTitle(position);

			}

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
