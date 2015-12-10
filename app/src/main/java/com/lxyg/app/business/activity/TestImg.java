package com.lxyg.app.business.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;

import java.io.File;

import dev.mirror.library.utils.ImageTools;

/**
 * Created by haha on 2015/9/10.
 */
public class TestImg extends BaseActivity {
    private ImageView mImageView;
    private Button mBtn;
    private ImageTools mImageTools;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_img);
        mBtn = (Button)findViewById(R.id.btn);
        mImageView = (ImageView)findViewById(R.id.img);

        mImageTools = new ImageTools(this);

        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn:
                mImageTools.showGetImageDialog("选择照片的方式!");
                break;
        }
    }

    private int mType = 0;
    private String mPath;
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
                            mImageView.setImageBitmap(bitmap2);
                            upLoadImg(bitmap2);
                            break;

                        case ImageTools.GALLERY:
                            Bitmap bitmap = mImageTools.getBitmapFromGallery(data);
                            mImageView.setImageBitmap(bitmap);
                            upLoadImg(bitmap);
                            break;
                    }

//                    Bitmap bitmap = mImageTools.getBitmapFromZoomPhoto(data);
//                    upLoadImg(bitmap);
//                    mImageView.setImageBitmap(mImageTools.getBitmapFromZoomPhoto(data));
                    break;
            }
        }
    }

    private void upLoadImg(Bitmap bitmap){
        AppAjaxParam ap = new AppAjaxParam();
        ap.put("ImgData", mImageTools.bitmapToString(bitmap));

        AppHttpClient aClient = new AppHttpClient();
        aClient.uploadImg(ap, new AppAjaxCallback.onResultListener() {

            @Override
            public void onResult(String data, String msg) {

                showToast(msg);
            }

            @Override
            public void onError(String error) {
                showToast("error------"+error);
            }
        });
    }

}