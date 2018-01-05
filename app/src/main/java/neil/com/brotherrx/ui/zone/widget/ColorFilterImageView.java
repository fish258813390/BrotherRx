package neil.com.brotherrx.ui.zone.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * @author neil
 * @date 2018/1/5
 */

public class ColorFilterImageView extends ImageView implements View.OnTouchListener {

    public ColorFilterImageView(Context context) {
        this(context, null, 0);
    }

    public ColorFilterImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorFilterImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    // 按下时图像变灰,手指离开或取消操作时恢复原色
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下时图像变灰
                setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY); //  PorterDuff.Mode.MULTIPLY 取两图层交集部分叠加后颜色
                break;

            case MotionEvent.ACTION_UP: // 手指离开或取消操作时恢复原色
            case MotionEvent.ACTION_CANCEL:
                setColorFilter(Color.TRANSPARENT);
                break;

            default:
                break;
        }
        return false;
    }
}
