package neil.com.brotherrx.ui.zone.bean;

/**
 * 评论分类
 * Created by neil on 2018/1/11 0011.
 */
public class CommentConfig {

    public static enum Type {
        PUBLIC("public"), REPLY("reply");

        private String value;

        private Type(String value) {
            this.value = value;
        }
    }

    public int circlePosition;
    public int commentPosition;
    public Type commentType;
    private String publishId;
    private String publishUserId;
    private String id; // 被评论人id
    private String name;
    private String headUrl;
    private boolean isOpen = false;

    public int getCirclePosition() {
        return circlePosition;
    }

    public void setCirclePosition(int circlePosition) {
        this.circlePosition = circlePosition;
    }

    public int getCommentPosition() {
        return commentPosition;
    }

    public void setCommentPosition(int commentPosition) {
        this.commentPosition = commentPosition;
    }

    public Type getCommentType() {
        return commentType;
    }

    public void setCommentType(Type commentType) {
        this.commentType = commentType;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
