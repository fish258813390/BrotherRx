package neil.com.brotherrx.ui.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.GrabOrderActivity;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.JokeBean;
import neil.com.brotherrx.ui.grab.GrabMainActivity;

/**
 * 主页面
 */
public class XiaohuaFragment extends Fragment {

    private Unbinder unbinder;
    private HeaderAndFooterRecyclerViewAdapter mAdapter;
    private List<JokeBean> mDatas;

    private int page = 1;
    private boolean mHasMore = false;
    private boolean isRefresh = true;

    @BindView(R.id.button)
    Button btn_test;

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
        View view = inflater.inflate(R.layout.fragment_xiaohua, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {

    }

    @OnClick({R.id.button})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                startActivity(new Intent(getActivity(), TestOneAc.class));

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("lifecycle","XiaohuaFragment----onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("lifecycle","XiaohuaFragment----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("lifecycle","XiaohuaFragment----onPause");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
