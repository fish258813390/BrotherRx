package neil.com.brotherrx.ui.zone.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaydenxiao.common.baseapp.AppCache;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.TimeUtil;

import java.util.List;

import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.entity.zone.CircleItem;
import neil.com.brotherrx.entity.zone.CommentItem;
import neil.com.brotherrx.entity.zone.FavortItem;
import neil.com.brotherrx.ui.zone.DatasUtil;
import neil.com.brotherrx.ui.zone.adapter.CircleAdapter;
import neil.com.brotherrx.ui.zone.adapter.CommentAdapter;
import neil.com.brotherrx.ui.zone.adapter.FavortListAdapter;
import neil.com.brotherrx.ui.zone.presenter.CircleZonePresenter;
import neil.com.brotherrx.ui.zone.widget.CommentListView;
import neil.com.brotherrx.ui.zone.widget.ExpandableTextView;
import neil.com.brotherrx.ui.zone.widget.FavortListView;
import neil.com.brotherrx.ui.zone.widget.MultiImageView;

/**
 * 动态说说item viewholder
 * Created by neil on 2018/1/4 0004.
 */
public class ZoneViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private int type;
    private View itemView;
    private CircleZonePresenter mPresenter;
    private CircleItem circleItem; // 说说bean
    private int position;

    public ImageView headIv;
    public TextView nameTv;
    public TextView urlTipTv;

    /**
     * 动态的内容
     */
    public ExpandableTextView contentTv;  // 说说内容
    public TextView timeTv; // 说说发布时间
    public TextView tvAddressOrDistance; //  说说发布位置(定位到的位置信息)
    public TextView deleteBtn; // 删除
    public TextView favortBtn; // 点赞
    public TextView snsBtn;
    public LinearLayout ll_comment; // 评论

    /**
     * 点赞列表
     */
    public FavortListView favortListTv;
    public LinearLayout urlBody;
    public LinearLayout digCommentBody;
    public View digLine;

    public CommentListView commentList; // 评论列表
    public ImageView urlImageIv; // 链接图片
    public TextView urlContentTv;   // 链接标题
    public MultiImageView multiImageView; // 说说图片

    // 复用自定义适配器
    public FavortListAdapter favortListAdapter;  // 点赞适配器
    public CommentAdapter commentAdapter; // 评论适配器

    public static ZoneViewHolder create(Context context, int type) {
        ZoneViewHolder imageViewHolder = new ZoneViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_circle_list, null), context, type);
        return imageViewHolder;
    }


    public ZoneViewHolder(View itemView, Context context, int type) {
        super(itemView);
        this.itemView = itemView;
        this.type = type;
        this.mContext = context;
        initView();
    }

    private void initView() {
        ViewStub linkOrImgViewStub = itemView.findViewById(R.id.linkOrImgViewStub);
        switch (type) {
            case CircleAdapter.ITEM_VIEW_TYPE_URL: // 链接view
                linkOrImgViewStub.setLayoutResource(R.layout.item_circle_viewstub_urlbody);
                linkOrImgViewStub.inflate();
                LinearLayout urlBodyView = itemView.findViewById(R.id.urlBody);
                if (urlBodyView != null) {
                    urlBody = urlBodyView;
                    urlImageIv = itemView.findViewById(R.id.urlImageIv);
                    urlContentTv = itemView.findViewById(R.id.urlContentTv);
                }
                break;

            case CircleAdapter.ITEM_VIEW_TYPE_IMAGE: // 图文view
            default:
                linkOrImgViewStub.setLayoutResource(R.layout.item_circle_viewstub_imgbody);
                linkOrImgViewStub.inflate();
                MultiImageView multiImageView = itemView.findViewById(R.id.multiImagView);
                if (multiImageView != null) {
                    this.multiImageView = multiImageView;
                }
                break;
        }

        headIv = itemView.findViewById(R.id.headIv);  // 用户头像
        nameTv = itemView.findViewById(R.id.nameTv);  // 用户名
        digLine = itemView.findViewById(R.id.lin_dig); // 分割线

        contentTv = itemView.findViewById(R.id.contentTv); // 说说内容
        urlTipTv = itemView.findViewById(R.id.urlTipTv);
        timeTv = itemView.findViewById(R.id.timeTv); // 时间
        tvAddressOrDistance = (TextView) itemView.findViewById(R.id.tv_address_or_distance); // 距离

        deleteBtn = (TextView) itemView.findViewById(R.id.deleteBtn); // 删除说说(默认隐藏)
        favortBtn = (TextView) itemView.findViewById(R.id.favortBtn); // 点赞
        snsBtn = (TextView) itemView.findViewById(R.id.commentBtn); // 评论

        ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment); // 评论输入布局
        favortListTv = (FavortListView) itemView.findViewById(R.id.favortListTv); // 评论区
        digCommentBody = (LinearLayout) itemView.findViewById(R.id.digCommentBody); // 评论区 父布局

        commentList = itemView.findViewById(R.id.commentList); // 评论布局
        commentAdapter = new CommentAdapter(mContext); // 评论适配器
        favortListAdapter = new FavortListAdapter(); // 点赞适配器

        favortListTv.setAdapter(favortListAdapter);
        commentList.setAdapter(commentAdapter);
    }

    // 设置数据
    public void setData(CircleZonePresenter mPresenter2, CircleItem circleItem2, final int position2) {
        if (mPresenter2 == null || circleItem2 == null) {
            LogUtils.d("mPresenter2 || circleItem2 == null");
            return;
        }
        this.circleItem = circleItem2;
        this.mPresenter = mPresenter2;
        this.position = position2;
        List<FavortItem> goodjobs = circleItem.getGoodjobs();// 点赞分类
        List<CommentItem> replys = circleItem.getReplys(); // 评论
        boolean hasFaort = circleItem.getGoodjobs().size() > 0 ? true : false;
        boolean hasComment = circleItem.getReplys().size() > 0 ? true : false;
        // 头像
        ImageLoaderUtils.displayRound(mContext, headIv, DatasUtil.getRandomPhotoUrl());

        nameTv.setText(circleItem.getNickName());
        timeTv.setText(TimeUtil.getfriendlyTime(circleItem.getCreateTime())); // 发布时间

        contentTv.setText(circleItem.getContent(), position);
        // 距离
        tvAddressOrDistance.setText("广州 <7KM");
        contentTv.setVisibility(TextUtils.isEmpty(circleItem.getContent()) ? View.GONE : View.VISIBLE);
        //是否显示删除图标
        deleteBtn.setVisibility(AppCache.getInstance().getUserId().equals(circleItem.getUserId()) ? View.VISIBLE : View.GONE);
        // 删除
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
//                    mPresenter.deleteCircle(circleItem.getId(), position);
                }
            }
        });

        switch (type){

        }

    }

}
