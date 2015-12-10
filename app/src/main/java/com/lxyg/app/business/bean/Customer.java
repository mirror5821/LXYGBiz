package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable{
	private String new_add;//;//0,
	private String time;//2015-08-03private String ,
	private String allU;//1,
	private String huoyue;//0
	
	
	public String getNew_add() {
		return new_add;
	}
	public void setNew_add(String new_add) {
		this.new_add = new_add;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAllU() {
		return allU;
	}
	public void setAllU(String allU) {
		this.allU = allU;
	}
	public String getHuoyue() {
		return huoyue;
	}
	public void setHuoyue(String huoyue) {
		this.huoyue = huoyue;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
