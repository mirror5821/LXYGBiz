package com.lxyg.app.business.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lxyg.app.business.R;
import com.lxyg.app.business.adapter.OrderPagerAdapter;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.utils.AppAjaxCallback;
import com.lxyg.app.business.utils.AppAjaxParam;
import com.lxyg.app.business.utils.AppHttpClient;
import com.lxyg.app.business.utils.DensityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.DpUtil;

/**
 * Created by haha on 2015/9/25.
 */
public class OrderNewFragment  extends  BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_new,null);
    }

    private ViewPager mViewPager;
    private HorizontalScrollView mHView;
    private RadioGroup mRG;

    private OrderPagerAdapter mAdapter;
    //    1可抢单 2代发货 3待收货（配送中） 4交易完成 5拒收 6 让单 7流单
    private String mOrderStrs [] = {"待发货","待收货","交易完成","拒收"};
    private int [] mOrderStatus = {ORDER_STATUS2,ORDER_STATUS3,ORDER_STATUS4,ORDER_STATUS5};
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleText("订单管理");
        mHView = (HorizontalScrollView)view.findViewById(R.id.s_view);
        mRG = (RadioGroup)view.findViewById(R.id.view_select);
        mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){

        int mOrderLength = mOrderStatus.length;
        final RadioButton []  rbs = new RadioButton[mOrderLength];
        for (int i=0;i<mOrderLength;i++){
            View v = getActivity().getLayoutInflater().inflate(R.layout.view_tab_rb, null);
            RadioButton tempButton = (RadioButton)v.findViewById(R.id.rb1);
            tempButton.setText(mOrderStrs[i]);
            tempButton.setId(i+30000);
            rbs[i] = tempButton;

            mRG.addView(tempButton, DpUtil.dip2px(getActivity(), 100),  LinearLayout.LayoutParams.MATCH_PARENT);
        }


        mViewPager.setAdapter(new ViewPagerAdapter2(getChildFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rbs[position].setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case 30000:
                        mViewPager.setCurrentItem(0);
                        break;
                    case 30001:
                        mViewPager.setCurrentItem(1);
                        break;
                    case 30002:
                        mViewPager.setCurrentItem(2);
                        break;
                    case 30003:
                        mViewPager.setCurrentItem(3);
                        break;
                }
            }
        });

        rbs[0].setChecked(true);
        mViewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter2 extends FragmentStatePagerAdapter{


        public ViewPagerAdapter2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            OrderListFragment commentFragment = new OrderListFragment();
            Bundle b = new Bundle();
            b.putInt(INTENT_ID,mOrderStatus[position]);
            commentFragment.setArguments(b);

            return commentFragment;
        }

        @Override
        public int getCount() {
            return mOrderStatus.length;
        }
    }
}
