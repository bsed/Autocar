package com.huanyun.autocar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.adapter.CarUsedTradeAdapter;
import com.huanyun.autocar.adapter.CarWashAdapter;
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
 * 二手车交易
 */
public class CarUsedTradeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
		RefreshLayout.OnLoadListener{

	public static final String TAG = CarUsedTradeActivity.class
			.getSimpleName();
	public static CarUsedTradeActivity carUsedTradeActivity;
	private CommonTitleBar commonTitleBar;
	private RefreshLayout refreshLayout;
	private ListView listView;
	private int page = 1;
	private boolean noMore;
	private static int pagesize = 10;
	private CarUsedTradeAdapter carUsedTradeAdapter;
	private List<Map<String, Object>> workData;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		carUsedTradeActivity = this;
		setContentView(R.layout.activity_car_used_trade);
		initView();
		setListener();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("二手车交易");
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
		if(carUsedTradeAdapter==null){
			carUsedTradeAdapter = new CarUsedTradeAdapter(CarUsedTradeActivity.this,workData);
		}
		listView.setAdapter(carUsedTradeAdapter);
	}

	private void setListener() {
		refreshLayout.setOnRefreshListener(this);
		refreshLayout.setOnLoadListener(this);
	}

	private void getProductList(int pageNo){
		Api api = new Api(CarUsedTradeActivity.this, new RequestCallBack<Object>() {
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

					if (carUsedTradeAdapter == null) {
						carUsedTradeAdapter = new CarUsedTradeAdapter(CarUsedTradeActivity.this, workData);
					}

					if (page > 1) {
						carUsedTradeAdapter.refreshData(workData);
						return;
					}

					listView.setAdapter(carUsedTradeAdapter);
					carUsedTradeAdapter.refreshData(workData);
					carUsedTradeAdapter.notifyDataSetInvalidated();

					if (workData.size() == 0) {
						// nothingImg.setVisibility(View.VISIBLE);
					} else {
						// nothingImg.setVisibility(View.GONE);
					}
				}else{
					Toast.makeText(CarUsedTradeActivity.this, error, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
		api.usedCarList(pageNo);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		carUsedTradeActivity = null;
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
					getProductList(page);
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
				getProductList(page);
			}
		}, 0);
	}
}
