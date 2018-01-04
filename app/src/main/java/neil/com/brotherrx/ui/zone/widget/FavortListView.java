package neil.com.brotherrx.ui.zone.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import neil.com.brotherrx.ui.zone.adapter.FavortListAdapter;
import neil.com.brotherrx.ui.zone.spannable.ISpanClick;

/**
 * Created by neil on 2018/1/4 0004.
 */

public class FavortListView extends TextView {

    private ISpanClick mSpanClickListener;

    public void setSpanClickListener(ISpanClick listener) {
        this.mSpanClickListener = listener;
    }

    public ISpanClick getSpanClickListener(){
        return  mSpanClickListener;
    }


    public FavortListView(Context context) {
        super(context);
    }

    public FavortListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FavortListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(FavortListAdapter adapter){
        adapter.bindListView(this);
    }
}
