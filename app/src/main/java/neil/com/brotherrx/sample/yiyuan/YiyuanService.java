package neil.com.brotherrx.sample.yiyuan;


import java.util.Map;

import neil.com.brotherrx.entity.ResBodyBean;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by neil on 2017/10/18 0018.
 * 易源Api热点新闻接口
 */
public interface YiyuanService {

    /**
     * @param n             page
     * @param showapi_appid
     * @param showapi_sign
     * @return
     */
    @FormUrlEncoded
    @POST("738-1")
    Observable<YiyuanApiResult<ResBodyBean>> getHotSearchRank(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("738-1")
    Observable<YiyuanApiResult<ResBodyBean>> getHotSearchRankNew(@Field("n") int page, @Field("showapi_appid") String showapi_appid, @Field("showapi_sign") String showapi_sign);

}
