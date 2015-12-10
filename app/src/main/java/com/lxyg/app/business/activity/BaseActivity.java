package com.lxyg.app.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.ActivityManager;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.bean.Order;
import com.lxyg.app.business.bean.Order.orderItems;
import com.lxyg.app.business.iface.PostDataListener;
import com.lxyg.app.business.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.NetUtil;
import com.lxyg.app.business.utils.VoiceUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import dev.mirror.library.activity.DevBaseActivity;
import dev.mirror.library.utils.PriceUtil;

/**
 * activity基类
 * @author 王沛栋
 *
 */
public class BaseActivity extends DevBaseActivity implements Constants{
	public static boolean isForeground = false;
	public static boolean isDownloadNewVersion = false;

	private Context mContext;
	private final  String mPageName = "AnalyticsHome";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//添加到appmanager
		ActivityManager.getInstance().addActivity(this);
		//监听订单消息
		registerMessageReceiver();
		//监听网络状态
		registerNetReceiver();
		//友盟更新
		UmengUpdateAgent.update(this);
		//		静默下载更新
		//		UmengUpdateAgent.silentUpdate(this);
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

			@Override
			public void onClick(int status) {
				switch (status) {
				case UpdateStatus.Update:
					isDownloadNewVersion = true;
					return;

				default:
					//友盟自动更新目前还没有提供在代码里面隐藏/显示更新对话框的
					//"以后再说"按钮的方式，所以在这里弹个Toast比较合适
					showToast("非常抱歉，您需要更新应用才能继续使用");
//					finish();
				}
			}
		});

		MobclickAgent.setDebugMode(true);
		//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		//		MobclickAgent.setAutoLocation(true);
		//		MobclickAgent.setSessionContinueMillis(1000);

		MobclickAgent.updateOnlineConfig(this);
	}


	/**
	 * 设置titlebar右侧文字和点击事件
	 * @param str
	 */
	public void setRightTitle(String str){
		initTextView(R.id.right_text).setText(str);
	}

	/**
	 * 设置后退事件
	 */
	public void setBack(){
		ImageView iv = (ImageView)findViewById(R.id.left_icon);
		iv.setImageResource(R.mipmap.ic_back_w);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private TextView mTvTitle;
	/**
	 * 设置titlebar文字
	 * @param title
	 */
	public void setTitleText(String title){
		mTvTitle = (TextView)findViewById(R.id.bar_title);
		mTvTitle.setText(title);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
		isForeground = true;
		MobclickAgent.onResume(getApplicationContext());
		checkNet();
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(mContext);
		if(mDialog!=null){
			if(mDialog.isShowing()){
				mDialog.dismiss();
				mDialog.cancel();
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
	}


	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
		isForeground = false;
		MobclickAgent.onPause(getApplicationContext());

		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(mContext);
	}

	private BroadcastReceiver netReceiver;
	public void registerNetReceiver(){
		//注册网络监听
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		netReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				checkNet();
			}

		};
		registerReceiver(netReceiver, filter);

	}


	/**
	 * 检查网络
	 */
	private void checkNet(){
//		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//		//获取手机网络连接
//		NetworkInfo mobNetInfo = connManager.getNetworkInfo(new Network().describeContents());//.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//		//获取wifi连接
//		NetworkInfo wifiNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		boolean isNet = false;
//		if(!mobNetInfo.isConnected()&& !wifiNetInfo.isConnected()){
//			isNet = false;
//		}else{
//			isNet = true;
//		}
//		showSetNet(isNet);
	}

	private AlertDialog.Builder mNetBuilder;
	private Dialog mNetDialog;
	/**
	 * 显示设置网络的弹出框
	 */
	private void showSetNet(boolean isNet){
		if(mNetBuilder == null){
			mNetBuilder = new AlertDialog.Builder(getActivity());

			mNetBuilder.setTitle("没有可用网络");
			mNetBuilder.setMessage("当前网络不可用,是否设置网络?");
			//取消物理返回键
			mNetBuilder.setCancelable(false);
			mNetBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//打开设置网络界面
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					return;

				}
			});
			mNetBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					finish();
				}
			});

			mNetDialog = mNetBuilder.create();
		}

		if(isNet){
			if(mNetDialog == null)
				return;
			if(mNetDialog.isShowing()){
				mNetDialog.dismiss();
				mNetDialog.cancel();
			}
		}else{
			if(mNetDialog.isShowing())
				return;
			mNetDialog.show();
		}

	}




	/**
	 * 以下内容为接收jpush推送的自定义消息
	 */
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.lxyg.app.business.activity.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!TextUtils.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				if(TextUtils.isEmpty(extras)){
					return;
				}
				System.out.println("message---------"+messge+"   "+extras);
				showOrderGrabDialog(messge,extras);
			}
		}
	}

	AppHttpClient mHttpClient2;
	private AlertDialog.Builder mBuilder;
	private Dialog mDialog;

	private TextView orderNum;
	private TextView orderTime;
	private TextView priceAndNum;
	private CheckBox cb;
	private LinearLayout viewProduct;
	private TextView area;
	private TextView street;
	private TextView namePhone;
	private TextView sendName;
	private TextView sendTime;
	//抢单按钮
	private Button qiang;
	private JSONObject jb;
//	private JSONObject jbQiang;
	/**
	 * message : 你有一笔新的订单   请注意查收
	 * extras : {"orderId":"xxsdalksdjdkajkad"}
	 * @param msg
	 */
	private void showOrderGrabDialog(String msg,String ordId){

		if(AppContext.IS_LOGIN){
			if(mHttpClient2 == null){
				mHttpClient2 = new AppHttpClient(ORDER_LIST);
			}
			VoiceUtil.msgToVoice(getApplicationContext(), msg);

			try {
				jb = new JSONObject(ordId);
				int i = jb.getInt("type");
				if(i!=1){
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

//			jbQiang = new JSONObject();
			try {
				jb.put("orderStatus", 1);
				jb.put("pg", 1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mHttpClient2.postData(ORDER_LIST, new AppAjaxParam(jb), new onRecevieDataListener<Order>() {

				@Override
				public void onReceiverData(List<Order> data, String msg) {

					if(mBuilder == null){
						mBuilder = new AlertDialog.Builder(getActivity());
					}
					View popupView = getLayoutInflater().inflate(R.layout.activity_order_grab, null);

					mBuilder.setView(popupView);
					if(mDialog == null){
						mDialog = mBuilder.create();
					}
					//					final Dialog d = mBuilder.create();


					orderNum = (TextView)popupView.findViewById(R.id.tv_num);
					orderTime = (TextView)popupView.findViewById(R.id.tv_date);
					priceAndNum = (TextView)popupView.findViewById(R.id.tv_num_price);
					cb = (CheckBox)popupView.findViewById(R.id.rb_product_detail);
					viewProduct = (LinearLayout)popupView.findViewById(R.id.view1);
					area = (TextView)popupView.findViewById(R.id.tv_area);
					street = (TextView)popupView.findViewById(R.id.tv_street);
					namePhone = (TextView)popupView.findViewById(R.id.tv_name_phone);
					sendName = (TextView)popupView.findViewById(R.id.tv_send_name);
					sendTime = (TextView)popupView.findViewById(R.id.tv_send_time);
					//抢单按钮
					qiang = (Button)popupView.findViewById(R.id.btn_qiang);




					Order o = data.get(0);

					String sName = o.getSend_name();
					sendName.setText(o.getPay_name()+"/"+sName);
					if(sName.equals("定时送")){
						sendTime.setText(o.getSend_time());
					}
					orderNum.setText(o.getOrder_no());
					orderTime.setText(o.getCreate_time());
					priceAndNum.setText("共有"+o.getOrderItems().size()+"钟商品,总价￥"+PriceUtil.floatToString(o.getPrice()));
					area.setText(o.getProvince_name()+" "+o.getCity_name()+" "+o.getArea_name());
					street.setText(o.getStreet());
					namePhone.setText(o.getName()+" "+o.getPhone());

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
//					viewProduct.removeAllViews();
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


					qiang.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							NetUtil.loadData(ORDER_GRAB, new AppAjaxParam(jb), new PostDataListener() {

								@Override
								public void getDate(String data,String msg) {
									mDialog.dismiss();
									mDialog.cancel();
									showToast(msg);

								}

								@Override
								public void error(String msg) {
									mDialog.dismiss();
									mDialog.cancel();
									showToast(msg);
								}
							});
						}
					});

					mDialog.show();
				}
				@Override
				public void onReceiverError(String msg) {

				}

				@Override
				public Class<Order> dataTypeClass() {
					return Order.class;
				}
			});
		}
	}

}
