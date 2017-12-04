package neil.com.brotherrx;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import neil.com.baseretrofitrx.base.BaseActivity;
import neil.com.baseretrofitrx.utils.LogUtils;
import neil.com.brotherrx.sample.yiyuan.YiyuanPresenter;
import neil.com.brotherrx.sample.yiyuan.YiyuanView;

public class MainActivity extends BaseActivity<YiyuanPresenter,YiyuanView> implements YiyuanView{

    @BindView(R.id.btn_test1)
    Button btnTest1;

    @Override
    protected YiyuanPresenter createPresenter() {
        return new YiyuanPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        System.out.print("11111");
        Toast.makeText(this,"2213123",Toast.LENGTH_LONG).show();
        btnTest1.setText("55555");
    }


    @OnClick({R.id.btn_test1})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_test1:
                Map<String,String> parms = new HashMap<>();
                parms.put("m","10");
                parms.put("showapi_appid","47526");
                parms.put("showapi_sign","c05733048bb9427f8ae9b8ede645ff23");
//                mPresenter.getHotListRank(parms);
                mPresenter.getHotListRankNew(10,"47526","c05733048bb9427f8ae9b8ede645ff23");
                break;
            case R.id.btn_test2:

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
}
