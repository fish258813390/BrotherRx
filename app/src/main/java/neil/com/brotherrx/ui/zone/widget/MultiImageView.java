package neil.com.brotherrx.ui.zone.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jaydenxiao.common.commonutils.DisplayUtil;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.neil.common.utils.ImageUtils;

import java.util.List;

import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.R;

/**
 * 图片展示(显示1-N张图片)
 *
 * @author neil
 * @date 2018/1/5
 */
public class MultiImageView extends LinearLayout {

    public int MAX_WIDTH = 0;
    private List<String> imagesList; // 照片的Url列表
    /**
     * 长度 单位为Pixel
     **/
    private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = DisplayUtil.dip2px(3);// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;  // match, wrap

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setList(List<String> imageList) {
        if (imageList == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = imageList;

        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();
        }

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setList(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 确定view的宽度
     * Determines the width of this view
     * @param measureSpec
     *            A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            // 最大满足 We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;
        //pxOneMaxWandH
        onePicPara = new LayoutParams(match, wrap);

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);

        rowPara = new LayoutParams(match, wrap);
    }

    // 根据imageView的数量初始化不同的View布局,同时每一个view作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            LogUtils.d("imagesList == null || imagesList.size() == 0");
            return;
        }

        if (imagesList.size() == 1) {
            addView(createImageView(0, false));
        } else {
            int allCount = imagesList.size();
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2;
            } else {
                MAX_PER_ROW_COUNT = 3;
            }
            // 图片展示的总行数
            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                // 图片展示
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(rowPara);

                if (rowCursor != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                // 图片展示的总列数(根据行数进行动态展示)
                int columenCount;
                if (rowCursor != rowCount - 1) {
                    // 不是最后一行时
                    columenCount = MAX_PER_ROW_COUNT;
                } else {
                    // 最后一行
                    columenCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT : allCount % MAX_PER_ROW_COUNT;
                }
                // 添加图片父布局(LinearLayout)
                addView(rowLayout);

                // 计算行偏移
                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;
                // 给每一行动态添加图片
                for (int columnCursor = 0; columnCursor < columenCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));
                }
            }
        }

    }

    private ImageView createImageView(int position, boolean isMultiImage) {
        String url = imagesList.get(position);
        ImageView imageView = new ColorFilterImageView(getContext());
        if (isMultiImage) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
        } else {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setMaxHeight(pxOneMaxWandH);
            imageView.setLayoutParams(onePicPara);
        }
        imageView.setTag(R.string.zone_img_position, position);
        imageView.setId(url.hashCode());
        imageView.setOnClickListener(mImageViewOnClickListener);
        ImageLoaderUtils.display(getContext(), imageView, ImageUtils.getImageUrl(url));
        return imageView;
    }


    // 图片点击事件处理
    private OnClickListener mImageViewOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, (Integer) view.getTag(R.string.zone_img_position));
            }
        }
    };

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
