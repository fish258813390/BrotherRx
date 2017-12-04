package neil.com.baseretrofitrx.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * activity 基类
 * Created by neil on 2017/12/4
 */
public abstract class BaseActivity<T extends BasePresenter<V>, V> extends AppCompatActivity {

    public final String Tag = getClass().getSimpleName(); // 标记

    private CompositeSubscription mCompositeSubscription; // 线程安全,由所有订阅者的组，添加所有订阅者

    private static BaseActivity mForegroundActivity = null; // 记录前台的activity

    protected Context mContext;

    protected Bundle mSaveInstanceState;

    protected static boolean isInBackground = false; // 是否在后台运行

    protected T mPresenter;

    public void setIsInBackground(boolean flag) {
        isInBackground = flag;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        isInBackground = false;
        ActivityCollector.addActivity(this);
        mSaveInstanceState = savedInstanceState;

        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
            mPresenter.setLifeSubscription(new LifeSubscription() {
                @Override
                public void bindSubscription(Subscription subscription) {
                    if (mCompositeSubscription == null) {
                        mCompositeSubscription = new CompositeSubscription();
                    }
                    mCompositeSubscription.add(subscription);
                }
            });
        }
    }

    protected abstract T createPresenter();

    @Override
    protected void onResume() {
        super.onResume();
        mForegroundActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInBackground = false;
        mForegroundActivity = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            isInBackground = true;
        }
    }

    @Override
    public void finish() {
        isInBackground = false;
        super.finish();
    }

    @Override
    protected void onDestroy() {
        isInBackground = false;
        ActivityCollector.removeActivity(this);
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
        // 销毁时,清楚订阅信息
        if(this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()){
            this.mCompositeSubscription.clear();
        }
    }

    /**
     * 获取当前处于前台的activity
     */
    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }

    /**
     * 退出出应用
     */
    public void exitApp() {
        ActivityCollector.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    /**
     * APP是否在前台
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }



}
