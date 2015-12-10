package com.lxyg.app.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.adapter.GoodsGridAdapter;
import com.lxyg.app.business.bean.Goods;
import com.lxyg.app.business.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONObject;

import java.util.List;

public class GoodsActivity<T> extends BaseActivity implements OnItemClickListener{
	private GridView mGridView;
	private List<Goods> mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_gridview);
		
		setBack();
		setTitleText("商品分类");
		mGridView = (GridView)findViewById(R.id.gridview);

		setRightTitle("库存预警");
		
		loadData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.right_text:

				break;
		}
	}



	private boolean isFirstLoad = true;
	@Override
	protected void onResume() {
		super.onResume();
		if(!isFirstLoad){
			loadData();
		}

	}
	private void loadData(){
		JSONObject jb = new JSONObject();
		AppHttpClient ac = new AppHttpClient(GOODS_CATAGORY);
		
		ac.postData(GOODS_CATAGORY, new AppAjaxParam(jb),new onRecevieDataListener<Goods>() {

			@Override
			public void onReceiverData(List<Goods> data, String msg) {
				mList = data;
//				mList.remove(4);

//				Goods g = new Goods();
//				g.setName("非标产品");
//				g.setNum("0");
//
//				mList.add(g);

				//暂时屏蔽掉进口干红
				mGridView.setAdapter(new GoodsGridAdapter(getApplicationContext(), mList));
				mGridView.setOnItemClickListener(GoodsActivity.this);
				isFirstLoad = false;
			}

			@Override
			public void onReceiverError(String msg) {
				showToast(msg);
			}

			@Override
			public Class<Goods> dataTypeClass() {
				return Goods.class;
			}
		});
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startActivity(new Intent(GoodsActivity.this,GoodsListActivity.class).
				putExtra(INTENT_ID, mList.get(position).getGoodsId()));
	}
}
