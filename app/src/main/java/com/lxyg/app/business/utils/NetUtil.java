package com.lxyg.app.business.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lxyg.app.business.iface.PostDataListener;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;

public class NetUtil {
	private static AppHttpClient mHttpClient;

	/**
	 * 不用传递参数的获取data
	 * @param postData
	 */
	public static void loadData(final String fName,AppAjaxParam ap,final PostDataListener postData){
		if(mHttpClient == null){
			mHttpClient = new AppHttpClient(fName);
		}


		mHttpClient.postData1(fName, ap, new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				postData.getDate(data,msg);
			}

			@Override
			public void onError(String error) {
				postData.error(error);
			}
		});
	}
	
	/**
	 * 判断网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
