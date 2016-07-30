package com.huanyun.autocar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.huanyun.autocar.interfaces.OnDateSelected;
import com.huanyun.autocar.network.Api;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.huanyun.autocar.widget.DatePicker;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * 审车
 */
public class WhoseCarActivity extends BaseActivity {

	public static final String TAG = WhoseCarActivity.class
			.getSimpleName();
	public static WhoseCarActivity whoseCarActivity;
	private CommonTitleBar commonTitleBar;
	private DatePicker datePicker;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		whoseCarActivity = this;
		setContentView(R.layout.activity_whose_car);
		initData();
		initView();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("审车");
		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		commonTitleBar.setRightImg(R.mipmap.ic_launcher);
		commonTitleBar.setRightBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});

		datePicker = findView(R.id.datepicker);
		datePicker.setOnDateSelected(new OnDateSelected() {
			@Override
			public void selected(List<String> date) {
//				for (String s : date) {
//					Toast.makeText(WhoseCarActivity.this, s, Toast.LENGTH_SHORT).show();
//				}
				if(CommonSettingProvider.getUserId(WhoseCarActivity.this).equals("")){
					Toast.makeText(WhoseCarActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
				}else {
					if (date.size() < 1) {
						Toast.makeText(WhoseCarActivity.this, "请选择日期", Toast.LENGTH_SHORT).show();
					} else {
						shenChe(CommonSettingProvider.getUserId(WhoseCarActivity.this),date.get(date.size() - 1));
					}
				}
			}
		});


	}

	private void initData(){
	}

	private void shenChe(String userId,String scdate){
		beginLoading(WhoseCarActivity.this);
		Api api = new Api(WhoseCarActivity.this, new RequestCallBack<Object>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				LogUtils.i("返回其他邀请-->" + arg0.result);
				endLoading();
				Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
				if (dataMap == null) {
					return;
				}

				String error = (String) dataMap.get("error");
				if (error == null || error.equals("")) {
					Toast.makeText(WhoseCarActivity.this, "日期提交成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(WhoseCarActivity.this, error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				endLoading();
			}
		});
		api.shenChe(userId,scdate);
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		whoseCarActivity = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

	}


}
