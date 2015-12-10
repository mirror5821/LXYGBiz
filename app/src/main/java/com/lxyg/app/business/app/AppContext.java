package com.lxyg.app.business.app;

import android.os.Build;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechUtility;
import com.lxyg.app.business.R;

import cn.jpush.android.api.JPushInterface;
import dev.mirror.library.app.BaseAppContext;

/**
 * app全局配置类
 * @author 王沛栋
 *
 * 极光推送
 * AppKey		192340a8f20ba3485ee27ab6
 * Master Secret		30a8c96c704970ca447ed803  
 *
 */


public class AppContext extends BaseAppContext{
	public static double Longitude =0;
	public static double Latitude = 0;
	public static String Address;
	public static int SHOP_TYPE;//判断的是is_norm字段  1是标准商店 其他事非标准商店
	public static String TOKEN;

	/**
	 * 用于判断是否已经登录
	 */
	public static boolean IS_LOGIN = false;

	public static boolean IsImmersed = false; //根据系统版本是否能时候通知栏沉浸模式
	@Override
	public void onCreate() {
		//初始化讯飞语音
//		SpeechUtility.createUtility(AppContext.this, "appid=" + getString(R.string.xunfei_app_id));//559a3684
		SpeechUtility.createUtility(AppContext.this, "appid=559a3684");//559a3684
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);

		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);  //初始化极光推送

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			IsImmersed = true;
		}

	}
}
