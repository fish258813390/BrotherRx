package neil.com.brotherrx.ui.zone.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.zone.CommentItem;
import neil.com.brotherrx.ui.zone.spannable.CircleMovementMethod;
import neil.com.brotherrx.ui.zone.spannable.NameClickListener;
import neil.com.brotherrx.ui.zone.spannable.NameClickable;
import neil.com.brotherrx.ui.zone.widget.CommentListView;

/**
 * 评论区适配器
 *
 * @author neil
 * @date 2018/1/5
 */
public class CommentAdapter {

    private Context mContext;
    private CommentListView mListView;
    private List<CommentItem> mDatas; // 评论数据

    public CommentAdapter(Context mContext, List<CommentItem> mDatas) {
        this.mContext = mContext;
        setDatas(mDatas);
    }

    public void bindListView(CommentListView listView) {
        if (listView == null) {
            throw new IllegalArgumentException("CommentListView is null....");
        }
        mListView = listView;
    }

    public void setDatas(List<CommentItem> datas) {
        if (datas == null) {
            datas = new ArrayList<CommentItem>();
        }
        mDatas = datas;
    }

    public List<CommentItem> getDatas() {
        return mDatas;
    }

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public CommentItem getItem(int position) {
        if (mDatas == null) {
            return null;
        }
        if (position < mDatas.size()) {
            return mDatas.get(position);
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public View getView(final int position) {
        LogUtils.d("CommentAdapter getView-----------------------" + position);
        View convertView = View.inflate(mContext, R.layout.item_social_comment, null);
        TextView commentTv = convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(R.color.circle_name_selector_color,
                R.color.circle_name_selector_color);
        final CommentItem bean = mDatas.get(position);
        String name = bean.getUserNickname();
        String id = bean.getId();
        String toReplyName = "";
        if (bean.getAppointUserid() != null) {
            toReplyName = bean.getAppointUserNickname();
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, bean.getUserId(), 0));
        if (!TextUtils.isEmpty(toReplyName)) {
            // 可以回复的信息
            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, bean.getAppointUserNickname(), 1));
        }
        builder.append(": ");

        // 转换表情字符
        String contentBodyStr = bean.getContent();
//        SpannableString contentSpanText = new SpannableString(contentBodyStr);
//        contentSpanText.setSpan(new UnderlineSpan(), 0, contentSpanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(contentBodyStr);
        commentTv.setText(builder);

        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    mListView.getOnItemClickListener().onItemClick(position);
                }
            }
        });

        commentTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    mListView.getOnItemLongClickListener().onItemLongClick(position);
                    return true;
                }
                return false;
            }
        });
        return convertView;
    }


    // 名字信息 监听
    private SpannableString setClickableSpan(String textStr, String userId, int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new NameClickable(new NameClickListener(subjectSpanText, userId), position),
                0,
                subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public void notifyDataSetChanged() {
        if (mListView == null) {
            throw new NullPointerException("listview is null, please bindListView first...");
        }
        mListView.removeAllViews();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            int index = i;
            View view = getView(index);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }
            mListView.addView(view, index, layoutParams);
        }

    }
}
