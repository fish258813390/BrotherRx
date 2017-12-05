package neil.com.brotherrx.sample.yiyuan;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Objects;

import neil.com.baseretrofitrx.ProgressSubscriber;
import neil.com.baseretrofitrx.base.BasePresenter;
import neil.com.baseretrofitrx.listener.SubscriberOnNextListener;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.entity.ResBodyBean;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by neil on 2017/12/4 0004.
 */

public class YiyuanPresenter extends BasePresenter<YiyuanView> {

    public YiyuanPresenter(Context context) {
        super(context);
    }

    public void getHotListRank(Map<String, String> params) {
        invoke(YiyuanModel.getInstance().getHotRankList(params), new ProgressSubscriber<YiyuanApiResult<ResBodyBean>>(new SubscriberOnNextListener<YiyuanApiResult<ResBodyBean>>() {
            @Override
            public void onNext(YiyuanApiResult<ResBodyBean> resultData) {
                LogUtils.d("易源数据返回--->" + resultData);
            }

            @Override
            public void onError(Throwable e) {

            }
        }, mContext));
    }


    public void getHotListRankNew(int page, String showapi_appid, String showapi_sign) {
        invoke(YiyuanModel.getInstance().getHotRankListNew(page, showapi_appid, showapi_sign), new ProgressSubscriber<YiyuanApiResult<ResBodyBean>>(new SubscriberOnNextListener<YiyuanApiResult<ResBodyBean>>() {
            @Override
            public void onNext(YiyuanApiResult<ResBodyBean> resultData) {
                LogUtils.d("易源数据返回--热搜->" + resultData);
            }

            @Override
            public void onError(Throwable e) {

            }
        }, mContext));
    }

    public void multiRequest(int page, String showapi_appid, String showapi_sign, int page2) {
        invokeMerge(new ProgressSubscriber<Object>(new SubscriberOnNextListener<Object>() {
                    @Override
                    public void onNext(Object obj) {
                        LogUtils.d("请求合并----->易源数据返回--热搜->" + new Gson().toJson(obj));

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d("易源数据返回--热搜请求异常->" + e.getMessage());
                    }
                }, mContext),
                YiyuanModel.getInstance().getHotRankListNew(page, showapi_appid, showapi_sign),
                YiyuanModel.getInstance().getHotRankListNew(page2, showapi_appid, showapi_sign));
    }


    // flatMap 操作符
    public void getDataFlatMap(int page, String showapi_appid, String showapi_sign) {
       YiyuanModel.getInstance().getHotRankListNew(page, showapi_appid, showapi_sign).
                        flatMap(new Func1<YiyuanApiResult<ResBodyBean>, Observable<ResBodyBean.ListBean>>() {
                            @Override
                            public Observable<ResBodyBean.ListBean> call(YiyuanApiResult<ResBodyBean> yiyuanApiResult) {
                                return Observable.from(yiyuanApiResult.getShowapi_res_body().getList());
                            }
                        });
    }

}
