package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.business.R;

/**
 * 抢单
 * @author 王沛栋
 *
 */
public class OrderGrabActivity extends BaseActivity{
	private TextView orderNum,orderTime,priceAndNum,area,street,namePhone,sendName,sendTime;
	private LinearLayout viewProduct;
	private Button qiang;
	private CheckBox cb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_grab);
		
		setBack();
		setTitleText("立即抢单");
		
		
		orderNum = (TextView)findViewById(R.id.tv_num);
		orderTime = (TextView)findViewById(R.id.tv_date);
		priceAndNum = (TextView)findViewById(R.id.tv_num_price);
		cb = (CheckBox)findViewById(R.id.rb_product_detail);
		viewProduct = (LinearLayout)findViewById(R.id.view1);
		area = (TextView)findViewById(R.id.tv_area);
		street = (TextView)findViewById(R.id.tv_street);
		namePhone = (TextView)findViewById(R.id.tv_name_phone);
		sendName = (TextView)findViewById(R.id.tv_send_name);
		sendTime = (TextView)findViewById(R.id.tv_send_time);
		//抢单按钮
		qiang = (Button)findViewById(R.id.btn_qiang);
		qiang.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_qiang:
			
			break;
		}
	}
}
