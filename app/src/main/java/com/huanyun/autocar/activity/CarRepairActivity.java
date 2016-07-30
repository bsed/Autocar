package com.huanyun.autocar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.adapter.CarRepairAdapter;
import com.huanyun.autocar.adapter.CarWashAdapter;
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
 * 维修
 */
public class CarRepairActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
		RefreshLayout.OnLoadListener{

	public static final String TAG = CarWashActivity.class
			.getSimpleName();
	public static CarRepairActivity carRepairActivity;
	private CommonTitleBar commonTitleBar;
	private RefreshLayout refreshLayout;
	private ListView listView;
	private int page = 1;
	private boolean noMore;
	private static int pagesize = 10;
	private CarWashAdapter carWashAdapter;
	private List<Map<String, Object>> workData;
	private String latitude, longitude;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		carRepairActivity = this;
		latitude = CommonSettingProvider.getLatitude(CarRepairActivity.this);
		longitude = CommonSettingProvider.getLongitude(CarRepairActivity.this);
		setContentView(R.layout.activity_car_repair);
		initView();
		setListener();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("维修");
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
		refreshLayout = findView(R.id.refreshlayout);
		listView = findView(R.id.list);;
		refreshLayout.setColorSchemeResources(R.color.ykyin_orange,R.color.ykyin_green,R.color.ykyin_blue);
		refreshLayout.post(new Thread(new Runnable() {
			@Override
			public void run() {
				refreshLayout.setRefreshing(true);
			}
		}));
		initData();
		onRefresh();

	}

	private void initData(){
		workData = new ArrayList<Map<String, Object>>();
		if(carWashAdapter==null){
			carWashAdapter = new CarWashAdapter(CarRepairActivity.this,workData,2);
		}
		listView.setAdapter(carWashAdapter);
	}

	private void setListener() {
		refreshLayout.setOnRefreshListener(this);
		refreshLayout.setOnLoadListener(this);
	}

	private void getProductList(int pageNo, String longitude,String latitude,int type){
		Api api = new Api(CarRepairActivity.this, new RequestCallBack<Object>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				LogUtils.i("返回其他邀请-->" + arg0.result);
				refreshLayout.setRefreshing(false);
				Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
				if (dataMap == null) {
					return;
				}

				String error = (String) dataMap.get("error");
				if (error == null || error.equals("")) {

					List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataMap.get("list");
					if (dataList.size() % pagesize != 0) {
						noMore = true;
					}

					if (page == 1) {
						workData = new ArrayList<Map<String, Object>>();
					}

					JsonUtil.addList(workData, dataList);

					if (carWashAdapter == null) {
						carWashAdapter = new CarWashAdapter(CarRepairActivity.this, workData,2);
					}

					if (page > 1) {
						carWashAdapter.refreshData(workData);
						return;
					}

					listView.setAdapter(carWashAdapter);
					carWashAdapter.refreshData(workData);
					carWashAdapter.notifyDataSetInvalidated();

					if (workData.size() == 0) {
						// nothingImg.setVisibility(View.VISIBLE);
					} else {
						// nothingImg.setVisibility(View.GONE);
					}
				}else{
					Toast.makeText(CarRepairActivity.this,error,Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
		api.shopList(pageNo, longitude,latitude,type);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		carRepairActivity = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

	}


	@Override
	public void onLoad() {

		refreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshLayout.setLoading(false);
				if(!noMore){
					page++;
					getProductList(page, longitude,latitude,2);
				}else{
					refreshLayout.setLoading(false);
				}
			}
		}, 0);
	}

	@Override
	public void onRefresh() {
		refreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				noMore = false;
				page = 1;
				getProductList(page, longitude,latitude,2);
			}
		}, 0);
	}
}
