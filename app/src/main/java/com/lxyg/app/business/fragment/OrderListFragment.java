package com.lxyg.app.business.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.activity.OrderDetailsActivity;
import com.lxyg.app.business.activity.OrderShouActivity;
import com.lxyg.app.business.activity.PrintTicketActivity;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Constants;
import com.lxyg.app.business.bean.Order;
import com.lxyg.app.business.iface.PostDataListener;
import com.lxyg.app.business.service.LocalService;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.LocalUtil;
import com.lxyg.app.business.utils.NetUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

/**
 * Created by 王沛栋 on 2015/11/17.
 */
public class OrderListFragment extends BaseFragment{
    @Override
    public int setLayoutId() {
        return R.layout.fragment_order_new2;
    }

    private List<Order> mList = new ArrayList<>();
    private int mStatus;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStatus = getArguments().getInt(INTENT_ID);
    }

    private MyAdapter mAdapter;
    private UltimateRecyclerView mListView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mAdapter == null){
            mListView = (UltimateRecyclerView)view.findViewById(R.id.ultimate_recycler_view);

            loadData();
        }
    }


    class MyAdapter extends UltimateViewAdapter {
        final private List<Order> list;
        private int mStatus;
        private LayoutInflater mInfater;

        public MyAdapter(List<Order> list, Context context,int status) {
            this.list = list;
            mStatus = status;
            mInfater = LayoutInflater.from(context);
        }

        @Override
        public UltimateRecyclerviewViewHolder getViewHolder(View view) {
            UltimateRecyclerviewViewHolder g = new UltimateRecyclerviewViewHolder(view);
            return g;
        }



        @Override
        public HolderView onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
            return new HolderView(view, true);
        }

        @Override
        public UltimateRecyclerviewViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return new UltimateRecyclerviewViewHolder(parent);
        }

        @Override
        public int getAdapterItemCount() {
            return list.size();
        }

        @Override
        public long generateHeaderId(int position) {
            return 0;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (VIEW_TYPES.HEADER == getItemViewType(position)) {
                onBindHeaderViewHolder(holder, position);
            } else if (VIEW_TYPES.NORMAL == getItemViewType(position)) {
                final Order o = list.get(hasHeaderView() ? position - 1 : position);


                final HolderView h = (HolderView)holder;

                h.rang.setVisibility(View.GONE);
                h.qiang.setVisibility(View.GONE);
                switch (mStatus) {
                    case 1:
                        h.viewB1.setVisibility(View.VISIBLE);
                        //抢单事件  type1
                        h.qiang.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                btnEvent(position, 1);
                            }
                        });
                        break;
                    case 2:
                        //如果为2  则为抢到的订单  所以无法让单
                        if(o.getIs_rob()==2){
                            h.rang.setVisibility(View.GONE);
                        }else{
                            h.rang.setVisibility(View.VISIBLE);
                        }
                        //如果为非标店 则隐藏
                        if(AppContext.SHOP_TYPE != 1){
                            h.rang.setVisibility(View.GONE);
                        }
                        h.rang.setVisibility(View.GONE);
                        h.viewB2.setVisibility(View.VISIBLE);
                        //让单事件 type2
                        h.rang.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                btnEvent(position, 2);
                            }
                        });
                        //详情事件
                        h.details1.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                showDetails(position);
                            }
                        });
                        //发货事件 type3
                        h.fahuo.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                btnEvent(position, 3);
                            }
                        });
                        break;
                    case 3:
                        h.viewB3.setVisibility(View.VISIBLE);
                        //详情事件
                        h.details2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                showDetails(position);
                            }
                        });
                        //确认收货事件 type4
                        h.sendOk.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                btnEvent(position, 4);
                            }
                        });

                        break;
                    case 4:
                        h.viewB4.setVisibility(View.VISIBLE);

                        //详情事件
                        h.details4.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                showDetails(position);
                            }
                        });
                        break;

                    case 6:
                        h.viewB6.setVisibility(View.VISIBLE);

                        //详情事件
                        h.details6.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                showDetails(position);
                            }
                        });
                        break;

                }

                String sName = o.getSend_name();
                h.sendName.setText(o.getPay_name()+"/"+sName);
                if(sName.equals("定时送")){
                    h.sendTime.setText(o.getSend_time());
                }
                h.orderNum.setText(o.getOrder_no());
                h.orderTime.setText(o.getCreate_time());
                h.priceAndNum.setText("共有"+o.getOrderItems().size()+"种商品,总价￥"+ PriceUtil.floatToString(o.getPrice()));

                if(!TextUtils.isEmpty(o.getAddress())){
                    h.area.setText(o.getFull_address());
                }

                h.street.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(o.getName())){
                    h.namePhone.setText(o.getName()+" "+o.getPhone());
                }else if(!TextUtils.isEmpty(o.getRec_name())){
                    h.namePhone.setText(o.getRec_name()+" "+o.getRec_phone());
                }else{
                    h.namePhone.setVisibility(View.GONE);
                }

                h.cb.setTag("check"+position);
                //		cb.setChecked(mIsSelected.get(position)==null?false:mIsSelected.get(position));
                h.cb.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        mIsSelected.put(position,  cb.isChecked());
                    }
                });

                h.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            h.viewProduct.setVisibility(View.VISIBLE);
                        }else{
                            h.viewProduct.setVisibility(View.GONE);
                        }
                    }
                });
                h.cb.setChecked(false);
                h.viewProduct.setVisibility(View.GONE);
                h.viewProduct.removeAllViews();
                for (Order.orderItems item : o.getOrderItems()) {
                    View view = mInfater.inflate(R.layout.item_order_goods, null);

                    TextView name = (TextView)view.findViewById(R.id.name);
                    TextView price = (TextView)view.findViewById(R.id.price);
                    ImageView img = (ImageView)view.findViewById( R.id.img);
                    TextView numPrice = (TextView)view.findViewById(R.id.price_num);

                    AppContext.displayImage(img, item.getCover_img());
                    name.setText(item.getName());
                    int p = item.getProduct_price();
                    int n = Integer.valueOf(item.getProduct_number());
                    numPrice.setText("￥"+ PriceUtil.floatToString(p)+"x"+n);
                    price.setText("￥"+PriceUtil.floatToString(p*n));
                    h.viewProduct.addView(view);
                }


//                if (getOnItemClickListener() != null) {
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            getOnItemClickListener().onItemClickListener(v,position);
//                        }
//                    });
//                }

            }else if(VIEW_TYPES.FOOTER == getItemViewType(position)){//结尾同理头部
                onBindHeaderViewHolder(holder,position);
            }
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
//            setHeaderView(holder,position);
        }

//        public abstract void setHeaderView(RecyclerView.ViewHolder holder, int position);



        public class HolderView extends UltimateRecyclerviewViewHolder {
            TextView orderNum,orderTime,priceAndNum;
            LinearLayout viewB1,viewB2,viewB3,viewB4,viewB6,viewProduct;
            TextView area,street,namePhone,sendName,sendTime;
            CheckBox cb;
            Button qiang,rang,details1,fahuo,details2,details4,details6,sendOk;
            public HolderView(View itemView, boolean isItem) {
                super(itemView);
                if(isItem){

                    viewB1 = (LinearLayout)itemView.findViewById(R.id.view_b1);
                    viewB2 = (LinearLayout)itemView.findViewById(R.id.view_b2);
                    viewB3 = (LinearLayout)itemView.findViewById(R.id.view_b3);
                    viewB4 = (LinearLayout)itemView.findViewById(R.id.view_b4);
                    viewB6 = (LinearLayout)itemView.findViewById(R.id.view_b6);


                    orderNum = (TextView)itemView.findViewById(R.id.tv_num);
                    orderTime = (TextView)itemView.findViewById(R.id.tv_date);
                    priceAndNum = (TextView)itemView.findViewById(R.id.tv_num_price);
                    cb = (CheckBox)itemView.findViewById(R.id.rb_product_detail);
                    viewProduct = (LinearLayout)itemView.findViewById(R.id.view1);
                    area = (TextView)itemView.findViewById(R.id.tv_area);
                    street = (TextView)itemView.findViewById(R.id.tv_street);
                    namePhone = (TextView)itemView.findViewById(R.id.tv_name_phone);
                    sendName = (TextView)itemView.findViewById(R.id.tv_send_name);
                    sendTime = (TextView)itemView.findViewById(R.id.tv_send_time);
                    //抢单按钮
                    qiang = (Button)itemView.findViewById(R.id.btn_qiang);

                    //让单按钮
                    rang = (Button)itemView.findViewById(R.id.btn_rang_s);
                    //详情按钮1 小的
                    details1 = (Button)itemView.findViewById(R.id.btn_details);
                    //发货按钮
                    fahuo = (Button)itemView.findViewById(R.id.btn_fa);

                    //详情按钮2 大的
                    details2 = (Button)itemView.findViewById(R.id.btn_details2);
                    //详情按钮2 大的
                    details4 = (Button)itemView.findViewById(R.id.btn_details4);
                    //详情按钮2 大的
                    details6 = (Button)itemView.findViewById(R.id.btn_details6);
                    //确认送达
                    sendOk = (Button)itemView.findViewById(R.id.btn_ok);
                }

            }

            @Override
            public void onItemSelected() {
                itemView.setBackgroundColor(Color.LTGRAY);
            }

            @Override
            public void onItemClear() {
                itemView.setBackgroundColor(0);
            }
        }

//        public interface OnItemClickListener {
//            void onItemClickListener(View view, int position);
//        }
//
//        public OnItemClickListener l;
//
//        public OnItemClickListener getOnItemClickListener() {
//            return l;
//        }
//
//        public void setOnItemClickListener(OnItemClickListener l) {
//            this.l = l;
//        }
    }
    private int mPageNo = 1;
    private int mDefaultPage = 1;
    private AppHttpClient mHttpClient;

    //    1可抢单 2代发货 3待收货（配送中） 4交易完成 5拒收 6 让单 7流单
    public void loadData() {
        JSONObject jb = new JSONObject();

        try {
            jb.put("orderStatus", mStatus);
            jb.put("pg", mPageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mHttpClient == null){
            mHttpClient = new AppHttpClient(ORDER_LIST);
        }
        mHttpClient.postData(ORDER_LIST, new AppAjaxParam(jb), new AppAjaxCallback.onRecevieDataListener<Order>() {

            @Override
            public void onReceiverData(List<Order> data, String msg) {
                if(data.size() == 0){
                    mListView.disableLoadmore();
                }else{
                    mListView.enableLoadmore();
                }

                if(mAdapter == null){
                    mList.addAll(data);
                    mAdapter = new MyAdapter(mList,getActivity(),mStatus);

                    mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    mListView.setHasFixedSize(true);
                    mListView.setSaveEnabled(true);
                    mListView.setClipToPadding(false);
                    mListView.setAdapter(mAdapter);
                    mListView.enableLoadmore();
                    mAdapter.setCustomLoadMoreView(LayoutInflater.from(getActivity()).inflate(R.layout.view_load_more, null));
                    // mGridAdapter.setCustomHeaderView(new UltimateRecyclerView.CustomRelativeWrapper(this));
//                    mRecycleView.setParallaxHeader(getActivity().getLayoutInflater().inflate(R.layout.view_load_header, mRecycleView, false));
                    mListView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
                        @Override
                        public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                            mPageNo++;
                            loadData();
                        }
                    });
                    mListView.setEmptyView(R.layout.view_empty_view);

                    mListView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mPageNo = 1;
                            loadData();
                        }
                    });
                }else{
                    if(mPageNo == mDefaultPage){
                        mList.clear();
                    }

                    mList.addAll(data);
                    mAdapter.notifyDataSetChanged();

                    mListView.setRefreshing(false);
                }


            }

            @Override
            public void onReceiverError(String msg) {

            }

            @Override
            public Class<Order> dataTypeClass() {
                return Order.class;
            }
        });
    }

    /**
     *
     * @param position
     * @param type
     *
     * 抢单事件1  让单2  发货3 确认收货4
     */
    private void btnEvent(final int position,final int type){
        String fname = null;
        int s = 0;
        switch (type) {
            case 1:
                //抢单
                fname = ORDER_GRAB;
                break;

            case 2:
                //让单
                fname = ORDER_RANG;

                break;
            case 3:
                //发货 2
                //开启定位
//                LocalUtil.startLocalService(getActivity(), 60, LocalService.class, LocalService.ACTION);

                s =2;
                fname = ORDER_UPDATE_STATUS;
                break;
            case 4:
                //确认收货 3
                startActivity(new Intent(getActivity(),OrderShouActivity.class).putExtra(INTENT_ID, mList.get(position).getOrder_id()));
                return;
        }

        if(TextUtils.isEmpty(fname)){
            showToast("此功能暂无开放");
            return;
        }
        JSONObject jb = new JSONObject();
        try {
            jb.put("orderId", mList.get(position).getOrder_id());
            if(s !=0){
                jb.put("orderStatus", s);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalFname = fname;
        NetUtil.loadData(fname, new AppAjaxParam(jb), new PostDataListener() {

            @Override
            public void getDate(String data, String msg) {
                if(finalFname.equals(ORDER_UPDATE_STATUS)){
                    startActivity(new Intent(getActivity(),PrintTicketActivity.class).
                            putExtra(INTENT_ID,JsonUtils.beanToString(mList.get(position),Order.class)));
                }

                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                showToast(msg);
            }

            @Override
            public void error(String msg) {
                showToast(msg);
            }
        });
    }


    /**
     * 显示详情
     * @param position
     * 我实在是不愿意写那个parcle什么的
     *
     */
    private void showDetails(int position){
        startActivity(new Intent(getActivity(), OrderDetailsActivity.class).putExtra(INTENT_ID,
                JsonUtils.beanToString(mList.get(position), Order.class)).putExtra("type", mStatus));
    }


}