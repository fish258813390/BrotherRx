package neil.com.brotherrx.ui.grab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import neil.com.brotherrx.R;

/**
 * @author neil
 * @date 2017/12/6
 */

public class FragmentTwo extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_two, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }



}
