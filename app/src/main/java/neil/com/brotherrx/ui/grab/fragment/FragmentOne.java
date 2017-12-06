package neil.com.brotherrx.ui.grab.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import neil.com.brotherrx.R;
import neil.com.brotherrx.adapter.TabAdapter;
import neil.com.brotherrx.ui.grab.fragment.child.ChildFragmentOne;
import neil.com.brotherrx.ui.grab.fragment.child.ChildFragmentTwo;

/**
 * 抢单
 * @author neil
 * @date 2017/12/6
 */

public class FragmentOne extends Fragment {

    private View view;

    @BindView(R.id.line_tab_layout)
    TabLayout lineTabLayout;
    @BindView(R.id.line_view_pager)
    ViewPager lineViewPager;
    private List<String> titles;
    private List<Fragment> fragments;
    private int newCount=20;
    private int nonArrivalCount=123;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);

        unbinder = ButterKnife.bind(this, view);

        init();

        return view;

    }

    private void init() {
        titles = new ArrayList<>();
        titles.add("可抢单");
        titles.add("已抢单");
        fragments = new ArrayList<>();

        ChildFragmentOne childFragmentOne = new ChildFragmentOne();
        ChildFragmentTwo childFragmentTwo = new ChildFragmentTwo();
        fragments.add(childFragmentOne);
        fragments.add(childFragmentTwo);
        lineViewPager.setAdapter(new TabAdapter(getActivity().getSupportFragmentManager(), fragments, titles));
        lineTabLayout.setupWithViewPager(lineViewPager);
        setIndicator(getActivity(), lineTabLayout, 10, 10);
        //添加分割线
        LinearLayout linearLayout = (LinearLayout) lineTabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerPadding(dip2px(15));
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.layout_divider_vertical));
    }


    /**
     * 修改tabLayoutIndicator长度
     */
    public void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * dp2px(leftDip, context));
        int right = (int) (getDisplayMetrics(context).density * dp2px(rightDip, context));

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    //尺寸转化
    public int dp2px(float dp, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }

    public int sp2px(float sp, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * dip转换px
     */
    public int dip2px(float dip) {
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
