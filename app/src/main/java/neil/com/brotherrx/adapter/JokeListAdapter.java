package neil.com.brotherrx.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.JokeBean;
import neil.com.brotherrx.widget.CircleImageView;

/**
 * 笑话列表
 */
public class JokeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;

    private Fragment mContext;

    private List<JokeBean> mDatas;

    private View.OnClickListener onItemClick;

    private View.OnLongClickListener onLongClickListener;

    private int screenWidth;

    public JokeListAdapter(Fragment context, List<JokeBean> datas, View.OnClickListener onItemClick, View.OnLongClickListener onLongClickListener) {

        this.mContext = context;

        this.mDatas = datas;

        this.onItemClick = onItemClick;

        this.onLongClickListener = onLongClickListener;

        mInflater = LayoutInflater.from(context.getActivity());

        DisplayMetrics metric = new DisplayMetrics();
        context.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.list_item_joke, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        JokeBean jokeBean = mDatas.get(position);


        if (jokeBean != null) {

            LogUtils.e(jokeBean.toString());

            Glide.with(mContext)
                    .load(jokeBean.getUserAvatar())
                    .asBitmap()
                    .centerCrop()
                    .into(viewHolder.imgUser);

            viewHolder.textUserName.setText(jokeBean.getUserName());
            viewHolder.textLastTime.setText(jokeBean.getLastTime());
            viewHolder.textTitle.setText(jokeBean.getTitle());

            JokeBean.DataBean dataBean = jokeBean.getDataBean();
            if (dataBean != null) {
                if (TextUtils.isEmpty(dataBean.getContent())) {

                    viewHolder.textContent.setVisibility(View.GONE);
                    viewHolder.imgJoke.setVisibility(View.VISIBLE);
                    viewHolder.textTitle.setVisibility(View.VISIBLE);

                    double width = Double.parseDouble(dataBean.getWidth());
                    double height = Double.parseDouble(dataBean.getHeight());
                    ViewGroup.LayoutParams lp = viewHolder.imgJoke.getLayoutParams();
                    lp.width = (int) (screenWidth * 0.8);
                    lp.height = (int) (screenWidth * 0.8 * height / width);
                    viewHolder.imgJoke.setLayoutParams(lp);

                    String url = dataBean.getShowImg();
                    String gifUrl = dataBean.getGifsrcImg();

                    if (TextUtils.isEmpty(gifUrl)) {
                        Glide.with(mContext).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.imgJoke);
                    } else {
                        Glide.with(mContext).load(gifUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.imgJoke);
                    }

                } else {
                    viewHolder.textContent.setVisibility(View.VISIBLE);
                    viewHolder.imgJoke.setVisibility(View.GONE);
                    viewHolder.textTitle.setVisibility(View.GONE);

                    viewHolder.textContent.setText(dataBean.getContent());
                }
            }

            List<String> tags = jokeBean.getTags();
            if (tags != null) {

                int size = tags.size();
                if (size == 0) {
                    updateTags(viewHolder, View.GONE, View.GONE, View.GONE, View.GONE);
                } else if (size == 1) {
                    viewHolder.textTag1.setText(tags.get(0));
                    updateTags(viewHolder, View.VISIBLE, View.GONE, View.GONE, View.GONE);
                } else if (size == 2) {
                    viewHolder.textTag1.setText(tags.get(0));
                    viewHolder.textTag2.setText(tags.get(1));
                    updateTags(viewHolder, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
                } else if (size == 3) {
                    viewHolder.textTag1.setText(tags.get(0));
                    viewHolder.textTag2.setText(tags.get(1));
                    viewHolder.textTag3.setText(tags.get(2));
                    updateTags(viewHolder, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE);
                } else {
                    viewHolder.textTag1.setText(tags.get(0));
                    viewHolder.textTag2.setText(tags.get(1));
                    viewHolder.textTag3.setText(tags.get(2));
                    viewHolder.textTag4.setText(tags.get(3));
                    updateTags(viewHolder, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                }
                viewHolder.layoutTags.setVisibility(View.VISIBLE);
            } else {
                updateTags(viewHolder, View.GONE, View.GONE, View.GONE, View.GONE);
                viewHolder.layoutTags.setVisibility(View.GONE);
            }

            viewHolder.imgJoke.setTag(R.string.app_name, position);
            viewHolder.imgJoke.setOnClickListener(onItemClick);

            viewHolder.textContent.setTag(R.string.app_name, position);
            viewHolder.textContent.setOnLongClickListener(onLongClickListener);
        }
    }

    private void updateTags(ViewHolder viewHolder, int v1, int v2, int v3, int v4) {
        viewHolder.textTag1.setVisibility(v1);
        viewHolder.textTag2.setVisibility(v2);
        viewHolder.textTag3.setVisibility(v3);
        viewHolder.textTag4.setVisibility(v4);
    }

    @Override
    public int getItemCount() {

        return mDatas != null ? mDatas.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgJoke)
        public ImageView imgJoke;

        @BindView(R.id.textContent)
        public TextView textContent;

        @BindView(R.id.layoutTags)
        public LinearLayout layoutTags;

        @BindView(R.id.textTitle)
        public TextView textTitle;

        @BindView(R.id.textTag1)
        public TextView textTag1;

        @BindView(R.id.textTag2)
        public TextView textTag2;

        @BindView(R.id.textTag3)
        public TextView textTag3;

        @BindView(R.id.textTag4)
        public TextView textTag4;

        @BindView(R.id.imgUser)
        public CircleImageView imgUser;

        @BindView(R.id.textUserName)
        public TextView textUserName;

        @BindView(R.id.textLastTime)
        public TextView textLastTime;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
