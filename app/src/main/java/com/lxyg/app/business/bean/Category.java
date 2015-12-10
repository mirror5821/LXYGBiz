package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.business.iface.AddrBase;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/4.
 */
public class Category implements Parcelable,AddrBase {
    private List<Brand> brands;//[],
    private String name;//private String 海鲜干制品private String ,
    private String img;//private String http://lxyg8.b0.upaiyun.com/platform/c_hxgzp.pngprivate String ,
    private String typeId;//1

    public static class Brand implements AddrBase{
        private String p_type_id;//1,
        private String p_type_name;//private String 海鲜干制品private String ,
        private String name;//private String 干贝private String ,
        private String img;//null,
        private String brandId;//1


        @Override
        public String getAddrName() {
            return name;
        }

        public String getP_type_id() {
            return p_type_id;
        }

        public void setP_type_id(String p_type_id) {
            this.p_type_id = p_type_id;
        }

        public String getP_type_name() {
            return p_type_name;
        }

        public void setP_type_name(String p_type_name) {
            this.p_type_name = p_type_name;
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

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }
    }
    
    @Override
    public String getAddrName() {
        return name;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
