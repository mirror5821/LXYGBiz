package com.lxyg.app.business.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lxyg.app.business.R;
import com.lxyg.app.business.activity.RegisterActivity;
import com.lxyg.app.business.bean.CityFromDB;
import com.lxyg.app.business.bean.User;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.DBHelper;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.ImageTools;
import dev.mirror.library.utils.ImageTools.OnBitmapCreateListener;

public class Register4Fragment extends BaseFragment {

	@Override
	public int setLayoutId() {
		return R.layout.fragment_register4;
	}

	public ImageTools mImageTools;

	private EditText mETBankName, mETKName,mETNo,mETZH,mETCardNo;

	private ImageView mImg1,mImg2,mImg3;

	private String mName,mNo,mZh,mCardNo,mBankName,   mBNum;
	private String mImgNo,mImgCard,mImgYY,  mImgId;


	private Spinner mProSpinner;
	private Spinner mCitySpinner;
	private List<CityFromDB> allList;

	/*
	 * 当前所有的省列表
	 */
	private List<CityFromDB> mCurrentProvices = new ArrayList<CityFromDB>();
	private ArrayList<String> mPros = new ArrayList<String>();
	private String mCurrentProName;
	/*
	 * 当前省的市列表
	 */
	private List<CityFromDB> mCurrentCity = new ArrayList<CityFromDB>();
	private ArrayList<String> mCitys = new ArrayList<String>();
	private String mCurrentCityName;

	private User mUser;

	@SuppressWarnings("unused")
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		RegisterActivity activity = (RegisterActivity)getActivity();
		activity.jump();
		mImageTools = new ImageTools(this);
		mUser = SharePreferencesUtil.getShopInfo(getActivity());
		mETBankName = initEditText(R.id.bank_name);
		mETKName = initEditText(R.id.name);
		mETNo = initEditText(R.id.no);
		mETZH= initEditText(R.id.zhihang);
		mETCardNo = initEditText(R.id.card_num);

		initLinearLayout(R.id.view1);
		initLinearLayout(R.id.view2);
		initLinearLayout(R.id.view3);

		mImg1 = initImageView(R.id.img1);
		mImg2 = initImageView(R.id.img2);
		mImg3 = initImageView(R.id.img3);

		initButton(R.id.sub);

		mProSpinner = (Spinner) view.findViewById(R.id.province_spinner);
		mCitySpinner = (Spinner) view.findViewById(R.id.city_spinner);

		try {
			DBHelper hp = new DBHelper(getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}

		final FinalDb db = FinalDb.create(getActivity(), "citycode.db");
		allList = db.findAll(CityFromDB.class);
		for (CityFromDB city : allList) {
			if (city.getFather_id().equals(String.valueOf(0))) {
				mCurrentProvices.add(city);
				mPros.add(city.getRegion_name());
			}
		}
		if (mPros.size() < 1) {
			return;
		}
		System.out.println("mPros.size() = " + mPros.size());
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> proAdapter=new ArrayAdapter<String>(getActivity(), R.layout.item_selector, mPros);
		mProSpinner.setAdapter(proAdapter);
		mProSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mCurrentProName = parent.getItemAtPosition(position).toString();
				mCurrentCityName = "";
				mCurrentCity.clear();
				mCitys.clear();
				String currentProId = null;
				for (CityFromDB city : mCurrentProvices) {
					if (mCurrentProName.equals(city.getRegion_name())) {
						currentProId = city.getRegion_code();
					}
				}
				for (CityFromDB city : allList) {
					if (city.getFather_id().equals(currentProId)) {
						mCurrentCity.add(city);
						mCitys.add(city.getRegion_name());
					}
				}
				if (mCitys.size() < 1) {
					return;
				}
				// 建立Adapter并且绑定数据源
				ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(),  R.layout.item_selector, mCitys);
				mCitySpinner.setAdapter(cityAdapter);
				mCitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						mCurrentCityName = parent.getItemAtPosition(position).toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private int mPhotoType = 1;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.view1:
			mPhotoType = 1;
			mImageTools.showGetImageDialog("选择照片的方式!");
			break;

		case R.id.view2:
			mPhotoType = 2;
			mImageTools.showGetImageDialog("选择照片的方式!");
			break;

		case R.id.view3:
			mPhotoType = 3;
			mImageTools.showGetImageDialog("选择照片的方式!");
			break;

		case R.id.sub:
			mName = mETKName.getText().toString().trim();
			mNo = mETNo.getText().toString().trim();
			mBankName = mETBankName.getText().toString().trim();
			mZh = mETZH.getText().toString().trim();
			mCardNo = mETCardNo.getText().toString().trim();

			if(TextUtils.isEmpty(mName)){
				showToast("请输入持卡人姓名!");
				return;
			}

			if(TextUtils.isEmpty(mNo)){
				showToast("请输入负责人身份证号码!");
				return;
			}

			if(TextUtils.isEmpty(mBankName)){
				showToast("请输入银行名称!");
				return;
			}

			if(TextUtils.isEmpty(mZh)){
				showToast("请输入支行名称!");
				return;
			}
			if(TextUtils.isEmpty(mCardNo)){
				showToast("请输入银行卡号!");
				return;
			}
			if(TextUtils.isEmpty(mCurrentProName)){
				showToast("请选择银行开户行省份");
				return;
			}
			if(TextUtils.isEmpty(mCurrentCityName)){
				showToast("请选择银行开户行城市");
				return;
			}

			sub();
			break;
		}
	}

	private void sub(){


		JSONObject jb = new JSONObject();
		try{
			jb.put("userName", mName);
			jb.put("idNum",mNo );
			jb.put("handlerImg", mImgNo);
			jb.put("idImg", mImgId);
			jb.put("bankName", mBankName);
			jb.put("branchName",mZh );
			jb.put("bankCardNum", mCardNo);
			jb.put("bankImg", mImgCard);
			jb.put("bussinessImg", mImgYY);//mImgNo,mImgCard,mImgYY
			jb.put("bussinessNum", mBNum);
			jb.put("coverImg", null);
			jb.put("shopId", null);
			jb.put("uid", mUser.getUuid());
//			jb.put("uid", "e6c74604-350c-472b-9167-af6e5fe15c46");
			jb.put("bankProvince",mCurrentProName );
			jb.put("bankCity", mCurrentCityName);
		}catch(JSONException e){

		}

		AppHttpClient mHttpClient = new AppHttpClient(SHOP_ZIZHI);
		mHttpClient.postData1(SHOP_ZIZHI, new AppAjaxParam(jb),
				new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				showToast(msg);
				getActivity().finish();
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});

	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode ==Activity.RESULT_OK){

			switch (requestCode) {
			case ImageTools.CAMARA:
				mImageTools.getBitmapFromCamara(new OnBitmapCreateListener() {

					@Override
					public void onBitmapCreate(Bitmap bitmap, String path) {

						initImages(bitmap);
					}
				});
				break;

			case ImageTools.GALLERY:
				Bitmap b = mImageTools.getBitmapFromGallery(data);
				initImages(b);
				break;
			}
		}
	}


	private void initImages(Bitmap bitmap){
		switch (mPhotoType) {
		case 1:
			mImg1.setVisibility(View.VISIBLE);
			mImg1.setImageBitmap(bitmap);
			break;
		case 2:
			mImg2.setImageBitmap(bitmap);
			mImg2.setVisibility(View.VISIBLE);
			break;
		case 3:
			mImg3.setImageBitmap(bitmap);
			mImg3.setVisibility(View.VISIBLE);
			break;

		}

		upLoadImg(bitmap);
	}

	private void upLoadImg(Bitmap bitmap){
		showProgressDialog("正在上传图片");
		AppAjaxParam ap = new AppAjaxParam();
		ap.put("ImgData", mImageTools.bitmapToString(bitmap));

		AppHttpClient aClient = new AppHttpClient();
		aClient.uploadImg(ap, new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				switch (mPhotoType) {
				case 1:
					mImgNo = data;
					break;

				case 2:
					mImgCard = data;
					break;
				case 3:
					mImgYY = data;
					break;
				}
				showToast(msg);
				cancelProgressDialog();
			}

			@Override
			public void onError(String error) {
				cancelProgressDialog();
				showToast(error);
			}
		});
	}
}
