package neil.com.baseretrofitrx;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import neil.com.baseretrofitrx.listener.SubscriberOnNextListener;
import neil.com.baseretrofitrx.utils.NetworkUtils;
import rx.Subscriber;

/**
 * 用于在http请求开始时，自动显示一个ProgressDialog
 * Created by neil on 2017/11/5 .
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private static final String TAG = "ProgressSubscriber";

    /**
     * 是否弹窗
     */
    private boolean showProgress = true;

    /**
     * 软引用回调接口
     */
    private SubscriberOnNextListener<T> mSubscriberOnNextListener;

    private ProgressDialogHandler mProgressDialogHandler;


    /**
     * 构造
     *
     * @param api
     */
    private Context context;

    public ProgressSubscriber(SubscriberOnNextListener<T> mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
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
        if (!NetworkUtils.isNetAvailable(context)) {
            Toast.makeText(context, "当前网络不可用，请检查网络情况！", Toast.LENGTH_SHORT).show();
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
        Log.e(TAG, "ProgressSubscriber.onError: " + e.toString());
        Log.e(TAG, "ProgressSubscriber.onError: " + e.getMessage());
        dismissProgressDialog();
        mSubscriberOnNextListener.onError(e);
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }


    /**
     * 取消progressDialog的时候，取消对obserable的订单，同时也取消了网络请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
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
