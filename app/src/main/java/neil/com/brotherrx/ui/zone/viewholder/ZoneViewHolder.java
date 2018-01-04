package neil.com.brotherrx.ui.zone.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import neil.com.brotherrx.entity.zone.CircleItem;
import neil.com.brotherrx.ui.zone.presenter.CircleZonePresenter;
import neil.com.brotherrx.ui.zone.widget.ExpandableTextView;
import neil.com.brotherrx.ui.zone.widget.FavortListView;

/**
 * 动态说说item viewholder
 * Created by neil on 2018/1/4 0004.
 */
public class ZoneViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private int type;
    private View itemView;
    private CircleZonePresenter mPresenter;
    private CircleItem circleItem; // 说说bean
    private int position;

    public ImageView headIv;
    public TextView nameTv;
    public TextView urlTipTv;

    /**
     * 动态的内容
     */
    public ExpandableTextView contentTv;  // 说说内容
    public TextView timeTv; // 说说发布时间
    public TextView tvAddressOrDistance; //  说说发布位置(定位到的位置信息)
    public TextView deleteBtn; // 删除
    public TextView favortBtn; // 点赞
    public TextView snsBtn;
    public LinearLayout ll_comment; // 评论

    /**
     * 点赞列表
     */
    public FavortListView favortListView;
    public LinearLayout urlBody;
    public LinearLayout diacommentBody;
    public View digLine;

    // 评论列表

    public ZoneViewHolder(View itemView) {
        super(itemView);
    }
}
