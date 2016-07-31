package com.huanyun.autocar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.huanyun.autocar.widget.LoadingDialog;

public abstract class BaseActivity extends Activity implements
		OnClickListener {

	public static final String TAG = BaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 
	 *
	 * 
	 */
	protected void jumpBack() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		finish();
	}

	/**
	 * 
	 *
	 */
	protected void jumpBack1Second() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					jumpBack();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	/**
	 * 
	 *
	 */
	protected void loadNext(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	public <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	 private LoadingDialog processDialog;

	 protected void beginLoading(String msg,Context context ) {
	 if (processDialog == null || !processDialog.isShowing()) {
	 processDialog = LoadingDialog.createDialog(context);
	 processDialog.setCancelable(true);
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
	 processDialog.setCancelable(true);
	 processDialog.show();
	 }
	 }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
