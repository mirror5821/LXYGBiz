package com.lxyg.app.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.bean.Order;
import com.lxyg.app.business.bean.Order.orderItems;
import com.lxyg.app.business.iface.PostDataListener;
import com.lxyg.app.business.service.LocalService;
import com.lxyg.app.business.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.LocalUtil;
import com.lxyg.app.business.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

public class OrdeListActivity extends BaseListActivity<Order> implements Constants{
	private final AppHttpClient mHttpClient = new AppHttpClient(ORDER_LIST);

	private int mStatus=0;
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer,Boolean> mIsSelected;
	//是否第一次加载数据 用于保存checkbox的状态
	private boolean mFirstLoad = true;

	private LayoutInflater mInflater;

	@Override
	public int setLayoutId() {
		return R.layout.activity_order_list;
	}

	private TextView mTvTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mList = new ArrayList<Order>();
		mInflater = getLayoutInflater();


	}



	/**
	 *
	 * 1可抢单 2代发货 3待收货（配送中） 4交易完成 5拒收 6 让单 7流单
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView =mInflater.inflate(R.layout.item_order_list, null);
		}
		LinearLayout viewB1 = (LinearLayout)convertView.findViewById(R.id.view_b1);
		LinearLayout viewB2 = (LinearLayout)convertView.findViewById(R.id.view_b2);
		LinearLayout viewB3 = (LinearLayout)convertView.findViewById(R.id.view_b3);
		LinearLayout viewB4 = (LinearLayout)convertView.findViewById(R.id.view_b4);
		LinearLayout viewB6 = (LinearLayout)convertView.findViewById(R.id.view_b6);


		final TextView orderNum = (TextView)convertView.findViewById(R.id.tv_num);
		final TextView orderTime = (TextView)convertView.findViewById(R.id.tv_date);
		final TextView priceAndNum = (TextView)convertView.findViewById(R.id.tv_num_price);
		final CheckBox cb = (CheckBox)convertView.findViewById(R.id.rb_product_detail);
		final LinearLayout viewProduct = (LinearLayout)convertView.findViewById(R.id.view1);
		final TextView area = (TextView)convertView.findViewById(R.id.tv_area);
		final TextView street = (TextView)convertView.findViewById(R.id.tv_street);
		final TextView namePhone = (TextView)convertView.findViewById(R.id.tv_name_phone);
		final TextView sendName = (TextView)convertView.findViewById(R.id.tv_send_name);
		final TextView sendTime = (TextView)convertView.findViewById(R.id.tv_send_time);
		//抢单按钮
		final Button qiang = (Button)convertView.findViewById(R.id.btn_qiang);

		//让单按钮
		final Button rang = (Button)convertView.findViewById(R.id.btn_rang_s);
		//详情按钮1 小的
		final Button details1 = (Button)convertView.findViewById(R.id.btn_details);
		//发货按钮
		final Button fahuo = (Button)convertView.findViewById(R.id.btn_fa);

		//详情按钮2 大的
		final Button details2 = (Button)convertView.findViewById(R.id.btn_details2);
		//详情按钮2 大的
		final Button details4 = (Button)convertView.findViewById(R.id.btn_details4);
		//详情按钮2 大的
		final Button details6 = (Button)convertView.findViewById(R.id.btn_details6);
		//确认送达
		final Button sendOk = (Button)convertView.findViewById(R.id.btn_ok);

		Order o = mList.get(position);

		switch (mStatus) {
		case 1:
			viewB1.setVisibility(View.VISIBLE);
			//抢单事件  type1
			qiang.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btnEvent(position, 1);
				}
			});
			break;
		case 2:
			//如果为2  则为抢到的订单  所以无法让单
			if(o.getIs_rob()==2){
				rang.setVisibility(View.GONE);
			}else{
				rang.setVisibility(View.VISIBLE);
			}
			//如果为非标店 则隐藏
			if(AppContext.SHOP_TYPE != 1){
				rang.setVisibility(View.GONE);
			}

			viewB2.setVisibility(View.VISIBLE);
			//让单事件 type2
			rang.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btnEvent(position, 2);
				}
			});
			//详情事件
			details1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDetails(position);
				}
			});
			//发货事件 type3
			fahuo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btnEvent(position, 3);
				}
			});
			break;
		case 3:
			viewB3.setVisibility(View.VISIBLE);
			//详情事件
			details2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDetails(position);
				}
			});
			//确认收货事件 type4
			sendOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btnEvent(position, 4);
				}
			});

			break;
		case 4:
			viewB4.setVisibility(View.VISIBLE);

			//详情事件
			details4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDetails(position);
				}
			});
			break;

		case 6:
			viewB6.setVisibility(View.VISIBLE);

			//详情事件
			details6.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDetails(position);
				}
			});
			break;

		}

		String sName = o.getSend_name();
		sendName.setText(o.getPay_name()+"/"+sName);
		if(sName.equals("定时送")){
			sendTime.setText(o.getSend_time());
		}
		orderNum.setText(o.getOrder_no());
		orderTime.setText(o.getCreate_time());
		priceAndNum.setText("共有"+o.getOrderItems().size()+"种商品,总价￥"+PriceUtil.floatToString(o.getPrice()));

		if(!TextUtils.isEmpty(o.getAddress())){
			area.setText(o.getFull_address());
		}
//		area.setText(o.getProvince_name()+" "+o.getCity_name()+" "+o.getArea_name());
//		street.setText(o.getStreet());
		street.setVisibility(View.GONE);
		if(!TextUtils.isEmpty(o.getName())){
			namePhone.setText(o.getName()+" "+o.getPhone());
		}else if(!TextUtils.isEmpty(o.getRec_name())){
			namePhone.setText(o.getRec_name()+" "+o.getRec_phone());
		}else{
			namePhone.setVisibility(View.GONE);
		}

//		area.setText(o.getProvince_name()+" "+o.getCity_name()+" "+o.getArea_name());
//		street.setText(o.getStreet());
//		namePhone.setText(o.getName()+" "+o.getPhone());
		cb.setTag("check"+position);
		//		cb.setChecked(mIsSelected.get(position)==null?false:mIsSelected.get(position));
		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsSelected.put(position,  cb.isChecked());
			}
		});

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					viewProduct.setVisibility(View.VISIBLE);
				}else{
					viewProduct.setVisibility(View.GONE);
				}
			}
		});
		cb.setChecked(false);
		viewProduct.setVisibility(View.GONE);
		viewProduct.removeAllViews();
		for (orderItems item : o.getOrderItems()) {
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
			viewProduct.addView(view);
		}

		return convertView;
	}

	String fname = null;
	/**
	 *
	 * @param position
	 * @param type
	 *
	 * 抢单事件1  让单2  发货3 确认收货4
	 */
	private void btnEvent(final int position,final int type){

		int s = 0;
		switch (type) {
		case 1:
			//抢单
			fname = ORDER_GRAB;
			break;

		case 2:
			//让单
			fname = ORDER_RANG;

			break;
		case 3:
			//发货 2
			//开启定位
//			LocalUtil.startLocalService(OrdeListActivity.this, 60,  LocalService.class, LocalService.ACTION);

			s =2;
			fname = ORDER_UPDATE_STATUS;
			break;
		case 4:
			//确认收货 3
			startActivity(new Intent(getActivity(),OrderShouActivity.class).putExtra(INTENT_ID, mList.get(position).getOrder_id()));
			return;
		}

		if(TextUtils.isEmpty(fname)){
			showToast("此功能暂无开放");
			return;
		}
		JSONObject jb = new JSONObject();
		try {
			jb.put("orderId", mList.get(position).getOrder_id());
			if(s !=0){
				jb.put("orderStatus", s);
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}
		NetUtil.loadData(fname,new AppAjaxParam(jb), new PostDataListener() {

			@Override
			public void getDate(String data,String msg) {
				if(fname.equals(ORDER_UPDATE_STATUS)){
					startActivity(new Intent(getActivity(),PrintTicketActivity.class).
							putExtra(INTENT_ID,JsonUtils.beanToString(mList.get(position),Order.class)));
				}

				mList.remove(position);
				mAdapter.notifyDataSetChanged();
				showToast(msg);


			}

			@Override
			public void error(String msg) {
				showToast(msg);
			}
		});
	}
	/**
	 * 显示详情
	 * @param position
	 * 我实在是不愿意写那个parcle什么的
	 *
	 */
	private void showDetails(int position){
		//		startActivity(new Intent(OrdeListActivity.this,OrderDetailsActivity.class).putExtra(INTENT_ID,
		//				new Gson().toJson(mList.get(position), Order.class).toString()));
		startActivity(new Intent(OrdeListActivity.this,OrderDetailsActivity.class).putExtra(INTENT_ID,
				JsonUtils.beanToString(mList.get(position), Order.class)).putExtra("type", mStatus));
	}

	@Override
	public void loadData() {
		//下面这句话会报错
		if(getIntent().getExtras() == null){
			mStatus = 0;
		}else{
			mStatus = getIntent().getExtras().getInt(INTENT_ID);
		}

		mTvTitle = (TextView)findViewById(R.id.bar_title);
		mTvTitle.setText("订单列表");

		ImageView img = (ImageView)findViewById(R.id.left_icon);
		img.setImageResource(R.mipmap.ic_back_w);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});


		JSONObject jb = new JSONObject();
		mListView.getRefreshableView().setDividerHeight(0);
		try {
			jb.put("orderStatus", mStatus);
			jb.put("pg", pageNo);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mHttpClient.postData(ORDER_LIST, new AppAjaxParam(jb), new onRecevieDataListener<Order>() {

			@Override
			public void onReceiverData(List<Order> data, String msg) {
				if(mFirstLoad)
					if(mIsSelected == null){
						mIsSelected = new HashMap<Integer,Boolean>();
					}
				if(pageNo == mDefaultPage){
					mList.clear();
					//如果第一次加载列表 则往cb添加数据 否则刷新禁止清空数据
					if(mFirstLoad){
						mFirstLoad = false;
						for(int i=0; i<data.size();i++) {
							mIsSelected.put(mList.size()+i,false);
						}
					}
				}else{
					for(int i=0; i<data.size();i++) {
						mIsSelected.put(mList.size()+i,false);
					}
				}
				mList.addAll(data);

				//如果没有发货数据
				if(pageNo == mDefaultPage){
					if(mStatus == 3){
						if(mList.size()==0){
							LocalUtil.stopPollingService(OrdeListActivity.this,  LocalService.class, LocalService.ACTION);
						}
					}
				}
				setListAdapter();
			}

			@Override
			public void onReceiverError(String msg) {
				//				showToast(msg);
				setLoadingFailed(msg);
			}

			@Override
			public Class<Order> dataTypeClass() {
				return Order.class;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View view,
			int position, long num) {

	}

	@Override
	protected void onResume() {
		super.onResume();
		//重新加载数据
		pageNo = 1;
		loadData();
	}

}
