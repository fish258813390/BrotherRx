package neil.com.brotherrx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import neil.com.brotherrx.R;
import neil.com.brotherrx.adapter.recycler.HaoCommonAdapter;
import neil.com.brotherrx.adapter.recycler.base.ViewHolder;
import neil.com.brotherrx.entity.GrabTargetBean;

/**
 * @author neil
 * @date 2017/12/7
 */

public class GrabAdapter extends HaoCommonAdapter<GrabTargetBean> {


    public GrabAdapter(Context context, int layoutId, List<GrabTargetBean> datas) {
        super(context, layoutId, datas);
    }

    public void updateRecyclerView(List<GrabTargetBean> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, GrabTargetBean grabTargetBean, int position) {
        if (grabTargetBean != null) {
            holder.setText(R.id.name, grabTargetBean.getName());
            holder.setText(R.id.phone, grabTargetBean.getPhone());
            holder.setText(R.id.status, grabTargetBean.getIsEnable());
        } else {

        }
    }
}
