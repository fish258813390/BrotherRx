package neil.com.brotherrx.ui.zone.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import neil.com.baseretrofitrx.base.BaseApplication;
import neil.com.brotherrx.R;

/**
 * 文字监听(点赞名字点击监听)
 *
 * Created by neil on 2018/1/4 0004.
 */
public class NameClickable extends ClickableSpan implements View.OnClickListener {

    private final ISpanClick mListener;
    private int mPositon;

    public NameClickable(ISpanClick mListener,int mPositon) {
        this.mListener = mListener;
        this.mPositon = mPositon;
    }


    @Override
    public void onClick(View widget) {
        mListener.onClick(mPositon);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        int colorValue  = BaseApplication.getContext().getResources().getColor(R.color.main_color);
        ds.setColor(colorValue);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
