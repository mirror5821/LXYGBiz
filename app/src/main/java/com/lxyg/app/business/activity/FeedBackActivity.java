package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lxyg.app.business.R;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedBackActivity extends BaseActivity{
	private EditText mEt;
	private AppHttpClient mAppHttpClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		setBack();
		setTitleText("意见反馈");
		
		mAppHttpClient = new AppHttpClient(FEED_BACK);
		mEt = initEditText(R.id.et);
		initButton(R.id.sub);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sub:
			String msg = mEt.getText().toString();
			if(TextUtils.isEmpty(msg)){
				showToast("请留下您的宝贵建议!");
				return;
			}
			
			sub(msg);
			break;

		}
	}
	private void sub(String msg) {
		JSONObject jb = new JSONObject();
		try {
			jb.put("content", msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		mAppHttpClient.postData1(FEED_BACK, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				showToast(msg);
				finish();
			}

			@Override
			public void onError(String error) {
				showToast(error);
				
			}
		});
	}
}
