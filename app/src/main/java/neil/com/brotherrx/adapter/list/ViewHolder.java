package neil.com.brotherrx.adapter.list;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * BaseAdapter的ViewHolder抽取及优化
 * 
 * @author created by fish at 2016.3.5
 */
public class ViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	public ViewHolder(Context context,ViewGroup parent,int layoutId,int position){
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this); //this指代当前的viewholder
	}

	public int getPosition() {
		return mPosition;
	}

	public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if(convertView == null){
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}
	
	/***
	 * 通过viewId 来获取组件对象
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId){
		View view  = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 获取当前的convertView 组件
	 * @return
	 */
	public View getmConvertView() {
		return mConvertView;
	}
	
	/**
	 * 给TextView组件设置信息
	 * @param viewId 组件id
	 * @param text 文字信息
	 * @return 当前对象
	 */
	public ViewHolder setText(int viewId,String text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	/***
	 * 给ImageView设置信息
	 * @param viewId 组件id
	 * @param resource 图片资源
	 * @return 当前对象
	 */
	public ViewHolder setImageResource(int viewId ,int resource){
		ImageView iv = getView(viewId);
		iv.setImageResource(resource);
		return this;
	}
}
