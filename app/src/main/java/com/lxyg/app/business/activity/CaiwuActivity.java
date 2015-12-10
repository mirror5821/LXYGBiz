package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.Caiwu;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;

import dev.mirror.library.utils.JsonUtils;

/**
 * Created by haha on 2015/9/2.
 */
public class CaiwuActivity extends BaseListActivity<Caiwu> {


    private LayoutInflater mInflater;

    @Override
    public int setLayoutId() {
        return R.layout.activity_order_list;
    }

    private TextView mTvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mList = new ArrayList<Caiwu>();
        mInflater = getLayoutInflater();


    }

    /**
     *
     * 1可抢单 2代发货 3待收货（配送中） 4交易完成 5拒收 6 让单 7流单
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView =mInflater.inflate(R.layout.item_caiwu, null);
        }
        final TextView name = (TextView)convertView.findViewById(R.id.name);
        final TextView time = (TextView)convertView.findViewById(R.id.time);
        final TextView price = (TextView)convertView.findViewById(R.id.price);

        Caiwu c = mList.get(position);

        //@"订单收入",@"订单佣金收入",@"订单佣金支出",@"账号提现"];
        if(c.getBalance_type().equals("1")){
            name.setText("订单收入");
            price.setTextColor(getResources().getColor(R.color.cawu_in));
//            price.setText("+ " + Float.valueOf(Integer.valueOf(c.getIn_come()) / 100));
            price.setText("+ " + Double.valueOf(c.getIn_come()) / 100);
        }
        if(c.getBalance_type().equals("2")){
            name.setText("订单佣金收入");
            price.setTextColor(getResources().getColor(R.color.cawu_in));
//            price.setText(PriceUtil.floatToString(Integer.valueOf(c.getIn_come())));
            price.setText("+ " + Double.valueOf(c.getIn_come()) / 100);
        }
        if(c.getBalance_type().equals("3")){
            name.setText("订单佣金支出");
            price.setTextColor(getResources().getColor(R.color.cawu_out));
//            price.setText("+ "+Float.valueOf(Integer.valueOf(c.getExpend())/100));
            price.setText(""+Double.valueOf(c.getExpend()) / 100);

        }
        if(c.getBalance_type().equals("4")){
            name.setText("账号提现");
            price.setTextColor(getResources().getColor(R.color.cawu_out));
//            price.setText("+ " + Float.valueOf(Integer.valueOf(c.getExpend()) / 100));
            price.setText(""+Double.valueOf(c.getExpend()) / 100);
        }
        if(c.getBalance_type().equals("5")){
            name.setText("货到付款订单");
            price.setTextColor(getResources().getColor(R.color.cawu_none));
//            price.setText("+ " + Float.valueOf(Integer.valueOf(c.getIn_come()) / 100));
            price.setText(""+Double.valueOf(c.getIn_come()) / 100);
        }
        time.setText(c.getCreate_time());

        return convertView;
    }

    private AppHttpClient mAppHttpClient;
    @Override
    public void loadData() {
        setBack();
        setTitleText("财务明细");
        mAppHttpClient = new AppHttpClient(ACCONT_DETAIL);
        mAppHttpClient.postData1(ACCONT_DETAIL, new AppAjaxParam(new JSONObject()), new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mList.addAll(JsonUtils.parseList(data,Caiwu.class));
                mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                setListAdapter();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View view,
                            int position, long num) {

    }


}
