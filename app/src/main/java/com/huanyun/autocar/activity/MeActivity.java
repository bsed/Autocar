package com.huanyun.autocar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.huanyun.autocar.network.Api;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MeActivity extends BaseActivity {

	public static final String TAG = MeActivity.class
			.getSimpleName();
	public static MeActivity meActivity;
	private CommonTitleBar commonTitleBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		meActivity = this;
		setContentView(R.layout.activity_me);
		initView();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("我的");
		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findView(R.id.xiangyin_out_but).setOnClickListener(this);
		findView(R.id.cardview_order).setOnClickListener(this);
		((TextView)findViewById(R.id.user_tv)).setText(CommonSettingProvider.getUserName(MeActivity.this));

	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.xiangyin_out_but:
				this.finish();
				cleanData();
				Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				break;
			case R.id.cardview_order:
				loadNext(MyOrderActivity.class);
				break;
		}

	}

	private void cleanData(){
		CommonSettingProvider.setUserId(MeActivity.this, "");
		CommonSettingProvider.setUserName(MeActivity.this, "");
		CommonSettingProvider.setNickName(MeActivity.this, "");

		JPushInterface.setAlias(MeActivity.this, "", new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				// TODO Auto-generated method stub
			}
		});
	}


}
