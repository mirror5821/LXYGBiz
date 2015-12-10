package com.lxyg.app.business.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Shop;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import dev.mirror.library.utils.ImageTools;
import dev.mirror.library.utils.JsonUtils;

/**
 * Created by mirror on 2015/9/23.
 */
public class NonStandardInfoUpdateActivity extends BaseActivity{
    private Shop mShop;
    private String mIntent;

    private EditText mTvTitle,mTvTitle2,mTvTitle3;
    private ImageView mImg;
    private Button mBtn;
    private ImageTools mImageTools;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonstandar_info_update);
        setTitleText("修改店铺封面");
        setBack();

        if(mImageTools == null){
            mImageTools = new ImageTools(this);
        }
        mIntent = getIntent().getExtras().getString(INTENT_ID);
        mShop = JsonUtils.parse(mIntent, Shop.class);

        mTvTitle = (EditText)findViewById(R.id.shop_title);
        mImg = (ImageView)findViewById(R.id.img1);
        mTvTitle2 = (EditText)findViewById(R.id.title2);
        mTvTitle3 = (EditText)findViewById(R.id.title3);
        mBtn = (Button)findViewById(R.id.sub);
        mBtn.setOnClickListener(this);
        mImg.setOnClickListener(this);

        mImgNo = mShop.getCover_img();
        mTvTitle.setText(mShop.getTitle());
        mTvTitle2.setText(mShop.getTitle2());
        mTvTitle3.setText(mShop.getTitle3());
        AppContext.displayImage(mImg, mShop.getCover_img());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub:
                sub();
                break;
            case R.id.img1:
                mImageTools.showGetImageDialog("选择照片的方式!");
                break;
        }
    }

    private AppHttpClient mHttpClient2;
    private void sub(){
        String title = mTvTitle.getText().toString();
        String title2 = mTvTitle2.getText().toString();
        String title3 = mTvTitle3.getText().toString();
        if(TextUtils.isEmpty(title)){
            showToast("请输入店铺标语");
            return;
        }
        if(TextUtils.isEmpty(title2)){
            showToast("请输入店铺标题2");
            return;
        }
        if(TextUtils.isEmpty(title3)){
            showToast("请输入店铺标题3");
            return;
        }
        if(TextUtils.isEmpty(mImgNo)){
            showToast("请设置店铺封面");
            return;
        }
        JSONObject jb = new JSONObject();
        try{

            jb.put("cover_img",mImgNo);
            jb.put("title",title);
            jb.put("title2",title2);
            jb.put("title3", title3);
        }catch (JSONException e){

        }
        if(mHttpClient2 == null){
            mHttpClient2 = new AppHttpClient(UPDATE_SHOP_INFO);
        }
        mHttpClient2.postData1(UPDATE_SHOP_INFO, new AppAjaxParam(jb), new AppAjaxCallback.onResultListener() {
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
        //UPDATE_SHOP_INFO


    }

    private int mType = 0;//选择照片的类型
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {

                case ImageTools.CAMARA:
                    mType = ImageTools.CAMARA;
                    mImageTools.getBitmapFromCamara(new ImageTools.OnBitmapCreateListener() {

                        @Override
                        public void onBitmapCreate(Bitmap bitmap, String path) {
                            mImageTools.startZoomPhotoByCamera4To3(Uri.fromFile(new File(path)), 800, 600);
                        }
                    });
                    break;

                case ImageTools.GALLERY:
                    mType = ImageTools.GALLERY;
                    mImageTools.startZoomPhoto4To3(data.getData(), 800, 600);
                    break;
                case ImageTools.BITMAP:
                    switch (mType){
                        case ImageTools.CAMARA:
                            Bitmap bitmap2 = mImageTools.getBitmapFromZoomPhoto(data);
                            initImages(bitmap2);
                            break;

                        case ImageTools.GALLERY:
                            Bitmap bitmap = mImageTools.getBitmapFromGallery(data);
                            initImages(bitmap);
                            break;
                    }

                    break;
            }
        }
    }

    private void initImages(Bitmap bitmap){
        mImg.setVisibility(View.VISIBLE);
        mImg.setImageBitmap(bitmap);

        upLoadImg(bitmap);
    }
    private String mImgNo;
    private void upLoadImg(Bitmap bitmap){
        AppAjaxParam ap = new AppAjaxParam();
        ap.put("ImgData", mImageTools.bitmapToString(bitmap));

        AppHttpClient aClient = new AppHttpClient();
        aClient.uploadImg(ap, new AppAjaxCallback.onResultListener() {

            @Override
            public void onResult(String data, String msg) {
                mImgNo = data;
                showToast(msg);
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }
}
