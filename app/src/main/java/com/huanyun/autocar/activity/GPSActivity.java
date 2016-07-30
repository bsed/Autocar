package com.huanyun.autocar.activity;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.application.MyApplication;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.huanyun.autocar.network.Api;
import com.huanyun.autocar.widget.ActionSheetDialog;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.util.Map;

/**
 * 导航
 */
public class GPSActivity extends BaseActivity {

	public static final String TAG = GPSActivity.class
			.getSimpleName();
	public static GPSActivity roadHelpActivity;
	private CommonTitleBar commonTitleBar;

	MapView mMapView;
	BaiduMap mBaiduMap;
	BitmapDescriptor bdA;
	InfoWindow mInfoWindow;
	View viewCache = null;
	View popupInfoRoot = null;
	TextView popupName = null;
	Button popupNavi = null;

	private String latitudeGo, longitudeGo;
	private String addressGo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		roadHelpActivity = this;
		setContentView(R.layout.activity_gps);
		initData();
		initView();
	}

	private void initData(){
		if(getIntent()!=null){
			latitudeGo = getIntent().getExtras().getString("latitude");
			longitudeGo = getIntent().getExtras().getString("longitude");
			addressGo = getIntent().getExtras().getString("address");
		}
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("导航");
		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commonTitleBar.setRightBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

		viewCache = getLayoutInflater().inflate(R.layout.custom_map_info, null);
		popupInfoRoot = (View) viewCache.findViewById(R.id.popup_info);
		popupName = (TextView) viewCache.findViewById(R.id.popup_name);
		popupNavi = (Button) viewCache.findViewById(R.id.navi);

		mapClickListener();
		initOverlay();
	}

	public void initOverlay() {
			try {
				LatLng point = new LatLng(Double.parseDouble(latitudeGo),
						Double.parseDouble(longitudeGo));

				MarkerOptions option = new MarkerOptions().position(point)
						.icon(bdA).zIndex(1);
				mBaiduMap.addOverlay(option);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
				mBaiduMap.setMapStatus(u);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
	}

	private void mapClickListener(){
		popupNavi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNaviDialog();
			}
		});
		mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
			}
		});
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@SuppressWarnings("unchecked")
			public boolean onMarkerClick(final Marker marker) {
				//final HotelBaseVO cur = (HotelBaseVO) marker.getExtraInfo().getSerializable("info");
				//cur.getPicture()
				popupName.setText(addressGo);
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
					mInfoWindow = new InfoWindow(viewCache, llInfo,0);
					mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}


	private void showNaviDialog(){
		new ActionSheetDialog(GPSActivity.this)
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("客户端导航", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								startNavi();

							}
						})
				.addSheetItem("在线导航", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
									startWebNavi();
							}
						}).show();
	}

	/**
	 * 开始导航 开启客户端导航
	 *
	 * @param
	 */
	public void startNavi() {
		LatLng pt1 = new LatLng(MyApplication.latitude, MyApplication.longitude);
		LatLng pt2 = new LatLng(Double.valueOf(latitudeGo),Double.valueOf(longitudeGo));

		// 构建 导航参数
		NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("从这里开始").endName("到这里结束");;
		try {
			BaiduMapNavigation.openBaiduMapNavi(para, this);
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			final AlertDialog.Builder builder = new AlertDialog.Builder(GPSActivity.this);
			builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					OpenClientUtil.getLatestBaiduMapApp(GPSActivity.this);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
	}

	public void startWebNavi() {
		try {
			LatLng pt1 = new LatLng(MyApplication.latitude, MyApplication.longitude);
			LatLng pt2 = new LatLng(Double.valueOf(latitudeGo),Double.valueOf(longitudeGo));
			// 构建 导航参数
			NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2);
			BaiduMapNavigation.openWebBaiduMapNavi(para, this);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

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
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
}
