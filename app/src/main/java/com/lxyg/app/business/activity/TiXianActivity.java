package com.lxyg.app.business.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.Balance;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.PassWordUtil;

import org.json.JSONException;
import org.json.JSONObject;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

public class TiXianActivity extends BaseActivity{
	private AppHttpClient mAppHttpClient;

	private TextView mTvBalance,mTvBankName,mTvBankNo,mTvName;
	private EditText mEtBalance;
	private Button mBtnTX;

	private String mIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tixian);

		setBack();
		setTitleText("提现");

		mTvBalance = (TextView)findViewById(R.id.balance);
		mTvBankName = (TextView)findViewById(R.id.bank_name);
		mTvBankNo = (TextView)findViewById(R.id.bank_num);
		mTvName = (TextView)findViewById(R.id.name);

		mEtBalance = initEditText(R.id.et_balance);

		mBtnTX = initButton(R.id.sub);

		getIdenti();

	}

	private int mBlanceAll;
	private void getIdenti(){
		mAppHttpClient = new AppHttpClient(SHOW_IDENTI);
		JSONObject jb = new JSONObject();
		mAppHttpClient.postData1(SHOW_IDENTI, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				Balance b = JsonUtils.parse(data, Balance.class);
				mBlanceAll = Integer.valueOf(b.getBalance());
				if(mBlanceAll == 0){
					showToast("您没有可提现余额!");
					return;
				}
				mTvBalance.setText("可提现金额 " + PriceUtil.floatToString(mBlanceAll) + " 元");
				mTvBankName.setText(b.getBank_name());
				mTvBankNo.setText(b.getBank_card_num());
				mTvName.setText(b.getUser_name());

				mBtnTX.setOnClickListener(TiXianActivity.this);
			}

			@Override
			public void onError(String error) {

				showToast(error);
				finish();
			}
		});
	}
	private int mBlance;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sub:

			if(TextUtils.isEmpty(mEtBalance.getText().toString().trim())){
				showToast("请输入提现金额");
				return;
			}


			mBlance = Integer.valueOf(mEtBalance.getText().toString().trim());
			if(mBlance == 0){
				showToast("请输入提现金额");
				return;
			}

			if(mBlance>mBlanceAll){
				showToast("账户余额不足");
				return;
			}
			startActivityForResult(new Intent(TiXianActivity.this,TiXianInputActivity.class), 2001);

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
			case 2001:
				Bundle mBundle = data.getExtras();
				mIntent = mBundle.getString("pass");

				sub();
				break;
			}
		}
	}

	private void sub(){
		mAppHttpClient = new AppHttpClient(TI_XIAN);
		JSONObject jb = new JSONObject();
		//info={"uid":"48957caade98442b","amount":1286300,"pawd":"e10adc3949ba59ab"}
		try {
			jb.put("amount", mBlance);
			jb.put("pawd", PassWordUtil.md5PassWord(mIntent));

		} catch (JSONException e) {
			e.printStackTrace();
		}


		mAppHttpClient.postData1(TI_XIAN, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				showNormalDialog("提现", "提现成功,我们将在24小时内将钱款转入您的银行账户中", "确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
//				showToast(msg);
//				finish();
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}


}
