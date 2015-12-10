package com.lxyg.app.business.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.bean.Goods;
import com.lxyg.app.business.iface.GoodsListener;
import com.lxyg.app.business.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.business.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mirror.library.utils.PriceUtil;

/**
 * 商品管理
 * @author 王沛栋
 *
 */
public class GoodsListFragment extends BaseListFragment<Goods> implements Constants,GoodsListener{

	private String mTypeId;
	private String mType;
	private String mNotify;
	private int mBrandId;

	private TextView mTvNotif;
	private LinearLayout mViewBtn;
	private Button mBtnSub;
	private CheckBox mCbAll;

	private AppHttpClient mHttpClient;

	private int mProductNum = 30;


	/**
	 * 创建时实例化必须项
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageNo = 1;
		mList = new ArrayList<Goods>();
		mHttpClient = new AppHttpClient(GOODS_LIST);

		//产品类型
		mTypeId = getArguments().getString(INTENT_ID);
		//操作类型
		mType = getArguments().getString(TYPE_ID);
		//标题显示文字
		mNotify = getArguments().getString(NOTIFY_TV);
		//brandId
		mBrandId = getArguments().getInt("BrandId");
	}



	/**
	 * view创建实例化view
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTvNotif = (TextView)view.findViewById(R.id.tv);
		mTvNotif.setText(mNotify);

		mViewBtn = (LinearLayout)view.findViewById(R.id.view_btn);
		mBtnSub = (Button)view.findViewById(R.id.btn_goods);
		mBtnSub.setOnClickListener(this);
		mCbAll = (CheckBox)view.findViewById(R.id.cb_all);

		if(mType.equals("2")){
			mBtnSub.setText("确认缺货");
		}

		mCbAll.setOnClickListener(this);
	}

	/**
	 * 加载layout文件
	 */
	@Override
	public int setLayoutId() {
		return R.layout.fragment_goods;
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_goods:
				if(mType.equals("1")){
					showNumDialog();
				}else{
					sub();
				}


				break;

			case R.id.cb_all: //全选 反选事件
				mIsSelected.clear();

				if(mCbAll.isChecked()){
					for(int i=0; i<mList.size();i++) {
						mIsSelected.put(i,true);
					}
				}else{
					for(int i=0; i<mList.size();i++) {
						mIsSelected.put(i,false);
					}
				}
				mAdapter.notifyDataSetChanged();
		}
	}

	//用于记录单个更改库存时的位置，用于刷新界面使用
	private int mPosition;
	private AlertDialog.Builder mBuilder;
	private void showNumDialog(){
		if(mBuilder == null){
			mBuilder = new AlertDialog.Builder(getActivity());
		}
		//"请输入库存数", "请输入批量上货库存", "确定",
		mBuilder.setTitle("请输入批量上货库存数");

		View v= getActivity().getLayoutInflater().inflate(R.layout.view_input_num,null);
		mBuilder.setView(v);

		final Dialog d = mBuilder.create();
		final EditText et = (EditText)v.findViewById(R.id.et);
		final Button btnCancel = (Button)v.findViewById(R.id.cancel);
		final Button btnOk = (Button)v.findViewById(R.id.ok);

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = et.getText().toString();
				if (TextUtils.isEmpty(num)) {
					showToast("请输入库存数!");
					return;
				}

				mProductNum = Integer.valueOf(num);

				d.dismiss();

				if(alertEvent == 1){
					sub();
				}else{
					updateKucun();
				}
			}
		});

		d.show();
	}


	private void updateKucun(){
		if(TextUtils.isEmpty(mProductId)){
			showToast("请选择更改库存的产品");
			return;
		}

		mHttpClient = new AppHttpClient(UPDATE_KUCUN);
		JSONObject jb = new JSONObject();
		try {
			//"s_uid":"48957caade98442b",p_id:1,p_num:123
			jb.put("s_uid",AppContext.TOKEN);
			jb.put("p_id",mProductId);
			jb.put("p_num",mProductNum);
		}catch (JSONException e){
			return;
		}
		mHttpClient.postData1(UPDATE_KUCUN, new AppAjaxParam(jb), new onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				showToast(msg);
				mList.get(mPosition).setProduct_number(mProductNum);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});


	}

	/**
	 *
	 */
	private void sub(){
		if(mList.size()<1)
			return;
		String pid = null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<mIsSelected.size();i++){
			if(mIsSelected.get(i)){
				sb.append(mList.get(i).getProductId()+",");
			}
		}
		pid = sb.toString();
		if(pid.equals("")){
			showToast("请选择上架或者下架的商品!");
			return;
		}
		pid = pid.substring(0, pid.lastIndexOf(","));

		JSONObject jb = new JSONObject();
		try {
			jb.put("type", mType);
			jb.put("pids",pid);
			jb.put("product_number",mProductNum);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AppHttpClient ac = new AppHttpClient(GOODS_REPERTORY);
		ac.postData1(GOODS_REPERTORY, new AppAjaxParam(jb), new onResultListener() {

			@Override
			public void onResult(String data, String msg) {

				showToast(msg);
				//设置全选按钮为false
				mCbAll.setChecked(false);
				//清空列表选择按钮
				for(int i=0; i<mList.size();i++) {
					mIsSelected.put(i,false);
				}

				mList.clear();
				//更新列表
				pageNo = 1;
				mAdapter.notifyDataSetChanged();
				//加载数据  因为列表上商品的性质已经改变
				loadData();
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}

	/**
	 * 实现listview界面
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = getActivity().getLayoutInflater().inflate(R.layout.item_goods_list, null);
		}
		final LinearLayout v = (LinearLayout)convertView.findViewById(R.id.v);
		final CheckBox cb = (CheckBox)convertView.findViewById(R.id.cb);
		cb.setTag("check"+position);
		cb.setChecked(mIsSelected.get(position)==null?false:mIsSelected.get(position));
		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!cb.isChecked()){
					mCbAll.setChecked(false);
				}
				mIsSelected.put(position,  cb.isChecked());
			}
		});


		TextView name = (TextView)convertView.findViewById(R.id.name);
		TextView price = (TextView)convertView.findViewById(R.id.price);
		TextView title = (TextView)convertView.findViewById(R.id.t);
		TextView kucun = (TextView)convertView.findViewById(R.id.kucun);
		ImageView img = (ImageView)convertView.findViewById(R.id.img);
		ImageView imgShang = (ImageView)convertView.findViewById(R.id.img_shang);
//		if(mType.equals("2")){
//			cb.setVisibility(View.GONE);
//		}
		Goods g = mList.get(position);
		//1已上架 2未下架

		int status = Integer.valueOf(g.getStatus());

		String pName = g.getName();
		name.setText(pName);
		price.setText("￥"+PriceUtil.floatToString(g.getPrice()));
		title.setText(g.getTitle());
		AppContext.displayImage(img, g.getCover_img());

		if(status == 1){
			int kc = g.getProduct_number();
			final int version = Build.VERSION.SDK_INT;
			if (version >= 21) {
				if(kc<QUE_HUO_NUM){
					kucun.setTextColor(ContextCompat.getColor(getActivity(),R.color.main_red));
					convertView.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_kucun_warn));
				}else{
					kucun.setTextColor(ContextCompat.getColor(getActivity(),R.color.btn_blue));
					convertView.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_normal_press_white));
				}


			} else {
				if(g.getProduct_number()<QUE_HUO_NUM){
					kucun.setTextColor(getActivity().getResources().getColor(R.color.main_red));
					convertView.setBackground(getActivity().getResources().getDrawable(R.mipmap.ic_kucun_warn));
				}else{
					kucun.setTextColor(getActivity().getResources().getColor(R.color.btn_blue));
					convertView.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_normal_press_white));
				}
			}

			kucun.setText("库存:"+kc);

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertEvent = 2;
					mProductId = mList.get(position).getProductId();
					mPosition = position;
					showNumDialog();
				}
			});

		}else{

		}
		return convertView;
	}

	// 用来控制CheckBox的选中状况
	private static HashMap<Integer,Boolean> mIsSelected;

	//是否第一次加载数据 用于保存checkbox的状态
	private boolean mFirstLoad = true;

	//加载listview数据
	@Override
	public void loadData() {
//		if(mType.equals("2")){
//			mViewBtn.setVisibility(View.GONE);
//		}
		JSONObject jb = new JSONObject();
		try{
			if(mBrandId == 0){
				jb.put("typeId", mTypeId);
				jb.put("brandId",0);
			}else{
				jb.put("typeId", 0);
				jb.put("brandId",mBrandId);
			}

			jb.put("type", mType);
			jb.put("pg", pageNo+"");

		}catch(JSONException e){

		}

		mHttpClient.postData(GOODS_LIST, new AppAjaxParam(jb), new onRecevieDataListener<Goods>() {

			@Override
			public void onReceiverData(List<Goods> data, String msg) {
				if(mFirstLoad)
					if(mIsSelected == null){
						mIsSelected = new HashMap<Integer,Boolean>();
					}
				if(pageNo == mDefaultPage){
					mList.clear();
					//如果第一次加载列表 则往cb添加数据 否则刷新禁止清空数据
					if(mFirstLoad){
						mFirstLoad = false;
						for(int i=0; i<data.size();i++) {
							mIsSelected.put(mList.size()+i,false);
						}
					}
				}else{
					for(int i=0; i<data.size();i++) {
						mIsSelected.put(mList.size()+i,false);
					}
				}


				mList.addAll(data);
				setListAdapter();
			}

			@Override
			public void onReceiverError(String msg) {
				setLoadingFailed(msg);
//				showToast(msg);
			}

			@Override
			public Class<Goods> dataTypeClass() {
				return Goods.class;
			}
		});
	}


	private int alertEvent = 1;
	private String mProductId;
	//点击事件
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View view,
							int position, long num) {
		if(mType.equals("2")){
			alertEvent = 2;
			mProductId = mList.get(position-1).getProductId();
			showNumDialog();
		}

	}



	@Override
	public void change() {
		loadData();
		mCbAll.setChecked(false);
		for(int i=0; i<mList.size();i++) {
			mIsSelected.put(i,false);
		}
	}
}
