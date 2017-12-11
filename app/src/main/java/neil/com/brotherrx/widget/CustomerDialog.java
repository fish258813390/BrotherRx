package neil.com.brotherrx.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import neil.com.brotherrx.R;


public class CustomerDialog extends Dialog {
    private Window window;
    private CustomerDialog instance;

    public CustomerDialog(Context context) {
        super(context, R.style.transparentFrameWindowStyle);
        instance = this;
        window = getWindow();
        setCanceledOnTouchOutside(false);
    }

    /**
     * 上传图片
     *
     * @param listener
     * @return
     */
    public CustomerDialog userPicDialog(View.OnClickListener listener) {
        setContentView(R.layout.dialog_adavater_layout);
        TextView tvPicture = findViewById(R.id.tv_picture);
        TextView tvAlbum = findViewById(R.id.tv_album);
        TextView cancel = findViewById(R.id.cancel);
        window.setGravity(Gravity.BOTTOM);
        // 设置显示动画
        // 设置点击外围解散
        CustomerDialog.this.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(listener);
        tvPicture.setOnClickListener(listener);
        tvAlbum.setOnClickListener(listener);
        return instance;
    }


}
