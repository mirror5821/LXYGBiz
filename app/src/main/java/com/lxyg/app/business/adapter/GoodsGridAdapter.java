package com.lxyg.app.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Goods;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

public class GoodsGridAdapter extends DevListBaseAdapter<Goods>{

	private LayoutInflater mInflater;
	private List<Goods> mList;
	public GoodsGridAdapter(Context context, List<Goods> list) {
		super(context, list);
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	@Override
	public View initView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_goods, null);
		}
		TextView name = (TextView)ViewHolder.get(convertView, R.id.name);
		TextView no = (TextView)ViewHolder.get(convertView, R.id.no);
		ImageView img = (ImageView)ViewHolder.get(convertView, R.id.img);
		
		Goods g = mList.get(position);
		name.setText(g.getName());
		no.setText(g.getNum());
		
		AppContext.displayImage(img,g.getImg());
		
		return convertView;
	}
}