package com.lxyg.app.business.bean;

import com.lxyg.app.business.iface.AddrBase;

public class Address implements AddrBase{

	private String addr;
	private String name;
	private double pointX; 
	private double pointY; 



	public String getAddr() {
		return addr;
	}



	public void setAddr(String addr) {
		this.addr = addr;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public double getPointX() {
		return pointX;
	}



	public void setPointX(double pointX) {
		this.pointX = pointX;
	}



	public double getPointY() {
		return pointY;
	}



	public void setPointY(double pointY) {
		this.pointY = pointY;
	}



	@Override
	public String getAddrName() {
		return addr+name;
	}

}
