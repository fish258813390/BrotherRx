package neil.com.brotherrx.ui.zone.adapter;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import java.util.List;

import neil.com.baseretrofitrx.base.BaseApplication;
import neil.com.brotherrx.R;
import neil.com.brotherrx.ui.zone.bean.FavortItem;
import neil.com.brotherrx.ui.zone.spannable.CircleMovementMethod;
import neil.com.brotherrx.ui.zone.spannable.NameClickable;
import neil.com.brotherrx.ui.zone.widget.FavortListView;

/**
 * 点赞适配器
 * Created by neil on 2018/1/4 0004.
 */
public class FavortListAdapter {

    private FavortListView mListView; // 点赞listview
    private List<FavortItem> datas; // 点赞数据

    public List<FavortItem> getDatas() {
        return datas;
    }

    public void setDatas(List<FavortItem> datas){
        this.datas = datas;
    }

    public void bindListView(FavortListView listView) {
        if (listView == null) {
            throw new IllegalArgumentException("FavortListView is null ....");
        }
        this.mListView = listView;
    }

    // 获得点赞数
    public int getCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    // 获取某个item
    public Object getItem(int positon) {
        if (datas != null && datas.size() > positon) {
            return datas.get(positon);
        }
        return null;
    }

    // 获取单个position
    public long getItemId(int position) {
        return position;
    }

    // 刷新点赞列表 点赞之间以"," 隔开
    public void notifyDataSetChanged() {
        if (mListView == null) {
            throw new NullPointerException("listview is null, please bindListView first...");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (datas != null && datas.size() > 0) {
            // 添加点赞图标,并且名字之间用,隔开
            builder.append(setImageSpan());
            FavortItem item = null;
            for (int i = 0; i < datas.size(); i++) {
                item = datas.get(i);
                if (null != item) {
                    builder.append(setClickableSpan(item.getUserNickname(), i));
                    if (i != datas.size() - 1) {
                        builder.append(", ");
                    }
                }
            }
        }
        mListView.setText(builder);
        mListView.setMovementMethod(new CircleMovementMethod(R.color.circle_name_selector_color));
    }


    // 设置点赞图片 显示在第一个字符(排头)
    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(BaseApplication.getContext(), R.drawable.dianzansmal, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }

    // 设置名字显示及名字监听
    private SpannableString setClickableSpan(String textStr, int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new NameClickable(mListView.getSpanClickListener(), position), 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

}
