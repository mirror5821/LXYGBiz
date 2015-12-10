package com.lxyg.app.business.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.activity.CaptureActivity;
import com.lxyg.app.business.R;

public class AccountActivity extends BaseActivity{
	private String mIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		setBack();
		setTitleText("账号设置");
		
		mIntent = getIntent().getExtras().getString(INTENT_ID);
		
		initLinearLayout(R.id.view1);
		initLinearLayout(R.id.view2);
		initLinearLayout(R.id.view3);
		initLinearLayout(R.id.view4).setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.view1:
			startActivity(new Intent(getActivity(),UpdatePassActivity.class));
			break;

		case R.id.view2:
			startActivity(new Intent(getActivity(),UpdateAccountPassActivity.class));
			break;
			
		case R.id.view3:
			startActivity(new Intent(getActivity(),ShopQRImgActivity.class).putExtra(INTENT_ID, mIntent));
			break;
		case R.id.view4:
			startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
			//(new Intent(getActivity(),CaptureActivity.class));
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode ==Activity.RESULT_OK){

			switch (requestCode) {
			case 2001:
				Uri uriData = data.getData();
				showToast(uriData.toString());
				break;

			}
		}
	}
}
