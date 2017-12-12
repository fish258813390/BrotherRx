package neil.com.brotherrx.ui.grab.fragment.child;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.adapter.GrabAdapter;
import neil.com.brotherrx.adapter.GrabTargetAdapter;
import neil.com.brotherrx.adapter.recycler.GrabTestAdapter;
import neil.com.brotherrx.entity.GrabTargetBean;
import neil.com.brotherrx.widget.recycler.LoadMoreListener;
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

    SwipeRefreshLayout layoutSwipeRefresh;
    NRecyclerView nRecyclerView;

    GrabAdapter grabAdapter;
    private List<GrabTargetBean> mDatas = new ArrayList<>();

    private String codeFooter = ""; // 0000 获取数据失败 ;2222 没有更多数据了

    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.common_page_data_loading, container, false);
        }
//        ButterKnife.bind(getActivity(), view);

        initView();
        initRecyclerView();

        return view;
    }

    private void initView() {
        layoutSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.layoutSwipeRefresh);
        nRecyclerView = (NRecyclerView) view.findViewById(R.id.rv_main);

    }


    private void initRecyclerView() {
        if (mDatas != null) {
            mDatas.clear();
        }
        initData();
        grabAdapter = new GrabAdapter(getActivity(), R.layout.list_item_grab, mDatas);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        nRecyclerView.setAdapter(grabAdapter);
        nRecyclerView.setLayoutManager(linearLayoutManager);

        nRecyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

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
                        LogUtils.d("没有更多数据了");
                    } else if ("3333".equals(codeFooter)) {
                        // TODO 可以加载更多
                        loadMore();
                    }
                } else {
                    // TODO 响应点击事件
                    Toast.makeText(getContext(),position +"",Toast.LENGTH_SHORT).show();
                }
            }
        });


        layoutSwipeRefresh.setOnRefreshListener(onRefreshListener);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            GrabTargetBean grabTargetBean = new GrabTargetBean();
            grabTargetBean.setName("已抢单第[01]组," + i);
            grabTargetBean.setPhone(i + "---" + new Random().nextInt(4));
            if (i % 4 == 0) {
                grabTargetBean.setIsEnable("可抢");
            } else {
                grabTargetBean.setIsEnable("不可抢");
            }
            mDatas.add(grabTargetBean);
        }
        if (mDatas.size() >= 10) {
//            setLoadMore();
//            setDataLoadFaillayout("");
            clcikLoadMore();
        } else {
            setNomoreDataLayout();
        }
    }

    private void setNomoreDataLayout() {
        codeFooter = "2222";
        TextView tvFootMsg = new TextView(getActivity());
        tvFootMsg.setText("没有更多数据了...");
        nRecyclerView.loadMoreComplete();
        nRecyclerView.loadMoreEnd();
        nRecyclerView.setIsNoMore(true);
    }

    private void setDataLoadFaillayout(String msg) {
        codeFooter = "0000";
        TextView tvFootMsg = new TextView(getActivity());
        tvFootMsg.setText("数据请求失败,请重试");
        nRecyclerView.setFootEndView(tvFootMsg);
        nRecyclerView.loadMoreEnd();
    }

    private void clcikLoadMore() {
        codeFooter = "3333";
//        TextView tvFootMsg = new TextView(getActivity());
//        tvFootMsg.setText("点击加载更多...");
//        nRecyclerView.setFootEndView(tvFootMsg);
        nRecyclerView.loadMoreComplete();
        nRecyclerView.canLoadMore();
    }

    private void setLoadMore(){
        nRecyclerView.setFootLoadingView();
    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            layoutSwipeRefresh.setRefreshing(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    page = 1;
                    refreshData();
                }
            }, 500);

        }
    };

    private void refreshData() {
        if (mDatas != null) {
            mDatas.clear();
        }
        for (int i = 0; i < 10; i++) {
            GrabTargetBean grabTargetBean = new GrabTargetBean();
            grabTargetBean.setName("刷新第[01]组," + i);
            grabTargetBean.setPhone(i + "---" + new Random().nextInt(4));
            if (i % 2 == 0) {
                grabTargetBean.setIsEnable("可抢");
            } else {
                grabTargetBean.setIsEnable("不可抢");
            }
            mDatas.add(grabTargetBean);
        }
        LogUtils.d("数据重新刷新成功！");
        nRecyclerView.setIsNoMore(false);
        nRecyclerView.refreshComplete();
        grabAdapter.updateRecyclerView(mDatas);
        if (mDatas.size() >= 10) {
            setLoadMore();
        } else {
            setNomoreDataLayout();
        }
    }

    private void loadMore() {
        if (mDatas != null) {
            for (int i = 0; i < 5; i++) {
                GrabTargetBean grabTargetBean = new GrabTargetBean();
                grabTargetBean.setName("加载更多___第[01]组," + i);
                grabTargetBean.setPhone(i + "---" + new Random().nextInt(4));
                if (i % 2 == 0) {
                    grabTargetBean.setIsEnable("可抢");
                } else {
                    grabTargetBean.setIsEnable("不可抢");
                }
                mDatas.add(grabTargetBean);
            }
            if (page == 4) {
                setNomoreDataLayout();
            } else {
                setLoadMore();
            }
            page++;
        }
    }
}
