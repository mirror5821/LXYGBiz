package com.lxyg.app.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lxyg.app.business.R;
import com.lxyg.app.business.fragment.GoodsListFragment;
import com.lxyg.app.business.iface.GoodsListener;


public class GoodsListActivity extends BaseActivity {

	private ViewPager mViewPager;
	private RadioButton [] radios = new RadioButton[2];
	private RadioGroup mRg;

	private String mTypeId;
	private int mBrand = 0;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);

		setBack();
		setTitleText("商品列表");

		Bundle bundle = getIntent().getExtras();
		mTypeId = bundle.getString(INTENT_ID);
		mBrand = bundle.getInt("brandId",0);

		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				selectTab(arg0);
				
				Fragment fragment = (Fragment) mViewPager.getAdapter().instantiateItem(
						mViewPager, mViewPager.getCurrentItem());
				if (fragment instanceof GoodsListener) {
					((GoodsListFragment) fragment).change();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		radios[0] = (RadioButton)findViewById(R.id.rb1);
		radios[1] = (RadioButton)findViewById(R.id.rb2);

		mRg = (RadioGroup)findViewById(R.id.rg);

		mRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb1:
					mViewPager.setCurrentItem(0);
					break;

				case R.id.rb2:
					mViewPager.setCurrentItem(1);
					break;
				}
			}
		});
		if(savedInstanceState == null){
			selectTab(0);
		}
	}
	
	private void selectTab(int tab){
		radios[tab].setChecked(true);
	}

	class ViewPagerAdapter extends FragmentStatePagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			GoodsListFragment commentFragment = new GoodsListFragment();
			Bundle b = new Bundle();
			b.putString(INTENT_ID, mTypeId);
			b.putInt("BrandId",mBrand);
			String type = "1";
			String notify = getResources().getString(R.string.goods_all_notif);
			switch (arg0) {
			case 0:
				type = "1";		
				notify = getResources().getString(R.string.goods_all_notif);
				break;
			case 1:
				type = "2";
				notify = getResources().getString(R.string.goods_my_notif);
				break;
			}
			b.putString(NOTIFY_TV, notify);
			b.putString(TYPE_ID, type);
			commentFragment.setArguments(b);
			return commentFragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

	}
}

