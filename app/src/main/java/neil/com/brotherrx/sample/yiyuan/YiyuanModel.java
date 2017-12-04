
package neil.com.brotherrx.sample.yiyuan;

import java.util.Map;

import neil.com.baseretrofitrx.base.BaseModel;
import neil.com.brotherrx.config.Constants;
import neil.com.brotherrx.entity.ResBodyBean;
import rx.Observable;

/**
 * 易源
 * Created by neil on 2017/12/4 0004.
 */
public class YiyuanModel extends BaseModel {

    private YiyuanService yiyuanService;

    private YiyuanModel(String baseUrl) {
        super(baseUrl);
        yiyuanService = retrofit.create(YiyuanService.class);
    }

    private static class SingleInstanceHolder {
        private static final YiyuanModel yiyuanModel = new YiyuanModel(Constants.YIYUAN_BASE_URL);
    }

    public static YiyuanModel getInstance() {
        return SingleInstanceHolder.yiyuanModel;
    }

    public Observable<YiyuanApiResult<ResBodyBean>> getHotRankList(Map<String, String> params) {
        return yiyuanService.getHotSearchRank(params);
    }


    public Observable<YiyuanApiResult<ResBodyBean>> getHotRankListNew(int page, String showapi_appid, String showapi_sign) {
        return yiyuanService.getHotSearchRankNew(page,showapi_appid,showapi_sign);
    }




}
