package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lxyg.app.business.R;
import com.lxyg.app.business.iface.PostDataListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderShouActivity extends BaseActivity{
	private EditText mEtCode;
	private Button mBtn;
	private String mOrderId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_shouhuo);
		setBack();
		setTitleText("确认收货");
		
		mOrderId = getIntent().getExtras().getString(INTENT_ID);
		
		mEtCode = (EditText)findViewById(R.id.name);
		mBtn = (Button)findViewById(R.id.sub);
		
		mBtn.setOnClickListener(this);
	}
	private String mCode;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.sub:
			mCode = mEtCode.getText().toString().trim();
			if(TextUtils.isEmpty(mCode)){
				showToast("请输入收货码");
				return;
			}
			sub();
			break;

		}
	}
	
	private void sub(){
		showProgressDialog("正在确认订单");
		JSONObject jb = new JSONObject();
		try {
			jb.put("orderId", mOrderId);
			jb.put("orderStatus", 3);
			jb.put("recCode", mCode);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		NetUtil.loadData(ORDER_UPDATE_STATUS,new AppAjaxParam(jb), new PostDataListener() {

			@Override
			public void getDate(String data,String msg) {
				cancelProgressDialog();
				showToast(msg);
				finish();
			}

			@Override
			public void error(String msg) {
				cancelProgressDialog();
				showToast(msg);
			}
		});
	}

}
