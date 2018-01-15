package neil.com.brotherrx.ui.zone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.bean.PageBean;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baseapp.AppCache;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import java.util.List;

import butterknife.BindView;
import neil.com.brotherrx.R;
import neil.com.brotherrx.ui.zone.adapter.CircleAdapter;
import neil.com.brotherrx.ui.zone.bean.CircleItem;
import neil.com.brotherrx.ui.zone.bean.CommentConfig;
import neil.com.brotherrx.ui.zone.bean.CommentItem;
import neil.com.brotherrx.ui.zone.bean.FavortItem;
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

    private CircleAdapter mAdapter; // 评论
    private CommentConfig mCommentConfig;

    private int mScreenHeight; // 屏幕高度
    private int mEditTextBodyHeight; // 可编辑文字的高度

    private int mCurrentKeyboardH; //
    private int mSelectCircleItemH; //
    private int mSelectCommentItemOffset; //
    private LinearLayoutManager linearLayoutManager;


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
        mAdapter = new CircleAdapter(this, mPresenter);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        linearLayoutManager = new LinearLayoutManager(this);
        irc.setLayoutManager(linearLayoutManager);
        irc.setAdapter(mAdapter);


        // 首次加载数据
        loadData();
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAdapter.getPageBean().isRefresh()) {
                    mPresenter.getNotReadNewsCount();
                }
                mPresenter.getListData(
                        "0",
                        AppCache.getInstance().getUserId(),
                        mAdapter.getPageBean().getLoadPage(),
                        mAdapter.getPageBean().getRows()
                );
            }
        }, 500);
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
        if (mAdapter.getPageBean().isRefresh()) {
            mAdapter.reset(circleItemList);
            irc.setRefreshEnabled(false);
        } else {
            mAdapter.addAll(circleItemList);
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        }
        // 判断是否可以加载更多
        if (pageBean.getTotalPage() <= pageBean.getPage()) {
            // 不可加载
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
        }
        if(mAdapter.getData().size() > 0){
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        } else {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        }

    }

    @Override
    public void update2AddFavort(int circlePosition, FavortItem favortItem) {

    }

    @Override
    public void update2DeleteFavort(int circlePosition, String userId) {

    }

    @Override
    public void update2AddComment(int circlePosition, CommentItem commentItem) {

    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId, int commentPosition) {

    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {

    }


}
