package com.huanyun.autocar.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import cn.jpush.android.api.JPushInterface;

@SuppressLint("NewApi")
public final class MyApplication extends Application {
	private static final String TAG = MyApplication.class.getSimpleName();
	
    private static MyApplication instance;
    private static Context mContext;

    public static double latitude, longitude;
    public static String city;
    public static String province;
    public static String address;
    public static LocationClient mLocationClient;
    public static MyLocationListener mMyLocationListener;

    @Override
    public void onCreate() {
		super.onCreate();
		instance = this;
		mContext = getApplicationContext();
        initLibrary(mContext);
        SDKInitializer.initialize(this);

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
        refreshLocation();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }

    private static  void initLibrary(Context context){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            //Receive Location
            if(location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address=location.getAddrStr();
                province=location.getProvince();
                city = location.getCity();
            }
            Log.i(TAG, "Addr = " + location.getAddrStr());
            Log.i(TAG, "ltitude = " + location.getAltitude());
            Log.i(TAG, "City = " + location.getCity());
            Log.i(TAG, "Direction = " + location.getDirection());
            Log.i(TAG, "Floor = " + location.getFloor());
            Log.i(TAG, "Latitude = " + location.getLatitude());
            Log.i(TAG, "Longitude = " + location.getLongitude());
            Log.i(TAG, "Province = " + location.getProvince());

            if(location.getCity()!=null&&!location.getCity().equals("")){
                CommonSettingProvider.setLatitude(mContext, latitude+"");
                CommonSettingProvider.setLongitude(mContext, longitude+"");
//                CommonSettingProvider.MainSet.setCurCityName(mContext,city);
            }
        }
    }

    private static void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setOpenGps(true);
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
//		int span=5000;
//		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
    }

    public static void refreshLocation() {
        if (mLocationClient != null) {
            // 开始定位
            mLocationClient.start();
            mLocationClient.requestLocation();
        } else {
            mLocationClient = new LocationClient(mContext);
            mLocationClient.registerLocationListener(mMyLocationListener);
            initLocation();
            // 开始定位
            mLocationClient.start();
            mLocationClient.requestLocation();
        }
    }
}