package neil.com.brotherrx.widget.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * 自定义 RecyclerView
 * 支持 listview、gridview 自动加载(可以手动控制) 参照RecyclerViewActivityTest2
 * created by fish 2017-03-05
 */
public class NRecyclerView extends RecyclerView {

    private Context mContext;

    private LoadMoreListener loadMoreListener; // 加载更多 回调监听

    private boolean canloadMore = true;  // 是否可加载更多

    private Adapter mAdapter; // 适配器

    private Adapter mFooterAdapter; // footer适配器

    private boolean isLoadingData = false; // 是否在加载数据

    private LoadingMoreFooter footView;  // footView 加载更多布局

    private int currentScrollState = 0; // 当前滑动的状态

    private int lastVisibleItemPosition; // 最后一个可见的item的位置

    private boolean isNoMore = false; // 是否没有更多数据,滑到到底部是否自动加载; false自动加载,true则加载完成

    protected LayoutManagerType layoutManagerType; // 当前RecyclerView类型

    private int[] lastPositions; // 最后一个的位置

    private static final int HIDE_THRESHOLD = 20; // 触发在上下滑动监听器的容差距离

    private int mDistance = 0; // 滑动的距离

    private boolean mIsScrollDown = true; // 是否需要监听控制

    private int mScrolledYDistance = 0; // Y轴移动的实际距离（最顶部为0）

    private int mScrolledXDistance = 0; // X轴移动的实际距离（最左侧为0）

    private float mLastY = -1;

    private static final float DRAG_RATE = 2.2f;

    private View mEmptyView;

    private LScrollListener mLScrollListener;

    public NRecyclerView(Context context) {
        this(context, null);
        init(context);
    }

    public NRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public NRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        footView = new LoadingMoreFooter(mContext);
        addFootView(footView);
        footView.setGone();
    }

    public interface LScrollListener {

        void onScrollUp();//scroll down to up

        void onScrollDown();//scroll from up to down

        void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance

        void onScrollStateChanged(int state);
    }


    // 获得FootAdapter适配器
    public NFooterAdapter getFooterAdapter(Context context) {
        return (NFooterAdapter) mFooterAdapter;
    }

    public LoadingMoreFooter getLoadingMoreFooter() {
        return footView;
    }

    /**
     * 点击监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        if (mFooterAdapter != null && mFooterAdapter instanceof NFooterAdapter) {
            ((NFooterAdapter) mFooterAdapter).setOnItemClickListener(onItemClickListener);
        }
    }

    /**
     * 长按监听
     *
     * @param listener
     */
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        if (mFooterAdapter != null && mFooterAdapter instanceof NFooterAdapter) {
            ((NFooterAdapter) mFooterAdapter).setOnItemLongClickListener(listener);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mFooterAdapter = new NFooterAdapter(this, footView, adapter);
        super.setAdapter(mFooterAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                break;
            default:
                mLastY = -1; // reset
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        int firstVisibleItemPosition = 0;
        LayoutManager layoutManager = getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LinearLayout:
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GridLayout:
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case StaggeredGridLayout: //瀑布流
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(lastPositions);
                firstVisibleItemPosition = findMax(lastPositions);
                break;
        }

        // 根据类型来计算出第一个可见的item的位置，由此判断是否触发到底部的监听器
        // 计算并判断当前是向上滑动还是向下滑动
        calculateScrollUpOrDown(firstVisibleItemPosition, dy);
        // 移动距离超过一定的范围，我们监听就没有啥实际的意义了
        mScrolledXDistance += dx;
        mScrolledYDistance += dy;
        mScrolledXDistance = (mScrolledXDistance < 0) ? 0 : mScrolledXDistance;
        mScrolledYDistance = (mScrolledYDistance < 0) ? 0 : mScrolledYDistance;
        if (mIsScrollDown && (dy == 0)) {
            mScrolledYDistance = 0;
        }
        //Be careful in here
        if (null != mLScrollListener) {
            mLScrollListener.onScrolled(mScrolledXDistance, mScrolledYDistance);
        }
    }

    /**
     * 计算当前是向上滑动还是向下滑动
     */
    private void calculateScrollUpOrDown(int firstVisibleItemPosition, int dy) {
        if (null != mLScrollListener) {
            if (firstVisibleItemPosition == 0) {
                if (!mIsScrollDown) {
                    mIsScrollDown = true;
                    mLScrollListener.onScrollDown();
                }
            } else {
                if (mDistance > HIDE_THRESHOLD && mIsScrollDown) {
                    mIsScrollDown = false;
                    mLScrollListener.onScrollUp();
                    mDistance = 0;
                } else if (mDistance < -HIDE_THRESHOLD && !mIsScrollDown) {
                    mIsScrollDown = true;
                    mLScrollListener.onScrollDown();
                    mDistance = 0;
                }
            }
        }

        if ((mIsScrollDown && dy > 0) || (!mIsScrollDown && dy < 0)) {
            mDistance += dy;
        }
    }


    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        currentScrollState = state;

        if (state == RecyclerView.SCROLL_STATE_IDLE && loadMoreListener != null && !isLoadingData && canloadMore) {
            LayoutManager layoutManager = getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            if (visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 1 && totalItemCount > visibleItemCount && !isNoMore) {
                if (footView != null) {
                    footView.setVisible();
                }
//                isLoadingData = true; // 可以来主动设置是否加载
                loadMoreListener.onLoadMore();
            }

        }
    }

    //取到最后的一个节点
    private int last(int[] lastPositions) {
        int last = lastPositions[0];
        for (int value : lastPositions) {
            if (value > last) {
                last = value;
            }
        }
        return last;
    }

    /**
     * 底部加载更多视图
     *
     * @param view
     */
    public void addFootView(LoadingMoreFooter view) {
        footView = view;
    }

    /**
     * 设置底部加载中效果
     *
     * @param view (自定义view)
     */
    public void setFootLoadingView(View view) {
        if (footView != null) {
            footView.addFootLoadingView(view);
        }
    }

    /**
     * 设置底部加载中
     */
    public void setFootLoadingView() {
        if (footView != null) {
            footView.addFootLoadingView();
        }
    }

    /**
     * 设置底部到底了布局
     *
     * @param view 自定义view
     */
    public void setFootEndView(View view) {
        if (footView != null) {
            footView.addFootEndView(view);
        }
    }

    /**
     * 下拉刷新后初始化底部状态
     */
    public void refreshComplete() {
        if (footView != null) {
            footView.setGone();
        }
        isLoadingData = false;
    }

    /**
     * 加载数据完成
     */
    public void loadMoreComplete() {
        if (footView != null) {
            footView.setGone();
        }
        isLoadingData = false;
    }

    /**
     * 到底了
     */
    public void loadMoreEnd() {
        if (footView != null) {
            footView.setEnd();
        }
    }

    /**
     * 到底了还可以加载 2017.3.4 (如果分页的size过小或者不足时，可以再根据是否是底部footer进行点击操作)
     */
    public void canLoadMore() {
        if (footView != null) {
            footView.setLoadMore();
        }
    }

    /**
     * 设置是否可加载更多
     */
    public void setCanloadMore(boolean flag) {
        canloadMore = flag;
    }

    public boolean getCanLoadMore() {
        return canloadMore;
    }

    /**
     * 设定是否全部加载完成，如果设未true，则滑到底部不自动加载
     */
    public void setIsNoMore(boolean hasMore) {
        isNoMore = hasMore;
    }

    public boolean getIsNoMore() {
        return isNoMore;
    }


    /**
     * 设置加载更多监听
     *
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        loadMoreListener = listener;
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mFooterAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mFooterAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };


}
