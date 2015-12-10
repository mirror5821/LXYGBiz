package com.lxyg.app.business.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;

import com.lxyg.app.business.R;
import com.lxyg.app.business.fragment.Forget1Fragment;
import com.lxyg.app.business.fragment.Forget2Fragment;

/**
 * 注册
 * @author 王沛栋
 * @version 
 * 
 */
public class ForgetPassActivity extends BaseActivity{
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pass);

		setBack();
		setTitleText("忘记密码");
		
		radios[0] = (RadioButton)findViewById(R.id.rb1);
		radios[1] = (RadioButton)findViewById(R.id.rb2);
		
		if(savedInstanceState == null){
			switchFragment(new Forget1Fragment());//Register1Fragment
			
		}
	}
	
	@SuppressWarnings("static-access")
	public void switchFragment(Fragment fragment){
		if(fragment instanceof Forget1Fragment){
			selectId(0);
		}else if(fragment instanceof Forget2Fragment){
			selectId(1);
		}
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setTransition(fragmentTransaction.TRANSIT_ENTER_MASK);
		fragmentTransaction.replace(R.id.content, fragment).commit();
	}
	
	public void selectId(int id){
		radios[id].setChecked(true);
	}
}
