package com.ronin.net.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 *
 * @author donghailong
 */
public class ProgressDialogHelper {

    private static final int MSG_SHOW_DIALOG = 0X0001;
    private static final int MSG_DISMISS_DIALOG = 0X0002;

    private Context context;
    private ProgressDialog progressDialog;
    private DialogInterface.OnCancelListener listener;
    private boolean isCancel = false;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_DIALOG:
                    initProgressDialog();
                    break;
                case MSG_DISMISS_DIALOG:
                    if (null != progressDialog) {
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    public ProgressDialogHelper(Context cx, DialogInterface.OnCancelListener l) {
        this.context = cx;
        this.listener = l;
    }

    private void initProgressDialog() {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(isCancel);
            if (isCancel) {
                progressDialog.setOnCancelListener(listener);
            }
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void show() {
        if (null != handler) {
            handler.sendEmptyMessage(MSG_SHOW_DIALOG);
        }
    }


    public void dismiss() {
        if (null != handler) {
            handler.sendEmptyMessage(MSG_DISMISS_DIALOG);
        }
    }

    public void setCancel(boolean cancel) {
        this.isCancel = cancel;
    }
}
