package neil.com.brotherrx.ui.zone.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.bean.PageBean;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import java.util.List;

import butterknife.Bind;
import butterknife.BindView;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.zone.CircleItem;
import neil.com.brotherrx.ui.zone.contract.CircleZoneContract;
import neil.com.brotherrx.ui.zone.model.ZoneModel;
import neil.com.brotherrx.ui.zone.presenter.CircleZonePresenter;


/**
 * 朋友圈
 * Created by neil on 2018/1/2 0002.
 */
public class CircleZoneActivity extends BaseActivity<CircleZonePresenter, ZoneModel> implements CircleZoneContract.View, View.OnClickListener {

    @BindView(R.id.irc)
    IRecyclerView irc;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;

    /**
     * 启动入口
     */
    public static void startAction(Context context) {
        Intent intent = new Intent(context, CircleZoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_zone;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void updateNotReadNewsCount(int count, String icon) {

    }

    @Override
    public void setListData(List<CircleItem> circleItemList, PageBean pageBean) {

    }


}
