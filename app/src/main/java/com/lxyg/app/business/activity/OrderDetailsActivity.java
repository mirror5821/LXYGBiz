package com.lxyg.app.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Order;
import com.lxyg.app.business.bean.Order.orderItems;
import com.lxyg.app.business.iface.PostDataListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

public class OrderDetailsActivity extends BaseActivity{
	private TextView mTvNum,mTvStatus,mTvType,mTvPayMent,mTvPrice,mTvAddress,mTvTime,mTvFahuo,mTvFinish,mTvRang;
	private LinearLayout mView;
	private LinearLayout viewB1,viewB2,viewB3,viewFahuo,viewFinish,viewRang;
	private Button mBtnRang;

	private Order mOrder;
	private int mStatus;//通过哪里进入的详情

	private Bundle mBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		setBack();
		setTitleText("订单详情");
		setRightTitle("打印小票");

		mBundle = getIntent().getExtras();
		mOrder = JsonUtils.parse(mBundle.getString(INTENT_ID), Order.class);
		mStatus = mBundle.getInt("type");

		mTvNum = initTextView(R.id.num);
		mTvStatus = initTextView(R.id.status);
		mTvType = initTextView(R.id.type);
		mTvPayMent = initTextView(R.id.payment);
		mTvPrice = initTextView(R.id.price);
		mTvAddress = initTextView(R.id.address);
		mTvTime = initTextView(R.id.time);
		mTvFahuo = initTextView(R.id.fahuo_time);
		mTvFinish = initTextView(R.id.finish_time);
		mTvRang = initTextView(R.id.time_rang);

		viewB1 = (LinearLayout)findViewById(R.id.view_b1);
		viewB2 = (LinearLayout)findViewById(R.id.view_b2);
		viewB3 = (LinearLayout)findViewById(R.id.view_b3);
		viewFahuo = (LinearLayout)findViewById(R.id.view_fahuo);
		viewFinish = (LinearLayout)findViewById(R.id.view_finish);
		viewRang = (LinearLayout)findViewById(R.id.view_rang_time);


		mView = initLinearLayout(R.id.view1);


		//抢单按钮
		initButton(R.id.btn_qiang);

		//让单按钮
		mBtnRang = initButton(R.id.btn_rang_s);

		//如果为2  则为抢到的订单  所以无法让单
		if(mOrder.getIs_rob()==2){
			mBtnRang.setVisibility(View.GONE);
		}
		//发货按钮
		initButton(R.id.btn_fa);

		//确认送达
		initButton(R.id.btn_ok);

		initView();
	}

	private void initView(){
		mTvNum.setText(mOrder.getOrder_no());

		//判断是否是抢单 或普通弹
		switch (mOrder.getIs_rob()) {
		case 1:
			mTvType.setText("普通订单");
			break;

		case 2:
			mTvType.setText("抢单订单");
			break;
		}
		mTvPayMent.setText(mOrder.getPay_name());
		mTvPrice.setText(PriceUtil.floatToString(mOrder.getPrice()));
		mTvAddress.setText(mOrder.getProvince_name()+" "+mOrder.getCity_name()+" "+mOrder.getArea_name()+"\n"
				+mOrder.getStreet()+"\n"+mOrder.getName()+" "+mOrder.getPhone());
		mTvTime.setText(mOrder.getCreate_time());

		//动态添加view
		for (orderItems item : mOrder.getOrderItems()) {
			View view = getLayoutInflater().inflate(R.layout.item_order_goods, null);
			TextView name = (TextView)view.findViewById(R.id.name);
			TextView price = (TextView)view.findViewById(R.id.price);
			ImageView img = (ImageView)view.findViewById( R.id.img);
			TextView numPrice = (TextView)view.findViewById(R.id.price_num);

			AppContext.displayImage(img, item.getCover_img());
			name.setText(item.getName());
			int p = item.getProduct_price();
			int n = Integer.valueOf(item.getProduct_number());
			numPrice.setText("￥"+PriceUtil.floatToString(p)+"x"+n);
			price.setText("￥"+PriceUtil.floatToString(p*n));
			mView.addView(view);
		}

		//根据入口显示不同的底部操作按钮
		switch (mStatus) {
		case 2:
			mTvStatus.setText("待发货");
			viewB2.setVisibility(View.VISIBLE);
			break;
		case 3:
			mTvStatus.setText("配送中");
			viewFahuo.setVisibility(View.VISIBLE);
			mTvFahuo.setText(mOrder.getSend_goods_time());
			viewB3.setVisibility(View.VISIBLE);
			break;
		case 4:
			mTvStatus.setText("交易完成");
			viewFahuo.setVisibility(View.VISIBLE);
			mTvFahuo.setText(mOrder.getSend_goods_time());
			viewFinish.setVisibility(View.VISIBLE);
			mTvFinish.setText(mOrder.getFinish_time());
//			viewB4.setVisibility(View.VISIBLE);
			break;
		case 6:
			viewRang.setVisibility(View.VISIBLE);
			mTvRang.setText(mOrder.getLet_order_time());
			mTvStatus.setText("让单");
//			viewB1.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_qiang:
				btnEvent(1);
				break;
			case R.id.btn_rang_s:
				btnEvent(2);
				break;
			case R.id.btn_fa:
				btnEvent(3);
				break;
			case R.id.btn_ok:
				btnEvent(4);
				break;
			case R.id.right_text:
				startActivity(new Intent(OrderDetailsActivity.this,PrintTicketActivity.class).
						putExtra(INTENT_ID,
								JsonUtils.beanToString(mOrder,Order.class)));
				break;
		}
	}

	/**
	 *
	 * @param type
	 *
	 * 抢单事件1  让单2  发货3 确认收货4
	 */
	private void btnEvent(final int type){
		String fname = null;
		int s = 0;
		switch (type) {
		case 1:

			break;

		case 2:
			fname = ORDER_RANG;
			break;
		//发货
		case 3:
			s =2;
			fname = ORDER_UPDATE_STATUS;
			break;
		//确认收货
		case 4:
			//确认收货 3
			startActivity(new Intent(getActivity(),OrderShouActivity.class).putExtra(INTENT_ID,mOrder.getOrder_id()));
			return;
		}

		if(TextUtils.isEmpty(fname)){
			showToast("此功能暂无开放");
			return;
		}
		JSONObject jb = new JSONObject();
		try {
			jb.put("orderId",mOrder.getOrder_id());
			if(s !=0){
				jb.put("orderStatus", s);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		NetUtil.loadData(fname,new AppAjaxParam(jb), new PostDataListener() {

			@Override
			public void getDate(String data,String msg) {
				showToast(msg);
				finish();
			}

			@Override
			public void error(String msg) {
				showToast(msg);
			}
		});
	}
}
