package com.lxyg.app.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.NonStandard;
import com.lxyg.app.business.draglistview.DragListAdapter;
import com.lxyg.app.business.draglistview.DragListView;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NonStandardActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private DragListView dragListView;
    private Button mBtn;

    private DragListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonstanderd);

        setBack();
        setTitleText("商品列表");
        setRightTitle("添加产品");

        dragListView = (DragListView) findViewById(R.id.other_drag_list);
        mBtn = (Button)findViewById(R.id.sub);
        mBtn.setOnClickListener(this);

        loadData();
    }

    private boolean isFirstLoad = true;
    @Override
    protected void onResume() {
        super.onResume();
        if(isFirstLoad){
            isFirstLoad = false;
            return;
        }
        mList.clear();
        loadData();
    }

    AppHttpClient mHttpClient = new AppHttpClient(NONSTANDAR_PRODUCT);
    private List<NonStandard> mList = new ArrayList<NonStandard>();
    private void loadData(){
        JSONObject jb = new JSONObject();

        mHttpClient.postData(NONSTANDAR_PRODUCT, new AppAjaxParam(jb), new AppAjaxCallback.onRecevieDataListener<NonStandard>() {
            @Override
            public void onReceiverData(List<NonStandard> data, String msg) {
                mList.addAll(data);

                adapter = new DragListAdapter(getApplicationContext(), mList);
                dragListView.setAdapter(adapter);
                dragListView.setOnItemClickListener(NonStandardActivity.this);

            }

            @Override
            public void onReceiverError(String msg) {
                showToast(msg);
            }

            @Override
            public Class<NonStandard> dataTypeClass() {
                return NonStandard.class;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub:
                sub();
                break;
            case R.id.right_text:
                startActivity(new Intent(NonStandardActivity.this,NonStandardAddActivity.class));
                break;
        }
    }

    private void sub(){
        mHttpClient = new AppHttpClient(NONSTANDAR_PRODUCT_LIST_UPDATE);
        try{
            JSONObject jb = new JSONObject();
            StringBuilder sb = new StringBuilder();
            for(NonStandard n:mList){
                sb.append(n.getFb_product_id());
                sb.append(",");
            }
            jb.put("pids",sb.toString());

            mHttpClient.postData1(NONSTANDAR_PRODUCT_LIST_UPDATE, new AppAjaxParam(jb), new AppAjaxCallback.onResultListener() {
                @Override
                public void onResult(String data, String msg) {
                    showToast(msg);
                }

                @Override
                public void onError(String error) {
                    showToast(error);
                }
            });
        }catch (JSONException e){

        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(NonStandardActivity.this,NonStandardUpdateActivity.class).putExtra(INTENT_ID,mList.get(position).getFb_product_id()));
    }
}

