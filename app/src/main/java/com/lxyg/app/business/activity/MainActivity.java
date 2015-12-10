package com.lxyg.app.business.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.fragment.CaiwuFragment;
import com.lxyg.app.business.fragment.CustomerFragment;
import com.lxyg.app.business.fragment.IndexFragment;
import com.lxyg.app.business.fragment.MyFragment;
import com.lxyg.app.business.fragment.OrderFragment;
import com.lxyg.app.business.fragment.OrderNewFragment;
import com.lxyg.app.business.iface.OnSelectTab;

/**
 * 主栏目界面
 * @author 王沛栋
 *
 */
public class MainActivity extends BaseFragmentTabActivity implements OnSelectTab{

	@Override
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	/**
	 * 设置底部文字
	 */
	@Override
	public String[] setTabTitles() {
		return  new String[] {"首页","订单","财务","客户","账户"};
	}


	/**
	 * 设置底部图标
	 */
	@Override
	public int[] setTabIcons() {
		return new int[]{R.drawable.tab1,R.drawable.tab2,R.drawable.tab3,R.drawable.tab4,R.drawable.tab5};
	}


	/**
	 * 设置切换fragment
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Fragment> Class<T>[] setFragment() {
		//OrderFragment
		return  new Class[] { IndexFragment.class, OrderNewFragment.class,
				CaiwuFragment.class,CustomerFragment.class,MyFragment.class };
	}

//	public <T extends Fragment> Class<T>[] setFragment() {
//		return  new Class[] { IndexFragment.class,OrderNewFragment.class,
//				CaiwuFragment.class,CustomerFragment.class,MyFragment.class };
//	}

	/**
	 * 此方法不用任何操作
	 */
	@Override
	public Bundle setFragmentArgment(int position) {
		return null;
	}


	/**
	 * 切换tab
	 */
	@Override
	public void onSelect(int index) {
		mFragmentTabHost.setCurrentTab(index);
	}
	@Override
	protected void onResume() {
		super.onResume();
		//如果登录失败  则重新登录
		if(!AppContext.IS_LOGIN){
			startActivity(new Intent(MainActivity.this,LoginActivity.class));
			finish();
		}
		//如果没有token  也重新登录
		if(TextUtils.isEmpty(AppContext.TOKEN)){
			startActivity(new Intent(MainActivity.this,LoginActivity.class));
			finish();
		}
	}

	/**
	 * 设置两次退出程序
	 */
	private long last = 0;
	@Override
	public void onBackPressed() {
//		if (mFragmentTabHost.getCurrentTab() != 0) {
//			mFragmentTabHost.setCurrentTab(0);
//		} else {
//			if (System.currentTimeMillis() - last > 2000) {
//				showToast( "再按一次返回键退出");
//				last = System.currentTimeMillis();
//			} else {
//				super.onBackPressed();
//			}
//		}

		if (System.currentTimeMillis() - last > 2000) {
			showToast( "再按一次返回键退出");
			last = System.currentTimeMillis();
		} else {
			super.onBackPressed();
		}

	}

}

