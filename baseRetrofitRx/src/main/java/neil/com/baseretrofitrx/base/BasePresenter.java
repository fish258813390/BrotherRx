package neil.com.baseretrofitrx.base;

import android.content.Context;

import java.lang.ref.WeakReference;

import neil.com.baseretrofitrx.ProgressSubscriber;
import rx.Observable;
import rx.Subscriber;

/**
 * 泛型类型 view的引用
 * Created by neil on 2017/11/6
 */
public class BasePresenter<T> {

    public String tag = this.getClass().getSimpleName();

    private WeakReference<T> mViewRef; // 弱引用,有效防止view内存泄漏

    protected Context mContext; // 上下文

    public LifeSubscription lifeSubscription; // 绑定订阅

    public BasePresenter() {
        super();
    }

    public BasePresenter(Context context) {
        this.mContext = context;
    }

    public void setLifeSubscription(LifeSubscription lifeSubscription) {
        this.lifeSubscription = lifeSubscription;
    }

    // 处理单个请求订阅
    protected <T> void invoke(Observable<T> observable, ProgressSubscriber<T> subscription) {
        BaseModel.invoke(lifeSubscription, observable, subscription);
    }

    // 处理多个请求订阅 合并数据源
    protected <T> void invokeMerge(Subscriber<T> subscription, Observable<T>... observable) {
        BaseModel.invokeMerge(lifeSubscription, subscription, observable);
    }


    /**
     * 关联view
     *
     * @param view
     */
    void attach(T view) {
        mViewRef = new WeakReference<>(view);
    }

    /**
     * 解除关联
     */
    void detach() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }


    /**
     * 获取引用view
     *
     * @return
     */
    public T getView() {
        return mViewRef.get();
    }


}
