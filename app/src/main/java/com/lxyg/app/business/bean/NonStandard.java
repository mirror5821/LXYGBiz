package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by haha on 2015/9/12.
 */
public class NonStandard implements Parcelable {
    private String p_unit_name;//private String 千克private String ,
    private String fb_product_id;//23,
    private String payment;//null,

    private List<productImgs> productImgs;

    private String index_show;//1,
    private String cover_img;//private String http://7xk59r.com1.z0.glb.clouddn.com/485c875fc6244510private String ,
    private String s_uid;//private String 01a029f54e1548f2private String ,
    private String order_no;//1,
    private String descripation;//private String 测试飞镖private String ,
    private String title;//private String 测试飞镖private String ,
    private int cash_pay;//0,
    private int price;//1000,
    private int market_price;//1000,
    private String hide;//0,
    private String name;//private String 测试飞镖private String ,
    private String create_time;//private String 2015-09-12 11:32:55private String ,
    private String modify_time;//null

    public static class productImgs{
        private String img_url;//"http://7xk59r.com1.z0.glb.clouddn.com/485c875fc6244510",
        private String product_id;//":23,
        private String proimgsId;//":20

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProimgsId() {
            return proimgsId;
        }

        public void setProimgsId(String proimgsId) {
            this.proimgsId = proimgsId;
        }
    }

    public String getP_unit_name() {
        return p_unit_name;
    }

    public void setP_unit_name(String p_unit_name) {
        this.p_unit_name = p_unit_name;
    }

    public String getFb_product_id() {
        return fb_product_id;
    }

    public void setFb_product_id(String fb_product_id) {
        this.fb_product_id = fb_product_id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<productImgs> getProductImgs() {
        return productImgs;
    }

    public void setProductImgs(List<productImgs>  productImgs) {
        this.productImgs = productImgs;
    }

    public String getIndex_show() {
        return index_show;
    }

    public void setIndex_show(String index_show) {
        this.index_show = index_show;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getS_uid() {
        return s_uid;
    }

    public void setS_uid(String s_uid) {
        this.s_uid = s_uid;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getDescripation() {
        return descripation;
    }

    public void setDescripation(String descripation) {
        this.descripation = descripation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCash_pay() {
        return cash_pay;
    }

    public void setCash_pay(int cash_pay) {
        this.cash_pay = cash_pay;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMarket_price() {
        return market_price;
    }

    public void setMarket_price(int market_price) {
        this.market_price = market_price;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
