package neil.com.brotherrx.ui.lifecycle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lnyp.flexibledivider.HorizontalDividerItemDecoration;
import com.lnyp.recyclerview.EndlessRecyclerOnScrollListener;
import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lnyp.recyclerview.RecyclerViewLoadingFooter;
import com.lnyp.recyclerview.RecyclerViewStateUtils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.adapter.JokeListAdapter;
import neil.com.brotherrx.entity.JokeBean;
import neil.com.brotherrx.http.HttpUtils;
import neil.com.brotherrx.http.JokeUtil;
import neil.com.brotherrx.widget.CustomerDialog;

/**
 * 主页面
 */
public class MainFragment extends Fragment {

    @BindView(R.id.layoutSwipeRefresh)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.listJuzi)
    public RecyclerView listInspirations;

    private Unbinder unbinder;
    private HeaderAndFooterRecyclerViewAdapter mAdapter;
    private List<JokeBean> mDatas;

    private ClipboardManager clipboardManager;

    private int page = 1;
    private boolean mHasMore = false;
    private boolean isRefresh = true;

    public static final String URL_SUFFIX = ".html";

    // 最新笑话
    public static final String PENGFU_NEW_JOKES = "https://m.pengfu.com/index_";

    // 处理请求返回信息
    private MyHandler mHandler = new MyHandler();

    private class MyHandler extends Handler {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    RecyclerViewStateUtils.setFooterViewState(listInspirations, RecyclerViewLoadingFooter.State.Normal);
                    swipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();

                    break;
                case 1:
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), listInspirations, mHasMore, RecyclerViewLoadingFooter.State.NetWorkError, mFooterClick);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        initView();
        refreshReq();
        return view;
    }

    private void initView() {
        mDatas = new ArrayList<>();

        JokeListAdapter jokeListAdapter = new JokeListAdapter(this, mDatas, onClickListener, onLongClickListener);
        mAdapter = new HeaderAndFooterRecyclerViewAdapter(jokeListAdapter);
//        TextView textView = new TextView(getActivity());
//        textView.setText("1213213123123");
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), BeautyWordIndexActivity.class);
//                startActivity(intent);
//            }
//        });
//        mAdapter.addHeaderView(textView);
        listInspirations.setAdapter(mAdapter);
        listInspirations.setLayoutManager(new LinearLayoutManager(getActivity()));
        listInspirations.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .colorResId(R.color.divider_color)
                        .size(20)
                        .build());

        listInspirations.addOnScrollListener(mOnScrollListener);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

    }




    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            RecyclerViewLoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(listInspirations);
            if (state == RecyclerViewLoadingFooter.State.Loading) {
                return;
            }
            if (mHasMore) {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), listInspirations, mHasMore, RecyclerViewLoadingFooter.State.Loading, null);
                qryJokes();
            } else {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), listInspirations, mHasMore, RecyclerViewLoadingFooter.State.TheEnd, null);
            }
        }
    };

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), listInspirations, mHasMore, RecyclerViewLoadingFooter.State.Loading, null);
            qryJokes();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshReq();
        }
    };



    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("lifecycle","MainFragment----onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("lifecycle","MainFragment----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("lifecycle","MainFragment----onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void refreshReq() {
        isRefresh = true;
        page = 1;
        qryJokes();
    }

    private void qryJokes() {
        final String url = PENGFU_NEW_JOKES + page + URL_SUFFIX;
        System.out.println(url);
        HttpUtils.doGetAsyn(url, new HttpUtils.CallBack() {

            @Override
            public void onRequestComplete(String result) {
                if (result == null) {
                    mHandler.sendEmptyMessage(1);
                    return;
                }
                Document doc = Jsoup.parse(result);
                if (doc != null) {
                    JokeUtil jokeUtil = new JokeUtil();
                    List<JokeBean> jokeBeens = jokeUtil.getNewJokelist(doc);
                    if (jokeBeens != null) {
                        page++;
                        mHasMore = true;
                        if (isRefresh) {
                            mDatas.clear();
                            isRefresh = false;
                        }
                        mDatas.addAll(jokeBeens);
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                int pos = (int) view.getTag(R.string.app_name);
                LogUtils.e("MainFragment我是第" + pos + "个");
//                JokeBean jokeBean = mDatas.get(pos);
//                String showImg = jokeBean.getDataBean().getShowImg();
//                String gifSrcImg = jokeBean.getDataBean().getGifsrcImg();
//                Intent intent = new Intent(getActivity(), PhotoActivity.class);
//                intent.putExtra("showImg", showImg);
//                intent.putExtra("gifSrcImg", gifSrcImg);
//                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            int pos = (int) view.getTag(R.string.app_name);
            JokeBean jokeBean = mDatas.get(pos);
            String content = jokeBean.getDataBean().getContent();
            ClipData clip = ClipData.newPlainText("content", content);
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "已复制", Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
