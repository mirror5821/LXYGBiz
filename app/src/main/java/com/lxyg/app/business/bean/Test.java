package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Test implements Parcelable{
	public Test(){

	}
	private String id;
	private String store_id;
	private String images;
	private String start_time;
	private String end_time;
	private String www;
	private String content;
	private String addtime;
	private String name;
	private String cityid;
	private String biaozhi;
	private String paixu;


	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getBiaozhi() {
		return biaozhi;
	}

	public void setBiaozhi(String biaozhi) {
		this.biaozhi = biaozhi;
	}

	public String getPaixu() {
		return paixu;
	}

	public void setPaixu(String paixu) {
		this.paixu = paixu;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public Test(Parcel parcel){
		id = parcel.readString();
		store_id = parcel.readString();
		images = parcel.readString();
		start_time = parcel.readString();
		end_time = parcel.readString();
		www = parcel.readString();
		content = parcel.readString();
		addtime = parcel.readString();
		name = parcel.readString();
		cityid = parcel.readString();
		biaozhi = parcel.readString();
		paixu = parcel.readString();
		
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(store_id);
		dest.writeString(images);
		dest.writeString(start_time);
		dest.writeString(end_time);
		dest.writeString(www);
		dest.writeString(content);
		dest.writeString(addtime);
		dest.writeString(name);
		dest.writeString(cityid);
		dest.writeString(biaozhi);
		dest.writeString(paixu);

	}

	public static final Creator<Test> CREATOR = new Creator<Test>() {

		@Override
		public Test[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Test[size];
		}

		@Override
		public Test createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Test(source);
		}
	};

}
