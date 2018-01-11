package neil.com.brotherrx.ui.zone.contract;


import com.aspsine.irecyclerview.bean.PageBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import neil.com.brotherrx.entity.Result;
import neil.com.brotherrx.ui.zone.bean.CircleItem;
import neil.com.brotherrx.ui.zone.bean.CommentConfig;
import neil.com.brotherrx.ui.zone.bean.CommentItem;
import neil.com.brotherrx.ui.zone.bean.FavortItem;
import rx.Observable;

/**
 * 朋友圈 契约接口
 * Created by neil on 2018/1/2 0002.
 */
public interface CircleZoneContract {

    interface Model extends BaseModel {
        // 获取朋友圈未读消息
        Observable<String> getZoneNotReadNews();

        Observable<Result> getListDatas(String type, String userId, int page, int rows);

        // 点赞
        Observable<Result> addFavort(String publishId, String publishUserId);

        // 取消赞
        Observable<Result> deleteFavort(String publishId, String publishUserId);

        // 添加评论
        Observable<Result> addComment(String publishUserId, CommentItem circleItem);

        // 删除评论
        Observable<Result> deleteComment(String commentId);

    }

    interface View extends BaseView {

        void updateNotReadNewsCount(int count, String icon);

        void setListData(List<CircleItem> circleItemList, PageBean pageBean);

        void update2AddFavort(int circlePosition, FavortItem favortItem);

        void update2DeleteFavort(int circlePosition, String userId);

        void update2AddComment(int circlePosition, CommentItem commentItem);

        void update2DeleteComment(int circlePosition, String commentId, int commentPosition);

        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        // 获取未读总数
        public abstract void getNotReadNewsCount();

        // 获取数据
        public abstract void getListData(String type, String userId, int page, int rows);

        // 点赞
        public abstract void addFavort(String publishId, String publishUserId, int circlePosition, android.view.View view);

        // 取消点赞
        public abstract void deleteFavort(String publicId, String publishUserId, int circlePosition);

        // 增加评论
        public abstract void addComment(String content, CommentConfig commentConfig);

        // 删除评论
        public abstract void deleteComment(String content, CommentConfig commentConfig);

        // 显示评论输入框
        public abstract void showEditTextBody(CommentConfig commentConfig);

    }


}
