package neil.com.brotherrx.view;

/**
 * Created by neil on 2017/12/4 0004.
 */

public interface BaseView {

    void onSucces(String apiCode, String data);

    void onFail(String apiCode, String errorMsg);

    void onError(String apiCode, String errorMsg);
}
