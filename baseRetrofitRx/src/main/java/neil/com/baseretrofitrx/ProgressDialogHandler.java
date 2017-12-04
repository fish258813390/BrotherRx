package neil.com.baseretrofitrx;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * Created by neil on 2017/11/5 0005.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 11;

    public static final int DISMISS_PROGRESS_DIALOG = 22;

    private CustomProgressDialog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    /**
     * @param context
     * @param mProgressCancelListener
     * @param cancelable
     */
    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener, boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog() {
        if (pd == null) {
            //pd = new ProgressDialog(context);
            pd = CustomProgressDialog.createDialog(context);
            pd.setCancelable(cancelable);
            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
