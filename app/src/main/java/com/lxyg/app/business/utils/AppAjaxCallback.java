package com.lxyg.app.business.utils;

import android.text.TextUtils;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dev.mirror.library.utils.JsonUtils;

/**
 * 
 * 请求返回结果封装类
 * @author 王沛栋
 *
 * @param <T>
 */
public class AppAjaxCallback<T> extends AjaxCallBack<String>{
	private onRecevieDataListener<T> mRecevieDataListener;
	private onResultListener mListener;
	public AppAjaxCallback(onRecevieDataListener<T> dataListener) {
		mRecevieDataListener = dataListener;
	}

	public AppAjaxCallback(onResultListener dataListener) {
		mListener = dataListener;
	}
	/**
	 * 定义普通数据请求listenre
	 * @author 王沛栋
	 *
	 */
	public interface onResultListener{
		void onResult(String data, String msg);
		void onError(String error);
	}

	/**
	 * 定义list数据请求listener
	 * @author 王沛栋
	 *
	 * @param <T>
	 */
	public interface onRecevieDataListener<T>{
		void onReceiverData(List<T> data, String msg);
		void onReceiverError(String msg);
		Class<T> dataTypeClass();
	}

	private String listNoData = "未查询到任何数据\n下拉刷新数据";
	@Override
	public void onSuccess(String t) {
		super.onSuccess(t);
		System.out.println("-----------------"+t);
		if(!TextUtils.isEmpty(t)){
			try{
				JSONObject jb = new JSONObject(t);
				String status = jb.getString("code");

				String msg = jb.getString("msg");				
				//如果==10001，表示查询错误 
				if(!status.equals("10001")){
					String data = jb.getString("data").toString();
					//表示请求是列表数据
					if(mRecevieDataListener !=null){
						List<T> list = JsonUtils.parseList(data, mRecevieDataListener.dataTypeClass());
						//如果数据不为空
						if(list!=null){
							mRecevieDataListener.onReceiverData(list, msg);
						}else{
							try{
								//判断分页list的返回
								JSONObject jb2 = new JSONObject(data);								
								if(!jb2.getString("list").equals(null)){
									mRecevieDataListener.onReceiverData(JsonUtils.parseList(jb2.getString("list"),
											mRecevieDataListener.dataTypeClass()),msg);
								}else{
									mRecevieDataListener.onReceiverError(listNoData);
								}
							}catch(Exception e){
								mRecevieDataListener.onReceiverError(listNoData);
							}
//							mRecevieDataListener.onReceiverError("----"+msg);
						}
					}else{
						mListener.onResult(data,msg);
					}
				}else{//表示没有返回数据
					if(mRecevieDataListener!=null){
						mRecevieDataListener.onReceiverError(listNoData);
					}else{
						mListener.onError(msg);
					}
				}
			}catch(JSONException e){
				if(mRecevieDataListener!=null){
					mRecevieDataListener.onReceiverError("请求失败");
				}else{
					mListener.onError("请求失败");
				}
			}
		}else{
			if(mRecevieDataListener!=null){
				mRecevieDataListener.onReceiverError("请求失败");
			}else{
				mListener.onError("请求失败");
			}
		}
	}
	@Override
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		super.onFailure(t, errorNo, strMsg);
		//正式发布时 请不要使用afinal反馈的失败内容，替换成人性化内容
		if(mRecevieDataListener!=null){
			mRecevieDataListener.onReceiverError("请求失败");
		}else{
			mListener.onError("请求失败");
		}
	}

}
