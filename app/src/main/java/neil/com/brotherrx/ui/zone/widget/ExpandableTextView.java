package neil.com.brotherrx.ui.zone.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.DisplayUtil;

import neil.com.brotherrx.R;

/**
 * 可折叠显示说说信息
 * Created by neil on 2018/1/4 0004.
 */
public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    private static final int MAX_COLLAPSED_LINES = 5; // 默认最高行数

    private static final int DEFAULT_ANIM_DURATION = 200; //  默认动画执行时间

    protected TextView mTvContent; // 内容textview

    protected TextView mTvExpandCollapse; // 展开收起textView

    protected boolean mRelayout; // 是否有重新绘制

    protected boolean mCollapsed = true; // 默认收起

    private Drawable mExpandDrawable; // 展开图片

    private Drawable mCollapseDrawable;// 收起图片

    private int mAnimationDuration; // 动画执行时间

    private boolean mAnimating; // 是否正在执行动画

    private OnExpandStateChangeListener mListener; // 点击展开后的回调接口

    private SparseBooleanArray mCollapsedStatus; // listview等列表情况下保存每个item的收起/展开状态

    private int mPosition; // 列表位置

    private int mMaxCollapsedLines;// 设置内容最大行数,超过隐藏

    private int mCollapsedHeight; // 这个lineaylayout 容器的高度

    private int mTextHeightWithMaxlines; // 内容tv的真实高度

    private int mMarginBetweenTxtAndBottom; // 内容tvMatginTop 和 Bottom 的高度

    private int contentTextColor; // 内容的颜色

    private int collapseExpandTextColor; // 收起展开颜色

    private float contentTextSize; // 内容字体大小

    private float collapseExpandTextSize; // 收起展开字体大小

    private String textCollapse; // 收起文字
    private String textExpand; // 展开文字
    private boolean showExpandCollapseDrawable = true; // 是否显示图标

    private int grarity; // 收起展开位置，默认左边


    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    private void init(AttributeSet attrs) {
        mCollapsedStatus = new SparseBooleanArray();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        // 内容最大行数,超过折叠
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        // 动画执行时间
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        // 展开图片
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
        // 收起图片
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);
        // 收起文字
        textCollapse = typedArray.getString(R.styleable.ExpandableTextView_textCollapse);
        // 展开文字
        textExpand = typedArray.getString(R.styleable.ExpandableTextView_textExpand);
        // 是否显示图标
        showExpandCollapseDrawable = typedArray.getBoolean(R.styleable.ExpandableTextView_showExpandCollapseDrawable, true);

        if (showExpandCollapseDrawable) {
            if (mExpandDrawable == null) {
                // 展开图片
                mExpandDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_green_arrow_down);
            }
            if (mCollapseDrawable == null) {
                // 收起图片
                mCollapseDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_green_arrow_up);
            }
        }

        // 收起文字
        if (TextUtils.isEmpty(textCollapse)) {
            textCollapse = getContext().getString(R.string.shink);
        }
        // 展开文字
        if (TextUtils.isEmpty(textExpand)) {
            textExpand = getContext().getString(R.string.expand);
        }

        // 内容文字颜色
        contentTextColor = typedArray.getColor(R.styleable.ExpandableTextView_contentTextColor, ContextCompat.getColor(getContext(), R.color.light_gray));
        // 内容文字大小
        contentTextSize = typedArray.getDimension(R.styleable.ExpandableTextView_contentTextSize, DisplayUtil.sp2px(14));

        // 收起展开文字颜色
        collapseExpandTextColor = typedArray.getColor(R.styleable.ExpandableTextView_collapseExpandTextColor, ContextCompat.getColor(getContext(), R.color.main_color));
        // 收起展开文字大小
        collapseExpandTextSize = typedArray.getDimension(R.styleable.ExpandableTextView_collapseExpandTextSize, DisplayUtil.sp2px(14));

        // 收起展开位置，默认左边
        grarity = typedArray.getInt(R.styleable.ExpandableTextView_collapseExpandGrarity, Gravity.LEFT);

        typedArray.recycle();
        // 设置布局方向为垂直方向
        setOrientation(LinearLayout.VERTICAL);
        // 默认设置为隐藏
        setVisibility(GONE);
    }

    /**
     * 渲染完成时初始化view
     */
    @Override
    protected void onFinishInflate() {
        findViews();
    }

    private void findViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_expand_shink, this);
        mTvContent = (TextView) findViewById(R.id.expandable_text);
        mTvContent.setOnClickListener(this);
        mTvExpandCollapse = findViewById(R.id.expand_collapse);
        if (showExpandCollapseDrawable) {
            // 在 文字 上、下、左、右设置图标
            mTvExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(null, null, mCollapsed ? mExpandDrawable : mCollapseDrawable, null);
        }
        // 全文 | 收起
        mTvExpandCollapse.setText(mCollapsed ? getResources().getString(R.string.expand) : getResources().getString(R.string.shink));
        mTvExpandCollapse.setOnClickListener(this);

        mTvContent.setTextColor(contentTextColor);
        mTvContent.getPaint().setTextSize(contentTextSize);

        mTvExpandCollapse.setTextColor(collapseExpandTextColor);
        mTvExpandCollapse.getPaint().setTextSize(collapseExpandTextSize);

        // 设置收起展开位置 左或者右
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = grarity;
        mTvExpandCollapse.setLayoutParams(lp);
    }

    // 点击事件
    @Override
    public void onClick(View v) {
        if (mTvExpandCollapse.getVisibility() != View.VISIBLE) {
            // 不可见时直接返回
            return;
        }
        mCollapsed = !mCollapsed;
        // 修改收起/展开 图标及文字显示
        if (showExpandCollapseDrawable) {
            mTvExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(null, null, mCollapsed ? mExpandDrawable : mCollapseDrawable, null);
        }
        // 收起?全文:收起
        mTvExpandCollapse.setText(mCollapsed ? getResources().getString(R.string.expand) : getResources().getString(R.string.shink));
        // 保存位置状态
        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }
        // 执行展示/收起 动画
        mAnimating = true;
        ValueAnimator valueAnimator;
        if (mCollapsed) {
            // 展开文字
            // mTvContent.setMaxLines(mMaxCollapsedLines);
            valueAnimator = new ValueAnimator().ofInt(getHeight(), mCollapsedHeight);
        } else {
            // 收起文字
            valueAnimator = new ValueAnimator().ofInt(getHeight(),
                    getHeight() + mTextHeightWithMaxlines - mTvContent.getHeight());
        }

        // 操作属性,用到属性动画 监听
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                mTvContent.setMaxHeight(animatedValue - mMarginBetweenTxtAndBottom);
                getLayoutParams().height = animatedValue; // MATCH_PARENT 或 WRAP_CONTENT --疑问?
                requestLayout();
            }
        });

        // 动画监听
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后发送结束的信号
                // clear the animation flag
                mAnimating = false;
                // 通知状态回调
                if (mListener != null) {
                    mListener.onExpandStateChanged(mTvContent, !mCollapsed);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.setDuration(mAnimationDuration);
        valueAnimator.start();
    }

    /**
     * 当动画还在执行时,拦截点击事件,不让child处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mAnimating;
    }


    /**
     * 重新测量内容的高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 当没有发生变化时,调用测量并返回 If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        mTvExpandCollapse.setVisibility(View.GONE);
        mTvContent.setMaxLines(Integer.MAX_VALUE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); // 测量高度

        // 如果内容真实行数小于等于最大行数,不处理
        if (mTvContent.getLineCount() <= mMaxCollapsedLines) {
            return;
        }
        // 获取内容tv 真实高度
        mTextHeightWithMaxlines = getRealTextViewHeight(mTvContent);

        // 如果是收起状态,重新设置最大行数
        if (mCollapsed) {
            mTvContent.setMaxLines(mMaxCollapsedLines);
        }
        mTvExpandCollapse.setVisibility(View.VISIBLE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); // 重新测量高度

        if (mCollapsed) {
            mTvContent.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - mTvContent.getHeight();
                }
            });
            // 保存这个容器的测量高度
            mCollapsedHeight = getMeasuredHeight();
        }
    }

    /**
     * 设置内容
     *
     * @param text
     */
    public void setText(CharSequence text) {
        mRelayout = true;
        mTvContent.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置内容,列表情况下,带有保存位置收起/展开状态
     *
     * @param text
     * @param position
     */
    public void setText(CharSequence text, int position) {
        mPosition = position;
        // 获取状态, 如无,默认是true:收起
        mCollapsed = mCollapsedStatus.get(position, true);
        clearAnimation();
        // 设置收起/展开图标和文字
        if (showExpandCollapseDrawable) {
            mTvExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(null, null, mCollapsed ? mExpandDrawable : mCollapseDrawable, null);
        }
        mTvExpandCollapse.setText(mCollapsed ? getResources().getString(R.string.expand) : getResources().getString(R.string.shink));

        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    // 获取内容
    public CharSequence getText() {
        if (mTvContent == null) {
            return "";
        }
        return mTvContent.getText();
    }

    /**
     * 获取内容tv 真实高度(含padding)
     *
     * @param textView
     * @return
     */
    private static int getRealTextViewHeight(TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }


    public void setOnExpandStateChangeListener(OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    /**
     * 定义状态改变回调接口
     */
    public interface OnExpandStateChangeListener {

        /**
         * textview展开后的回调
         *
         * @param textView
         * @param isExpnded 是否已经展开
         */
        void onExpandStateChanged(TextView textView, boolean isExpnded);
    }

}
