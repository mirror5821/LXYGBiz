package com.lxyg.app.business.draglistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.NonStandard;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.PriceUtil;

/***
 * 自定义适配器
 *
 * @author zhangjia
 *
 */
public class DragListAdapter extends BaseAdapter {
	private List<NonStandard> mList;
	private Context context;
	private LayoutInflater mLayoutInflater;

	public DragListAdapter(Context context, List<NonStandard> mList) {
		this.context = context;
		this.mList = mList;
		mLayoutInflater = LayoutInflater.from(context);
	}

	public void showDropItem(boolean showItem){
		this.ShowItem = showItem;
	}

	public void setInvisiblePosition(int position){
		invisilePosition = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/***
		 * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
		 * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
		 */
		convertView = mLayoutInflater.inflate(R.layout.item_nonstandard, null);

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView title = (TextView) convertView.findViewById(R.id.t);
		TextView price = (TextView) convertView.findViewById(R.id.price);
		TextView cash = (TextView) convertView.findViewById(R.id.cash);
		ImageView img = (ImageView) convertView.findViewById(R.id.img);

		NonStandard n = mList.get(position);
		name.setText(n.getName());
		title.setText(n.getTitle());
		price.setText("售价: ￥"+PriceUtil.floatToString(n.getPrice()));
		AppContext.displayImage(img, n.getCover_img());
		cash.setText("红包抵现: ￥" + PriceUtil.floatToString(n.getCash_pay()));

		if (isChanged){
			if (position == invisilePosition){
				if(!ShowItem){
//					convertView.findViewById(R.id.drag_list_item_text).setVisibility(View.INVISIBLE);
					convertView.findViewById(R.id.drag_list_item_image).setVisibility(View.INVISIBLE);
				}
			}
			if(lastFlag != -1){
				if(lastFlag == 1){
					if(position > invisilePosition){
						Animation animation;
						animation = getFromSelfAnimation(0, -height);
						convertView.startAnimation(animation);
					}
				}else if(lastFlag == 0){
					if(position < invisilePosition){
						Animation animation;
						animation = getFromSelfAnimation(0, height);
						convertView.startAnimation(animation);
					}
				}
			}
		}
		return convertView;
	}




	/***
	 * 动态修改ListVIiw的方位.
	 *
	 * @param start
	 *            点击移动的position
	 * @param down
	 *            松开时候的position
	 */
	private int invisilePosition = -1;
	private boolean isChanged = true;
	private boolean ShowItem = false;

	public void exchange(int startPosition, int endPosition) {
		NonStandard  startObject = getItem(startPosition);
		if(startPosition < endPosition){
			mList.add(endPosition + 1, startObject);
			mList.remove(startPosition);
		}else{
			mList.add(endPosition,startObject);
			mList.remove(startPosition + 1);
		}
		isChanged = true;
	}

	public void exchangeCopy(int startPosition, int endPosition) {
		NonStandard startObject = getCopyItem(startPosition);
		if(startPosition < endPosition){
			mCopyList.add(endPosition + 1,  startObject);
			mCopyList.remove(startPosition);
		}else{
			mCopyList.add(endPosition,startObject);
			mCopyList.remove(startPosition + 1);
		}
		isChanged = true;
	}


	public NonStandard getCopyItem(int position) {
		return mCopyList.get(position);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public NonStandard getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private List<NonStandard> mCopyList = new ArrayList<NonStandard>();

	public void copyList(){
		mCopyList.clear();
		for (NonStandard str : mList) {
			mCopyList.add(str);
		}
	}

	public void pastList(){
		mList.clear();
		for (NonStandard str : mCopyList) {
			mList.add(str);
		}
	}

	private boolean isSameDragDirection = true;
	private int lastFlag = -1;
	private int height;

	public void setIsSameDragDirection(boolean value){
		isSameDragDirection = value;
	}

	public void setLastFlag(int flag){
		lastFlag = flag;
	}

	public void setHeight(int value){
		height = value;
	}

	public Animation getFromSelfAnimation(int x,int y){
		TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
		go.setInterpolator(new AccelerateDecelerateInterpolator());
		go.setFillAfter(true);
		go.setDuration(100);
		go.setInterpolator(new AccelerateInterpolator());
		return go;
	}
}