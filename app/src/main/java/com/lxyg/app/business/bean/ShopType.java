package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.business.iface.AddrBase;

/**
 * Created by haha on 2015/9/14.
 */
public class ShopType implements Parcelable,AddrBase{
    private String title;//private String 烟酒private String ,
    private String name;//private String 烟酒店private String ,
    private String img;//null,
    private String type_id;//1

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
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
