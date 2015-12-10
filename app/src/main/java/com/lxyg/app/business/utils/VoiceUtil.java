package com.lxyg.app.business.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.Timer;
import java.util.TimerTask;

public class VoiceUtil {
	// 默认发音人
	private static String voicer = "xiaoyan";

	// 语音合成对象
	private static SpeechSynthesizer mTts;
	private static String mMsg;

	private static boolean mIsStop = false;

	/**
	 * 文本转声音
	 * @param context
	 * @param msg
	 */
	public static void msgToVoice(Context context,String msg){
		if(mTts == null){
			// 初始化合成对象
			mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
		}
		mMsg = msg;
		setParam();
		mTts.startSpeaking(msg, mTtsListener);
	}

	/**
	 * 关闭声音
	 */
	public static void stopSpeaking(){
		if(mTts !=null){
			mTts.stopSpeaking();
			mIsStop = true;
		}
	}
	
	/**
	 * 参数设置
	 * @param
	 * @return 
	 */
	private static void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置在线合成发音人
		mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
		//设置合成语速
		mTts.setParameter(SpeechConstant.SPEED, "50");
		//设置合成音调
		mTts.setParameter(SpeechConstant.PITCH,"50");
		//设置合成音量
		mTts.setParameter(SpeechConstant.VOLUME, "50");
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}

	/**
	 * 初始化监听。
	 */
	private static InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}		
		}
	};

	/**
	 * 合成回调监听。
	 */
	private static SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
		}

		@Override
		public void onSpeakPaused() {
		}

		@Override
		public void onSpeakResumed() {
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		@Override
		public void onCompleted(SpeechError error) {


			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					mTts.startSpeaking(mMsg, mTtsListener);
				}
			};


			if(mIsStop)
				timer.cancel();
			timer.schedule(task,10000);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};
	
}
