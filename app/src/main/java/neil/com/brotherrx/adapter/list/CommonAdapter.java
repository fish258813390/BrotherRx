package neil.com.brotherrx.adapter.list;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/***
 * 通用的Adapter
 * @author fish123 created by fish at 2016.3.5
 *
 * @param <T> 实体类
 */
public abstract class CommonAdapter<T> extends BaseAdapter{
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int layoutId; //listview布局id
	
	public CommonAdapter(Context mContext, List<T> mDatas,int layoutId) {
		super();
		this.mContext = mContext;
		this.mDatas = mDatas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
		
		convert(holder, getItem(position)); // 交由子类去实现
		
		return holder.getmConvertView(); // 返回convertView
	}
	
	public abstract void convert(ViewHolder holder,T t);

}
