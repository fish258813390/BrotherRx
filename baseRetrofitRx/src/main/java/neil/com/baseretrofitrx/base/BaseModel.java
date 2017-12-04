package neil.com.baseretrofitrx.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import neil.com.baseretrofitrx.utils.Apputils;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by neil on 2017/11/6 0006.
 */
public class BaseModel {

    private static final int DEFAULT_TIMEOUT = 60; // 默认超时时间

    protected Retrofit retrofit;

    private OkHttpClient.Builder httpClientBuilder;

    protected BaseModel() {
        httpClientBuilder = new OkHttpClient.Builder();
        /**
         * 设置debug模式下的网络请求拦截设置
         */
        if (Apputils.isApkInDebug(BaseApplication.getContext())) {
            httpClientBuilder.retryOnConnectionFailure(true) ////断网重连
                    .addInterceptor(new AddHeaderInterceptor())
                    .addInterceptor(new LogInterceptor());
        } else {
            httpClientBuilder.addInterceptor(new AddHeaderInterceptor()).addInterceptor(new ResponseInterceptor());
        }
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("")
                .build();
    }

    public BaseModel(String url) {
        httpClientBuilder = new OkHttpClient.Builder();
        if (Apputils.isApkInDebug(BaseApplication.getContext())) {
            httpClientBuilder.retryOnConnectionFailure(true).addInterceptor(new LogInterceptor());
        } else {
//            httpClientBuilder.addInterceptor(new ResponseInterceptor());
            httpClientBuilder.retryOnConnectionFailure(true).addInterceptor(new LogInterceptor());
        }
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    public class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody,Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }

    /**
     * 添加公共头信息
     */
    private class AddHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .header("juid", "")
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 响应数据拦截器,可以用于对登录信息进行校验,例如多终端设备进行剔除操作
     */
    private class ResponseInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(chain.request());
            MediaType mediaType = response.body().contentType();
            ResponseBody originalBody = response.body();
            String content = "";
//            if (null != originalBody) {
//                content = originalBody.string();
//                JsonObject asJsonObject = new JsonParser().parse(content).getAsJsonObject();
//                JsonElement jsonElement = asJsonObject.get("flag");
//                String asString = jsonElement.getAsString();
//                if (!"".equals(asString) && null != asString) {
//                    // TODO 对返回数据进行处理
//                    // 可以跳转到登录界面
//                }
//            }
            return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
        }
    }


    /**
     * 请求体定制：统一添加定制参数
     */
    private class AddBodyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            if (original.body() instanceof FormBody) {
                FormBody.Builder newFormBody = new FormBody.Builder();
                FormBody oidFormBody = (FormBody) original.body();
                for (int i = 0; i < oidFormBody.size(); i++) {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
                }
                newFormBody.add("juid", "");
                requestBuilder.method(original.method(), newFormBody.build());
            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }

    }

    /**
     * 方法调用,绑定生命周期订阅 (此处为何用静态？)
     *
     * @param lifeSubscription
     * @param observable
     * @param subscriber
     * @param <T>
     */
    public static <T> void invoke(LifeSubscription lifeSubscription, Observable<T> observable, Subscriber<T> subscriber) {
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        lifeSubscription.bindSubscription(subscription);
    }

    /**
     * 组合请求 合并发射源
     *
     * @param lifeSubscription
     * @param subscriber
     * @param observable
     * @param <T>
     */
    public static <T> void invokeMerge(LifeSubscription lifeSubscription, Subscriber<T> subscriber, Observable<T>... observable) {
        Observable<T> myObservable = null;
        switch (observable.length) {
            case 2:
                myObservable.mergeWith(observable[0]).mergeWith(observable[1]);
                break;

            case 3:
                myObservable.mergeWith(observable[0]).mergeWith(observable[1]).mergeWith(observable[2]);
                break;

            case 4:
                myObservable.mergeWith(observable[0]).mergeWith(observable[1]).mergeWith(observable[2]).mergeWith(observable[3]);
                break;
        }
        Subscription subscription = myObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        lifeSubscription.bindSubscription(subscription);
    }


}
