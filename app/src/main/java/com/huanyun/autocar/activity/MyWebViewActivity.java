package com.huanyun.autocar.activity;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.NetWorkUtils;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.huanyun.autocar.widget.HTML5WebView;

/**
 * Created by admin on 2016/1/27.
 */
public class MyWebViewActivity extends BaseActivity{

    public static MyWebViewActivity myWebViewActivity;
    private CommonTitleBar commonTitleBar;
    private ListView listView;
    private String logisticsInfo,logisticsNo;

    private LinearLayout liner_main;
    private HTML5WebView mWebView;

    private String url,titleName,htmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myWebViewActivity = this;
        setContentView(R.layout.activity_webview);
        initData();
        initView();
        liner_main = findView(R.id.liner_main);
        mWebView = new HTML5WebView(this);
        mWebView.requestFocus();

        mWebView.loadUrl(htmlData);

//        htmlData = htmlData.replaceAll("&amp;", "");
//        htmlData = htmlData.replaceAll("quot;", "\"");
//        htmlData = htmlData.replaceAll("lt;", "<");
//        htmlData = htmlData.replaceAll("gt;", ">");
//
//        mWebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);

        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

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
        if(NetWorkUtils.isNetworkAvailable(MyWebViewActivity.this)){
            beginLoading(MyWebViewActivity.this);
        }
    }

    private void initData(){
        if(getIntent()!=null){
            url = (String) getIntent().getExtras().getString("html_url");
            titleName = (String) getIntent().getExtras().getString("title_name");
            htmlData = (String) getIntent().getExtras().getString("htmlData");
        }
    }

    private void initView() {
        commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
        commonTitleBar.setTitleTxt(titleName);
        commonTitleBar.setLeftTxtBtn("返回");
        commonTitleBar.visbleLeftBtn();
        commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        commonTitleBar.setRightImg(R.mipmap.ic_launcher);
        commonTitleBar.setRightBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    @Override
    protected void onDestroy() {
        myWebViewActivity = null;
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
