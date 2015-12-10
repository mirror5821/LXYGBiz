package com.lxyg.app.business.utils;

import android.content.Context;

public class VerificationUtil {
	
	/**
	 * 是否资质认证通过
	 * @return
	 */
	public static boolean zizhiVerification(Context context){
		SharePreferencesUtil.getShopInfo(context);
		if(SharePreferencesUtil.getShopInfo(context).getQ_verifi()==1){
			return true;
		}
		return false;
	}
	
}
