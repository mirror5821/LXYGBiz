package dev.mirror.library.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.widget.Toast;

public class ImageTools{

	private Context mContext;
	private String[] mItems = { "相机", "图库" };

	private String mFolderString = "/kkwy/";
	private static String mPath;
	public static final int CAMARA = 300;
	public static final int GALLERY = 301;
	public static final int BITMAP = 302;
	private int defaultHeight = 720;

	private int defaultWidth = 1280;
	private boolean fromFragment;
	private Fragment mFragment;

	@SuppressWarnings("unused")
	private ImageTools(){
	}

	public void setFromFragment(boolean from)
	{
		this.fromFragment = from;
	}

	public void setFragment(Fragment fragment) {
		this.mFragment = fragment;
	}

	public void setFolder(String folder)
	{
		this.mFolderString = folder;
	}

	public void setHeight(int height) {
		this.defaultHeight = height;
	}

	public void setWidth(int width) {
		this.defaultWidth = width;
	}

	public ImageTools(Context context)
	{
		this.mContext = context;
		initFile();
	}

	public ImageTools(Fragment fragment)
	{
		this.mFragment = fragment;
		this.mContext = fragment.getActivity();
		this.fromFragment = true;
		initFile();
	}

	@SuppressLint({"NewApi"})
	public String bitmapToString(Bitmap bitmap)
	{
		if (bitmap != null) {
			String str = null;
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
			byte[] bytes = bStream.toByteArray();
			str = Base64.encodeToString(bytes, 0);
			return str;
		}
		return null;
	}

	public static Bitmap stringToBitmap(String str){
		byte [] b = str.getBytes();
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	public static  byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static  Bitmap Bytes2Bimap(byte[] b){
		if(b.length!=0){
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		else {
			return null;
		}
	}

	private void initFile()
	{
		File file = new File(Environment.getExternalStorageDirectory() +
				this.mFolderString);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file + ".nomedia");
		if (!file.exists())
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				try {
					throw new IOException("无法创建" + file.toString() + "文件");
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}

	public void showGetImageDialog(String title)
	{
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(this.mContext);
		mBuilder.setTitle(title);
		mBuilder.setItems(this.mItems, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which) {
					case 0:
						ImageTools.this.openCamara();
						break;
					case 1:
						ImageTools.this.openGallery();
						break;
				}
			}
		});
		mBuilder.show();
	}

	public void openCamara()
	{
		Intent intent = new Intent();

		if (Environment.getExternalStorageState().equals(
				"mounted"))
		{
			mPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
					this.mFolderString;
//			mPath += System.currentTimeMillis()+".jpg";
			mPath += System.currentTimeMillis();
			intent.setAction("android.media.action.IMAGE_CAPTURE");
			intent.putExtra("output",Uri.fromFile(new File(mPath)));
			if (this.fromFragment)
				this.mFragment.startActivityForResult(intent, CAMARA);
			else
				((Activity)this.mContext).startActivityForResult(intent, CAMARA );
		}
	}

	public void openGallery()
	{
		Intent intent = new Intent("android.intent.action.PICK",
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		if (this.fromFragment)
			this.mFragment.startActivityForResult(intent, GALLERY);
		else
			((Activity)this.mContext).startActivityForResult(intent, GALLERY);
	}

	public void getBitmapFromCamara(final OnBitmapCreateListener onBitmapCreateListener)
	{
		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				onBitmapCreateListener.onBitmapCreate((Bitmap)msg.obj, ImageTools.mPath);
			}
		};
		new Thread() {
			@SuppressWarnings("unused")
			public void run() {

				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				int size = 1;
				int width = 1;
				int height = 1;
				do {
					width /= 2;
					height /= 2;
					size *= 2;
				}while ((width >= ImageTools.this.defaultWidth) && (height >= ImageTools.this.defaultHeight));

				//测试使用4 如有问题请更换
				size = 4;
				options.inSampleSize = size;
				Bitmap mBitmap = BitmapFactory.decodeFile(ImageTools.mPath, options);

				Message message = new Message();
				message.obj = mBitmap;
				message.what = 0;
				mHandler.sendMessage(message);
			}
		}
				.start();
	}


	public Bitmap getBitmapFromGallery(Intent data)
	{
		Uri selectedImage = data.getData();
		String[] filePathColumn = { "_data" };

		Cursor cursor = this.mContext.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();

		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		int size = 1;
		int width = 1;
		int height = 1;
		do {
			width /= 2;
			height /= 2;
			size *= 2;
		}while ((width >= ImageTools.this.defaultWidth) && (height >= ImageTools.this.defaultHeight));

		//测试使用4 如有问题请更换
		size = 4;
		options.inSampleSize = size;
		Bitmap btp =BitmapFactory.decodeFile(filePath, options);

		return btp;
	}



	public void startZoomPhotoByCamera(Uri uri, int outputX, int outputY){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");
		//剪裁比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		//输出大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);

		intent.putExtra("noFaceDetection", true);//不采用脸部识别
		intent.putExtra("return-data", true);//回调方法data.getExtras().getParcelable("data")返回数据为空

		try {
			if (this.fromFragment)
				this.mFragment.startActivityForResult(intent, BITMAP);
			else
				((Activity)this.mContext).startActivityForResult(intent, BITMAP);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
			Toast.makeText(this.mContext, "未找到可以剪裁图片的程序", Toast.LENGTH_SHORT).show();
		}
	}

	public void startZoomPhotoByCamera4To3(Uri uri, int outputX, int outputY){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");
		//剪裁比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		//输出大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);

		intent.putExtra("noFaceDetection", true);//不采用脸部识别
		intent.putExtra("return-data", true);//回调方法data.getExtras().getParcelable("data")返回数据为空

		try {
			if (this.fromFragment)
				this.mFragment.startActivityForResult(intent, BITMAP);
			else
				((Activity)this.mContext).startActivityForResult(intent, BITMAP);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
			Toast.makeText(this.mContext, "未找到可以剪裁图片的程序", Toast.LENGTH_SHORT).show();
		}
	}

	public void startZoomPhoto(Uri uri, int outputX, int outputY){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");
		//剪裁比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		//输出大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);

		intent.putExtra("noFaceDetection", true);//不采用脸部识别
		intent.putExtra("return-data", false);//回调方法data.getExtras().getParcelable("data")返回数据为空

		try {
			if (this.fromFragment)
				this.mFragment.startActivityForResult(intent, BITMAP);
			else
				((Activity)this.mContext).startActivityForResult(intent, BITMAP);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
			Toast.makeText(this.mContext, "未找到可以剪裁图片的程序", Toast.LENGTH_SHORT).show();
		}
	}

	public void startZoomPhoto4To3(Uri uri, int outputX, int outputY){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");
		//剪裁比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		//输出大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);

		intent.putExtra("noFaceDetection", true);//不采用脸部识别
		intent.putExtra("return-data", false);//回调方法data.getExtras().getParcelable("data")返回数据为空

		try {
			if (this.fromFragment)
				this.mFragment.startActivityForResult(intent, BITMAP);
			else
				((Activity)this.mContext).startActivityForResult(intent, BITMAP);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
			Toast.makeText(this.mContext, "未找到可以剪裁图片的程序", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 拍照截图获取的图片内容
	 * @param data
	 * @return
	 */
	public Bitmap getBitmapFromZoomPhoto(Intent data)
	{
		Bundle extras = data.getExtras();
		if (extras != null) {
			return (Bitmap)extras.getParcelable("data");
		}


		return null;
	}

	/**
	 * 获取图库选择截图图片
	 * @param data
	 * @return
	 */
	public Bitmap getBitmapFromGalleryByZoom(Intent data)
	{
		Uri selectedImage = data.getData();
		String[] filePathColumn = { "_data" };

		Cursor cursor = this.mContext.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();

		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		int size = 1;
		int width = 1;
		int height = 1;
		do {
			width /= 2;
			height /= 2;
			size *= 2;
		}while ((width >= ImageTools.this.defaultWidth) && (height >= ImageTools.this.defaultHeight));

		//这里不用压缩  因为之前已经压缩过了
		size = 1;
		options.inSampleSize = size;
		Bitmap btp =BitmapFactory.decodeFile(filePath, options);

		return btp;
	}

	public static abstract interface OnBitmapCreateListener
	{
		public abstract void onBitmapCreate(Bitmap paramBitmap, String paramString);
	}
}