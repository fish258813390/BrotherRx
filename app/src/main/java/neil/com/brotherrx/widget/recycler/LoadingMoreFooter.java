package neil.com.brotherrx.widget.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import neil.com.brotherrx.R;


/**
 * recyclerView Footer 加载更多
 */
public class LoadingMoreFooter extends LinearLayout {

    private Context context;
    private LinearLayout loading_view_layout; // 加载更多布局
    private LinearLayout load_more_layout; //  loadingMore
    private LinearLayout end_layout; //  没有数据可以加载
    private ProgressBar progressBar;
    private TextView textView;


    public LoadingMoreFooter(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        setGravity(Gravity.CENTER); // 设置居中
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // 设置布局参数
        LayoutInflater layoutInflater = LayoutInflater.from(context); // 初始化布局填填充器
        View view = layoutInflater.inflate(R.layout.recycler_view_footer_loading, null); // 给当前布局 实例化 footer_layout布局 (当前布局 = footer_layout布局)
        loading_view_layout = (LinearLayout) view.findViewById(R.id.loading_view_layout); // 底部布局
        progressBar = (ProgressBar) loading_view_layout.findViewById(R.id.progressBar); // 进度条
        textView = (TextView) loading_view_layout.findViewById(R.id.textView); // 文字显示

        load_more_layout = (LinearLayout) view.findViewById(R.id.load_more_layout); // can loading_more
        TextView tvLoadMore = new TextView(context);
        tvLoadMore.setText("加载更多~");
        addFootLoadMoreView(tvLoadMore);

        end_layout = (LinearLayout) view.findViewById(R.id.end_layout); // 没有数据可以加载 (考虑到实际情况可能有:网络出错,数据获取失败...相关问题,所以自己定义并添加)

//        addFootLoadingView(new ProgressBar(context, null, android.R.attr.progressBarStyle));
        addFootLoadingView(); // 修改后

        TextView textView = new TextView(context);
        textView.setText("已经到底啦~");
        addFootEndView(textView); //  设置底部到底了布局

        addView(view); // 添加子布局
    }

    /**
     * 设置底部加载中效果
     */
    public void addFootLoadingView() {
        setLoadingVisible();
    }


    /**
     * 设置底部到底了布局
     */
    public void addFootEndView(View view) {
        end_layout.removeAllViews();
        end_layout.addView(view);
    }

    /**
     * 数据已经加载完成--- 已经没有更多数据
     */
    public void setEnd() {
        setVisibility(VISIBLE);
        loading_view_layout.setVisibility(GONE);
        load_more_layout.setVisibility(GONE);
        end_layout.setVisibility(VISIBLE);
    }

    /**
     * 加载中--- 显示正在加载
     */
    public void setLoadingVisible() {
        setVisibility(VISIBLE);
        loading_view_layout.setVisibility(VISIBLE);
        end_layout.setVisibility(GONE);
        load_more_layout.setVisibility(GONE);
    }


    public void setGone() {
        setVisibility(GONE);
    }

    /**
     * 第一页数据不足时,手动加载更多
     */
    public void addFootLoadMoreView(View view) {
        load_more_layout.removeAllViews();
        load_more_layout.addView(view);
    }

    /**
     * 当限定分页条数,主动触发加载更多时调用
     */
    public void setLoadMoreByClick() {
        setVisibility(VISIBLE);
        load_more_layout.setVisibility(VISIBLE);
        end_layout.setVisibility(GONE);
        loading_view_layout.setVisibility(GONE);
    }

}
