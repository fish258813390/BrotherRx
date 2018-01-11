package neil.com.brotherrx.ui.zone.presenter;

import com.alibaba.fastjson.JSON;
import com.aspsine.irecyclerview.bean.PageBean;
import com.jaydenxiao.common.commonutils.JsonUtils;
import com.jaydenxiao.common.commonutils.LogUtils;

import java.util.List;
import java.util.Random;

import neil.com.brotherrx.entity.Result;
import neil.com.brotherrx.ui.zone.bean.CircleItem;
import neil.com.brotherrx.ui.zone.DatasUtil;
import neil.com.brotherrx.ui.zone.bean.CommentConfig;
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

    @Override
    public void addFavort(String publishId, String publishUserId, int circlePosition, CircleZoneContract.View view) {

    }

    @Override
    public void deleteFavort(String publicId, String publishUserId, int circlePosition) {

    }

    @Override
    public void addComment(String content, CommentConfig commentConfig) {

    }

    @Override
    public void deleteComment(String content, CommentConfig commentConfig) {

    }


}
