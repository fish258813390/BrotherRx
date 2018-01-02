package neil.com.brotherrx.ui.zone.model;

import com.jaydenxiao.common.baseapp.AppCache;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.LogUtils;

import neil.com.brotherrx.entity.Result;
import neil.com.brotherrx.ui.zone.DatasUtil;
import neil.com.brotherrx.ui.zone.contract.CircleZoneContract;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by neil on 2018/1/2 0002.
 */
public class ZoneModel implements CircleZoneContract.Model {

    @Override
    public Observable<String> getZoneNotReadNews() {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(AppCache.getInstance().getIcon());
                subscriber.onCompleted();
                LogUtils.logd("ZoneModel(getZoneNotReadNews)----->" + AppCache.getInstance().getIcon());
            }
        }).compose(RxSchedulers.<String>io_main());
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Result> getListDatas(String type, String userId, int page, int rows) {
        return Observable.create(new Observable.OnSubscribe<Result>() {
            @Override
            public void call(Subscriber<? super Result> subscriber) {
                Result result = DatasUtil.getZoneListDatas();
                subscriber.onNext(result);
                subscriber.onCompleted();
                LogUtils.logd("ZoneModel(getListDatas)----->" + result.toString());
            }
        }).compose(RxSchedulers.<Result>io_main());
    }
}
