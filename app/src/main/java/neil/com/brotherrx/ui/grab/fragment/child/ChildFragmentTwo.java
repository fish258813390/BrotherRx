package neil.com.brotherrx.ui.grab.fragment.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import neil.com.brotherrx.R;

/**
 * 抢单
 * @author neil
 * @date 2017/12/6
 */

public class ChildFragmentTwo extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_child, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
