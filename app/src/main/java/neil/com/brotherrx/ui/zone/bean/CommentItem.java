package neil.com.brotherrx.ui.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说说 里的评论bean
 * Created by neil on 2017/12/31 0031.
 */
public class CommentItem implements Parcelable{

    /**
     "id":24,
     "content":"肥死你",
     "createTime":1471489658000,
     "appointUserid":0,
     "publishId":13,
     "appointUserNickname":"",
     "userId":10002,
     "pictures":"",
     "userNickname":"小鹏"
     */

    private String appointUserNickname;
    private String appointUserid;
    private String content; // 评论内容
    private String createTime;
    private String id;
    private String pictures;
    private String publishId; // 说说发布者id
    private String userId; // 评论用户id
    private String userNickname; // 用户名

    public CommentItem() {
    }

    public CommentItem(String appointUserNickname, String appointUserid, String content, String publishId, String userId, String userNickname) {
        this.appointUserNickname = appointUserNickname;
        this.appointUserid = appointUserid;
        this.content = content;
        this.publishId = publishId;
        this.userId = userId;
        this.userNickname = userNickname;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }



    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    // 将值写入Parcel 中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appointUserNickname);
        dest.writeString(this.appointUserid);
        dest.writeString(this.content);
        dest.writeString(this.createTime);
        dest.writeString(this.id);
        dest.writeString(this.pictures);
        dest.writeString(this.publishId);
        dest.writeString(this.userId);
        dest.writeString(this.userNickname);
    }

    /**
     * 这里读的顺序必须与writeToParcel(Parcel dest,int flags) 方法中写的顺序保持一致
     * 否则数据会有差错,导致读取的属性值不对
     * @param source
     */
    protected CommentItem(Parcel source) {
        appointUserNickname = source.readString();
        appointUserid = source.readString();
        content = source.readString();
        createTime = source.readString();
        id = source.readString();
        pictures = source.readString();
        publishId = source.readString();
        userId = source.readString();
        userNickname = source.readString();
    }
}
