package neil.com.brotherrx.ui.zone.spannable;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.BaseMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import neil.com.baseretrofitrx.base.BaseApplication;
import neil.com.brotherrx.R;

/**
 * 评论信息
 *
 * @author neil
 * @date 2018/1/5
 */
public class CircleMovementMethod extends BaseMovementMethod {

    public final String TAG = CircleMovementMethod.class.getSimpleName();

    public final static int DEFAULT_COLOR = R.color.transparent;
    private int mTextViewBgColorId; // 内容背景颜色
    private int mClickableSpanBgClorId; // 可点击区域的背景颜色

    private BackgroundColorSpan mBgSpan;
    private ClickableSpan[] mClickLinks; // 可点击的区域文字
    private boolean isPassToTv = true; //true:响应textview的点击事件,false:响应设置的clickableSpan事件

    public boolean isPassToTv() {
        return isPassToTv;
    }

    public void setPassToTv(boolean isPassToTv) {
        this.isPassToTv = isPassToTv;
    }

    public CircleMovementMethod(int clickableSpanBgClorId) {
        mClickableSpanBgClorId = clickableSpanBgClorId;
        mTextViewBgColorId = DEFAULT_COLOR;
    }

    public CircleMovementMethod(int clickableSpanBgClorId, int textViewBgColorId) {
        mClickableSpanBgClorId = clickableSpanBgClorId;
        mTextViewBgColorId = textViewBgColorId;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            mClickLinks = buffer.getSpans(off, off, ClickableSpan.class);
            if (mClickLinks.length > 0) {
                // 点击的是Span区域,不要把点击事件传递
                setPassToTv(false);
                Selection.setSelection(buffer,
                        buffer.getSpanStart(mClickLinks[0]),
                        buffer.getSpanEnd(mClickLinks[1]));
                // 设置点击区域的背景色
                mBgSpan = new BackgroundColorSpan(
                        BaseApplication.getContext().getResources().getColor(mClickableSpanBgClorId));
                buffer.setSpan(mBgSpan,
                        buffer.getSpanStart(mClickLinks[0]),
                        buffer.getSpanEnd(mClickLinks[0]),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                setPassToTv(true);
                widget.setBackgroundResource(mTextViewBgColorId);
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (mClickLinks.length > 0) {
                mClickLinks[0].onClick(widget);
                if (mBgSpan != null) {
                    buffer.removeSpan(mBgSpan);
                }
            } else {
                if (mBgSpan != null) {
                    buffer.removeSpan(mBgSpan);
                }
            }
            Selection.removeSelection(buffer);
            widget.setBackgroundResource(DEFAULT_COLOR);
        } else if (action == MotionEvent.ACTION_MOVE) {

        } else {
            if (mBgSpan != null) {
                buffer.removeSpan(mBgSpan);
            }
            widget.setBackgroundResource(DEFAULT_COLOR);
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}
