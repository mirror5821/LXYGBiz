package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.fragment.Register1Fragment;
import com.lxyg.app.business.fragment.Register2Fragment;
import com.lxyg.app.business.fragment.Register3Fragment;
import com.lxyg.app.business.fragment.Register4Fragment;

/**
 * 注册
 * @author 王沛栋
 * @version 
 * 
 */
public class RegisterActivity extends BaseActivity{
	private String phone;
	private String pass;
	private String code;
	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	private RadioButton [] radios = new RadioButton[4];
	private TextView mTvRight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		setBack();
		setTitleText("注册新用户");
		
		radios[0] = (RadioButton)findViewById(R.id.rb1);
		radios[1] = (RadioButton)findViewById(R.id.rb2);
		radios[2] = (RadioButton)findViewById(R.id.rb3);
		radios[3] = (RadioButton)findViewById(R.id.rb4);
		
		mTvRight = (TextView)findViewById(R.id.right_text);
		
		if(savedInstanceState == null){
			switchFragment(new Register1Fragment());//Register1Fragment
			
		}
	}
	
	public void jump(){
		mTvRight.setText("跳过");
		mTvRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@SuppressWarnings("static-access")
	public void switchFragment(Fragment fragment){
		if(fragment instanceof Register1Fragment){
			selectId(0);
		}else if(fragment instanceof Register2Fragment){
			selectId(1);
		}else if(fragment instanceof Register3Fragment){
			selectId(2);
		}else if(fragment instanceof Register4Fragment){
			selectId(3);
		}
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setTransition(fragmentTransaction.TRANSIT_ENTER_MASK);
		fragmentTransaction.replace(R.id.content, fragment).commit();
	}
	
	public void selectId(int id){
		radios[id].setChecked(true);
	}
}
