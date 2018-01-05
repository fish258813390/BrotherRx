package neil.com.brotherrx.ui.zone.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import neil.com.brotherrx.ui.zone.adapter.CommentAdapter;

/**
 * 评论列表
 * Created by neil on 2018/1/4 0004.
 */
public class CommentListView extends LinearLayout {

    private OnItemClickListener mOnItemClickListener;  // 点击事件
    private OnItemLongClickListener mOnItemLongClickListener; // 长按点击事件

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(CommentAdapter adapter){
        adapter.bindListView(this);
    }

    public void setOnItemClick(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        mOnItemLongClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return mOnItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener(){
        return mOnItemLongClickListener;
    }


    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(int position);
    }
}
