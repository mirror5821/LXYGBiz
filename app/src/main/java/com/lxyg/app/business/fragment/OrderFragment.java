package com.lxyg.app.business.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.activity.OrdeListActivity;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.User;
import com.lxyg.app.business.iface.OnSelectTab;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderFragment extends BaseFragment{
	private AppHttpClient mAppHttpClient = new AppHttpClient(ORDER_MANAGE);
	private TextView mTv1,mTv2,mTv3,mTv4,mTv5,mTv6;
	@Override
	public int setLayoutId() {
		return R.layout.fragment_order;
	}

	private User mUser;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mUser = SharePreferencesUtil.getShopInfo(getActivity());
	}
	
	
	private LinearLayout mViewQiang,mViewDaiFa,mViewDaiShou,mViewOk,mViewTui,mViewRang;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitleText("订单");
		
		if(mUser.getQ_verifi()!=1){
			mOnSelectTab.onSelect(4);
		}
		
		
		
		mTv1 = initTextView(R.id.n1);
		mTv2 = initTextView(R.id.n2);
		mTv3 = initTextView(R.id.n3);
		mTv4 = initTextView(R.id.n4);
		mTv5 = initTextView(R.id.n5);
		mTv6 = initTextView(R.id.n6);
		
		mViewQiang = (LinearLayout)view.findViewById(R.id.view1);
		mViewDaiFa = (LinearLayout)view.findViewById(R.id.view2);
		mViewDaiShou = (LinearLayout)view.findViewById(R.id.view3);
		mViewOk = (LinearLayout)view.findViewById(R.id.view4);
		mViewTui = (LinearLayout)view.findViewById(R.id.view5);
		mViewRang = (LinearLayout)view.findViewById(R.id.view6);

		if(AppContext.SHOP_TYPE!=1){
			mViewQiang.setVisibility(View.GONE);
			mViewRang.setVisibility(View.GONE);
		}
		mViewQiang.setOnClickListener(this);
		mViewDaiFa.setOnClickListener(this);
		mViewDaiShou.setOnClickListener(this);
		mViewOk.setOnClickListener(this);
		mViewTui.setOnClickListener(this);
		mViewRang.setOnClickListener(this);
		
		loadData();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}
	
	private void loadData(){
		JSONObject jb = new JSONObject();
		mAppHttpClient.postData1(ORDER_MANAGE, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				
				try {
					JSONObject jb = new JSONObject(data);
					mTv1.setText(jb.getString("count1"));
					mTv2.setText(jb.getString("count2"));
					mTv3.setText(jb.getString("count3"));
					mTv4.setText(jb.getString("count4"));
					mTv5.setText(jb.getString("count5"));
					mTv6.setText(jb.getString("count6"));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			@Override
			public void onError(String error) {
			}
		});
	}
	private OnSelectTab mOnSelectTab;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mOnSelectTab = (OnSelectTab)activity;
	}

	/**
	 * 点击事件
	 * 打开不同的订单列表
	 * 1可抢单 2代发货 3待收货（配送中） 4交易完成 5拒收 6 让单 7流单
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = new Intent(getActivity(),OrdeListActivity.class);
		switch (v.getId()) {
		case R.id.view1:
			intent.putExtra(INTENT_ID, 1);
			break;

		case R.id.view2:
			intent.putExtra(INTENT_ID, 2);
			break;
		case R.id.view3:
			intent.putExtra(INTENT_ID, 3);
			break;
		case R.id.view4:
			intent.putExtra(INTENT_ID, 4);
			break;
		case R.id.view5:
			intent.putExtra(INTENT_ID, 5);
			break;
		case R.id.view6:
			intent.putExtra(INTENT_ID, 6);
			break;
		}
		startActivity(intent);
	}

}
