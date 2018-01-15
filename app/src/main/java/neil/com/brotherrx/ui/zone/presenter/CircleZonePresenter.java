package neil.com.brotherrx.ui.zone.presenter;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.aspsine.irecyclerview.bean.PageBean;
import com.jaydenxiao.common.baseapp.AppCache;
import com.jaydenxiao.common.commonutils.JsonUtils;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;

import java.util.List;
import java.util.Random;

import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.Result;
import neil.com.brotherrx.ui.zone.DatasUtil;
import neil.com.brotherrx.ui.zone.bean.CircleItem;
import neil.com.brotherrx.ui.zone.bean.CommentConfig;
import neil.com.brotherrx.ui.zone.bean.CommentItem;
import neil.com.brotherrx.ui.zone.bean.FavortItem;
import neil.com.brotherrx.ui.zone.contract.CircleZoneContract;
import rx.Subscriber;

/**
 * Created by neil on 2018/1/2 0002.
 */
public class CircleZonePresenter extends CircleZoneContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void getNotReadNewsCount() {
        mRxManage.add(mModel.getZoneNotReadNews().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String icon) {
                mView.updateNotReadNewsCount(10, icon);
            }
        }));
    }

    @Override
    public void getListData(String type, String userId, int page, int rows) {
        //加载更多不显示加载条
        if (page <= 1) {
            mView.showLoading("加载中...");
        }
        mRxManage.add(mModel.getListDatas(type, userId, page, rows).subscribe(new Subscriber<Result>() {
            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorTip("" + e.getMessage());
            }

            @Override
            public void onNext(Result result) {
                LogUtils.logd("获取回调数据成功----->" + result.toString());
                if (result != null) {
                    try {
                        List<CircleItem> circleItems = JSON.parseArray(JsonUtils.getValue(result.getMsg(), "list"), CircleItem.class);
                        for (int i = 0; i < circleItems.size(); i++) {
                            circleItems.get(i).setPictures(DatasUtil.getRandomPhotoUrlString(new Random().nextInt(9)));
                        }
                        PageBean pageBean = JSON.parseObject(JsonUtils.getValue(result.getMsg(), "page"), PageBean.class);
                        mView.setListData(circleItems, pageBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }

    /**
     * 点赞
     *
     * @param publishId
     * @param publishUserId
     * @param circlePosition
     * @param view
     */
    @Override
    public void addFavort(final String publishId, String publishUserId, final int circlePosition, View view) {
        mView.startProgressDialog();
        mRxManage.add(mModel.addFavort(publishId, publishUserId).subscribe(new Subscriber<Result>() {
            @Override
            public void onCompleted() {
                mView.stopProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(Result result) {
                if (result != null) {
                    // 点赞效果先省略
                    FavortItem item = new FavortItem(publishId, AppCache.getInstance().getUserId(), "jayden");
                    mView.update2AddFavort(circlePosition, item);
                }
            }
        }));

    }

    /**
     * 取消点赞
     *
     * @param publicId
     * @param publishUserId
     * @param circlePosition
     */
    @Override
    public void deleteFavort(String publicId, String publishUserId, final int circlePosition) {
        mView.startProgressDialog();
        mRxManage.add(mModel.deleteFavort(publicId, publishUserId).subscribe(new Subscriber<Result>() {
            @Override
            public void onCompleted() {
                mView.stopProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(Result result) {
                if (result != null) {
                    mView.update2DeleteFavort(circlePosition, AppCache.getInstance().getUserId());
                }
            }
        }));
    }


    /**
     * 添加评论
     *
     * @param content
     * @param config
     */
    @Override
    public void addComment(final String content, final CommentConfig config) {
        mView.startProgressDialog();
        mRxManage.add(mModel.addComment(config.getPublishUserId(), new CommentItem(
                config.getName(),
                config.getId(),
                content,
                config.getPublishId(),
                AppCache.getInstance().getUserId(),
                "jayden"))
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        mView.stopProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.stopProgressDialog();
                        ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result != null) {
                            mView.update2AddComment(config.circlePosition, new CommentItem(
                                    config.getName(),
                                    config.getId(),
                                    content,
                                    config.getPublishId(),
                                    AppCache.getInstance().getUserId(),
                                    "锋"
                            ));
                        }
                    }
                }));
    }

    /**
     * 删除评论
     *
     * @param circlePosition
     * @param commentId
     * @param commentPosition
     */
    @Override
    public void deleteComment(final int circlePosition, final String commentId, final int commentPosition) {
        mView.startProgressDialog();
        mRxManage.add(mModel.deleteComment(commentId).subscribe(new Subscriber<Result>() {
            @Override
            public void onCompleted() {
                mView.stopProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.stopProgressDialog();
                ToastUitl.showToastWithImg(mContext.getString(R.string.net_error), R.drawable.ic_wrong);
            }

            @Override
            public void onNext(Result result) {
                mView.update2DeleteComment(circlePosition, commentId, commentPosition);
            }
        }));
    }

    /**
     * 显示输入框
     * @param commentConfig
     */
    @Override
    public void showEditTextBody(CommentConfig commentConfig) {
        mView.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
    }


}
