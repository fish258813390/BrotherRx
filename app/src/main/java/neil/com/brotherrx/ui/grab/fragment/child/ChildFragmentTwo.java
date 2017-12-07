package neil.com.brotherrx.ui.grab.fragment.child;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import neil.com.brotherrx.R;

/**
 * 抢单
 *
 * @author neil
 * @date 2017/12/6
 */

public class ChildFragmentTwo extends Fragment {

    private View view;
    private ProgressDialog progressDialog;

    @BindView(R.id.layoutSwipeRefresh)
    public SwipeRefreshLayout layoutSwipeRefresh;
    @BindView(R.id.listJuzi)
    public RecyclerView listJuzi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_grab_list, container, false);
        }
        ButterKnife.bind(getActivity(), view);
        progressDialog = new ProgressDialog(getActivity());
        initView();
        progressDialog.show();
        return view;

    }

    private void initView() {


    }


}
