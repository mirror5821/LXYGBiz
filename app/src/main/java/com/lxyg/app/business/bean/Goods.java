package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Goods implements Parcelable{
	private String goodsId;//1,
	private String num;//0,
	private String name;//private String 烟private String ,
	private String img;//null
	private String title;//private String kakasadaprivate String ,
	private int price;//200,
	private String cover_img;//private String http://7xk59r.com1.z0.glb.clouddn.com/Frb_rrcOaLHeL-PT3Ddkga7eyQjJprivate String ,
	private String productId;//21
	private int status;//1已上架  2已下架
	private int product_number;//库存

	public int getProduct_number() {
		return product_number;
	}

	public void setProduct_number(int product_number) {
		this.product_number = product_number;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCover_img() {
		return cover_img;
	}
	public void setCover_img(String cover_img) {
		this.cover_img = cover_img;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
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
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}
	
	
}
