package neil.com.brotherrx.ui.lifecycle;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import neil.com.baseretrofitrx.base.BaseActivity;
import neil.com.baseretrofitrx.base.BasePresenter;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.JokeBean;

/**
 * 主页面
 */
public class TestOneAc extends BaseActivity {


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_one);
    }
}
