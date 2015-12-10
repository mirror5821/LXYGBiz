package com.lxyg.app.business.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lxyg.app.business.R;
import com.lxyg.app.business.activity.RegisterActivity;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.PassWordUtil;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Register2Fragment extends BaseFragment{
	@Override
	public int setLayoutId() {
		return R.layout.fragment_register2;
	}
	
	private EditText mEtPass1,mEtPass2;
	private Button mBtn;
	
	private String mPass1,mPass2;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mEtPass1 = (EditText)view.findViewById(R.id.pass1);
		mEtPass2 = (EditText)view.findViewById(R.id.pass2);
		
		mBtn = (Button)view.findViewById(R.id.sub);
		mBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sub:
			mPass1 = mEtPass1.getText().toString().trim();
			mPass2 = mEtPass2.getText().toString().trim();
			
			if(TextUtils.isEmpty(mPass1)||TextUtils.isEmpty(mPass2)){
				showToast("请输入密码!");
				return;
			}
			if(!mPass1.equals(mPass2)){
				showToast("两次密码输入不一致!");
				return;
			}
			sub();
			break;

		}
	}
	
	private void sub(){
		final RegisterActivity activity = (RegisterActivity)getActivity();
		final String phone = activity.getPhone();
		final String pass = PassWordUtil.md5PassWord(mPass1);
		activity.setPass(pass);
		
		JSONObject jb = new JSONObject();
		try {
			jb.put("phone",phone );
			jb.put("code", activity.getCode());
			jb.put("password", pass);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AppHttpClient ac = new AppHttpClient(REGISTER);
		ac.postData1(REGISTER, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				//先注册先我的信息  
				SharePreferencesUtil.saveLoginInfo(getActivity(), phone, mPass1);
				activity.switchFragment(new Register3Fragment());
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}
	
	
}
