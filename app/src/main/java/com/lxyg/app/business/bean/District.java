package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.business.iface.AddrBase;

/**
 * Created by 王沛栋 on 2015/11/4.
 */
public class District implements Parcelable ,AddrBase{
    public District(){

    }
    private String districtId;//":3,
    private String name;//":"名门世家

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String getAddrName() {
        return name;
    }
}
