package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.User;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.PassWordUtil;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePassActivity extends BaseActivity{
	private EditText mEtPass1,mEtPass2;
	
	private String mPass;
	private User mUser;
	
	private AppHttpClient mAppHttpClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_pass);
		setBack();
		setTitleText("修改密码");
		mAppHttpClient = new AppHttpClient(UPDATE_PASS);
		
		mEtPass1 = initEditText(R.id.pass1);
		mEtPass2 = initEditText(R.id.pass2);
		
		initButton(R.id.sub);
		
		mUser = SharePreferencesUtil.getLoginInfo(getApplicationContext());
	}
	private void sub(){
		JSONObject jb = new JSONObject();
		try {
			jb.put("password", PassWordUtil.md5PassWord(mPass));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		mAppHttpClient.postData1(UPDATE_PASS, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				//重新保存登陆数据
				SharePreferencesUtil.saveLoginInfo(getApplicationContext(), mUser.getPhone(), mPass);
				showToast(msg);
				finish();
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sub:
			mPass = mEtPass1.getText().toString().trim();
			
			if (mPass.equals(mEtPass2.getText().toString().trim())) {
				sub();
			}else{
				showToast("两次输入密码不一致");
			}
			break;
		}
	}
}
