package neil.com.brotherrx.entity;

/**
 * @author neil
 * @date 2017/12/6
 */

public class GrabTargetBean {

    private String name;

    private String phone;

    private String isEnable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public String toString() {
        return "GrabTargetBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", isEnable='" + isEnable + '\'' +
                '}';
    }
}
