package com.lxyg.app.business.utils;

import android.text.TextUtils;

import com.lxyg.app.business.BuildConfig;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Constants;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求参数封装类
 * @author 王沛栋
 *
 */
public class AppAjaxParam extends AjaxParams{
	public AppAjaxParam() {
		super();
	}
	
	public AppAjaxParam(JSONObject json){
		if(BuildConfig.DEBUG){
			
		}
		
		if(!TextUtils.isEmpty(AppContext.TOKEN)){
			try {
				json.put("uid", AppContext.TOKEN);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		put(Constants.JSONG_KEY, json.toString());
	}
}
