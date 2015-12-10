package com.lxyg.app.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.adapter.GoodsGridAdapter;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Category;
import com.lxyg.app.business.bean.Goods;
import com.lxyg.app.business.iface.AddrBase;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

public class GoodsNewActivity extends BaseActivity{

	private ListView mListView1;
	private GridView mGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_new);
		
		setBack();
		setTitleText("商品分类");
//		setRightTitle("库存预警");

		mListView1 = (ListView)findViewById(R.id.listview1);

		mGridView = (GridView)findViewById(R.id.gridview);
		loadData();

//		loadKucun();
	}


	private void loadKucun(){
		mHttpClient2 = new AppHttpClient(GOOG_KUCUN);
		JSONObject jb = new JSONObject();
		//"s_uid":"48957caade98442b",brand_id:4,p_id:0,is_low=0
		try{
			jb.put("s_uid", AppContext.TOKEN);
			jb.put("brand_id",0);
			jb.put("p_id",0);
			jb.put("is_low",40);
		}catch (JSONException e){

		}


		mHttpClient2.postData1(GOOG_KUCUN, new AppAjaxParam(jb), new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				showToast("fuck----"+data);
			}

			@Override
			public void onError(String error) {

			}
		});
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		if(!isFirstLoad){
//			loadData();
//		}
//
//	}


	private List<Category> mDatas = new ArrayList<Category>();
	private AddrAdapter mAdapterList;
	private AddrAdapter mAdapterGrid;

	private List<Category.Brand> mDatas2 = new ArrayList<Category.Brand>();
	private void loadData(){
		JSONObject jb = new JSONObject();
		AppHttpClient ac = new AppHttpClient(GOODS_CATEGORY);

		ac.postData(GOODS_CATEGORY, new AppAjaxParam(jb),new onRecevieDataListener<Category>() {

			@Override
			public void onReceiverData(List<Category> data, String msg) {
				mDatas.clear();
				mDatas.addAll(data);

				mDatas2.clear();
				mDatas2.addAll(mDatas.get(0).getBrands());

				if(mAdapterList == null){
					mAdapterList = new AddrAdapter(getApplicationContext(),mDatas,2);
					mListView1.setAdapter(mAdapterList);


					mAdapterGrid = new AddrAdapter(getApplicationContext(),mDatas2);
					mGridView.setAdapter(mAdapterGrid);
				}

				mAdapterList.setSelectedItem(0);

				mAdapterList.notifyDataSetChanged();
				mAdapterGrid.notifyDataSetChanged();


				mListView1.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						mDatas2.clear();
						mDatas2.addAll(mDatas.get(position).getBrands());

						mAdapterList.setSelectedItem(position);

						mAdapterList.notifyDataSetChanged();
						mAdapterGrid.notifyDataSetChanged();
					}
				});


				mGridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						startActivity(new Intent(GoodsNewActivity.this,GoodsListActivity.class).
								putExtra(INTENT_ID, mDatas.get(position).getTypeId()).putExtra("brandId",
								Integer.valueOf(mDatas2.get(position).getBrandId())));
					}
				});
			}

			@Override
			public void onReceiverError(String msg) {
				showToast(msg);
			}

			@Override
			public Class<Category> dataTypeClass() {
				return Category.class;
			}
		});
		
	}


	public static class AddrAdapter<T extends AddrBase>extends DevListBaseAdapter<T> {

		private int selectedItem = -1;
		private int mType = 0;
		private Context mContext;

		public AddrAdapter(Context context, List<T> list) {
			super(context, list);
			this.mContext = context;
		}


		public AddrAdapter(Context context, List<T> list,int type) {
			super(context, list);
			this.mType = type;
			this.mContext = context;
		}

		@Override
		public View initView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				if (mType == 2) {
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
				}else{
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv2, null);
				}

			}
			TextView tv = (TextView)convertView.findViewById(android.R.id.text1);


			if(mType == 2){
				if(position == selectedItem){
					tv.setText(Html.fromHtml("<font color='red'>" + getItem(position).getAddrName().trim() + "</font>"));
					convertView.setBackgroundResource(R.mipmap.ic_category_list_s);
				}else {
					tv.setText(getItem(position).getAddrName());
					convertView.setBackgroundResource(R.mipmap.ic_category_list_n);
				}
			}else{
				tv.setText(getItem(position).getAddrName());
			}

			return convertView;
		}

		public void setSelectedItem(int selectedItem){
			this.selectedItem = selectedItem;
		}
	}
}
