package com.lxyg.app.business.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.activity.AccountActivity;
import com.lxyg.app.business.activity.GoodsActivity;
import com.lxyg.app.business.activity.GoodsNewActivity;
import com.lxyg.app.business.activity.NonStandardActivity;
import com.lxyg.app.business.activity.SellerManagerActivity;
import com.lxyg.app.business.activity.SetActivity;
import com.lxyg.app.business.activity.ZizhiActivity;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Shop;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONObject;

import dev.mirror.library.utils.JsonUtils;

/**
 * 我的
 * @author 王沛栋
 *
 */
public class MyFragment extends BaseFragment{
	//用于存储店铺信息
	private String mShopInfo;
	private Shop mShop;
	
	@Override
	public int setLayoutId() {
		return R.layout.fragment_my;
	}
	
	private LinearLayout mViewZizhi,mViewSeller,mViewGoods,mViewCode,mViewSet;
	private TextView mTvShopName;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitleText("我的");
		mViewZizhi = (LinearLayout)view.findViewById(R.id.view_zizhi);
		mViewSeller = (LinearLayout)view.findViewById(R.id.view_seller);
		mViewGoods = (LinearLayout)view.findViewById(R.id.view_goods);
		mViewCode = (LinearLayout)view.findViewById(R.id.view_code);
		mViewSet = (LinearLayout)view.findViewById(R.id.view_set);
		mTvShopName = (TextView)view.findViewById(R.id.name);
		
		loadShopInfo();
		
	}
	private AppHttpClient mAppHttpClient;
	private void loadShopInfo(){
		JSONObject jb =new JSONObject();
		if(mAppHttpClient == null){
			mAppHttpClient = new AppHttpClient(SHOP_INFO);
		}
		
		mAppHttpClient.postData1(SHOP_INFO, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mShopInfo = data;
				mShop = JsonUtils.parse(data, Shop.class);
				
				mViewZizhi.setOnClickListener(MyFragment.this);
				mViewSeller.setOnClickListener(MyFragment.this);
				mViewGoods.setOnClickListener(MyFragment.this);
				mViewCode.setOnClickListener(MyFragment.this);
				mViewSet.setOnClickListener(MyFragment.this);
				
				
				mTvShopName.setText(mShop.getName());
			}

			@Override
			public void onError(String error) {
				
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadShopInfo();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.view_zizhi:
			startActivity(new Intent(getActivity(),ZizhiActivity.class).putExtra(INTENT_ID, mShopInfo));
			break;
		case R.id.view_seller:
			if(mShop.getQ_verifi()!=1){
				showToast("您的商户资质尚未申通通过");
				return;
			}
			startActivity(new Intent(getActivity(),SellerManagerActivity.class).putExtra(INTENT_ID, mShopInfo));
			break;
		case R.id.view_goods:
			if(mShop.getQ_verifi()!=1){
				showToast("您的商户资质尚未申通通过");
				return;
			}
			if(AppContext.SHOP_TYPE == 1)
				startActivity(new Intent(getActivity(), GoodsNewActivity.class));
			else
				startActivity(new Intent(getActivity(), NonStandardActivity.class).
						putExtra(INTENT_ID, "110"));
			break;
		case R.id.view_code://账户
			if(mShop.getQ_verifi()!=1){
				showToast("您的商户资质尚未申通通过");
				return;
			}
			startActivity(new Intent(getActivity(),AccountActivity.class).putExtra(INTENT_ID, mShop.getEwm_code()));
			break;
		
		case R.id.view_set://设置
			startActivity(new Intent(getActivity(),SetActivity.class));
			break;
		}
	}
}
