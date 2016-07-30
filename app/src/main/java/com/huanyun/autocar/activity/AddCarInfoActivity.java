package com.huanyun.autocar.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.GetUUid;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.Utils.LoadLocalImageUtil;
import com.huanyun.autocar.Utils.NetWorkUtils;
import com.huanyun.autocar.adapter.HListViewAdapter;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.huanyun.autocar.network.Api;
import com.huanyun.autocar.widget.ActionSheetDialog;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 添加车辆信息
 */
public class AddCarInfoActivity extends BaseActivity {

	public static final String TAG = AddCarInfoActivity.class
			.getSimpleName();
	public static AddCarInfoActivity addCarInfoActivity;
	private CommonTitleBar commonTitleBar;

	private TextView idCardHeadTv,xingshiz1Tv,jiashiz1Tv,idCardBackTv,xingshiz2Tv,jiashiz2Tv;
	private ImageView idCardHeadImg,xingshiz1Img,jiashiz1Img,idCardBackImg,xingshiz2Img,jiashiz2Img;

	private EditText carNoEdit,carPhoneEdit;


	public static final int TAKE_PHOTO = 990;
	public static final int CORP_IMAGE = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addCarInfoActivity = this;
		setContentView(R.layout.activity_add_car_info);
		initData();
		initView();
	}

	private void initView() {
		carNoEdit = findView(R.id.car_no_edit);
		carPhoneEdit = findView(R.id.car_phone_edit);
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("添加车辆信息");
		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commonTitleBar.setRightTxtBtn("上传");
		commonTitleBar.setRightBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkeData();
			}
		});

		idCardHeadTv = findView(R.id.id_card_head_tv);
		idCardHeadTv.setOnClickListener(this);

		xingshiz1Tv = findView(R.id.xingshiz_1_tv);
		xingshiz1Tv.setOnClickListener(this);

		jiashiz1Tv = findView(R.id.jiashiz_1_tv);
		jiashiz1Tv.setOnClickListener(this);

		idCardBackTv = findView(R.id.id_card_back_tv);
		idCardBackTv.setOnClickListener(this);

		xingshiz2Tv = findView(R.id.xingshiz_2_tv);
		xingshiz2Tv.setOnClickListener(this);

		jiashiz2Tv = findView(R.id.jiashiz_2_tv);
		jiashiz2Tv.setOnClickListener(this);

		idCardHeadImg = findView(R.id.id_card_head_img);
		xingshiz1Img = findView(R.id.driving_license_1_img);
		jiashiz1Img = findView(R.id.jiashiz_1_img);
		idCardBackImg = findView(R.id.id_card_back_img);
		xingshiz2Img = findView(R.id.xingshiz_2_img);
		jiashiz2Img = findView(R.id.jiashiz_2_img);

	}

	private void initData(){
		getQiniuToken();
		if(CommonSettingProvider.getUserId(AddCarInfoActivity.this).equals("")){
			Toast.makeText(AddCarInfoActivity.this, "暂未登录无法获取信息", Toast.LENGTH_SHORT).show();
		}else{
			getCarInfo();
		}
	}

	private void getCarInfo(){
		Api api = new Api(AddCarInfoActivity.this, new RequestCallBack<Object>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				LogUtils.i("返回其他邀请-->" + arg0.result);
				Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
				if (dataMap == null) {
					return;
				}
				String error = (String) dataMap.get("error");
				if (error == null || error.equals("")) {
					if(!dataMap.get("info").toString().equals("")){
					Map<String, Object> info = (Map<String, Object>) dataMap.get("info");
					if(info!=null){
						carNoEdit.setText(info.get("cpnum").toString());
						carPhoneEdit.setText(info.get("phone").toString());

						idCardHeadKey = info.get("idcard1").toString();
						idCardBackKey = info.get("idcard2").toString();
						xingshiz1Key = info.get("xscard1").toString();
						xingshiz2Key = info.get("xscard2").toString();
						jiashiz1Key = info.get("jscard1").toString();
						jiashiz2Key = info.get("jscard2").toString();

						LoadLocalImageUtil.getInstance().displayImageForNet(Api.QINIU_URL+idCardHeadKey, idCardHeadImg);
						LoadLocalImageUtil.getInstance().displayImageForNet(Api.QINIU_URL+idCardBackKey, idCardBackImg);
						LoadLocalImageUtil.getInstance().displayImageForNet(Api.QINIU_URL+xingshiz1Key, xingshiz1Img);
						LoadLocalImageUtil.getInstance().displayImageForNet(Api.QINIU_URL+xingshiz2Key, xingshiz2Img);
						LoadLocalImageUtil.getInstance().displayImageForNet(Api.QINIU_URL+jiashiz1Key, jiashiz1Img);
						LoadLocalImageUtil.getInstance().displayImageForNet(Api.QINIU_URL+jiashiz2Key, jiashiz2Img);
					}
					}
				}else{
					Toast.makeText(AddCarInfoActivity.this, error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
		api.getCarInfo(CommonSettingProvider.getUserId(AddCarInfoActivity.this));
	}

	String qiniuToken;
	private void getQiniuToken(){
		Api api = new Api(AddCarInfoActivity.this, new RequestCallBack<Object>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				LogUtils.i("返回其他邀请-->" + arg0.result);
				Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
				if (dataMap == null) {
					return;
				}
				Map<String, Object> responseMsg = (Map<String, Object>) dataMap.get("responseMsg");
				String error = (String) responseMsg.get("error");
				if (error == null || error.equals("")) {
					qiniuToken = responseMsg.get("qiniuToken").toString();
//					Toast.makeText(AddCarInfoActivity.this, qiniuToken, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(AddCarInfoActivity.this, error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
		api.getQiniuToken();
	}

	private String idCardHeadKey,xingshiz1Key,jiashiz1Key,idCardBackKey,xingshiz2Key,jiashiz2Key;
	public  void uploadImage(String path,String key,final String qiniutoken, final int type){
		if(path==null||path.equals("")){
			endLoading();
			Toast.makeText(AddCarInfoActivity.this,"请完善上传需要的图片资料",Toast.LENGTH_SHORT).show();
			return;
		}
		UploadManager uploadManager = new UploadManager();
		uploadManager.put(path, key, qiniutoken,
				new UpCompletionHandler() {
					@Override
					public void complete(String key, com.qiniu.android.http.ResponseInfo responseInfo, JSONObject jsonObject) {
						try {
							if (key == null || key.equals("")) {
								return;
							}
							if (responseInfo == null) {
								return;
							}
							if (jsonObject == null) {
								return;
							}
							Log.e("qiniu"+type, key);
							Log.e("qiniu"+type, responseInfo.toString());
							Log.e("qiniu"+type, jsonObject.toString());

//							Toast.makeText(AddCarInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
							if(type==1){
								idCardHeadKey = key;
							}else if(type==2){
								xingshiz1Key=key;
							}else if(type==3){
								jiashiz1Key=key;
							}else if(type==4){
								idCardBackKey=key;
							}else if(type==5){
								xingshiz2Key=key;
							}else if(type==6){
								jiashiz2Key=key;
							}

							addcarinfo();

						} catch (NullPointerException n) {
							endLoading();
							Toast.makeText(AddCarInfoActivity.this, "您的网络不给力呀,稍后操作~", Toast.LENGTH_SHORT).show();

						} catch (Exception e) {
							e.printStackTrace();
							endLoading();
						}
					}
				}, null);
	}


	String carNoS,carPhoneS;
	private void checkeData(){
		carNoS = carNoEdit.getText().toString();
		carPhoneS = carPhoneEdit.getText().toString();
		if(CommonSettingProvider.getUserId(AddCarInfoActivity.this).equals("")){
			Toast.makeText(AddCarInfoActivity.this,"请先登录后上传",Toast.LENGTH_SHORT).show();
		}
		if(carNoS==null||carNoS.equals("")){
			Toast.makeText(AddCarInfoActivity.this,"请填写车牌号",Toast.LENGTH_SHORT).show();
			return;
		}
		if(carPhoneS==null||carPhoneS.equals("")){
			Toast.makeText(AddCarInfoActivity.this,"请填写联系电话",Toast.LENGTH_SHORT).show();
			return;
		}
//		if(idCardHeadFilePath==null||idCardHeadFilePath.equals("")||
//		xingshiz1FilePath==null||xingshiz1FilePath.equals("")||
//				jiashiz1FilePath==null||jiashiz1FilePath.equals("")||
//				idCardBackFilePath==null||idCardBackFilePath.equals("")||
//				xingshiz2FilePath==null||xingshiz2FilePath.equals("")||
//				jiashiz2FilePath==null||jiashiz2FilePath.equals("")){
//			Toast.makeText(AddCarInfoActivity.this,"请完善上传需要的图片资料",Toast.LENGTH_SHORT).show();
//			return;
//		}

		if(qiniuToken!=null) {

			if (NetWorkUtils.isNetworkAvailable(AddCarInfoActivity.this)) {

				beginLoading(AddCarInfoActivity.this);

				if (idCardHeadKey == null || idCardHeadKey.equals("")) {
					uploadImage(idCardHeadFilePath, GetUUid.getUUID(), qiniuToken, 1);
				}
				if (xingshiz1Key == null || xingshiz1Key.equals("")) {
					uploadImage(xingshiz1FilePath, GetUUid.getUUID(), qiniuToken, 2);
				}
				if (jiashiz1Key == null || jiashiz1Key.equals("")) {
					uploadImage(jiashiz1FilePath, GetUUid.getUUID(), qiniuToken, 3);
				}
				if (idCardBackKey == null || idCardBackKey.equals("")) {
					uploadImage(idCardBackFilePath, GetUUid.getUUID(), qiniuToken, 4);
				}
				if (xingshiz2Key == null || xingshiz2Key.equals("")) {
					uploadImage(xingshiz2FilePath, GetUUid.getUUID(), qiniuToken, 5);
				}
				if (jiashiz2Key == null || jiashiz2Key.equals("")) {
					uploadImage(jiashiz2FilePath, GetUUid.getUUID(), qiniuToken, 6);
				}
				addcarinfo();
			}
		}else{
			Toast.makeText(AddCarInfoActivity.this,"请在网络状况良好的情况下上传",Toast.LENGTH_SHORT).show();
		}
	}


	private void addcarinfo(){
		if(idCardHeadKey!=null&&!idCardHeadKey.equals("")
				&&xingshiz1Key!=null&&!xingshiz1Key.equals("")
				&&jiashiz1Key!=null&&!jiashiz1Key.equals("")
				&&idCardBackKey!=null&&!idCardBackKey.equals("")
				&&xingshiz2Key!=null&&!xingshiz2Key.equals("")
				&&jiashiz2Key!=null&&!jiashiz2Key.equals("")){

			Api api = new Api(AddCarInfoActivity.this, new RequestCallBack<Object>() {
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
					Toast.makeText(AddCarInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(AddCarInfoActivity.this, error, Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					endLoading();
				}
			});
			api.addCarInfo(
					CommonSettingProvider.getUserId(AddCarInfoActivity.this),
					carNoS,
					carPhoneS,
					idCardHeadKey,
					idCardBackKey,
					xingshiz1Key,
					xingshiz2Key,
					jiashiz1Key,
					jiashiz2Key
			);
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        LogUtils.i(requestCode + "   " + resultCode);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
			case TAKE_PHOTO:
				try {
//                    LogUtils.i("相机的回调");
//					cropImageUriByTakePhoto();
					if(clickIndex == 1) {
						if (idCardHeadFilePath != null && !idCardHeadFilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(idCardHeadFilePath, idCardHeadImg);
							idCardHeadKey="";
						}
					}else if(clickIndex==2){
						if (xingshiz1FilePath != null && !xingshiz1FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(xingshiz1FilePath, xingshiz1Img);
							xingshiz1Key="";
						}
					}else if(clickIndex==3){
						if (jiashiz1FilePath != null && !jiashiz1FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(jiashiz1FilePath, jiashiz1Img);
							jiashiz1Key="";
						}
					}else if(clickIndex==4){
						if (idCardBackFilePath != null && !idCardBackFilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(idCardBackFilePath, idCardBackImg);
							idCardBackKey="";
						}
					}else if(clickIndex==5){
						if (xingshiz2FilePath != null && !xingshiz2FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(xingshiz2FilePath, xingshiz2Img);
							xingshiz2Key="";
						}
					}else if(clickIndex==6){
						if (jiashiz2FilePath != null && !jiashiz2FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(jiashiz2FilePath, jiashiz2Img);
							jiashiz2Key="";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case CORP_IMAGE:
				try {
					if(clickIndex == 1) {
						if (idCardHeadFilePath != null && !idCardHeadFilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(idCardHeadFilePath, idCardHeadImg);
							idCardHeadKey="";
						}
					}else if(clickIndex==2){
						if (xingshiz1FilePath != null && !xingshiz1FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(xingshiz1FilePath, xingshiz1Img);
							xingshiz1Key="";
						}
					}else if(clickIndex==3){
						if (jiashiz1FilePath != null && !jiashiz1FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(jiashiz1FilePath, jiashiz1Img);
							jiashiz1Key="";
						}
					}else if(clickIndex==4){
						if (idCardBackFilePath != null && !idCardBackFilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(idCardBackFilePath, idCardBackImg);
							idCardBackKey="";
						}
					}else if(clickIndex==5){
						if (xingshiz2FilePath != null && !xingshiz2FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(xingshiz2FilePath, xingshiz2Img);
							xingshiz2Key="";
						}
					}else if(clickIndex==6){
						if (jiashiz2FilePath != null && !jiashiz2FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(jiashiz2FilePath, jiashiz2Img);
							jiashiz2Key="";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 777:
				if(data==null){
					Toast.makeText(AddCarInfoActivity.this,"获取图片地址失败",Toast.LENGTH_SHORT).show();
					return;
				}
				Uri uri = data.getData();
				if(uri==null){
					Toast.makeText(AddCarInfoActivity.this,"获取图片地址失败",Toast.LENGTH_SHORT).show();
					return;
				}
				String mImagePath = getImageAbsolutePath(AddCarInfoActivity.this,uri);
				if(mImagePath==null){
					Toast.makeText(AddCarInfoActivity.this,"获取图片地址失败",Toast.LENGTH_SHORT).show();
					return;
				}
				if(clickIndex==1){
					idCardHeadFilePath = mImagePath;
				}
				if(clickIndex == 2){
					xingshiz1FilePath = mImagePath;
				}
				if(clickIndex == 3){
					jiashiz1FilePath = mImagePath;
				}
				if(clickIndex == 4){
					idCardBackFilePath = mImagePath;
				}
				if(clickIndex == 5){
					xingshiz2FilePath = mImagePath;
				}
				if(clickIndex == 6){
					jiashiz2FilePath = mImagePath;
				}

				try {
					if(clickIndex == 1) {
						if (idCardHeadFilePath != null && !idCardHeadFilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(idCardHeadFilePath, idCardHeadImg);
							idCardHeadKey="";
						}
					}else if(clickIndex==2){
						if (xingshiz1FilePath != null && !xingshiz1FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(xingshiz1FilePath, xingshiz1Img);
							xingshiz1Key="";
						}
					}else if(clickIndex==3){
						if (jiashiz1FilePath != null && !jiashiz1FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(jiashiz1FilePath, jiashiz1Img);
							jiashiz1Key="";
						}
					}else if(clickIndex==4){
						if (idCardBackFilePath != null && !idCardBackFilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(idCardBackFilePath, idCardBackImg);
							idCardBackKey="";
						}
					}else if(clickIndex==5){
						if (xingshiz2FilePath != null && !xingshiz2FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(xingshiz2FilePath, xingshiz2Img);
							xingshiz2Key="";
						}
					}else if(clickIndex==6){
						if (jiashiz2FilePath != null && !jiashiz2FilePath.equals("")) {
							LoadLocalImageUtil.getInstance().displayFromSDCard(jiashiz2FilePath, jiashiz2Img);
							jiashiz2Key="";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
		}
	}

	private int clickIndex = 1;

	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.id_card_head_tv:
				clickIndex = 1;
				dialogChangeHead();
				break;
			case R.id.xingshiz_1_tv:
				clickIndex = 2;
				dialogChangeHead();
				break;
			case R.id.jiashiz_1_tv:
				clickIndex = 3;
				dialogChangeHead();
				break;
			case R.id.id_card_back_tv:
				clickIndex = 4;
				dialogChangeHead();
				break;
			case R.id.xingshiz_2_tv:
				clickIndex = 5;
				dialogChangeHead();
				break;
			case R.id.jiashiz_2_tv:
				clickIndex = 6;
				dialogChangeHead();
				break;
		}
	}

	private void dialogChangeHead() {
		new ActionSheetDialog(AddCarInfoActivity.this)
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								startCameraIntent();
							}
						})
				.addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								doCropPhoto();
							}
						}).show();
	}

	public File picFile;
	public Uri photoUri;
	private String idCardHeadFilePath,xingshiz1FilePath,jiashiz1FilePath,idCardBackFilePath,xingshiz2FilePath,jiashiz2FilePath;
	public void startCameraIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String mImagePath = getUploadMediaPath() + "/tmp_upload_"
				+ System.currentTimeMillis() + ".jpg";
		if(clickIndex==1){
			idCardHeadFilePath = mImagePath;
		}
		if(clickIndex == 2){
			xingshiz1FilePath = mImagePath;
		}
		if(clickIndex == 3){
			jiashiz1FilePath = mImagePath;
		}
		if(clickIndex == 4){
			idCardBackFilePath = mImagePath;
		}
		if(clickIndex == 5){
			xingshiz2FilePath = mImagePath;
		}
		if(clickIndex == 6){
			jiashiz2FilePath = mImagePath;
		}
		picFile = new File(mImagePath);
		if (picFile.exists()) {
			boolean ret = picFile.delete();
			Log.e("删除零时文件---->" + ret, "");
		}
		if (!picFile.exists()) {
			try {
				picFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		photoUri = Uri.fromFile(picFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

		Log.e("路径:" + mImagePath,"");
		startActivityForResult(intent, TAKE_PHOTO);
	}

	protected void doCropPhoto() {
		try {
			String mImagePath = getUploadMediaPath() + "/tmp_upload_"
					+ System.currentTimeMillis() + ".jpg";

			if(clickIndex==1){
				idCardHeadFilePath = mImagePath;
			}
			if(clickIndex == 2){
				xingshiz1FilePath = mImagePath;
			}
			if(clickIndex == 3){
				jiashiz1FilePath = mImagePath;
			}
			if(clickIndex == 4){
				idCardBackFilePath = mImagePath;
			}
			if(clickIndex == 5){
				xingshiz2FilePath = mImagePath;
			}
			if(clickIndex == 6){
				jiashiz2FilePath = mImagePath;
			}

			picFile = new File(mImagePath);

			if (picFile.exists()) {
				boolean ret = picFile.delete();
			}
			if (!picFile.exists()) {
				try {
					picFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		/*	photoUri = Uri.fromFile(picFile);
			final Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setType("image*//*");
			intent.putExtra("crop", "true");
//			intent.putExtra("aspectX", 1);
//			intent.putExtra("aspectY", 1);
//			intent.putExtra("outputX", 150);
//			intent.putExtra("outputY", 150);
//			intent.putExtra("noFaceDetection", true);
//			intent.putExtra("scale", true);
//			intent.putExtra("return-data", false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, CORP_IMAGE);*/


			Intent local = new Intent();
			local.setType("image/*");
			local.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(local, 777);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUploadMediaPath() {
		File f = new File(Environment.getExternalStorageDirectory() + "/cheguanjia"
				+ "/uploadMedia/");
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		addCarInfoActivity = null;
		super.onDestroy();
	}

	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}


	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

}
