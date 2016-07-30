package com.huanyun.autocar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.adapter.CarRepairAdapter;
import com.huanyun.autocar.adapter.CarWashAdapter;
import com.huanyun.autocar.application.MyApplication;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.huanyun.autocar.network.Api;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.huanyun.autocar.widget.RefreshLayout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 道路救援
 */
public class RoadHelpActivity extends BaseActivity {

	public static final String TAG = RoadHelpActivity.class
			.getSimpleName();
	public static RoadHelpActivity roadHelpActivity;
	private CommonTitleBar commonTitleBar;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	MapView mMapView;
	BaiduMap mBaiduMap;

	private String latitude, longitude;
	private String address;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		roadHelpActivity = this;
		latitude = CommonSettingProvider.getLatitude(RoadHelpActivity.this);
		longitude = CommonSettingProvider.getLongitude(RoadHelpActivity.this);
		address = MyApplication.address;
		setContentView(R.layout.activity_road_help);
		initView();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("道路救援");
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
        findView(R.id.send_line).setOnClickListener(this);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
							// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

				Log.i(TAG, "Addr2 = " + location.getAddrStr());
				Log.i(TAG, "ltitude2 = " + location.getAltitude());
				Log.i(TAG, "City2 = " + location.getCity());
				Log.i(TAG, "Direction2 = " + location.getDirection());
				Log.i(TAG, "Floor2 = " + location.getFloor());
				Log.i(TAG, "Latitude2 = " + location.getLatitude());
				Log.i(TAG, "Longitude2 = " + location.getLongitude());
				Log.i(TAG, "Province2 = " + location.getProvince());

				latitude = location.getLatitude()+"";
				longitude = location.getLongitude()+"";
				if(location.getAddrStr()==null||location.getAddrStr().equals("")){
					address = MyApplication.address;
				}else{
					address = location.getAddrStr();
				}


			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void shopHelp(String userId, final String address,String longitude,String latitude){
		beginLoading(RoadHelpActivity.this);
		Api api = new Api(RoadHelpActivity.this, new RequestCallBack<Object>() {
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
					Toast.makeText(RoadHelpActivity.this, "您当前的位置是"+address+",救援信息已发送成功!", Toast.LENGTH_LONG).show();

				}else{
					Toast.makeText(RoadHelpActivity.this, error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				endLoading();
			}
		});
		api.shopHelp(userId,address, longitude, latitude);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.send_line:
				if(CommonSettingProvider.getUserId(RoadHelpActivity.this).equals("")){
					Toast.makeText(RoadHelpActivity.this,"请先登录后才能发送救援信息",Toast.LENGTH_SHORT).show();
				}else{
					if(address==null||address.equals("")){
						Toast.makeText(RoadHelpActivity.this,"用户定位失败，请开启GPS后尝试",Toast.LENGTH_SHORT).show();
					}else{
						shopHelp(CommonSettingProvider.getUserId(RoadHelpActivity.this),address,longitude,latitude);
					}
				}
				break;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
}
