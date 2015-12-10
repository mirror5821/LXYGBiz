package com.lxyg.app.business.fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lxyg.app.business.R;
import com.lxyg.app.business.bean.Customer;
import com.lxyg.app.business.bean.User;
import com.lxyg.app.business.iface.OnSelectTab;
import com.lxyg.app.business.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.SharePreferencesUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户列表类
 * @author 王沛栋
 *
 */
public class CustomerFragment extends BaseListFragment<Customer>{
	

	@Override
	public int setLayoutId() {
		return R.layout.fragment_list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = getActivity().getLayoutInflater().inflate(R.layout.item_customer, null);
		}
		TextView time = (TextView)convertView.findViewById(R.id.time);
		TextView newAdd = (TextView)convertView.findViewById(R.id.new_add);
		TextView huoYue = (TextView)convertView.findViewById(R.id.huoyue);
		TextView leiJi = (TextView)convertView.findViewById(R.id.leiji);
		
		Customer c = mList.get(position);
		time.setText(c.getTime());
		newAdd.setText(c.getNew_add());
		huoYue.setText(c.getHuoyue());
		leiJi.setText(c.getAllU());
		
		return convertView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if(mUser.getQ_verifi()!=1){
			mOnSelectTab.onSelect(4);
		}
		TextView mTvTitle   = (TextView)getView().findViewById(R.id.bar_title);
		mTvTitle.setText("客户");
		
		mListView.getRefreshableView().setDivider(
				new ColorDrawable(getResources().getColor(R.color.driver)));
		mListView.getRefreshableView().setDividerHeight(1);
		mListView.getRefreshableView().setDrawSelectorOnTop(true);
		mListView.setMode(Mode.DISABLED);
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.view_customer_header, null);
		mListView.getRefreshableView().addHeaderView(v);
	}

	AppHttpClient mAppHttpClient = new AppHttpClient(USER_MANAGE);
	@Override
	public void loadData() {
		JSONObject jb = new JSONObject();
		
		mAppHttpClient.postData(USER_MANAGE, new AppAjaxParam(jb), new onRecevieDataListener<Customer>() {

			@Override
			public void onReceiverData(List<Customer> data, String msg) {
				if(pageNo == mDefaultPage){
					mList.clear();
				}
				
				mList.addAll(data);
				setListAdapter();
			}

			@Override
			public void onReceiverError(String msg) {
				
			}

			@Override
			public Class<Customer> dataTypeClass() {
				return Customer.class;
			}
		});



	}

	private User mUser;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList = new ArrayList<Customer>();
		
		mUser = SharePreferencesUtil.getShopInfo(getActivity());
		
	}
	
	private OnSelectTab mOnSelectTab;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mOnSelectTab = (OnSelectTab)activity;
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View view,
			int position, long num) {

	}


}
