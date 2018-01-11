package neil.com.brotherrx.ui.zone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aspsine.irecyclerview.baseadapter.BaseReclyerViewAdapter;

import neil.com.brotherrx.ui.zone.bean.CircleItem;
import neil.com.brotherrx.ui.zone.presenter.CircleZonePresenter;
import neil.com.brotherrx.ui.zone.viewholder.ZoneViewHolder;

/**
 * 圈子列表adapter
 * Created by neil on 2018/1/4 0004.
 */
public class CircleAdapter extends BaseReclyerViewAdapter<CircleItem> {

    public static final int ITEM_VIEW_TYPE_DEFAULT = 0;
    public static final int ITEM_VIEW_TYPE_IMAGE = 1; // 图片
    public static final int ITEM_VIEW_TYPE_URL = 2; // url
    private Context mContext;
    private CircleZonePresenter mPresenter;

    public CircleAdapter(Context context, CircleZonePresenter mPresenter) {
        super(context);
        this.mContext = context;
        this.mPresenter = mPresenter;
    }

    @Override
    public int getItemViewType(int position) {
        return getData().get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ZoneViewHolder.create(mContext, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ZoneViewHolder) {
            ((ZoneViewHolder) holder).setData(mPresenter, get(position), position);
        }
    }
}
