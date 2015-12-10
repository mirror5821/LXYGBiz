package com.lxyg.app.business.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Shop;

import dev.mirror.library.utils.JsonUtils;

public class ZiZhiFragment extends BaseFragment{
	private TextView mTv1,mTv2;
	@Override
	public int setLayoutId() {
		return R.layout.fragment_zizhi;
	}
	
	private String mIntent;
	private Shop mShop;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIntent = getArguments().getString(INTENT_ID);
		mShop = JsonUtils.parse(mIntent, Shop.class);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mTv1 = initTextView(R.id.ver);
		mTv2 = initTextView(R.id.ver2);
		
		//-1 未验证 0 验证中 1验证通过 2 未验证通过  3 问题商户 4 黑名单商户
		switch (mShop.getQ_verifi()) {
		case -1:
			
			break;

		case 0:
			mTv1.setText("您的资料正在审核中");
			break;
		case 1:
			mTv1.setVisibility(View.GONE);
			mTv2.setVisibility(View.VISIBLE);
			mTv2.setText("已经审核通过");
			break;
		case 2:
			mTv1.setText("您的资料未审核通过");
			break;
		case 3:
			mTv1.setText("您的资料存在问题，请联系客服");
			break;
		case 4:
			mTv1.setText("您的资料存在问题，请联系客服");
			break;
		}
		
		initTextView(R.id.name).setText(mShop.getName());
		initTextView(R.id.bank_name).setText(mShop.getBank_name());
		initTextView(R.id.no).setText(mShop.getId_number());
		initTextView(R.id.kaihuhang).setText(mShop.getBank_branch_province()+" "+mShop.getBank_branch_city());
		initTextView(R.id.zhihang).setText(mShop.getBank_branch_name());
		initTextView(R.id.bank_name).setText(mShop.getBank_name());
		initTextView(R.id.card_num).setText(mShop.getBank_card_num());
	

		AppContext.displayImage(initImageView(R.id.img1),mShop.getHandle_id_img() );
		AppContext.displayImage(initImageView(R.id.img2), mShop.getBank_card_img());
		AppContext.displayImage(initImageView(R.id.img3),mShop.getBussiness_num_img() );
	}

}
