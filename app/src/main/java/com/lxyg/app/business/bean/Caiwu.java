package com.lxyg.app.business.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Caiwu implements Parcelable{
	private String int_let_order;//0,
	private String in_come;//0,
	private String in_order;//4800,
	private String create_time;//private String 2015-08-27 15:15:09private String ,
	private String s_uid;//private String 01a029f54e1548f2private String ,
	private String in_rob_order;//0,
	private String expend;//0,
	private String balance_type;//1,
	private String id;//4,
	private String order_id;//private String private String ,
	private String out_balance;//0

	public String getInt_let_order() {
		return int_let_order;
	}

	public void setInt_let_order(String int_let_order) {
		this.int_let_order = int_let_order;
	}

	public String getIn_come() {
		return in_come;
	}

	public void setIn_come(String in_come) {
		this.in_come = in_come;
	}

	public String getIn_order() {
		return in_order;
	}

	public void setIn_order(String in_order) {
		this.in_order = in_order;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getS_uid() {
		return s_uid;
	}

	public void setS_uid(String s_uid) {
		this.s_uid = s_uid;
	}

	public String getIn_rob_order() {
		return in_rob_order;
	}

	public void setIn_rob_order(String in_rob_order) {
		this.in_rob_order = in_rob_order;
	}

	public String getExpend() {
		return expend;
	}

	public void setExpend(String expend) {
		this.expend = expend;
	}

	public String getBalance_type() {
		return balance_type;
	}

	public void setBalance_type(String balance_type) {
		this.balance_type = balance_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOut_balance() {
		return out_balance;
	}

	public void setOut_balance(String out_balance) {
		this.out_balance = out_balance;
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
