package com.huanyun.autocar.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class RegistActivity extends BaseActivity {

	public static final String TAG = RegistActivity.class
			.getSimpleName();
	private RegistActivity timePackRegistInputPhoneActivity;
	private CommonTitleBar commonTitleBar;
	private TextView getVerificationTv,messageTv;
	private EditText xiangyinEditVerification,xiangyinEditPwd,xiangyinEditConfirmPwd;

	private String phoneNumber;
	private final int GET_PWD = 1;
	private final int GET_PWD_AGAIN = 2;
	private int regist_or_forget;
	private EditText registCarEdit,driverGetEdit;


	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GET_PWD:
					getVerificationTv.setEnabled(false);
					getVerificationTv.setText("" + count + "秒");
					break;
				case GET_PWD_AGAIN:
					getVerificationTv.setEnabled(true);
					getVerificationTv.setText("重新获取");
					break;

				default:
					break;
			}
		};
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		timePackRegistInputPhoneActivity = this;
		setContentView(R.layout.activity_regist);
		initData();
		initView();
		getVertifyCode(phoneNumber);
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		if(regist_or_forget==0){
			commonTitleBar.setTitleTxt("忘记密码");
		}else{
			commonTitleBar.setTitleTxt("设置登录密码");
		}

		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findView(R.id.xiangyin_confirm_but).setOnClickListener(this);

		registCarEdit = findView(R.id.register_car_edit);
		driverGetEdit = findView(R.id.driver_get_edit);
		registCarEdit.setOnClickListener(this);
		driverGetEdit.setOnClickListener(this);

		messageTv = findView(R.id.message_tv);
		messageTv.setText("点击向"+phoneNumber+"发送验证码");
		getVerificationTv = findView(R.id.get_verification_tv);
		getVerificationTv.setText("获取验证码");
		getVerificationTv.setOnClickListener(this);

		xiangyinEditVerification = findView(R.id.xiangyin_edit_verification);
		xiangyinEditPwd = findView(R.id.xiangyin_edit_pwd);
		xiangyinEditConfirmPwd = findView(R.id.xiangyin_edit_confirm_pwd);
	}

	private void initData(){
		if(getIntent()!=null) {
			phoneNumber = getIntent().getExtras().getString("phone_number");
			regist_or_forget = getIntent().getExtras().getInt("regist_or_forget");
		}else{
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		timePackRegistInputPhoneActivity = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.xiangyin_confirm_but){

			String verificationCode = xiangyinEditVerification.getText().toString();
			String pwd = xiangyinEditPwd.getText().toString();
			String pwdConfirm = xiangyinEditConfirmPwd.getText().toString();

			if(verificationCode==null||verificationCode.equals("")){
				Toast.makeText(RegistActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
				return;
			}
//			if(verificationCode.length() !=4){
//				Toast.makeText(RegistActivity.this,"请输入4位验证",Toast.LENGTH_SHORT).show();
//				return;
//			}

			if(pwd==null||pwd.equals("")){
				Toast.makeText(RegistActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
				return;
			}

			if(pwd.length()<6){
				Toast.makeText(RegistActivity.this,"请最少输入6位密码",Toast.LENGTH_SHORT).show();
				return;
			}

			if(pwdConfirm==null||pwdConfirm.equals("")){
				Toast.makeText(RegistActivity.this,"请再次输入密码",Toast.LENGTH_SHORT).show();
				return;
			}
			if(!pwd.equals(pwdConfirm)){
				Toast.makeText(RegistActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
				return;
			}

			if(regist_or_forget==1){
				addNewUser(phoneNumber,pwd,registCarEdit.getText().toString(),driverGetEdit.getText().toString());
			}else{
				modifyPwd(phoneNumber, pwd, verificationCode);
			}


		}else if(v.getId()==R.id.get_verification_tv){
			getVertifyCode(phoneNumber);
		}else if(v.getId()==R.id.register_car_edit){
			showTimePackerDialog(true);
		}else if(v.getId()==R.id.driver_get_edit){
			showTimePackerDialog(false);
		}
	}

		private void getVertifyCode(String phone){
			Api api = new Api(RegistActivity.this, new RequestCallBack<Object>() {
				@Override
				public void onStart() {
					super.onStart();
				}

				@Override
				public void onSuccess(ResponseInfo<Object> arg0) {
					LogUtils.i("返回其他邀请-->" + arg0.result);
					endLoading();
					Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
					String error = dataMap.get("error").toString();
					if (error == null || error.equals("")) {
						messageTv.setText("已向"+phoneNumber+"发送验证码,注意查收");
						startTimer();
					}else{
						Toast.makeText(RegistActivity.this, error, Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					endLoading();
				}
			});
			api.verificationSMS(phone);
		}


	private void addNewUser(String phonenum, String password,
							String djdate,String lqdate){
		beginLoading(RegistActivity.this);

		Api api = new Api(RegistActivity.this, new RequestCallBack<Object>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				LogUtils.i("返回其他邀请-->" + arg0.result);
				endLoading();
				Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
				String error = (String) dataMap.get("error");
//				{"error":"","user":{"id":"7","username":"18036393732"}}
				if (error == null || error.equals("")) {
					Map<String, Object> user = (Map<String, Object>) dataMap.get("user");
					if(user!=null){
						Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
						CommonSettingProvider.setUserId(RegistActivity.this, user.get("id").toString());
						CommonSettingProvider.setUserName(RegistActivity.this, user.get("username").toString());
						if(RegistInputPhoneActivity.timePackRegistInputPhoneActivity!=null){
							RegistInputPhoneActivity.timePackRegistInputPhoneActivity.finish();
						}
						if(LoginActivity.loginActivity!=null){
							LoginActivity.loginActivity.finish();
						}

						JPushInterface.setAlias(RegistActivity.this, user.get("username").toString(), new TagAliasCallback() {
							@Override
							public void gotResult(int arg0, String arg1, Set<String> arg2) {
								// TODO Auto-generated method stub
							}
						});

						finish();
					}
				}else{
					Toast.makeText(RegistActivity.this, error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				endLoading();
			}
		});
		api.register(phonenum,password,djdate,lqdate);
	}

	private void modifyPwd(String telephone,String plainpassword,String code){
		beginLoading(RegistActivity.this);

	}

	/**
	 * 等待的秒数
	 */
	int count;
	private void startTimer() {
		count = 120;
		// 获取验证码
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (count > 0) {
					Message msg = handler.obtainMessage();
					msg.what = GET_PWD;
					handler.sendMessage(msg);
				} else {
					Message msg = handler.obtainMessage();
					msg.what = GET_PWD_AGAIN;
					handler.sendMessage(msg);
					this.cancel();
				}
				count--;

			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, 1000);
	}

	boolean registerCar = true;
	Calendar calendar = Calendar.getInstance();
	Dialog dialog = null;
	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int year,
							  int month, int dayOfMonth) {
            // Calendar月份是从0开始,所以month要加1
			if(registerCar){
				registCarEdit.setText(year + "-" + (month + 1) + "-"+ dayOfMonth);
			}else{
				driverGetEdit.setText(year + "-" + (month + 1) + "-"+ dayOfMonth);
			}
		}
	};

	private void showTimePackerDialog(boolean registerCar) {
		this.registerCar = registerCar;
		dialog = new DatePickerDialog(this, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

}
