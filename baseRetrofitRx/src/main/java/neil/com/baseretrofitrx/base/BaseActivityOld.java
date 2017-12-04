package neil.com.baseretrofitrx.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import neil.com.baseretrofitrx.R;

/**
 * Created by neil on 2017/11/6
 * <p>
 * </p>
 */
public abstract class BaseActivityOld extends AppCompatActivity {
    protected Context mContext;//上下文
    LinearLayout rootView; // 根布局
    protected Toolbar toolbar; // ToolBar
    protected View titleBar;
    protected FrameLayout leftLayout; // 左边布局
    protected FrameLayout centerLayout; // 中间布局
    protected FrameLayout rightLayout;   // 右边布局
    protected TextView leftFunc; // 左边按钮
    protected TextView title;
    protected TextView rightFunc; // 右边按钮
    public ActivityState activityState = ActivityState.CREATE; //设置ActivityState的初始值

    //枚举
    public enum ActivityState {
        CREATE, START, RESUME, PAUSE, STOP, DESTORY
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        // 添加到activity 栈管理中


        // 外层根布局
        rootView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.frame_content, null);

        // 包含定制的ToolBar
        if (isWithTitle()) {
            View title = LayoutInflater.from(mContext).inflate(R.layout.title_bar, null);
            rootView.addView(title);
        }

        // 封装layout布局
        if (getLayoutId() > 0) {
            View content = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            content.setLayoutParams(params);
            rootView.addView(content);
        }

        // 填充布局
        setContentView(rootView);

        // 绑定注解
        ButterKnife.bind(this);

        if (isWithTitle()) {
            setSupportActionBar(toolbar);
            initTitle();
            setCustomerTitle(getCustomTitle());
        }

        //在子线程中执行initData()方法
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                initData();
                initListener();
            }
        });

        activityState = ActivityState.CREATE;
    }


    /**
     * 是否包含通用的titleBar
     *
     * @return
     */
    protected abstract boolean isWithTitle();

    /**
     * 通用的布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 通用的title
     *
     * @return
     */
    protected abstract String getCustomTitle();

    protected abstract void initData();

    protected void initListener() {
    }


    //初始化标题栏
    private void initTitle() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        titleBar = findViewById(R.id.title_lay);
        leftLayout = (FrameLayout) titleBar.findViewById(R.id.left_lay);
        rightLayout = (FrameLayout) titleBar.findViewById(R.id.right_lay);
        centerLayout = (FrameLayout) titleBar.findViewById(R.id.center_lay);
        leftFunc = (TextView) titleBar.findViewById(R.id.left_img);
        rightFunc = (TextView) titleBar.findViewById(R.id.right_img);
        title = (TextView) titleBar.findViewById(R.id.center_txt);
        leftLayout.setOnClickListener(titleClick);
        rightLayout.setOnClickListener(titleClick);
    }

    //设置标题栏标题
    public void setCustomerTitle(String msg) {
        title.setText(msg);
    }

    private View.OnClickListener titleClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.left_lay) {
                //标题栏左边的返回按钮的点击事件
                leftClick();
            } else if (i == R.id.right_lay) {
                //标题栏右边的按钮点击事件
                rightClick();
            }
        }
    };

    protected void leftClick() {
//        onBackPressed();
    }

    //关闭当前的activity
//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    protected void rightClick() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityState = ActivityState.START;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = ActivityState.STOP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = ActivityState.DESTORY;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = ActivityState.PAUSE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ActivityState.RESUME;
    }
}
