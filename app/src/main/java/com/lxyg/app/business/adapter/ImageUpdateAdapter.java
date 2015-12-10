package com.lxyg.app.business.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;

import java.util.List;

/**
 * Created by haha on 2015/9/12.
 */
public class ImageUpdateAdapter extends BaseAdapter {
    private List<String> mList;
    private LayoutInflater mInflater;
    public ImageUpdateAdapter(Context context, List<String> mList){
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_img_add, null);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_add);
            viewHolder.imgDelete = (ImageView)convertView.findViewById(R.id.img_delete);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String imgUrl = mList.get(position);
        if (!TextUtils.isEmpty(imgUrl)){
            AppContext.displayImage(viewHolder.img,imgUrl);
            viewHolder.imgDelete.setVisibility(View.VISIBLE);
//            if(imgUrl.startsWith("http")){
//                AppContext.displayImage(viewHolder.img,imgUrl);
//            }else{
//                viewHolder.img.setImageBitmap(mBitmap);
//            }
        }else{
            viewHolder.imgDelete.setVisibility(View.GONE);
            viewHolder.img.setImageResource(R.drawable.btn_photo_add);
        }

        return convertView;
    }

    private static class ViewHolder{
        ImageView img,imgDelete;
    }
}
