package neil.com.brotherrx.ui.lifecycle;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.neil.common.utils.ImageLoaderUtils;
import com.neil.common.widget.WaveView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.JokeBean;

/**
 * 我的关注
 */
public class QutuFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.img_logo)
    ImageView ivLogo;
    @BindView(R.id.wave_view)
    WaveView waveView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_care_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    protected void initView() {
        //设置头像跟着波浪背景浮动
        ImageLoaderUtils.displayRound(getContext(), ivLogo, R.mipmap.bgkobe);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.CENTER;
        waveView.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                lp.setMargins(0, 0, 0, (int) y + 2);
                ivLogo.setLayoutParams(lp);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("lifecycle", "QutuFragment----onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("lifecycle", "QutuFragment----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("lifecycle", "QutuFragment----onPause");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
