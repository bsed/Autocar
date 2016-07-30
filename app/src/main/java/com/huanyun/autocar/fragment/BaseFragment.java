package com.huanyun.autocar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.huanyun.autocar.widget.LoadingDialog;


/**
 * Created by admin on 2015/12/2.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void loadNext(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
    private LoadingDialog processDialog;

    protected void beginLoading(String msg,Context context ) {
        if (processDialog == null || !processDialog.isShowing()) {
            processDialog = LoadingDialog.createDialog(context);
            processDialog.setCancelable(false);
            processDialog.setMessage(msg);
            processDialog.show();
        }
    }

    protected void endLoading() {
        if (processDialog != null && processDialog.isShowing()) {
            processDialog.dismiss();
            processDialog = null;
        }
    }

    protected void beginLoading(Context context) {
        if (processDialog == null || !processDialog.isShowing()) {
            processDialog = LoadingDialog.createDialog(context);
            processDialog.setCancelable(false);
            processDialog.show();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
