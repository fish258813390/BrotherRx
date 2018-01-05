package neil.com.brotherrx.ui.zone.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 可折叠显示说说信息
 * Created by neil on 2018/1/4 0004.
 */
public class ExpandableTextView extends LinearLayout {

    private static final int MAX_COLLAPSED_LINES = 5; // 默认最高行数

    private static final int DEFAULT_ANIM_DURATION = 200; //  默认动画执行时间

    protected TextView mTvContent; // 内容textview

    protected TextView mTvExpandCollapse; // 展开收起textView

    protected boolean mRelayout; // 是否有重新绘制




    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
