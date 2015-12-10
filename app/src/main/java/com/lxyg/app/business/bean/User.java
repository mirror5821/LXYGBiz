package com.lxyg.app.business.bean;

/**
 * 商户信息类
 * @author 王沛栋
 *
 */
public class User {
	
	private String shopId;
	private String name;
	private String phone;
	private String passWord;
	private String city_name;//private String 郑州市private String ,
	private String area_id;//private String 700105private String ,
	private String link_man;//private String 王沛栋private String ,
	private String province_id;//private String 70private String ,
	private String street;//private String 纬一路26号private String ,
	private String full_address;//private String 河南省郑州市金水区纬一路26号private String ,
	private String lng;//private String 113.675172private String ,
	private String m_id;//0,
	private String city_id;//private String 7001private String ,
	private String province_name;//private String 河南省private String ,
	private String area_name;//private String 金水区private String ,
	private String create_time;//private String 2015-07-15private String ,
	private String uuid;//private String 51ddc71f-7ade-429d-8ea7-c1c81a82464eprivate String ,
	private String lat;//private String 34.772509private String ,
	private String temple_id;//0
	private String cover_img;
	private int q_verifi; //-1 未验证 0 验证中 1验证通过 2 未验证通过  3 问题商户 4 黑名单商户
	private int is_norm;
	private String shop_type;

	public int getIs_norm() {
		return is_norm;
	}

	public void setIs_norm(int is_norm) {
		this.is_norm = is_norm;
	}

	public String getShop_type() {
		return shop_type;
	}

	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}

	public int getQ_verifi() {
		return q_verifi;
	}
	public void setQ_verifi(int q_verifi) {
		this.q_verifi = q_verifi;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getCover_img() {
		return cover_img;
	}
	public void setCover_img(String cover_img) {
		this.cover_img = cover_img;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getLink_man() {
		return link_man;
	}
	public void setLink_man(String link_man) {
		this.link_man = link_man;
	}
	public String getProvince_id() {
		return province_id;
	}
	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getFull_address() {
		return full_address;
	}
	public void setFull_address(String full_address) {
		this.full_address = full_address;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getTemple_id() {
		return temple_id;
	}
	public void setTemple_id(String temple_id) {
		this.temple_id = temple_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
}
