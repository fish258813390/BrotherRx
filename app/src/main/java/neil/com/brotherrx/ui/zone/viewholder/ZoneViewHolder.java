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
import com.jaydenxiao.common.commonutils.ToastUitl;

import java.util.List;

import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;
import neil.com.brotherrx.ui.zone.bean.CircleItem;
import neil.com.brotherrx.ui.zone.bean.CommentConfig;
import neil.com.brotherrx.ui.zone.bean.CommentItem;
import neil.com.brotherrx.ui.zone.bean.FavortItem;
import neil.com.brotherrx.ui.zone.DatasUtil;
import neil.com.brotherrx.ui.zone.adapter.CircleAdapter;
import neil.com.brotherrx.ui.zone.adapter.CommentAdapter;
import neil.com.brotherrx.ui.zone.adapter.FavortListAdapter;
import neil.com.brotherrx.ui.zone.presenter.CircleZonePresenter;
import neil.com.brotherrx.ui.zone.spannable.ISpanClick;
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
    public void setData(final CircleZonePresenter mPresenter2, CircleItem circleItem2, final int position2) {
        if (mPresenter2 == null || circleItem2 == null) {
            LogUtils.d("mPresenter2 || circleItem2 == null");
            return;
        }
        this.circleItem = circleItem2;
        this.mPresenter = mPresenter2;
        this.position = position2;
        final List<FavortItem> favortDatas = circleItem.getGoodjobs();// 点赞分类
        final List<CommentItem> commentsDatas = circleItem.getReplys(); // 评论
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

        switch (type) {
            case CircleAdapter.ITEM_VIEW_TYPE_URL: // 处理链接动态和图片
                String linkImg = circleItem.getLinkImg();
                String linkTitile = circleItem.getLinkTitle();
                ImageLoaderUtils.display(mContext, urlImageIv, linkImg);
                urlContentTv.setText(linkTitile);
                urlBody.setVisibility(View.VISIBLE);
                urlTipTv.setVisibility(View.VISIBLE);
                break;
            case CircleAdapter.ITEM_VIEW_TYPE_IMAGE: // 处理图片
            default:
                List<String> photos = circleItem.getPictureList();
                if (photos != null && photos.size() > 0) {
                    multiImageView.setVisibility(View.VISIBLE);
                    multiImageView.setList(photos);
                    multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // 查看大图
                            LogUtils.d("点击了：" + position);
                        }
                    });
                } else {
                    multiImageView.setVisibility(View.GONE);
                }
                break;
        }

        // 点赞和评论
        ll_comment.setVisibility(View.VISIBLE);
        if (hasFaort || hasComment) {
            // 处理点赞
            if (hasFaort) {
                favortListTv.setSpanClickListener(new ISpanClick() {
                    @Override
                    public void onClick(int postion) {
                        String userId = favortDatas.get(postion).getUserId();
                        ToastUitl.showShort(userId);
                    }
                });
                favortListAdapter.setDatas(favortDatas);
                favortListAdapter.notifyDataSetChanged();
                favortListTv.setVisibility(View.VISIBLE);
                //favortBtn.setText(String.valueOf(favortDatas.size()));
            } else {
                favortListTv.setVisibility(View.GONE);
                favortBtn.setText("");
            }

            if (hasComment) {
                // 点击评论
                commentList.setOnItemClick(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
                        CommentItem commentItem = commentsDatas.get(commentPosition);
                        // 复制或者删除自己的评论
                        if (AppCache.getInstance().getUserId().equals(commentItem.getUserId())) {
                            ToastUitl.showShort("复制或删除自己的评论");
                        } else {
                            // 回复别人的评论
                            if (mPresenter != null) {
                                CommentConfig config = new CommentConfig();
                                config.circlePosition = position;
                                config.commentPosition = commentPosition;
                                config.commentType = CommentConfig.Type.REPLY;
                                config.setPublishId(circleItem.getId());
                                config.setPublishUserId(circleItem.getUserId()); // 动态人userId
                                config.setId(commentItem.getUserId()); // 评论人id
                                config.setName(commentItem.getUserNickname()); // 评论人nickname
                                mPresenter.showEditTextBody(config);
                            }
                        }

                    }
                });

                // 长按评论
                commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int position) {
                        ToastUitl.showShort("长按评论");
                    }
                });
//                snsBtn.setText(String.valueOf(commentsDatas.size()));
                commentAdapter.setDatas(commentsDatas);
                commentAdapter.notifyDataSetChanged();
                commentList.setVisibility(View.VISIBLE);
            } else {
                snsBtn.setText("");
                commentList.setVisibility(View.GONE);
            }
        } else {
            // 没有评论也没有赞
            favortListTv.setVisibility(View.GONE);
            commentList.setVisibility(View.GONE);
            favortBtn.setText("");
            snsBtn.setText("");
        }
        digLine.setVisibility(hasFaort && hasComment ? View.VISIBLE : View.GONE);
        // 评论按钮
        snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评论 TODO
                comment(circleItem.getId(), circleItem.getUserId(), position);
            }
        });
        // 评论区布局点击
        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评论 TODO
                comment(circleItem.getId(), circleItem.getUserId(), position);
            }
        });

        // 点赞
        favortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 判断是否已点赞
                String curUserFavortId = circleItem.getCurUserFavortId();
                if (!TextUtils.isEmpty(curUserFavortId)) {
                    // 取消点赞 TODO
                    favort(circleItem.getId(), circleItem.getUserId(), position, "取消", view);
                } else {
                    // 点赞 TODO
                    favort(circleItem.getId(), circleItem.getUserId(), position, "赞", view);
                }
            }
        });

        //头像点击
        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳到个人朋友圈
                ToastUitl.showShort("头像点击了" + position);
            }
        });
        urlTipTv.setVisibility(View.GONE);
    }

    // 点赞 | 取消点赞
    private long mLasttime = 0;

    private void favort(String publishId, String publishUserId, int circlePosition,
                        String mTitle, View view) {
        if (System.currentTimeMillis() - mLasttime < 700) {
            // 防止快速点击操作
            return;
        }
        mLasttime = System.currentTimeMillis();
        if (mPresenter != null) {
            if ("赞".equals(mTitle)) {
                // 点赞 TODO
                mPresenter.addFavort(publishId, publishUserId, circlePosition, view);
            } else {
                // 取消点赞 TODO
                mPresenter.deleteFavort(publishId, publishUserId, circlePosition);
            }
        }

    }

    // 评论
    private void comment(String publishId, String publishUserId, int circlePosition) {
        if (mPresenter != null) {
            CommentConfig config = new CommentConfig();
            config.circlePosition = circlePosition;
            config.commentType = CommentConfig.Type.PUBLIC;
            config.setPublishId(publishId);
            config.setPublishUserId(publishUserId);
            // TODO 评论
            mPresenter.showEditTextBody(config);
        }
    }

    public View getRootView() {
        return itemView.findViewById(R.id.ll_root);
    }

    public View getCommentListView() {
        return commentList;
    }

    public int getHeight() {
        if (itemView != null) {
            return itemView.getMeasuredHeight();
        } else {
            return 0;
        }
    }


}
