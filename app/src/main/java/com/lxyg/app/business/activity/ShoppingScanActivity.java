package com.lxyg.app.business.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.activity.CaptureActivity;
import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王沛栋 on 2015/12/4.
 */
public class ShoppingScanActivity extends BaseActivity{
    private List<Product> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightTitle("扫描");

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.right_text:
                startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {

                case 2001:
                    Uri uriData = data.getData();
                    //获取扫描结果
//                    initSearchView(uriData.toString(),1);
                    break;

            }
        }
    }
}
