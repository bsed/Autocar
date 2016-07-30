package com.huanyun.autocar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.widget.CommonTitleBar;

public class RegistInputPhoneActivity extends BaseActivity {

	public static final String TAG = RegistInputPhoneActivity.class
			.getSimpleName();
	public static RegistInputPhoneActivity timePackRegistInputPhoneActivity;
	private CommonTitleBar commonTitleBar;
	private EditText xiangyinEditPhone;
    private int regist_or_forget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		timePackRegistInputPhoneActivity = this;
		setContentView(R.layout.activity_regist_input_phone);
		initData();
		initView();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		if(regist_or_forget==0){
			commonTitleBar.setTitleTxt("忘记密码");
		}else{
			commonTitleBar.setTitleTxt("手机号注册登录");
		}

		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findView(R.id.xiangyin_next_but).setOnClickListener(this);
		xiangyinEditPhone = findView(R.id.xiangyin_edit_phone);

	}

	private void initData(){
		if(getIntent()!=null){
			regist_or_forget = getIntent().getExtras().getInt("regist_or_forget");
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
		if(v.getId()==R.id.xiangyin_next_but){

			String phonenumber = xiangyinEditPhone.getText().toString();

			if(phonenumber==null||phonenumber.equals("")){
				Toast.makeText(RegistInputPhoneActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
				return;
			}
			if(phonenumber.length() !=11){
				Toast.makeText(RegistInputPhoneActivity.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
				return;
			}

			checkPhone(phonenumber);

		}

	}


	private void checkPhone(final String phone){
//		beginLoading(TimePackRegistInputPhoneActivity.this);
		if(regist_or_forget==0){
			Intent intent = new Intent(RegistInputPhoneActivity.this,ForgetPwdActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("phone_number", phone);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			Intent intent = new Intent(RegistInputPhoneActivity.this,RegistActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("phone_number",phone);
			bundle.putInt("regist_or_forget",regist_or_forget);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}


}
