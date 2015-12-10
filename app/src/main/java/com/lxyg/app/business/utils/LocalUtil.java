package com.lxyg.app.business.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class LocalUtil {
	private static AlarmManager mManager;
	private static Intent mIntent;
	private static PendingIntent mPendingIntent;

	/**
	 *
	 * @param context
	 * @param seconds
	 * @param cls
	 * @param action
	 */
	private static void getInstance(Context context,int seconds,Class<?> cls,String action){
		//获取AlarmManager系统服务
		if(mManager == null){
			mManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		}
		//包装需要执行service的Intent
		if(mIntent == null){
			mIntent= new Intent(context,cls);
			mIntent.setAction(action);
		}
		if(mPendingIntent == null){
			mPendingIntent = PendingIntent.getService(context, seconds, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
	}

	/**
	 * 开启定位服务
	 */
	public static void startLocalService(Context context,int seconds,Class<?> cls,String action){
//		//获取AlarmManager系统服务
//		AlarmManager mManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		//包装需要执行service的Intent
//		Intent intent = new Intent(context,cls);
//		intent.setAction(action);
//		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		getInstance(context, seconds, cls, action);
		//触发服务的起始时间
		long triggerAtTime = SystemClock.elapsedRealtime();
		//使用AlarmManager的setRepeating方法设置顶起执行的时间间隔(seconds秒)和需要执行的service
		mManager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, seconds*1000, mPendingIntent);
	}


	public static void stopPollingService(Context context,Class<?> cls,String action){
//		AlarmManager mManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		Intent intent = new Intent(context,cls);
//		intent.setAction(action);
//		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		getInstance(context, 0, cls, action);
		mManager.cancel(mPendingIntent);
	}
}
