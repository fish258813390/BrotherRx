package neil.com.brotherrx;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import neil.com.baseretrofitrx.base.BaseActivity;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.sample.yiyuan.YiyuanPresenter;
import neil.com.brotherrx.sample.yiyuan.YiyuanView;
import neil.com.brotherrx.ui.grab.GrabMainActivity;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity<YiyuanPresenter, YiyuanView> implements YiyuanView {

    @BindView(R.id.btn_test1)
    Button btnTest1;
    @BindView(R.id.et_number)
    EditText etNumber;

    List<String> targetData = new ArrayList<>();
    List<String> targetData2 = new ArrayList<>();

    @Override
    protected YiyuanPresenter createPresenter() {
        return new YiyuanPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initListener();

    }

    private void initListener() {
        targetData.add("123");
        targetData.add("1234");
        targetData.add("123456");
        targetData.add("12345678");
        targetData.add("123456789");
        targetData.add("1244");
        targetData.add("1744");

        targetData2.add("1111111");
        targetData2.add("2222222");
        targetData2.add("3333333");
        targetData2.add("444444");

//        RxTextView.textChanges( etNumber )
//                .debounce( 600 , TimeUnit.MILLISECONDS )
//                .map(new Func1<CharSequence, String>() {
//                    @Override
//                    public String call(CharSequence charSequence) {
//                        //get the keyword
//                        String key = charSequence.toString() ;
//                        return key ;
//                    }
//                })
//                .observeOn( Schedulers.io() )
//                .map(new Func1<String, List<String>>() {
//                    @Override
//                    public List<String> call(String keyWord ) {
//                        //get list
//                        List<String> dataList = new ArrayList<String>() ;
//                        if ( ! TextUtils.isEmpty( keyWord )){
//                            for ( String s :targetData  ) {
//                                if (s != null) {
//                                    if (s.contains(keyWord)) {
//                                        dataList.add(s);
//                                    }
//                                }
//                            }
//                        }
//                        return dataList ;
//                    }
//                })
//                .observeOn( AndroidSchedulers.mainThread() )
//                .subscribe(new Action1<List<String>>() {
//                    @Override
//                    public void call(List<String> strings) {
//                        LogUtils.d("输入字符--转换-->" + strings.toString());
//                    }
//                }) ;


        // map
//        RxTextView.textChanges(etNumber)
//                .debounce(1000, TimeUnit.MILLISECONDS)
//                .map(new Func1<CharSequence, String>() {
//                    @Override
//                    public String call(CharSequence charSequence) {
//                        String keyWord = charSequence.toString();
//                        LogUtils.d("输入字符---->" + keyWord);
//                        if (keyWord.contains("1234")) {
//                            keyWord = keyWord.substring(0, 3);
//                        }
//                        LogUtils.d("字符经过滤后---->" + keyWord);
//                        return keyWord;
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .map(new Func1<String, List<String>>() {
//                    @Override
//                    public List<String> call(String keyWord) {
//                        //get list
//                        LogUtils.d("接收过滤字符---->" + keyWord);
//                        List<String> filtertData = new ArrayList<>();
//                        if (!TextUtils.isEmpty(keyWord)) {
//                            for (String str : targetData) {
//                                if (str.contains(keyWord)) {
//                                    filtertData.add(str);
//                                    LogUtils.d("[校验结果:包含]" + str);
//                                } else {
//                                    LogUtils.d("[校验结果:不包含]" + str);
//                                }
//                            }
//                        }
//                        return filtertData;
//                    }
//                })
//                .map(new Func1<List<String>, List<String>>() {
//                    @Override
//                    public List<String> call(List<String> filterNumberList) {
//                        LogUtils.d("过滤结果--->" + new Gson().toJson(filterNumberList) + "\n数量:" + filterNumberList.size() + ",对数量进行过滤(数量>=4)");
//                        if (filterNumberList != null && filterNumberList.size() >= 4) {
//                            LogUtils.d("数量符合要求--->" + filterNumberList.size());
//                            return filterNumberList;
//                        } else {
//                            LogUtils.d("数量不符合要求--->" + filterNumberList.size());
//                            return null;
//                        }
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<String>>() {
//                    @Override
//                    public void call(List<String> resultList) {
//                        LogUtils.d("最后结果--->" + resultList);
//                        if (null != resultList) {
//                            LogUtils.d("end--->" + resultList.toString());
//                        }
//                    }
//                });

        // rxjava flatMap
//        RxTextView.textChanges(etNumber)
//                .debounce(800, TimeUnit.MILLISECONDS)
//                .observeOn(Schedulers.io())
//                .flatMap(new Func1<CharSequence, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(CharSequence charSequence) {
//                        return getTargetString(charSequence.toString());
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String result) {
//                        LogUtils.d("flatMap最后结果--->" + result);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//
//                    }
//                });

        // rxjava flatMap
        RxTextView.textChanges(etNumber)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<CharSequence, Observable<List<String>>>() {
                    @Override
                    public Observable<List<String>> call(CharSequence charSequence) {
                        return getTargetList(charSequence.toString());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> result) {
                        LogUtils.d("flatMap最后结果--->" + new Gson().toJson(result));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private Observable<String> getTargetString(final String keyWord) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (String str : targetData2) {
                    if (!TextUtils.isEmpty(keyWord)) {
                        if (str.contains(keyWord)) {
                            LogUtils.d("[flatMap校验结果]" + str + "--->包含:" + keyWord);
                            subscriber.onNext(keyWord);
                        } else {
                            LogUtils.d("[flatMap校验结果:" + str + "--->不包含:" + keyWord);
                        }
                    } else {
                        subscriber.onNext(null);
                    }
                    subscriber.onCompleted();
                }
            }
        });
        // flatMap最后结果 subscribeOn(Schedulers.io())
    }

    private Observable<List<String>> getTargetList(final String keyWord) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                for (String str : targetData2) {
                    if (!TextUtils.isEmpty(keyWord)) {
                        if (str.contains(keyWord)) {
                            LogUtils.d("[flatMap校验结果]" + str + "--->包含:" + keyWord);
                            subscriber.onNext(targetData2);
                        } else {
                            LogUtils.d("[flatMap校验结果:" + str + "--->不包含:" + keyWord);
                        }
                    } else {
//                        subscriber.onNext(null);
                    }
                    subscriber.onCompleted();
                }
            }
        });

        // flatMap最后结果 subscribeOn(Schedulers.io())
    }


    @OnClick({R.id.btn_test1, R.id.btn_test2, R.id.btn_test3, R.id.btn_test4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test1:
                Map<String, String> parms = new HashMap<>();
                parms.put("m", "10");
                parms.put("showapi_appid", "47526");
                parms.put("showapi_sign", "c05733048bb9427f8ae9b8ede645ff23");
                mPresenter.getHotListRankNew(10, "47526", "c05733048bb9427f8ae9b8ede645ff23");
                break;
            case R.id.btn_test2:
                mPresenter.multiRequest(10, "47526", "c05733048bb9427f8ae9b8ede645ff23", 15);
                break;
            case R.id.btn_test3:
                Intent intent = new Intent(this, GrabOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_test4:
                Intent intent1 = new Intent(this, GrabMainActivity.class);
                startActivity(intent1);
                break;

        }
    }

    @Override
    public void onSucces(String apiCode, String data) {

    }

    @Override
    public void onFail(String apiCode, String errorMsg) {

    }

    @Override
    public void onError(String apiCode, String errorMsg) {

    }

    private void testFlatMap() {

    }

}
