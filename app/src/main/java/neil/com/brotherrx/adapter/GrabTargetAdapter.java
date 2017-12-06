package neil.com.brotherrx.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.GrabTargetBean;

/**
 * @author neil
 * @date 2017/12/6
 */
public class GrabTargetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;

    private Fragment mContext;

    private List<GrabTargetBean> mDatas;

    private View.OnClickListener onItemClick;

    private int itemHeight;

    public GrabTargetAdapter(Fragment mContext, List<GrabTargetBean> datas, View.OnClickListener onItemClick) {
        this.mContext = mContext;
        this.mDatas = datas;
        this.onItemClick = onItemClick;
        mInflater = LayoutInflater.from(mContext.getActivity());
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        itemHeight = dm.heightPixels * 2 / 5;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_grab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        GrabTargetBean grabTargetBean = mDatas.get(position);
        if (grabTargetBean != null) {
//            ViewGroup.LayoutParams lp = viewHolder.tvName.getLayoutParams();
//            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            lp.height = itemHeight;
//            viewHolder.tvName.setLayoutParams(lp);
            viewHolder.tvName.setText(grabTargetBean.getName());
            viewHolder.tvPhone.setText(grabTargetBean.getPhone());
            viewHolder.tvStatus.setText(grabTargetBean.getIsEnable());
            viewHolder.itemView.setTag(position);
            viewHolder.itemView.setOnClickListener(onItemClick);
        } else {

        }

    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        public TextView tvName;

        @BindView(R.id.phone)
        public TextView tvPhone;

        @BindView(R.id.status)
        public TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
