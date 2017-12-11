package neil.com.brotherrx;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neil.com.baseretrofitrx.base.BaseActivity;
import neil.com.baseretrofitrx.base.BasePresenter;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.ui.lifecycle.MainFragment;
import neil.com.brotherrx.ui.lifecycle.QutuFragment;
import neil.com.brotherrx.ui.lifecycle.XiaohuaFragment;
import neil.com.brotherrx.widget.NoScrollViewPager;

/**
 * Created by neil on 2017/12/11 0011.
 */

public class Index2Activity extends BaseActivity {

    @BindView(R.id.radioBtn1)
    public RadioButton radioBtn1;

    @BindView(R.id.radioBtn2)
    public RadioButton radioBtn2;

    @BindView(R.id.radioBtn3)
    public RadioButton radioBtn3;

    private FragmentManager fragmentManager;

    private MainFragment mainFragment = null;

    private XiaohuaFragment xiaohuaFragment = null;

    private QutuFragment qutuFragment = null;

    // 定义一个变量，来标识是否退出
    private static boolean enableExit = false;

    private List<Fragment> fragments;
    public static int currentPage = 0;
    @BindView(R.id.id_main_viewPager)
    NoScrollViewPager idMainViewPager;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    // 处理请求返回信息
    private MyHandler mHandler = new MyHandler();

    private static class MyHandler extends Handler {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    enableExit = false;
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index2);
        ButterKnife.bind(this);

        initView();
    }


    private void initView() {
        fragmentManager = getSupportFragmentManager();

        RadioGroup rgbottomBar = (RadioGroup) findViewById(R.id.rgBottomBar);

        fragments = new ArrayList<>();

        mainFragment = new MainFragment();
        xiaohuaFragment = new XiaohuaFragment();
        qutuFragment = new QutuFragment();

        fragments.add(mainFragment);
        fragments.add(xiaohuaFragment);
        fragments.add(qutuFragment);

        idMainViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        rgbottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectTab();
            }
        });
        radioBtn1.setChecked(true);

    }

    private void selectTab() {
        if (radioBtn1.isChecked()) {
            currentPage = 0;
            setTabSelection(currentPage);
        } else if (radioBtn2.isChecked()) {
            currentPage = 1;
            setTabSelection(currentPage);

        } else if (radioBtn3.isChecked()) {
            currentPage = 2;
            setTabSelection(currentPage);
        }
    }

    public void setTabSelection(int currentPage) {
        switch (currentPage) {
            case 0://未登录
                switchPager(currentPage);
                break;
            case 1:
                switchPager(currentPage);
                break;
            case 2:
                switchPager(currentPage);
                break;
            default:
        }
    }

    private void switchPager(int currentPage) {
        idMainViewPager.setCurrentItem(currentPage, false);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!enableExit) {
                enableExit = true;
                Toast.makeText(Index2Activity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 3000);
            } else {
                Index2Activity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("lifecycle","Index2Activity----onResume");
        setTabSelection(currentPage);
    }
}
