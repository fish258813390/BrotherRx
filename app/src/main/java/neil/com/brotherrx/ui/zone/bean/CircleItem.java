package neil.com.brotherrx.ui.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.jaydenxiao.common.baseapp.AppCache;
import com.neil.common.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 说说实体bean
 * Created by neil on 2018/1/2 0002.
 */
public class CircleItem implements Parcelable {

    public String address; // 定位地址
    private String appointUserNickname;
    private String appointUserid;
    private String content; // 说说发布内容
    private long createTime; // 说说发布时间
    private int goodjobCount; // 点赞总数
    private String id;
    private String isvalid; // 是否有效(0：正常；1：删除；2：对外隐藏)
    private double longitude; // 所处经度
    private double latitude; // 所处纬度
    private String pictures; // 说说附加图片
    private int replyCount; // 评论总数
    private int type;  // 动态类型0：普通消息 1：分享链接
    private String userId; // 用户id
    private String nickName; // 用户名
    private List<FavortItem> goodjobs = new ArrayList<>(); // 点赞分类
    private List<CommentItem> replys = new ArrayList<>(); // 评论分类
    private String linkImg; // 链接图片
    private String linkTitle; // 链接标题
    private int takeTimes;//接单总数

    protected CircleItem(Parcel in) {
        address = in.readString();
        appointUserNickname = in.readString();
        appointUserid = in.readString();
        content = in.readString();
        createTime = in.readLong();
        goodjobCount = in.readInt();
        id = in.readString();
        isvalid = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        pictures = in.readString();
        replyCount = in.readInt();
        type = in.readInt();
        userId = in.readString();
        nickName = in.readString();
        goodjobs = in.createTypedArrayList(FavortItem.CREATOR);
        replys = in.createTypedArrayList(CommentItem.CREATOR);
        linkImg = in.readString();
        linkTitle = in.readString();
        takeTimes = in.readInt();
    }

    public static final Creator<CircleItem> CREATOR = new Creator<CircleItem>() {
        @Override
        public CircleItem createFromParcel(Parcel in) {
            return new CircleItem(in);
        }

        @Override
        public CircleItem[] newArray(int size) {
            return new CircleItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(appointUserNickname);
        dest.writeString(appointUserid);
        dest.writeString(content);
        dest.writeLong(createTime);
        dest.writeInt(goodjobCount);
        dest.writeString(id);
        dest.writeString(isvalid);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(pictures);
        dest.writeInt(replyCount);
        dest.writeInt(type);
        dest.writeString(userId);
        dest.writeString(nickName);
        dest.writeTypedList(goodjobs);
        dest.writeTypedList(replys);
        dest.writeString(linkImg);
        dest.writeString(linkTitle);
        dest.writeInt(takeTimes);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAppointUserNickname() {
        return appointUserNickname;
    }

    public void setAppointUserNickname(String appointUserNickname) {
        this.appointUserNickname = appointUserNickname;
    }

    public String getAppointUserid() {
        return appointUserid;
    }

    public void setAppointUserid(String appointUserid) {
        this.appointUserid = appointUserid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getGoodjobCount() {
        return goodjobCount;
    }

    public void setGoodjobCount(int goodjobCount) {
        this.goodjobCount = goodjobCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<FavortItem> getGoodjobs() {
        return goodjobs;
    }

    public void setGoodjobs(List<FavortItem> goodjobs) {
        this.goodjobs = goodjobs;
    }

    public List<CommentItem> getReplys() {
        return replys;
    }

    public void setReplys(List<CommentItem> replys) {
        this.replys = replys;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public int getTakeTimes() {
        return takeTimes;
    }

    public void setTakeTimes(int takeTimes) {
        this.takeTimes = takeTimes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 获取图片链接
     */
    public List<String> getPictureList() {
        if (!TextUtils.isEmpty(pictures)) {
            List<String> photos = new ArrayList<>();
            String[] strings = pictures.split(";");
            if (strings != null && strings.length > 0) {
                for (String str : strings) {
                    if (!TextUtils.isEmpty(str)) {
                        photos.add(ImageUtils.getImageUrl(str));
                    }
                }
                return photos;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public CircleItem() {
    }

    public String getCurUserFavortId() {
        String curUserId = "";
        String myId = AppCache.getInstance().getUserId();
        if (goodjobs != null && !TextUtils.isEmpty(myId) && goodjobs.size() > 0) {
            for (FavortItem item : goodjobs) {
                if(myId.equals(item.getUserId())){
                    curUserId = item.getUserId();
                    return curUserId;
                }
            }
        }
        return curUserId;
    }
}
