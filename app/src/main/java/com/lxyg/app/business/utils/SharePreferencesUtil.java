package com.lxyg.app.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.bean.User;

import dev.mirror.library.utils.JsonUtils;

/**
 * 本地存储工具类
 * @author 王沛栋
 *
 */
public class SharePreferencesUtil implements Constants{
	private static  SharedPreferences mSPreferences;
	
	/**
	 * 保存登录信息
	 * @param context
	 * @param phone
	 * @param password
	 */
	public static void saveLoginInfo(Context context,String phone,String password){
		if(mSPreferences == null){
			mSPreferences = context.getSharedPreferences(USER_INFO, 0);
		}
		mSPreferences.edit().putString(USER_INFO_PHONE, phone).commit();
		mSPreferences.edit().putString(USER_INFO_PASS, password).commit();
	}
	
	/**
	 * 获取登录信息
	 * @param context
	 * @return
	 */
	public static User getLoginInfo(Context context){
		if(mSPreferences== null){
			mSPreferences = context.getSharedPreferences(USER_INFO, 0);  
		}
		if(!TextUtils.isEmpty(mSPreferences.getString(USER_INFO_PHONE, ""))){
			User u = new User();
			u.setPhone(mSPreferences.getString(USER_INFO_PHONE, ""));
			u.setPassWord(mSPreferences.getString(USER_INFO_PASS, ""));
			return u;
		}else{
			return null;
		}
	}
	
	/**
	 * 保存店铺信息
	 * @param context
	 * @param msg
	 */
	public static void saveShopInfo(Context context,String msg){
		if(mSPreferences == null){
			mSPreferences = context.getSharedPreferences(USER_INFO, 0);
		}
		mSPreferences.edit().putString(SHOP_INFO1, msg).commit();
	}
	
	/**
	 * 获取店铺信息
	 * @param context
	 * @return
	 */
	public static User getShopInfo(Context context){
		if(mSPreferences== null){
			mSPreferences = context.getSharedPreferences(USER_INFO, 0);  
		}
		String str = mSPreferences.getString(SHOP_INFO1, "");
		if(!TextUtils.isEmpty(str)){
			return JsonUtils.parse(str, User.class);
		}else{
			return null;
		}
	}
	
	/**
	 * 删除所有登陆信息
	 * @param context
	 */
	public static void deleteInfo(Context context){
		if(mSPreferences== null){
			mSPreferences = context.getSharedPreferences(USER_INFO, 0);  
		}
		mSPreferences.edit().clear().commit();
	}
	
}
