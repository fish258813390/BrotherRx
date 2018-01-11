package neil.com.brotherrx.ui.zone.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点赞分类
 * Created by neil on 2017/12/31 0031.
 */
public class FavortItem implements Parcelable {

    /**
     * {
     * "id":11,
     * "createTime":1471406833000,
     * "publishId":13,
     * "userId":10000,
     * "userNickname":"锋"
     * }
     */

    private String createTime;
    private String id;
    private String publishId;
    private String userId;
    private String userNickname;




    public static final Creator<FavortItem> CREATOR = new Creator<FavortItem>() {
        @Override
        public FavortItem createFromParcel(Parcel in) {
            return new FavortItem(in);
        }

        @Override
        public FavortItem[] newArray(int size) {
            return new FavortItem[size];
        }
    };

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

    public FavortItem() {
    }

    public FavortItem(String publishId, String userId, String userNickname) {
        this.publishId = publishId;
        this.userId = userId;
        this.userNickname = userNickname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createTime);
        dest.writeString(this.id);
        dest.writeString(this.publishId);
        dest.writeString(this.userId);
        dest.writeString(this.userNickname);
    }

    protected FavortItem(Parcel in) {
        createTime = in.readString();
        id = in.readString();
        publishId = in.readString();
        userId = in.readString();
        userNickname = in.readString();
    }
}
