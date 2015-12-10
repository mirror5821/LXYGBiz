package com.lxyg.app.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Address;
import com.lxyg.app.business.fragment.Register3Fragment;
import com.lxyg.app.business.iface.AddrBase;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.JsonUtils;

public class MapSelectActivity extends BaseActivity{
	private ListView mListView;
	
	private MapView mMapView = null;  
	private BaiduMap mBaiduMap;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	
	private static Context mContext;
	private double mLat,mLng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_select);
		setBack();
		setTitleText("位置选择");
		mContext = getApplicationContext();
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mListView = (ListView)findViewById(R.id.list);

		mBaiduMap = mMapView.getMap();

		//设置显示级别 3-18
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(18));
		
		//定位模式
		mCurrentMode = LocationMode.NORMAL;
		// 传入null则，恢复默认图标
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true,null));

		// 开启定位图层  暂时关闭后不会出现定位图层 
		//		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		//需设置此参数，否则无地址信息
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();

		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus status) {

			}

			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				LatLng center = status.target;

				mLat = center.latitude;
				mLng = center.longitude;

				getAddstrs(center);
//				showToast(center.latitude+"-------");
			}

			@Override
			public void onMapStatusChange(MapStatus status) {

			}
		});

	}

	

	MyLocationData locData;
	private boolean IS_FIRST_LOC = true;
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			if(IS_FIRST_LOC){
				locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);

				AppContext.Latitude = location.getLatitude();
				AppContext.Longitude = location.getLongitude();

				AppContext.Address = location.getAddrStr();
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

				mLocClient.stop();
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private String mCity;
	private String mCountry;
	private String mDistrict;
	private String mProvince;

	private void getAddstrs(LatLng center){
		FinalHttp fh = new FinalHttp();

		String url = "http://api.map.baidu.com/geocoder/v2/?ak=Vip0XHEvRRKbs9dPgMpNclzt&callback=renderReverse&location="+
				center.latitude+","+center.longitude+"&output=json&pois=1";
		fh.get(url, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				t = t.substring(t.indexOf("(")+1,t.lastIndexOf(")"));

				try{
					JSONObject jb = new JSONObject(t);
					String status = jb.getString("status");

					if(status.equals("0")){
						JSONObject result = jb.getJSONObject("result");
						JSONObject addressComponent = result.getJSONObject("addressComponent");
						mCity = addressComponent.getString("city");
						mCountry = addressComponent.getString("country");
						mDistrict = addressComponent.getString("district");
						mProvince = addressComponent.getString("province");

						showToast(mCountry+mProvince+mCity+mDistrict);
						
						String formatted_addStr= result.getString("formatted_address");
						final List<Address> lists = new ArrayList<Address>();
						//这是返回的推荐地址
						Address a = new Address();
						a.setAddr(formatted_addStr.replace(mProvince+mCity+mDistrict, ""));
						a.setName(" ");
						lists.add(a);
						//这是解析的其他列表地址
						lists.addAll(JsonUtils.parseList(result.getString("pois").replace(mProvince+mCity+mDistrict, ""), Address.class));
						
						mListView.setAdapter(new AddrAdapter<Address>(mContext, lists));
						mListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent i = new Intent(MapSelectActivity.this,Register3Fragment.class);
								i.putExtra(LAT, mLat);
								i.putExtra(LNG,mLng );
								i.putExtra(ADDRESS, lists.get(position).getAddr());
								i.putExtra(COUNTRY,mCountry );
								i.putExtra(PROVINCE, mProvince);
								i.putExtra(CITY,mCity );
								i.putExtra(DISTRICT, mDistrict);
								
								setResult(RESULT_OK, i);
								finish();
							}
							
						});
					}else{

					}
				}catch(JSONException e){
					showToast("e------"+e.getLocalizedMessage());
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});

	}
	
	public static class AddrAdapter<T extends AddrBase>extends DevListBaseAdapter<T>{

		public AddrAdapter(Context context, List<T> list) {
			super(context, list);
		}

		@Override
		public View initView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_selector, null);
			}
			TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
			tv.setText(getItem(position).getAddrName());
			return convertView;
		}

	}

	@Override  
	protected void onDestroy() {  
		super.onDestroy();  
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
		mMapView.onDestroy();  
	}  
	@Override  
	protected void onResume() {  
		super.onResume();  
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
		mMapView.onResume();  
	}  
	@Override  
	protected void onPause() {  
		super.onPause();  
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
		mMapView.onPause();  
	}  
}
