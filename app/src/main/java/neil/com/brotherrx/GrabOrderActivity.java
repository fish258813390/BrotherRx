package neil.com.brotherrx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import neil.com.baseretrofitrx.base.BaseActivity;
import neil.com.baseretrofitrx.base.BasePresenter;
import neil.com.brotherrx.ui.grab.fragment.FragmentOne;
import neil.com.brotherrx.ui.grab.fragment.FragmentTwo;

/**
 * @author neil
 * @date 2017/12/6
 */

public class GrabOrderActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar_container)
    public BottomNavigationBar bottom_navigation_bar_container;

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    private Unbinder unbinder;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_order_main);
        unbinder = ButterKnife.bind(this);

        initBottomNavBar();
    }

    /*初始化底部导航栏*/
    private void initBottomNavBar() {
        bottom_navigation_bar_container.setMode(BottomNavigationBar.MODE_FIXED);
        bottom_navigation_bar_container.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottom_navigation_bar_container.setBarBackgroundColor(R.color.white); //背景颜色
        bottom_navigation_bar_container.setInActiveColor(R.color.bottom_nav_normal); //未选中时的颜色
        bottom_navigation_bar_container.setActiveColor(R.color.bottom_nav_selected);//选中时的颜色

        BottomNavigationItem meijulItem = new BottomNavigationItem(R.mipmap.bottom_customer_off, "灵感");
        BottomNavigationItem allArticleItem = new BottomNavigationItem(R.mipmap.bottom_loan_off, "经典");

        bottom_navigation_bar_container.addItem(meijulItem).addItem(allArticleItem);
        bottom_navigation_bar_container.setFirstSelectedPosition(0);
        bottom_navigation_bar_container.initialise();
        bottom_navigation_bar_container.setTabSelectedListener(this);

        setDefaultFrag();
    }


    private void setDefaultFrag() {
        if (fragmentOne == null) {
            fragmentOne = new FragmentOne();
        }
        addFrag(fragmentOne);
        getSupportFragmentManager().beginTransaction().show(fragmentOne).commit();
    }

    @Override
    public void onTabSelected(int position) {
        hideAllFrag();//先隐藏所有frag
        switch (position) {
            case 0:
                if (fragmentOne == null) {
                    fragmentOne = new FragmentOne();

                }
                addFrag(fragmentOne);
                getSupportFragmentManager().beginTransaction().show(fragmentOne).commit();
                break;

            case 1:
                if (fragmentTwo == null) {
                    fragmentTwo = new FragmentTwo();
                }
                addFrag(fragmentTwo);
                getSupportFragmentManager().beginTransaction().show(fragmentTwo).commit();

                break;

        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /*隐藏frag*/
    private void hideFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && frag.isAdded()) {
            ft.hide(frag);
        }
        ft.commit();
    }

    /*隐藏所有fragment*/
    private void hideAllFrag() {
        hideFrag(fragmentOne);
        hideFrag(fragmentTwo);
    }

    /*添加Frag*/
    private void addFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && !frag.isAdded()) {
            ft.add(R.id.bottom_nav_content, frag);
        }
        ft.commit();
    }

}


