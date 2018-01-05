package neil.com.brotherrx.ui.zone.spannable;

import android.text.SpannableString;

import com.neil.common.utils.ToastUtils;

import neil.com.baseretrofitrx.base.BaseApplication;

/**
 * 点赞区和评论区 点击人名(用户名) 事件回调
 * @author neil
 * @date 2018/1/5
 */
public class NameClickListener implements ISpanClick {

    private SpannableString userName;
    private String userId;

    public NameClickListener(SpannableString userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    @Override
    public void onClick(int postion) {
        ToastUtils.showShort(BaseApplication.getContext(), "点击了:" + postion);
    }
}
