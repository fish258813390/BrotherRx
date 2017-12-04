package neil.com.baseretrofitrx.listener;

public interface SubscriberOnNextListener<T> {
    void onNext(T t);
    void onError(Throwable e);

}