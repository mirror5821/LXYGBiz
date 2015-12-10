package com.lxyg.app.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.User;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.NetUtil;
import com.lxyg.app.business.utils.PassWordUtil;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import dev.mirror.library.utils.JsonUtils;

/**
 * 登录
 * @author 王沛栋
 *
 */
public class LoginActivity extends BaseActivity{
	private EditText mEtPhone,mEtPass;
	private User mUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setRightTitle("注册");
		setTitleText("登陆蜂域商家版");

		mEtPhone = initEditText(R.id.name);
		mEtPass = initEditText(R.id.no);

		initTextView(R.id.forget);
		initButton(R.id.sub);

	}


	private String mPhone,mPass;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(isDownloadNewVersion){
			showToast("请安装新版本!");
			return;
		}
		switch (v.getId()) {
		case R.id.right_text:
			startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
			break;
		case R.id.sub:
			mPhone = mEtPhone.getText().toString().trim();
			mPass = mEtPass.getText().toString().trim();

			if(TextUtils.isEmpty(mPhone)){
				showToast("请输入登录手机号码!");
				return;
			}
			if(TextUtils.isEmpty(mPass)){
				showToast("请输入登录密码!");
				return;
			}
			sub(mPhone, mPass);
			break;
		//忘记密码
		case R.id.forget:
			startActivity(new Intent(LoginActivity.this,ForgetPassActivity.class));
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mUser = SharePreferencesUtil.getLoginInfo(getApplicationContext());
		if(mUser != null){
			mEtPhone.setText(mUser.getPhone());
			mEtPass.setText(mUser.getPassWord());
			sub(mUser.getPhone(), mUser.getPassWord());
		}
	}

	/**
	 * 登录
	 * @param phone
	 * @param pass
	 */
	private void sub(final String phone,final String pass){
		showProgressDialog("正在登陆...");
		JSONObject jb = new JSONObject();
		try {
			jb.put("phone", phone);
			jb.put("password",PassWordUtil.md5PassWord(pass));
			jb.put("version", PUBLIC_KEY);//android_pub
		} catch (JSONException e) {
			e.printStackTrace();
			cancelProgressDialog();
		}
		AppHttpClient ac = new AppHttpClient(LOGIN);
		ac.postData1(LOGIN, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, phone));

				SharePreferencesUtil.saveShopInfo(getApplicationContext(), data);
				SharePreferencesUtil.saveLoginInfo(getApplicationContext(), phone,pass);
				AppContext.IS_LOGIN = true;
				User user = JsonUtils.parse(data,User.class);
				AppContext.TOKEN = user.getUuid();
				AppContext.SHOP_TYPE = user.getIs_norm();
				showToast(msg);
				cancelProgressDialog();

//				startActivity(new Intent(LoginActivity.this,TestImg.class));
				startActivity(new Intent(LoginActivity.this,MainActivity.class));
				finish();
			}

			@Override
			public void onError(String error) {
				showToast(error);
				cancelProgressDialog();
			}
		});
	}

	/**
	 * 以下内容为设置推送消息
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
			case 0:
				break;

			case 6002:
				if (NetUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				}
				break;

			}

		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
			case 0:
				break;

			case 6002:
				if (NetUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
				}
				break;

			}

		}

	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	private final Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	};

}
