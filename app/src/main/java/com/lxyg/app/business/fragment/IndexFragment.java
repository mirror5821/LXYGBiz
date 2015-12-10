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
import com.lxyg.app.business.bean.User;
import com.lxyg.app.business.iface.OnSelectTab;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import dev.mirror.library.utils.PriceUtil;

/**
 * 首页
 * @author 王沛栋
 *
 */
public class IndexFragment extends BaseFragment{
	private AppHttpClient mAppHttpClient = new AppHttpClient(HOME);
	private User mUser;
	@Override
	public int setLayoutId() {
		return R.layout.fragment_index;
	}

	private LinearLayout mView1,mView21,mView22,mView3,mView4;
	private TextView mView2,mTvName;
	private TextView mTvSRY,mTvSRT,mTvSRE,mTvOrderNew,mTvOrderDai,mTvOrderQiang;
	private TextView mTvPNew,mTvPHou,mTvAll;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitleText("首页");
//		initImageView(R.id.left_icon).setImageResource(R.drawable.ic_message);
		mUser = SharePreferencesUtil.getShopInfo(getActivity());
		if(mUser.getQ_verifi()!=1){
			mOnSelectTab.onSelect(4);
		}
		
		//昨日营收
		mTvSRY = (TextView)view.findViewById(R.id.sr_y);
		//累计收入
		mTvSRT = (TextView)view.findViewById(R.id.sr_t);
		//余额
		mTvSRE = (TextView)view.findViewById(R.id.sr_e);
		//新的订单
		mTvOrderNew = (TextView)view.findViewById(R.id.o_new);
		//代收货订单
		mTvOrderDai = (TextView)view.findViewById(R.id.o_dai);
		//抢单订单
		mTvOrderQiang = (TextView)view.findViewById(R.id.o_qiang);
		
		//新增用户
		mTvPNew = (TextView)view.findViewById(R.id.p_new);
		//活跃用户
		mTvPHou = (TextView)view.findViewById(R.id.p_huo);
		//累计用户
		mTvAll = (TextView)view.findViewById(R.id.p_all);
		
		mView1 = (LinearLayout)view.findViewById(R.id.view1);
		mView1.setOnClickListener(this);
		
		mView2 = (TextView)view.findViewById(R.id.view2);
		mView2.setOnClickListener(this);
		
		mView21 = (LinearLayout)view.findViewById(R.id.view2_1);
		mView21.setOnClickListener(this);
		
		mView22 = (LinearLayout)view.findViewById(R.id.view2_2);
		mView22.setOnClickListener(this);
		
		mView3 = (LinearLayout)view.findViewById(R.id.view3);
		mView3.setOnClickListener(this);
		
		mView4 = (LinearLayout)view.findViewById(R.id.view4);
		mView4.setOnClickListener(this);
		
		mTvName = (TextView)view.findViewById(R.id.name);
		mTvName.setText(mUser.getName());
		mTvName.setOnClickListener(this);
		
		JSONObject jb = new JSONObject();
		mAppHttpClient.postData1(HOME, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				
				try {
					JSONObject jb = new JSONObject(data);
					JSONObject jb1 = new JSONObject(jb.getString("custormer"));
					
					mTvPNew.setText(jb1.getString("new_add"));
					mTvPHou.setText(jb1.getString("hyl"));
					mTvAll.setText(jb1.getString("allU"));
					
					JSONObject jb2 = new JSONObject(jb.getString("order"));
					mTvOrderDai.setText(jb2.getString("shouHuo"));
					mTvOrderNew.setText(jb2.getString("newOrder"));
					mTvOrderQiang.setText(jb2.getString("robOrder"));
					
					JSONObject jb3 = new JSONObject(jb.getString("account"));
					mTvSRE.setText(PriceUtil.floatToString(jb3.getInt("balance")));
					mTvSRT.setText(PriceUtil.floatToString(jb3.getInt("LJYS")));
					mTvSRY.setText(PriceUtil.floatToString(jb3.getInt("YS")));
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
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.left_icon:
			break;
		case R.id.view1:
			mOnSelectTab.onSelect(2);
			break;
		case R.id.view2:
			mOnSelectTab.onSelect(1);
			break;
		case R.id.view2_1:
			startActivity(new Intent(getActivity(),OrdeListActivity.class).putExtra(INTENT_ID, 2));
			break;
		case R.id.view2_2:
			startActivity(new Intent(getActivity(),OrdeListActivity.class).putExtra(INTENT_ID, 3));
			break;
		case R.id.view3:
			startActivity(new Intent(getActivity(),OrdeListActivity.class).putExtra(INTENT_ID, 1));
			break;
		case R.id.view4:
			mOnSelectTab.onSelect(3);
			break;
		case R.id.name:
			mOnSelectTab.onSelect(4);
			break;
		}
	}
}
