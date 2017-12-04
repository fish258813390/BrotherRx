package neil.com.baseretrofitrx;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.SoftReference;

import neil.com.baseretrofitrx.api.BaseApi;
import neil.com.baseretrofitrx.listener.HttpOnNextListener;
import neil.com.baseretrofitrx.utils.NetworkUtils;
import rx.Subscriber;

/**
 * 用于在http请求开始时，自动显示一个ProgressDialog
 * Created by neil on 2017/11/5 .
 */
public class HttpProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private static final String TAG = "HttpProgressSubscriber";

    /**
     * 是否弹窗
     */
    private boolean showProgress = true;

    /**
     * 软引用回调接口
     */
    private SoftReference<HttpOnNextListener<T>> mSubscriberOnNextListener;

    /**
     * 软引用反正内存泄露
     */
    private SoftReference<Context> mActivity;

    /**
     * 加载框
     */
    private ProgressDialog pd;

    private BaseApi api;

    private ProgressDialogHandler mProgressDialogHandler;


    /**
     * 构造
     *
     * @param api
     */
    public HttpProgressSubscriber(BaseApi api, SoftReference<HttpOnNextListener<T>> listenerSoftReference, SoftReference<Context>
            mActivity) {
        this.api = api;
        this.mSubscriberOnNextListener = listenerSoftReference;
        this.mActivity = mActivity;
        mProgressDialogHandler = new ProgressDialogHandler(mActivity.get(), this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始调用
     */
    @Override
    public void onStart() {
        if (!NetworkUtils.isNetAvailable(mActivity.get())) {
            Toast.makeText(mActivity.get(), "当前网络不可用，请检查网络情况！", Toast.LENGTH_SHORT).show();
            // 一定好主动调用下面这一句
            onCompleted();
            return;
        }
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "HttpProgressSubscriber.onError: " + e.toString());
        Log.e(TAG, "HttpProgressSubscriber.onError: " + e.getMessage());
        dismissProgressDialog();
        mSubscriberOnNextListener.get().onError(e);
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }


    /**
     * 取消progressDialog的时候，取消对obserable的订单，同时也取消了网络请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            mSubscriberOnNextListener.get().onCancel();
            this.unsubscribe();
        }
    }

    /**
     * 是否显示progressDialog
     *
     * @return
     */
    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean show) {
        this.showProgress = show;
    }

}
