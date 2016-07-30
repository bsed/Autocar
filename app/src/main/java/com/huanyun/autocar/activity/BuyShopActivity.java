package com.huanyun.autocar.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.NetWorkUtils;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.huanyun.autocar.widget.HTML5WebView;

/**
 * 购买
 */
public class BuyShopActivity extends BaseActivity {

	public static final String TAG = BuyShopActivity.class
			.getSimpleName();
	public static BuyShopActivity buyShopActivity;
	private CommonTitleBar commonTitleBar;

	private String tel,name,price,type,id,pcontent;
	private EditText numberEt;
	private LinearLayout liner_main;
	private HTML5WebView mWebView;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buyShopActivity = this;
		setContentView(R.layout.activity_buy_shop);
		initData();
		initView();
	}

	private void initData(){
		if(getIntent()!=null){
			tel = getIntent().getExtras().getString("tel");
			name = getIntent().getExtras().getString("name");
			price = getIntent().getExtras().getString("price");
			type = getIntent().getExtras().getString("type");
			id = getIntent().getExtras().getString("id");
			pcontent = getIntent().getExtras().getString("pcontent");
		}
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt(name);
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
		findView(R.id.add_tv).setOnClickListener(this);
		findView(R.id.cut_tv).setOnClickListener(this);
		findView(R.id.buy_tv).setOnClickListener(this);
		numberEt = findView(R.id.number_et);

		liner_main = findView(R.id.liner_main);
		mWebView = new HTML5WebView(this);
		mWebView.requestFocus();

		mWebView.loadUrl(pcontent);

		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
		liner_main.addView(mWebView.getLayout(), params);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.setVerticalScrollBarEnabled(false);

		//支持javascript
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		mWebView.getSettings().setSupportZoom(true);
		// 设置出现缩放工具
		mWebView.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.getSettings().setLoadWithOverviewMode(true);

		mWebView.setWebViewClient(new MyWebViewClient());
		if(NetWorkUtils.isNetworkAvailable(BuyShopActivity.this)){
			beginLoading(BuyShopActivity.this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.add_tv:
					int numberNew = Integer.valueOf(numberEt.getText().toString());
					numberNew = numberNew+1;
					numberEt.setText(numberNew+"");
				break;
			case R.id.cut_tv:
					int numberNewCut = Integer.valueOf(numberEt.getText().toString());
					if(numberNewCut>1){
						numberNewCut = numberNewCut-1;
					}
					numberEt.setText(numberNewCut+"");
				break;
			case R.id.buy_tv:
				int count = Integer.valueOf(numberEt.getText().toString());
				if(count<1){
					Toast.makeText(BuyShopActivity.this,"购买数量必须大于1",Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(BuyShopActivity.this, OrderDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", name);
				bundle.putString("price", price);
				bundle.putString("tel", tel);
				bundle.putString("type", type);
				bundle.putString("id", id);
				bundle.putString("count", numberEt.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
				break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
			// TODO Auto-generated method stub
			view.stopLoading();
			view.clearView();
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			endLoading();
		}
	}
}
