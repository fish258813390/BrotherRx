package neil.com.baseretrofitrx.api;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;

import neil.com.baseretrofitrx.entity.BaseResultEntity;
import neil.com.baseretrofitrx.listener.HttpOnNextListener;
import rx.functions.Func1;

/**
 * 基本网络请求类
 * <p>
 * 包含请求数据、方法、加载框显示配置等
 * </p>
 * Created by neil on 2017/11/5 0005.
 */
public abstract class BaseApi<T> implements Func1<BaseResultEntity<T>, T> {

    /**
     * rx生命周期管理
     */
    private SoftReference<RxAppCompatActivity> rxAppCompatActivity;

    /**
     * 回调
     */
    private SoftReference<HttpOnNextListener> listener;

    /**
     * 是否取消加载框
     */
    private boolean cancel;

    /**
     * 是否显示加载框
     */
    private boolean showProgress;

    /**
     * 是否需要缓存处理
     */
    private boolean cache;

    /**
     * 基础url
     */
    private String baseUrl = "";

    /**
     * 缓存方法，如果徐璈缓存必须设置这个参数，不需要就不用设置
     */
    private String method;

    /**
     * 超时时间--默认6秒
     */
    private int connectionTime = 6;

    /**
     * 有网情况下的本地缓存时间默认60s
     */
    private int cookieNetWorkTime = 60;

    /**
     * 无昂罗的情况下本地缓存时间默认30天
     */
    private int cookieNoNetWorkTime = 24* 60 *60 *30;

    public SoftReference<RxAppCompatActivity> getRxAppCompatActivity() {
        return rxAppCompatActivity;
    }

    public void setRxAppCompatActivity(SoftReference<RxAppCompatActivity> rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public SoftReference<HttpOnNextListener> getListener() {
        return listener;
    }

    public void setListener(SoftReference<HttpOnNextListener> listener) {
        this.listener = listener;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    public void setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
    }

    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }
}
