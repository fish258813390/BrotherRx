package neil.com.baseretrofitrx.listener;

/**
 * 成功回调处理
 * Created by neil on 2017/11/5 0005.
 */
public abstract class HttpOnNextListener<T> {

    /**
     * 成功后回调方法
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 缓存回调方法
     * @param msg
     */
    public void onCacheNext(String msg){

    }

    /**
     * 失败回调方法
     * @param e
     */
    public void onError(Throwable e){

    }

    /**
     * 取消回调
     */
    public void onCancel(){

    }

}
