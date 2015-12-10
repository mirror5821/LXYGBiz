package com.lxyg.app.business.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lxyg.app.business.activity.BaseActivity;
import com.lxyg.app.business.activity.LoginActivity;
import com.lxyg.app.business.utils.VoiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver{
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	//接收自定义消息
        	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {//接收到通知
            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {//点击通知
			//点击通知栏guan'bi
			VoiceUtil.stopSpeaking();
            processCustomMessage(context, bundle);

			//打开自定义的Activity
			Intent i = new Intent(context, LoginActivity.class);
			i.putExtras(bundle);
			//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);

//        	//打开自定义的Activity
//        	Intent i = new Intent(context, ActivityManager.getInstance().getLastActivity());
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}


	private void receivingNotification(Context context, Bundle bundle){
		//通知栏标题
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        //通知栏内容
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        //其他参数
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        String msg1 = "您有新的订单可以抢";
        String msg2 = "您有一笔新的订单";
        String msg3 = "交易完成";
        if(message.equals(msg3)){
        	//不播报内容
		}else{
			VoiceUtil.msgToVoice(context, message);
		}

    }

	// 打印所有的 intent extra 数据  在此输出所有数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			}
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if (BaseActivity.isForeground) {

			Intent msgIntent = new Intent(BaseActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(BaseActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(BaseActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}else{
			VoiceUtil.msgToVoice(context, message);
		}
	}
}
