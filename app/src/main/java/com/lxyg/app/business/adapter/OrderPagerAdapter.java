package com.lxyg.app.business.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.lxyg.app.business.fragment.OrderListFragment;

/**
 * Created by 王沛栋 on 2015/11/17.
 */
public class OrderPagerAdapter extends FragmentStatePagerAdapter {
    private int [] mOrderStatus;
    public OrderPagerAdapter(FragmentManager fm,int [] orderStatus) {
        super(fm);
        mOrderStatus = orderStatus;
    }

    @Override
    public Fragment getItem(int position) {
//        OrderListFragment fragment = new OrderListFragment();
//        return fragment.newInstanceState(mOrderStatus[position]);
//        return new OrderListFragment().newInstanceState(mOrderStatus[position]);
        return null;
    }

    @Override
    public int getCount() {
        return mOrderStatus.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return OrderPagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


}
