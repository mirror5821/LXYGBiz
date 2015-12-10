package com.lxyg.app.business.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.ActivityManager;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.utils.SharePreferencesUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import dev.mirror.library.utils.UIHelper;

public class SetActivity extends BaseActivity{
	private LinearLayout mViewXy,mViewVersion,mViewCache,mViewLx,mViewUs,mViewFk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		setBack();
		setTitleText("更多设置");
		
		mViewXy = (LinearLayout)findViewById(R.id.view_xy);
		mViewVersion = (LinearLayout)findViewById(R.id.view_version);
		mViewCache = (LinearLayout)findViewById(R.id.view_cache);
		mViewLx = (LinearLayout)findViewById(R.id.view_lx);
		mViewUs = (LinearLayout)findViewById(R.id.view_us);
		mViewFk = (LinearLayout)findViewById(R.id.view_fk);
		
		mViewXy.setOnClickListener(this);
		mViewVersion.setOnClickListener(this);
		mViewCache.setOnClickListener(this);
		mViewLx.setOnClickListener(this);
		mViewUs.setOnClickListener(this);
		mViewFk.setOnClickListener(this);
		
		initButton(R.id.logout);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.view_xy:
			startActivity(new Intent(SetActivity.this,AgreementActivity.class));
			break;
		case R.id.view_version:
			//手动更新
//			UmengUpdateAgent.forceUpdate(this);
//			checkUpdate();
			showToast("没有更新");
			break;
		case R.id.view_cache:
			AppContext.deleteBitmap();
			showToast("清除成功!");
			break;
		case R.id.view_lx:
			UIHelper.makePhoneCall(SetActivity.this, getResources().getString(R.string.kf_phone_num));
			break;
		case R.id.view_us:
			startActivity(new Intent(SetActivity.this,AboutUsActivity.class));
			break;
		case R.id.view_fk:
			startActivity(new Intent(SetActivity.this,FeedBackActivity.class));
			break;
			
		case R.id.logout://退出登录
			showNormalDialog("退出登录", "确定退出登录吗？", "确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharePreferencesUtil.deleteInfo(getActivity());
					startActivity(new Intent(SetActivity.this,LoginActivity.class));
					
					ActivityManager.getInstance().exit();
					
				}
			});
			break;
		}
	}
	
	private void checkUpdate(){
//		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes: // has update		        	
		            UmengUpdateAgent.showUpdateDialog(getApplicationContext(), updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		            showToast("没有更新");
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		            showToast("没有wifi连接， 只在wifi下更新");
		            break;
		        case UpdateStatus.Timeout: // time out
		        	showToast("超时");
		            break;
		        }
		    }
		});
//		UmengUpdateAgent.update(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}
