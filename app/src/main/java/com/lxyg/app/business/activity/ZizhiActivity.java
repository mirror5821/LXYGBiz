package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.Shop;
import com.lxyg.app.business.fragment.ZiZhi2Fragment;
import com.lxyg.app.business.fragment.ZiZhiFragment;

import dev.mirror.library.utils.JsonUtils;

/**
 * 资质管理
 * @author 王沛栋
 *
 */
public class ZizhiActivity extends BaseActivity{
	
	private Shop mShop;
	private String mIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_manager);
		setBack();
		setTitleText("资质管理管理");
		
		mIntent = getIntent().getExtras().getString(INTENT_ID);
		mShop = JsonUtils.parse(mIntent, Shop.class);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		//-1 未验证 0 验证中 1验证通过 2 未验证通过  3 问题商户 4 黑名单商户
		if(mShop.getQ_verifi() ==-1){
			fragmentTransaction.replace(R.id.content, new ZiZhi2Fragment()).commit();
		}else{
			Bundle b = new Bundle();
			b.putString(INTENT_ID, mIntent);
			ZiZhiFragment z = new ZiZhiFragment();
			z.setArguments(b);
			fragmentTransaction.replace(R.id.content, z).commit();
		}
		
		
	}
	
}
