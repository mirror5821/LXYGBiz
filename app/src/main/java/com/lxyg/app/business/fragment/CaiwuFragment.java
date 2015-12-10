package com.lxyg.app.business.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.activity.CaiwuActivity;
import com.lxyg.app.business.activity.TiXianActivity;
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
 * 财务fragment
 * @author 王沛栋
 *
 */
public class CaiwuFragment extends BaseFragment{
	private AppHttpClient mAppHttpClient;

	@Override
	public int setLayoutId() {
		return R.layout.fragment_caiwu;
	}

	private OnSelectTab mOnSelectTab;

	private User mUser;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUser = SharePreferencesUtil.getShopInfo(getActivity());
		mAppHttpClient = new AppHttpClient(ACCOUNT_MANAGER);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mOnSelectTab = (OnSelectTab)activity;
	}

	private TextView mTvY1,mTvY2,mTvY3,mTvY4,mTvY5;
	private TextView mTvA1,mTvA2,mTvA3,mTvA4,mTvA5,mTvA6,mTvRight;
	private Button mBtn;

	private int mQiang,mTiXian,mRang,mYingshou;
	private int mQiang2,mTiXian2,mRang2,mYingshou2;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitleText("财务");

		if(mUser.getQ_verifi()!=1){
			mOnSelectTab.onSelect(4);
		}

		mTvRight = (TextView)view.findViewById(R.id.right_text);
		mTvRight.setText("账务明细");

		mTvY1 = (TextView)view.findViewById(R.id.y1);
		mTvY2 = (TextView)view.findViewById(R.id.y2);
		mTvY3 = (TextView)view.findViewById(R.id.y3);
		mTvY4 = (TextView)view.findViewById(R.id.y4);
		mTvY5 = (TextView)view.findViewById(R.id.y5);

		mTvA1 = (TextView)view.findViewById(R.id.a1);
		mTvA2 = (TextView)view.findViewById(R.id.a2);
		mTvA3 = (TextView)view.findViewById(R.id.a3);
		mTvA4 = (TextView)view.findViewById(R.id.a4);
		mTvA5 = (TextView)view.findViewById(R.id.a5);
		mTvA6 = (TextView)view.findViewById(R.id.a6);
		
		mBtn = (Button)view.findViewById(R.id.tixian);
		mBtn.setOnClickListener(this);
		mTvRight.setOnClickListener(this);

		JSONObject jb = new JSONObject();
		mAppHttpClient.postData1(ACCOUNT_MANAGER, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				try {
					JSONObject jb = new JSONObject(data);
					JSONObject jb1 = new JSONObject(jb.getString("yesterday"));

					mQiang = jb1.getInt("ROB");
					mTiXian = jb1.getInt("TX");
					mRang = jb1.getInt("LET");
					mYingshou = jb1.getInt("YS");


					mTvY1.setText(PriceUtil.floatToString(mYingshou));
					mTvY2.setText(PriceUtil.floatToString(mRang));
					mTvY3.setText(PriceUtil.floatToString(mQiang));
					mTvY4.setText(PriceUtil.floatToString(mTiXian));
					mTvY5.setText(PriceUtil.floatToString(mYingshou+mRang-mQiang));


					JSONObject jb2 = new JSONObject(jb.getString("all"));

					mQiang2 = jb2.getInt("ROB");
					mTiXian2 = jb2.getInt("TX");
					mRang2 = jb2.getInt("LET");
					mYingshou2 = jb2.getInt("YS");


					mTvA1.setText(PriceUtil.floatToString(mYingshou2));
					mTvA2.setText(PriceUtil.floatToString(mRang2));
					mTvA3.setText(PriceUtil.floatToString(mQiang2));
					mTvA4.setText(PriceUtil.floatToString(mTiXian2));
					mTvA5.setText(PriceUtil.floatToString(mYingshou2+mRang2-mQiang2));

					mTvA6.setText(PriceUtil.floatToString(jb.getInt("balance")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String error) {

			}
		});

	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tixian:
			startActivity(new Intent(getActivity(),TiXianActivity.class));
			break;
		case R.id.right_text:
			startActivity(new Intent(getActivity(),CaiwuActivity.class));
			break;

		}
	}
}
