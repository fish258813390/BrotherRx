package neil.com.baseretrofitrx.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by neil on 2017/12/4
 */

public abstract class BaseFragment <T extends BasePresenter<V>, V> extends Fragment {

    protected T mPresenter;
    protected final String tag = getClass().getSimpleName();
    protected Context mContext;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {

            mPresenter.detach();
        }
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.clear();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    /**
     * @Description: 必须要实现的方法
     * @author mio4kon
     */
    protected abstract T createPresenter();
}
