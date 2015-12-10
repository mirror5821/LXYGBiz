package com.lxyg.app.business.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

public class LocalService extends Service implements Constants{
	public static final String ACTION = "com.lxyg.app.business.service";

//	private Notification mNotification;
//	private NotificationManager mManager;

	public MyLocationListenner myListener = new MyLocationListenner();
	private AppHttpClient mAppHttpClient;
	// 定位相关
	private	LocationClient mLocClient;
	//	private final Class<?> mActivity;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	//	public LocalService(Class<?> activity) {
	//		this.mActivity = activity;
	//	}

	@Override
	public void onCreate() {
		super.onCreate();
//		initNotifiManager();
		mAppHttpClient = new AppHttpClient(UPLOAD_LOC);
		initLocal();
	}

	private void initLocal(){
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		//需设置此参数，否则无地址信息
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void loc(){
		mLocClient.start();
	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		new LocalThread().start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

//	private void initNotifiManager(){
//		mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//		mNotification = new Notification();
//		mNotification.icon = R.drawable.ic_app_logo;
//		mNotification.tickerText = "测试服务通知";
//		mNotification.defaults|= Notification.DEFAULT_SOUND;
//		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//
//	}
//
//	private void showNotification(){
//		mNotification.when = System.currentTimeMillis();
//		Intent i = new Intent();
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
//		mNotification.setLatestEventInfo(this, getResources().getString(R.string.app_name), "你有一个新的消息", pendingIntent);
//		mManager.notify(0, mNotification);
//	}

	/**
	 * Polling thread
	 * 模拟向Server轮询的异步线程
	 * @Author Ryan
	 * @Create 2013-7-13 上午10:18:34
	 */
	//    int count = 0;
	class LocalThread extends Thread {
		@Override
		public void run() {
			//            System.out.println("Polling...");
			//            count ++;
			//            //当计数能被5整除时弹出通知
			//            if (count % 5 == 0) {
			//                showNotification();
			//                System.out.println("New message!");
			//            }
			System.out.println("New message!");
			loc();
		}
	}

	private void uploadLocation(){
		JSONObject jb = new JSONObject();
		try {
			jb.put("lat",mLat);
			jb.put("lng", mLng);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mAppHttpClient.postData1(UPLOAD_LOC, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				System.out.println(msg);
			}

			@Override
			public void onError(String error) {
				System.out.println("e-"+error);
			}
		});
	}

	MyLocationData locData;
	private double mLat,mLng;
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(100).latitude(location.getLatitude())
			.longitude(location.getLongitude()).build();

			mLat = location.getLatitude();
			mLng = location.getLongitude();
			uploadLocation();

			mLocClient.stop();


		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

}
