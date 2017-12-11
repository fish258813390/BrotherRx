package neil.com.brotherrx.ui.lifecycle;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.JokeBean;

/**
 * 主页面
 */
public class QutuFragment extends Fragment {

    private Unbinder unbinder;
    private HeaderAndFooterRecyclerViewAdapter mAdapter;
    private List<JokeBean> mDatas;

    private int page = 1;
    private boolean mHasMore = false;
    private boolean isRefresh = true;

    // 处理请求返回信息
    private MyHandler mHandler = new MyHandler();

    private class MyHandler extends Handler {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:


                    break;
                case 1:
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qutu, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {

    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("lifecycle","QutuFragment----onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("lifecycle","QutuFragment----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("lifecycle","QutuFragment----onPause");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
