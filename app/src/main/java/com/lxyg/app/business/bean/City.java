package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.business.iface.AddrBase;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/4.
 */
public class City implements Parcelable,AddrBase{

    private String province;//":"河南省",
    private int code;//":"70",
    private List<Citys> city;

    @Override
    public String getAddrName() {
        return province;
    }

    public static class Citys implements AddrBase{
        private String name;
        private int code;
        private List<Areas> area;

        @Override
        public String getAddrName() {
            return name;
        }


        public static class Areas implements AddrBase{
            private String name;
            private int code;

            @Override
            public String getAddrName() {
                return name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }


        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<Areas> getArea() {
            return area;
        }

        public void setArea(List<Areas> area) {
            this.area = area;
        }
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Citys> getCity() {
        return city;
    }

    public void setCity(List<Citys> city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
