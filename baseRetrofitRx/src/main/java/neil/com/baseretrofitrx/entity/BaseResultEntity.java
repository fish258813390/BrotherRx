package neil.com.baseretrofitrx.entity;

import java.io.Serializable;

/**
 * Created by neil on 2017/11/5 0005.
 */

public class BaseResultEntity<T> implements Serializable {

    /**
     * 响应码
     */
    public String code;

    /**
     * 响应信息描述
     */
    public String msg;

    /**
     * 响应结果
     */
    public T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
