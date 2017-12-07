package neil.com.brotherrx.ui.grab.fragment.child;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.adapter.GrabAdapter;
import neil.com.brotherrx.entity.GrabTargetBean;
import neil.com.brotherrx.widget.recycler.NFooterAdapter;
import neil.com.brotherrx.widget.recycler.NRecyclerView;

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
    SwipeRefreshLayout layoutSwipeRefresh;
    @BindView(R.id.rv_main)
    NRecyclerView nRecyclerView;

    GrabAdapter grabAdapter;
    private List<GrabTargetBean> mDatas;

    private String codeFooter = ""; // 0000 获取数据失败 ;2222 没有更多数据了

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_grab_list, container, false);
        }
        ButterKnife.bind(getActivity(), view);
        progressDialog = new ProgressDialog(getActivity());
        initView();
        initRecyclerView();
        progressDialog.show();
        return view;

    }

    private void initView() {


    }

    private void initRecyclerView() {
        if (mDatas != null) {
            mDatas.clear();
            mDatas = null;
        }
        mDatas = new ArrayList<>();

        grabAdapter = new GrabAdapter(getActivity(), R.layout.list_item_grab, mDatas);
        nRecyclerView.setAdapter(grabAdapter);

        nRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 判断当前布局是不是footer
                NFooterAdapter footerAdapter = nRecyclerView.getFooterAdapter(getActivity());
                boolean isFooter = footerAdapter.isFooter(position);
                if (isFooter) {
                    // 区分加载失败和没有更多数据
                    if ("0000".equals(codeFooter)) {
                        // TODO 加载失败
                    } else if ("2222".equals(codeFooter)) {
                        // TODO 没有更多数据
                    }
                } else {
                        // TODO 响应点击事件
                }
            }
        });

    }

    private void setNomoreDataLayout(){
        codeFooter = "2222";
        TextView tvFootMsg = new TextView(getActivity());
        tvFootMsg.setText("没有更多数据了...");
        nRecyclerView.loadMoreComplete();
        nRecyclerView.loadMoreEnd();
        nRecyclerView.setIsNoMore(true);
        grabAdapter.updateRecyclerView(mDatas);
    }

    private void setDataLoadFaillayout(){
        codeFooter = "0000";
        TextView tvFootMsg = new TextView(getActivity());
        tvFootMsg.setText("数据请求失败,请重试");
        nRecyclerView.setFootEndView(tvFootMsg);
        nRecyclerView.loadMoreEnd();
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            layoutSwipeRefresh.setRefreshing(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("数据重新刷新成功！");
                    nRecyclerView.setIsNoMore(false);
                    nRecyclerView.refreshComplete();
                }
            }, 500);

        }
    };
}
