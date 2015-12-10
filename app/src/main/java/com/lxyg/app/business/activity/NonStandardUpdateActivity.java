package com.lxyg.app.business.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.adapter.ImageUpdateAdapter;
import com.lxyg.app.business.bean.NonStandard;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.ImageTools;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.view.NoScrollGridView;


public class NonStandardUpdateActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private NoScrollGridView mGridView;
    private TextView mTvTitleRight;
    private EditText mEtName,mEtPrice,mEtMarketPrice,mEtCash,mEtDec,mEtUtit,mEtTitle;
    private Button mBtnDelete;

    private ImageUpdateAdapter mAdapter;
    private ImageTools mImageTools;
    private List<String> mListImg = new ArrayList<String>();
    private int mType = 0;//选择照片的类型
    private AppHttpClient mHttpClient;

    private String mName,mDec,mUnit,mSortName;
    private int mPrice,mMarketPrice,mCash;

    private String mId;
    private NonStandard mNonStandard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonstandar_update);
        setBack();
        setTitleText("商品添加");
        mTvTitleRight = (TextView)findViewById(R.id.right_text);
        mTvTitleRight.setText("发布");
        mTvTitleRight.setTextColor(getResources().getColor(R.color.main_red));
        mTvTitleRight.setOnClickListener(this);

        mId = getIntent().getStringExtra(INTENT_ID);

        mBtnDelete = (Button)findViewById(R.id.delete);

        mEtCash = (EditText)findViewById(R.id.e_cash);
        mEtPrice = (EditText)findViewById(R.id.price);
        mEtMarketPrice = (EditText)findViewById(R.id.market_price);
        mEtName = (EditText)findViewById(R.id.name);
        mEtDec = (EditText)findViewById(R.id.dec);
        mEtUtit = (EditText)findViewById(R.id.unit);
        mEtTitle = (EditText)findViewById(R.id.sort_title);


        mGridView = (NoScrollGridView)findViewById(R.id.gridview);

        mImageTools = new ImageTools(this);

        initData();
    }
    private void initData(){
        mHttpClient = new AppHttpClient(NONSTANDAR_PRODUCT_DETAILS);
        JSONObject jb = new JSONObject();
        try {
            jb.put("productId", mId);
        }catch (JSONException e){

        }
        mHttpClient.postData1(NONSTANDAR_PRODUCT_DETAILS, new AppAjaxParam(jb), new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mNonStandard = JsonUtils.parse(data,NonStandard.class);

                for(NonStandard.productImgs p: mNonStandard.getProductImgs()){
                    mListImg.add(p.getImg_url());
                }

                mListImg.add(null);

                mAdapter = new ImageUpdateAdapter(getApplicationContext(),mListImg);
                mGridView.setAdapter(mAdapter);
                mGridView.setOnItemClickListener(NonStandardUpdateActivity.this);

                mEtName.setText(mNonStandard.getName());
                mEtDec.setText(mNonStandard.getDescripation());
                mEtUtit.setText(mNonStandard.getP_unit_name());
                mEtTitle.setText(mNonStandard.getTitle());

                mEtPrice.setText(mNonStandard.getPrice()/100+"");
                mEtMarketPrice.setText(mNonStandard.getMarket_price() / 100 + "");
                mEtCash.setText(mNonStandard.getCash_pay() / 100+"");

                mBtnDelete.setOnClickListener(NonStandardUpdateActivity.this);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.delete:
                showNormalDialog("提示", "确定放弃本次购买？", "确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                });


                break;
            case R.id.right_text:
                mName = mEtName.getText().toString();
                mDec = mEtDec.getText().toString();
                mUnit = mEtUtit.getText().toString();
                mSortName = mEtTitle.getText().toString();

                mPrice = Integer.valueOf(mEtPrice.getText().toString())*100;
                mMarketPrice = Integer.valueOf(mEtMarketPrice.getText().toString())*100;
                mCash = Integer.valueOf(mEtCash.getText().toString())*100;

                if(TextUtils.isEmpty(mName)){
                    showToast("请输入产品名称");
                    return;
                }

                if(TextUtils.isEmpty(mDec)){
                    showToast("请输入产品详情描述");
                    return;
                }

                if(TextUtils.isEmpty(mUnit)){
                    showToast("请输入产品计量单位");
                    return;
                }

                if(TextUtils.isEmpty(mSortName)){
                    showToast("请输入产品简介");
                    return;
                }
                sub();
                break;
        }
    }

    private void delete(){
        showProgressDialog("正在删除");
        mHttpClient = new AppHttpClient(NONSTANDAR_PRODUCT_DELETE);
        JSONObject jb = new JSONObject();
        try{
            jb.put("productId",mId);

        }catch (JSONException e){

        }

        mHttpClient.postData1(NONSTANDAR_PRODUCT_DELETE, new AppAjaxParam(jb), new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                showToast(msg);
                cancelProgressDialog();
                finish();

            }

            @Override
            public void onError(String error) {
                cancelProgressDialog();
                showToast(error);
            }
        });
    }

    private void sub(){
        mHttpClient = new AppHttpClient(NONSTANDAR_PRODUCT_UPDATE);
        JSONObject jb = new JSONObject();
        try{
            jb.put("productId",mId);
            jb.put("price",mPrice);
            jb.put("marketPrice",mMarketPrice);
            jb.put("cash_pay",mCash);
            jb.put("name",mName);
            jb.put("title",mSortName);
            jb.put("cover_img",mListImg.get(0));
            jb.put("p_unit_name",mUnit);
            jb.put("descripation",mDec);
            JSONArray ja = new JSONArray();

            ja.put(mNonStandard.getCover_img());
            for(int i=0;i<mListImg.size()-1;i++){
                ja.put(mListImg.get(i));
            }

            jb.put("imgs",ja.toString());

        }catch (JSONException e){

        }

        mHttpClient.postData1(NONSTANDAR_PRODUCT_UPDATE, new AppAjaxParam(jb), new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                showToast(msg);
                finish();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //表示点击上传照片的按钮
        if(position == mListImg.size()-1){
            mImageTools.showGetImageDialog("选择照片的方式!");
        }else{//这里删除动作
            mListImg.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case ImageTools.CAMARA:
                    mType = ImageTools.CAMARA;
                    mImageTools.getBitmapFromCamara(new ImageTools.OnBitmapCreateListener() {

                        @Override
                        public void onBitmapCreate(Bitmap bitmap, String path) {
                            mImageTools.startZoomPhotoByCamera(Uri.fromFile(new File(path)), 1000, 1000);
                        }
                    });
                    break;

                case ImageTools.GALLERY:
                    mType = ImageTools.GALLERY;
                    mImageTools.startZoomPhoto(data.getData(),1000,1000);
                    break;
                case ImageTools.BITMAP:
                    switch (mType){
                        case ImageTools.CAMARA:
                            Bitmap bitmap2 = mImageTools.getBitmapFromZoomPhoto(data);
                            upLoadImg(bitmap2);
                            break;

                        case ImageTools.GALLERY:
                            Bitmap bitmap = mImageTools.getBitmapFromGallery(data);
                            upLoadImg(bitmap);
                            break;
                    }

                    break;
            }
        }
    }

    private void upLoadImg(final Bitmap bitmap){
        showProgressDialog("正在上传照片");
        AppAjaxParam ap = new AppAjaxParam();
        ap.put("ImgData", mImageTools.bitmapToString(bitmap));

        AppHttpClient aClient = new AppHttpClient();
        aClient.uploadImg(ap, new AppAjaxCallback.onResultListener() {

            @Override
            public void onResult(String data, String msg) {
                mListImg.add(mListImg.size()-1, data);

                mAdapter.notifyDataSetChanged();
                showToast(msg);
                cancelProgressDialog();
            }

            @Override
            public void onError(String error) {
                cancelProgressDialog();
                showToast(error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }
}

