package com.lxyg.app.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.City;
import com.lxyg.app.business.bean.District;
import com.lxyg.app.business.fragment.Register3Fragment;
import com.lxyg.app.business.iface.AddrBase;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.CityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

/**
 * Created by 王沛栋 on 2015/11/4.
 */
public class CitySelectActivity extends BaseActivity {
    private ListView mListView1,mListView2,mListView3;
    private LinearLayout mViewLoading;
    private TextView mTvEmpty;

    private List<City> mCitys;

    private AddrAdapter mAdapterCity;
    private AddrAdapter mAdapterArea;
    private AddrAdapter mAdapterDistrict;
    private List<City.Citys> mCity = new ArrayList<>();
    private List<City.Citys.Areas> mArea = new ArrayList<>();
    private List<District> mDistrict = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_select);

        mListView1 = (ListView)findViewById(R.id.listview1);
        mListView2 = (ListView)findViewById(R.id.listview2);
        mListView3 = (ListView)findViewById(R.id.listview3);
        mViewLoading = (LinearLayout)findViewById(R.id.loading);
        mTvEmpty = (TextView)findViewById(R.id.tv_empty);

        mCitys = new ArrayList<City>();
        mCitys.addAll(CityUtil.readCity(getApplicationContext()));

        mCity.addAll(mCitys.get(0).getCity());

        mAdapterCity = new AddrAdapter(getApplicationContext(),mCitys.get(0).getCity());

        mListView1.setAdapter(mAdapterCity);
        mAdapterCity.setSelectedItem(0);
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapterCity.setSelectedItem(position);
                mAdapterCity.notifyDataSetChanged();

                mArea.clear();
                if(position == 0){
                    mArea.addAll(CityUtil.readCity(getApplicationContext()).get(0).getCity().get(0).getArea());
                }else{
                    mArea.addAll(mCity.get(position).getArea());
                }

                mAdapterArea.setSelectedItem(0);
                mAdapterArea.notifyDataSetChanged();

                getDistrict(mArea.get(0).getCode());


            }
        });

        mArea = mCity.get(0).getArea();
        mAdapterArea = new AddrAdapter(getApplicationContext(),mArea);

        mListView2.setAdapter(mAdapterArea);
        mAdapterArea.setSelectedItem(0);

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapterArea.setSelectedItem(position);
                mAdapterArea.notifyDataSetChanged();

                getDistrict(mArea.get(position).getCode());

            }
        });

        getDistrict(mArea.get(0).getCode());

    }

    private AppHttpClient mHttpClient;
    private void getDistrict(int code){
        loading();
        if(mHttpClient == null){
            mHttpClient = new AppHttpClient(DISTRICTS);
        }

        JSONObject jb = new JSONObject();
        try {
            jb.put("area_id",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mHttpClient.postData(DISTRICTS, new AppAjaxParam(jb), new AppAjaxCallback.onRecevieDataListener<District>() {
            @Override
            public void onReceiverData(List<District> data, String msg) {
                if (data.size() == 0) {
                    loadEmpty();
                    return;
                }
                mDistrict.clear();
                mDistrict.addAll(data);

                if(mAdapterDistrict == null){
                    mAdapterDistrict = new AddrAdapter(getApplicationContext(),mDistrict);
                    mListView3.setAdapter(mAdapterDistrict);
                }

                loadOk();


                mAdapterDistrict.notifyDataSetChanged();


                mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(CitySelectActivity.this,Register3Fragment.class);
                        District d = mDistrict.get(position);
                        i.putExtra(DISTRICT_ID, d.getDistrictId());
                        i.putExtra(DISTRICT_NAME,d.getName());
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });
            }

            @Override
            public void onReceiverError(String msg) {
                loadEmpty();
            }

            @Override
            public Class<District> dataTypeClass() {
                return District.class;
            }
        });

    }

    private void loading(){
        mViewLoading.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    private void loadOk(){
        mViewLoading.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.GONE);
    }

    private void loadEmpty(){
        mViewLoading.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.VISIBLE);
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
            if(convertView == null){
//                switch (mType){
//                    case 0:
//                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
//                        break;
//                    case 2:
//                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
//                        break;
//                    case 3:
//                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv2, null);
//                        break;
//                }

                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
            }
            TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
            tv.setText(getItem(position).getAddrName());

            if(position == selectedItem){

                switch (mType){
                    case 0:
                        tv.setText(Html.fromHtml("<font color='red'>" + getItem(position).getAddrName() + "</font>"));
                        break;
                    case 2:
                        tv.setText(Html.fromHtml("<font color='red'>"+getItem(position).getAddrName()+"</font>"));
                        break;
                    case 3:
                        tv.setText(Html.fromHtml("<font color='blue'>"+getItem(position).getAddrName()+"</font>"));
                        break;
                }
            }

            return convertView;
        }

        public void setSelectedItem(int selectedItem){
            this.selectedItem = selectedItem;
        }

    }
}
