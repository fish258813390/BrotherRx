package neil.com.baseretrofitrx.base;

import rx.Subscription;

/**
 * 绑定订阅关系
 * Created by neil on 2017/11/6 0006.
 */
public interface LifeSubscription {

    void bindSubscription(Subscription subscription);
}
