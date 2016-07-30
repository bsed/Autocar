package com.huanyun.autocar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends BaseActivity {

	public static final String TAG = LoginActivity.class
			.getSimpleName();
	public static LoginActivity loginActivity;
	private CommonTitleBar commonTitleBar;
	private EditText xiangyinEditPhone,xiangyinEditPwd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loginActivity = this;
		setContentView(R.layout.activity_login);
		initData();
		initView();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("登录");
		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findView(R.id.xiangyin_login_but).setOnClickListener(this);
		findView(R.id.timepack_forget_tv).setOnClickListener(this);
		findView(R.id.timepack_regist_tv).setOnClickListener(this);
		xiangyinEditPhone = findView(R.id.xiangyin_edit_phone);
		xiangyinEditPwd = findView(R.id.xiangyin_edit_pwd);

	}

	private void initData(){
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		loginActivity = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.xiangyin_login_but){

			String phonenumber = xiangyinEditPhone.getText().toString();
			String password = xiangyinEditPwd.getText().toString();

			if(phonenumber==null||phonenumber.equals("")){
				Toast.makeText(LoginActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
				return;
			}
			if(phonenumber.length()!=11){
				Toast.makeText(LoginActivity.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
				return;
			}
			if(password==null||password.equals("")){
				Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
				return;
			}

			login(phonenumber,password);

		}else if(v.getId()==R.id.timepack_forget_tv){
			Intent intent = new Intent(LoginActivity.this,RegistInputPhoneActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("regist_or_forget",0);
			intent.putExtras(bundle);
			startActivity(intent);

		}else if(v.getId()==R.id.timepack_regist_tv){
			Intent intent = new Intent(LoginActivity.this,RegistInputPhoneActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("regist_or_forget", 1);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

	private void login(String phone,String pwd){
		beginLoading(LoginActivity.this);
		Api api = new Api(LoginActivity.this, new RequestCallBack<Object>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				LogUtils.i("返回其他邀请-->" + arg0.result);
				endLoading();
				Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
				String error = (String)dataMap.get("error");
				if(error==null||error.equals("")){
					Map<String, Object> user = (Map<String, Object>) dataMap.get("user");
//					"user":{"id":"2","username":"18013504686","nickname":"","password":"e10adc3949ba59abbe56e057f20f883e"}
					CommonSettingProvider.setUserId(LoginActivity.this, (String) user.get("id"));
					CommonSettingProvider.setUserName(LoginActivity.this, (String) user.get("username"));
					CommonSettingProvider.setNickName(LoginActivity.this, (String) user.get("nickname"));
					Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
					finish();

					JPushInterface.setAlias(LoginActivity.this, user.get("username").toString(), new TagAliasCallback() {
						@Override
						public void gotResult(int arg0, String arg1, Set<String> arg2) {
							// TODO Auto-generated method stub
						}
					});

				}else{
					Toast.makeText(LoginActivity.this,error,Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				endLoading();
			}
		});
		api.login(phone, pwd);
	}


}
