package com.lxyg.app.business.activity;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.PassWordUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class TiXianInput2Activity extends BaseActivity{
	private KeyboardView mKeyboardView;
    private TextView mTv,mTvError;
    
    private String mIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybroad_input);
		
		setBack();
		setTitleText("确认提现密码");
		mIntent = getIntent().getExtras().getString(INTENT_ID);

        mTv = (TextView)findViewById(R.id.tv);
        mTvError = (TextView)findViewById(R.id.tv2);
        
        mTv.setInputType(mTv.getInputType()
				| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);
		mKeyboardView.setKeyboard(new Keyboard(getApplicationContext(), R.xml.hexkbd));
		mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview
		mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
       
	}
	private AppHttpClient mAppHttpClient = new AppHttpClient(ADD_OR_UPDATE_ACCOUNT_PASS);
	private void sub(String pass){
		showProgressDialog("正在提交数据...");
		JSONObject jb = new JSONObject();
		try {
			jb.put("password", PassWordUtil.md5PassWord(pass));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mAppHttpClient.postData1(ADD_OR_UPDATE_ACCOUNT_PASS, new AppAjaxParam(jb), 
				new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				showToast(msg);
				cancelProgressDialog();
				finish();
			}

			@Override
			public void onError(String error) {
				cancelProgressDialog();
				finish();
				showToast(error);
			}
		});
	}
	
	private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener(){

		public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
		public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL

		@Override
		public void onKey(int primaryCode, int[] keyCodes){
			
			String str = mTv.getText().toString();
			mTvError.setVisibility(View.GONE);
			//现在是确定按钮
			if (primaryCode == CodeCancel){
				hideCustomKeyboard();
			}else if (primaryCode == CodeDelete){
				if(!TextUtils.isEmpty(str)){
					str = str.substring(0, str.length()-1);
				}else{
					return;
				}
			}else{ // insert character
				if(str.length()>6){
					return;
					
				}else{
					str = str +Character.toString((char) primaryCode);
				}
				
			}
			mTv.setText(str);
		}

		@Override
		public void onPress(int arg0){
		}

		@Override
		public void onRelease(int primaryCode){
			String str = mTv.getText().toString();
			if(str.length()==6){
				if(mIntent.equals(str)){
					sub(str);
//					startActivity(new Intent(TiXianInput2Activity.this,TiXianActivity.class).putExtra(INTENT_ID, ""));
				}else{
					mTvError.setVisibility(View.VISIBLE);
					mTv.setText("");
				}
			}else{
				str = str +Character.toString((char) primaryCode);
			}
		}

		@Override
		public void onText(CharSequence text){
		}

		@Override
		public void swipeDown(){
		}

		@Override
		public void swipeLeft(){
		}

		@Override
		public void swipeRight(){
		}

		@Override
		public void swipeUp(){
		}
	};

	
	public void showCustomKeyboard(View v){
		mKeyboardView.setVisibility(View.VISIBLE);
		mKeyboardView.setEnabled(true);
		if (v != null)
			((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public void hideCustomKeyboard(){
		mKeyboardView.setVisibility(View.GONE);
		mKeyboardView.setEnabled(false);
	}
	
	
}
