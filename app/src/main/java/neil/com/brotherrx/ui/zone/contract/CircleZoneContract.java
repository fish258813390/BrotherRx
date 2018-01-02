package neil.com.brotherrx.ui.zone.contract;


import com.aspsine.irecyclerview.bean.PageBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import neil.com.brotherrx.entity.Result;
import neil.com.brotherrx.entity.zone.CircleItem;
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


    }

    interface View extends BaseView {

        void updateNotReadNewsCount(int count, String icon);

        void setListData(List<CircleItem> circleItemList, PageBean pageBean);

    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        // 获取未读总数
        public abstract void getNotReadNewsCount();

        // 获取数据
        public abstract void getListData(String type, String userId, int page, int rows);


    }


}
