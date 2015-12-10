package com.lxyg.app.business.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.activity.CitySelectActivity;
import com.lxyg.app.business.activity.MapSelectActivity;
import com.lxyg.app.business.activity.RegisterActivity;
import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.bean.ShopType;
import com.lxyg.app.business.bean.User;
import com.lxyg.app.business.iface.AddrBase;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.PassWordUtil;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.ImageTools;
import dev.mirror.library.utils.JsonUtils;

public class Register3Fragment extends BaseFragment{
	private TextView mTvMap,mTvType,mTvArea;
	private EditText mEtName,mEtConstact,mEtStreet,mEtTitle;
	private LinearLayout mViewImg;
	private ImageView mImg1;
	@Override
	public int setLayoutId() {
		return R.layout.fragment_register3;
	}

	private User mUser;
	private static Context mContext;
	public ImageTools mImageTools;
	private EditText mEtTitle2,mEtTitle3;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContext = getActivity().getApplicationContext();
		mUser = SharePreferencesUtil.getLoginInfo(getActivity());
		mTvMap = initTextView(R.id.map);
		mEtName = initEditText(R.id.name);
		mEtConstact = initEditText(R.id.no);
		mEtStreet = initEditText(R.id.street);
		mTvType = initTextView(R.id.type);
		mTvArea = initTextView(R.id.area);

		//非标店铺封面添加
		mViewImg = (LinearLayout)view.findViewById(R.id.view1);
		mViewImg.setVisibility(View.GONE);

		mImg1 = (ImageView)view.findViewById(R.id.img1);
		mImg1.setOnClickListener(this);

		mEtTitle = (EditText)view.findViewById(R.id.shop_title);
		mEtTitle2 = (EditText)view.findViewById(R.id.title2);
		mEtTitle3 = (EditText)view.findViewById(R.id.title3);

		initTextView(R.id.phone).setText(mUser.getPhone());
		if(mImageTools == null){
			mImageTools = new ImageTools(this);
		}

		initButton(R.id.sub);

	}

	private void loadShopType(final ShopTypeListenr shopType){
		AppHttpClient ac = new AppHttpClient(Constants.SHOP_TYPE);
		ac.postData1(Constants.SHOP_TYPE, null, new onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				shopType.getList(JsonUtils.parseList(data, ShopType.class));
			}

			@Override
			public void onError(String error) {

			}
		});
	}

	public interface ShopTypeListenr{
		void getList(List<ShopType> lists);
	}

	private ShopType mShopType = new ShopType();
	private void showShopTypeDialog(){
		loadShopType(new ShopTypeListenr() {
			@Override
			public void getList(final List<ShopType> lists) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("请选择店铺类型!");
				builder.setAdapter(new AddrAdapter<ShopType>(getActivity(), lists), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(which!=0){
							mViewImg.setVisibility(View.VISIBLE);
						}else{
							mViewImg.setVisibility(View.GONE);
						}
						mShopType = lists.get(which);
						mTvType.setText(mShopType.getName());
						dialog.dismiss();
					}
				});
				builder.create();
				builder.show();
			}
		});
	}

	public static class AddrAdapter<T extends AddrBase>extends DevListBaseAdapter<T> {
		private Context mContext;
		public AddrAdapter(Context context, List<T> list) {
			super(context, list);
			mContext = context;
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
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.area:
				startActivityForResult(new Intent(getActivity(),CitySelectActivity.class), DISTRICT_REQUESTCODE);
				break;
			case R.id.img1:
				mImageTools.showGetImageDialog("选择照片的方式!");
				break;
			case R.id.type:
				showShopTypeDialog();
				break;
			case R.id.map:
				startActivityForResult(new Intent(getActivity(),MapSelectActivity.class), MAP_REQUESTCODE);
				break;
			case R.id.sub:
				name = mEtName.getText().toString().trim();
				if(TextUtils.isEmpty(name)){
					showToast("请输入店铺名称!");
					return;
				}
				concact = mEtConstact.getText().toString().trim();
				if(TextUtils.isEmpty(concact)){
					showToast("请输入联系人!");
					return;
				}
				if(TextUtils.isEmpty(provinceName)){
					showToast("请在地图上选择店铺位置!");
					return;
				}
				street = mEtStreet.getText().toString().trim();
				if(TextUtils.isEmpty(street)){
					showToast("请输入店铺详细地址!");
					return;
				}
				if(TextUtils.isEmpty(mShopType.getTitle())){
					showToast("请选择店铺类型!");
					return;
				}
				sub();
				break;
		}
	}

	private String mDistricId;
	private int mType = 0;//选择照片的类型
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
				case MAP_REQUESTCODE:
					Bundle mBundle = data.getExtras();
					provinceName = mBundle.getString(PROVINCE);
					cityName = mBundle.getString(CITY);
					areaName = mBundle.getString(DISTRICT);
					street = mBundle.getString(ADDRESS);
					lat = mBundle.getDouble(LAT);
					lng = mBundle.getDouble(LNG);

					mEtStreet.setText(street);
					mTvMap.setText(provinceName+" "+cityName+" "+areaName);
					showToast(mBundle.getString(CITY));
					break;
				case DISTRICT_REQUESTCODE:
					mDistricId = data.getExtras().getString(DISTRICT_ID);
					mTvArea.setText(data.getExtras().getString(DISTRICT_NAME));
					showToast(mDistricId);
					break;

				case ImageTools.CAMARA:
					mType = ImageTools.CAMARA;
					mImageTools.getBitmapFromCamara(new ImageTools.OnBitmapCreateListener() {

						@Override
						public void onBitmapCreate(Bitmap bitmap, String path) {
							mImageTools.startZoomPhotoByCamera4To3(Uri.fromFile(new File(path)), 800, 600);
						}
					});
					break;

				case ImageTools.GALLERY:
					mType = ImageTools.GALLERY;
					mImageTools.startZoomPhoto4To3(data.getData(), 800, 600);
					break;
				case ImageTools.BITMAP:
					switch (mType){
						case ImageTools.CAMARA:
							Bitmap bitmap2 = mImageTools.getBitmapFromZoomPhoto(data);
//							upLoadImg(bitmap2);
							initImages(bitmap2);
							break;

						case ImageTools.GALLERY:
							Bitmap bitmap = mImageTools.getBitmapFromGallery(data);
//							upLoadImg(bitmap);
							initImages(bitmap);
							break;
					}

					break;


			}
		}
	}

	private void initImages(Bitmap bitmap){

		mImg1.setVisibility(View.VISIBLE);
		mImg1.setImageBitmap(bitmap);

		upLoadImg(bitmap);
	}
	private String mImgNo;
	private void upLoadImg(Bitmap bitmap){
		AppAjaxParam ap = new AppAjaxParam();
		ap.put("ImgData", mImageTools.bitmapToString(bitmap));

		AppHttpClient aClient = new AppHttpClient();
		aClient.uploadImg(ap, new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mImgNo = data;
				showToast(msg);
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}

	private String name;
	private String concact;
	private double lat;
	private double lng;
	private String street;
	private String provinceName;
	private String cityName;
	private String areaName;
	private String templeId="0";
	private String coverImg;
	private int Mid=0;

	private void sub(){


		JSONObject jb = new JSONObject();
		try{
			jb.put("name", name);
			jb.put("password", PassWordUtil.md5PassWord(mUser.getPassWord()));
			jb.put("concact", concact);
			jb.put("phone", mUser.getPhone());			
			jb.put("lat", lat);
			jb.put("lng", lng);
			jb.put("street", street);
			jb.put("provinceName", provinceName);
			jb.put("cityName", cityName);
			jb.put("areaName", areaName);
			jb.put("templeId", templeId);
			jb.put("mId", Mid);
			jb.put("shop_type",mShopType.getType_id());
			jb.put("fulladdress", provinceName + cityName + areaName + street);
			//多加的字段districtId
			jb.put("districtId", mDistricId);


			if(!mShopType.getType_id().equals("1")){
				String title = mEtTitle.getText().toString();
				String title2 = mEtTitle2.getText().toString();
				String title3 = mEtTitle3.getText().toString();
				if(TextUtils.isEmpty(title)){
					showToast("请输入店铺标语");
					return;
				}
				if(TextUtils.isEmpty(title2)){
					showToast("请输入店铺标题2");
					return;
				}
				if(TextUtils.isEmpty(title3)){
					showToast("请输入店铺标题3");
					return;
				}
				if(TextUtils.isEmpty(mImgNo)){
					showToast("请设置店铺封面");
					return;
				}

				jb.put("cover_img",mImgNo);
				jb.put("title",title);
				jb.put("title2",title2);
				jb.put("title3",title3);
			}

		}catch(JSONException e){

		}

		String fName = Constants.ADD_SHOP_INFO;
		AppHttpClient ac = new AppHttpClient(fName);
		ac.postData1(fName, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				SharePreferencesUtil.saveShopInfo(getActivity(), data);
				showToast(msg);
				
				RegisterActivity activity = (RegisterActivity)getActivity();
				activity.switchFragment(new Register4Fragment());
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});


	}
}
