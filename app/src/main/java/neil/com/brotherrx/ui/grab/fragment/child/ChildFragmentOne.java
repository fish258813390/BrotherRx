package neil.com.brotherrx.ui.grab.fragment.child;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lnyp.flexibledivider.GridSpacingItemDecoration;
import com.lnyp.flexibledivider.HorizontalDividerItemDecoration;
import com.lnyp.recyclerview.EndlessRecyclerOnScrollListener;
import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lnyp.recyclerview.HeaderSpanSizeLookup;
import com.lnyp.recyclerview.RecyclerViewLoadingFooter;
import com.lnyp.recyclerview.RecyclerViewStateUtils;

import java.util.ArrayList;
import java.util.List;

import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.adapter.GrabTargetAdapter;
import neil.com.brotherrx.entity.GrabTargetBean;

/**
 * 抢单
 *
 * @author neil
 * @date 2017/12/6
 */

public class ChildFragmentOne extends Fragment {

    private static final String ARG_TYPE = "type";

    public SwipeRefreshLayout layoutSwipeRefresh;

    public RecyclerView listJuzi;

    private String type;

    private View view;

    private List<GrabTargetBean> mDatas;

    private HeaderAndFooterRecyclerViewAdapter mAdapter;

    private String page;

    private boolean mHasMore = true;

    private boolean isRefresh = true;

    private ProgressDialog progressDialog;


    private int flag = 0;

    private FloatingActionButton fab;

    public ChildFragmentOne() {
    }

    public static ChildFragmentOne newInstance(String type) {
        ChildFragmentOne fragment = new ChildFragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_grab_list, container, false);
        }
        progressDialog = new ProgressDialog(getActivity());
        initView();
        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        layoutSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.layoutSwipeRefresh);
        listJuzi = (RecyclerView) view.findViewById(R.id.listJuzi);
        fab = view.findViewById(R.id.fab_button);

        mDatas = new ArrayList<>();

        GrabTargetAdapter articleAdapter = new GrabTargetAdapter(this, mDatas, onClickListener);
        mAdapter = new HeaderAndFooterRecyclerViewAdapter(articleAdapter);
        listJuzi.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(listJuzi.getAdapter(), gridLayoutManager.getSpanCount()));
//        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration.Builder(getActivity(), linearLayoutManager.getSpanCount())
//                .setDividerColor(Color.parseColor("#F3F7F6"))
//                .build();

        listJuzi.setLayoutManager(linearLayoutManager);
        listJuzi.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .colorResId(R.color.divider_color)
                        .size(4)
                        .build());
        listJuzi.addOnScrollListener(mOnScrollListener);

        layoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        layoutSwipeRefresh.setOnRefreshListener(onRefreshListener);
//        refresh();


        listJuzi.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                LogUtils.d("FirstVisibleItemPosition:" + firstVisibleItemPosition);
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                LogUtils.d("firstCompletelyVisibleItemPosition:" + firstCompletelyVisibleItemPosition);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                LogUtils.d("lastVisibleItemPosition:" + lastVisibleItemPosition);
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                LogUtils.d("lastCompletelyVisibleItemPosition:" + lastCompletelyVisibleItemPosition);
                if (firstVisibleItemPosition >= 1) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.GONE);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) listJuzi.getLayoutManager();
                if (mDatas != null && mDatas.size() > 0) {
                    layoutManager.scrollToPosition(0);
                }
            }
        });

    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            page = null;
            isRefresh = true;

//            qryMeijus();
            refresh();

        }
    };

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            RecyclerViewLoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(listJuzi);
            if (state == RecyclerViewLoadingFooter.State.Loading) {
                return;
            }
            if (mHasMore) {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), listJuzi, mHasMore, RecyclerViewLoadingFooter.State.Loading, null);
//                qryMeijus();
                loadMore();
            } else {

                RecyclerViewStateUtils.setFooterViewState(getActivity(), listJuzi, mHasMore, RecyclerViewLoadingFooter.State.TheEnd, null);
//                RecyclerViewStateUtils.setFooterViewState(getActivity(), listJuzi, true, RecyclerViewLoadingFooter.State.NetWorkError, mFooterClick);
            }
        }
    };

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerViewStateUtils.setFooterViewState(getActivity(), listJuzi, mHasMore, RecyclerViewLoadingFooter.State.Loading, null);

//            qryMeijus();
            // 查询
        }
    };


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();

        }
    };

    private void refresh() {
        progressDialog.dismiss();
        if (isRefresh) {
            mDatas.clear();
            isRefresh = false;
        }
        // 刷新
        for (int i = 0; i < 10; i++) {
            GrabTargetBean grabTargetBean = new GrabTargetBean();
            grabTargetBean.setName("第[1]组," + i);
            grabTargetBean.setPhone("158774444" + i);
            if (i % 4 == 0) {
                grabTargetBean.setIsEnable("可抢");
            } else {
                grabTargetBean.setIsEnable("不可抢");
            }
            mDatas.add(grabTargetBean);
        }
        mDatas.addAll(mDatas);
        mAdapter.notifyDataSetChanged();
        layoutSwipeRefresh.setRefreshing(false);
        RecyclerViewStateUtils.setFooterViewState(listJuzi, RecyclerViewLoadingFooter.State.Normal);
        flag++;
    }


    private void loadMore() {
        if (flag == 5) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), listJuzi, mHasMore, RecyclerViewLoadingFooter.State.TheEnd, null);
        } else {
            List<GrabTargetBean> data = new ArrayList<>();
            // 刷新
            for (int i = 0; i < 5; i++) {
                GrabTargetBean grabTargetBean = new GrabTargetBean();
                grabTargetBean.setName("第[2]组," + i);
                grabTargetBean.setPhone("1587755555" + i);
                if (i % 4 == 0) {
                    grabTargetBean.setIsEnable("可抢");
                } else {
                    grabTargetBean.setIsEnable("不可抢");
                }
                data.add(grabTargetBean);
            }
            mDatas.addAll(data);
            mAdapter.notifyDataSetChanged();
            layoutSwipeRefresh.setRefreshing(false);
            flag++;
            RecyclerViewStateUtils.setFooterViewState(listJuzi, RecyclerViewLoadingFooter.State.Normal);
        }
        // 加载更多
//        if (flag == 3) {
//            mHasMore = false;
//        }
    }


}
