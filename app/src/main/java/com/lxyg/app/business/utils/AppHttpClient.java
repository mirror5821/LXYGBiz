package com.lxyg.app.business.utils;

import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import dev.mirror.library.utils.MD5Util;

/**
 * app请求封装类
 * @author 王沛栋
 *
 */
public class AppHttpClient extends FinalHttp{
	public AppHttpClient() {
	}
	public AppHttpClient(String fName) {		
		String appid = "999999";
		String chnl = "10086";
		String dev ="huawei";	
		String m = fName;
		String os = "android";
		String t = "122545450";
		String ver = "1.0.0";
		
		String secret = Constants.SECRET;
		String sign = MD5Util.stringToMd5("appid="+appid+"&chnl="+chnl+"&dev="+dev+"&m="+m+"&os="+os+"&t="+t+"&ver="+ver+"&secret="+secret);
		
		
		this.configTimeout(10000);
		this.addHeader("Connection", "keep-alive");
		this.addHeader("Content-Type", "application/json");//multipart/form-data
		this.addHeader("appid", appid);
		this.addHeader("chnl", chnl);
		this.addHeader("dev", dev);
		this.addHeader("os", os);
		this.addHeader("os_ver", "a_5.0.1");
		this.addHeader("sign", sign);
		this.addHeader("t", t);
		this.addHeader("ver", ver);
		this.addHeader("token", "userid");
		this.addHeader("m", fName);
		
		
	}
	
	
	
	public static final String SERVIER_HEADER = Constants.HOST_HEADER;
	
	public <T> void getData(String fName,AppAjaxParam params,onRecevieDataListener<T> listener){
		StringBuilder sb = new StringBuilder();
		sb.append(SERVIER_HEADER);
		sb.append(fName);
		get(sb.toString(), params,new AppAjaxCallback<T>(listener) );
	}
	
	public <T> void postData(String fName,AppAjaxParam params,onRecevieDataListener<T> listener){
		StringBuilder sb = new StringBuilder();
		sb.append(SERVIER_HEADER);
		sb.append(fName);
		post(sb.toString(), params,new AppAjaxCallback<T>(listener) );
	}
	
	public void getData1(String fName,AppAjaxParam params,onResultListener listener) {
		StringBuilder sb = new StringBuilder();
		sb.append(SERVIER_HEADER);
		sb.append(fName);
		get(sb.toString(), params,new AppAjaxCallback<>(listener) );
	}
	
	public void postData1(String fName,AppAjaxParam params,onResultListener listener) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(SERVIER_HEADER);
		sb.append(fName);
		post(sb.toString(), params,new AppAjaxCallback<>(listener) );
	}
	
	
	public void postData2(String fName,AjaxParams params,onResultListener listener) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(SERVIER_HEADER);
		sb.append(fName);
		post(sb.toString(), params,new AppAjaxCallback<>(listener) );
	}
	
	public void uploadImg(AppAjaxParam params,onResultListener listener) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.HOST_IMG_HEADER);
		sb.append(Constants.UPLOAD_IMG);
		post(sb.toString(), params,new AppAjaxCallback<>(listener) );
	}
	

}
