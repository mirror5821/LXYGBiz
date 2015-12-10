package com.lxyg.app.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Shop;

import dev.mirror.library.utils.JsonUtils;

/**
 * 商户信息管理
 * @author 王沛栋
 *
 */
public class SellerManagerActivity extends BaseActivity{
	
	private Shop mShop;
	private String mIntent;

	private LinearLayout mView1,mView2;
	private TextView mTvTitle,mTvTitle2,mTvTitle3;
	private TextView mTvCover;
	private ImageView mImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopinfo);
		setBack();
		setTitleText("商户信息管理");
		
		mIntent = getIntent().getExtras().getString(INTENT_ID);
		mShop = JsonUtils.parse(mIntent, Shop.class);
		
		initTextView(R.id.map).setText(mShop.getProvince_name()+" "+mShop.getCity_name()+" "+mShop.getArea_name());
		initTextView(R.id.name).setText(mShop.getLink_man());
		initTextView(R.id.no).setText(mShop.getUser_name());
		initTextView(R.id.street).setText(mShop.getStreet());

		mTvCover = (TextView)findViewById(R.id.tv_cover);

		String html = "<font size='22' color='#000000'>商家封面</font><font size-'12' color='#999999'>(点击修改封面活动)</font>";
		mTvCover.setText(Html.fromHtml(html));

		initTextView(R.id.phone).setText(mShop.getPhone());
		mTvTitle = (TextView)findViewById(R.id.shop_title);
		mImg = (ImageView)findViewById(R.id.img1);
		mTvTitle2 = (TextView)findViewById(R.id.title2);
		mTvTitle3 = (TextView)findViewById(R.id.title3);

		mView1 = (LinearLayout)findViewById(R.id.view1);
		mView1.setVisibility(View.GONE);
		mView1.setOnClickListener(this);
		if(mShop.getIs_norm() != 1){
			mView1.setVisibility(View.VISIBLE);

			mTvTitle.setText(mShop.getTitle());
			mTvTitle2.setText(mShop.getTitle2());
			mTvTitle3.setText(mShop.getTitle3());
			AppContext.displayImage(mImg,mShop.getCover_img());
		}
		
//		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
//		fragmentTransaction.replace(R.id.content, new Register3Fragment()).commit();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.view1:
				startActivity(new Intent(SellerManagerActivity.this,NonStandardInfoUpdateActivity.class).putExtra(INTENT_ID,mIntent));
				//为了避免修改后数据不同步  杀掉他
				finish();
				break;
		}
	}
}
