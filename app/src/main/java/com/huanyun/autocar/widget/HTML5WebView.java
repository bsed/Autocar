package com.huanyun.autocar.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.huanyun.autocar.R;

public class HTML5WebView extends WebView {

		private Context 							mContext;
		private MyWebChromeClient					mWebChromeClient;
		private View								mCustomView;
		private FrameLayout							mCustomViewContainer;
		private WebChromeClient.CustomViewCallback 	mCustomViewCallback;

		private FrameLayout							mContentView;
		private FrameLayout							mBrowserFrameLayout;
		private FrameLayout							mLayout;

		public String content_;

	    static final String LOGTAG = "HTML5WebView";
	    private boolean is_gone = false;


		private void init(Context context) {
			mContext = context;
			Activity a = (Activity) mContext;

			mLayout = new FrameLayout(context);

			mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(a).inflate(R.layout.custom_screen, null);
			mContentView = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.main_content);
			mCustomViewContainer = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.fullscreen_custom_content);

			mLayout.addView(mBrowserFrameLayout, COVER_SCREEN_PARAMS);

			mWebChromeClient = new MyWebChromeClient();


		    setWebChromeClient(mWebChromeClient);

		    setWebViewClient(new MyWebViewClient());

		    // Configure the webview
		    WebSettings s = getSettings();
		    s.setBuiltInZoomControls(true);
		    s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		    s.setUseWideViewPort(true);
		    s.setLoadWithOverviewMode(true);
		    s.setSavePassword(true);
		    s.setSaveFormData(true);
		    s.setJavaScriptEnabled(true);

		    // enable navigator.geolocation
		    s.setGeolocationEnabled(true);
		    s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

		    // enable Web Storage: localStorage, sessionStorage
		    s.setDomStorageEnabled(true);



		    mContentView.addView(this);
		}



		public HTML5WebView(Context context) {
			super(context);
			init(context);
		}

		public HTML5WebView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(context);
		}

		public HTML5WebView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init(context);
		}

		public FrameLayout getLayout() {
			return mLayout;
		}

	    public boolean inCustomView() {
			return (mCustomView != null);
		}

	    public void hideCustomView() {
			mWebChromeClient.onHideCustomView();
		}

	    @Override
	    protected void onWindowVisibilityChanged(int visibility) {
	    	// TODO Auto-generated method stub
	    	super.onWindowVisibilityChanged(visibility);
	    	if (visibility == View.GONE) {
	    		try {
	    		WebView.class.getMethod("onPause").invoke(this);// stop flash
	    		} catch (Exception e) {
	    		}
	    		this.pauseTimers();
	    		this.is_gone = true;
	    		} else if (visibility == View.VISIBLE) {
	    		try {
	    		WebView.class.getMethod("onResume").invoke(this);// resume flash
	    		} catch (Exception e) {
	    		}
	    		this.resumeTimers();
	    		this.is_gone = false;
	    		}
	    }

	    @Override
	    protected void onDetachedFromWindow() {
	    	// TODO Auto-generated method stub
	    	super.onDetachedFromWindow();
	    	if (this.is_gone) {
	    		try {
	    		this.destroy();
	    		} catch (Exception e) {
	    		}
	    	}

	    }

	    @Override
	    protected Parcelable onSaveInstanceState() {
	    	// TODO Auto-generated method stub
	    	return super.onSaveInstanceState();
	    }

	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    		if ((mCustomView == null) && canGoBack()){
	    			goBack();
	    			return true;
	    		}
	    	}
	    	return super.onKeyDown(keyCode, event);
	    }

	    public class MyWebChromeClient extends WebChromeClient {
			private Bitmap 		mDefaultVideoPoster;
			private View 		mVideoProgressView;

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				content_  = message;
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url, String message,
					JsResult result) {
				content_  = message;
				System.out.println("onJsConfirm message="+message);
				return super.onJsConfirm(view, url, message, result);
			}

	    	@Override
			public void onShowCustomView(View view, CustomViewCallback callback)
			{
				//Log.i(LOGTAG, "here in on ShowCustomView");
		        HTML5WebView.this.setVisibility(View.GONE);

		        // if a view already exists then immediately terminate the new one
		        if (mCustomView != null) {
		            callback.onCustomViewHidden();
		            return;
		        }

		        mCustomViewContainer.addView(view);
		        mCustomView = view;
		        mCustomViewCallback = callback;
		        mCustomViewContainer.setVisibility(View.VISIBLE);
			}

			@Override
			public void onHideCustomView() {

				if (mCustomView == null)
					return;

				// Hide the custom view.
				mCustomView.setVisibility(View.GONE);

				// Remove the custom view from its container.
				mCustomViewContainer.removeView(mCustomView);
				mCustomView = null;
				mCustomViewContainer.setVisibility(View.GONE);
				mCustomViewCallback.onCustomViewHidden();

				HTML5WebView.this.setVisibility(View.VISIBLE);

		        //Log.i(LOGTAG, "set it to webVew");
			}

			@Override
			public Bitmap getDefaultVideoPoster() {
				//Log.i(LOGTAG, "here in on getDefaultVideoPoster");
				if (mDefaultVideoPoster == null) {
					mDefaultVideoPoster = BitmapFactory.decodeResource(
							getResources(), R.drawable.default_video_poster);
			    }
				return mDefaultVideoPoster;
			}

			@Override
			public View getVideoLoadingProgressView() {
				//Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

		        if (mVideoProgressView == null) {
		            LayoutInflater inflater = LayoutInflater.from(mContext);
		            mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
		        }
		        return mVideoProgressView;
			}

	    	 @Override
	         public void onReceivedTitle(WebView view, String title) {
	         }

	         @Override
	         public void onProgressChanged(WebView view, int newProgress) {
	        	 ((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
	         }

	         @Override
	         public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
	             callback.invoke(origin, true, false);
	         }



	    }


	/*    webView1.setWebViewClient(new WebViewClient() {
	    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    		view.loadUrl(url);
	    		return true;            }
	    	@Override
	    	public WebResourceResponse shouldInterceptRequest(WebView view,String url) {
	    		if (url.startsWith("http") || url.startsWith("https")) {
	    			return super.shouldInterceptRequest(view, url);
	    			} else {
	    				Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	    				startActivity(in);
	    				return null;
	    				}
	    		}
	    	});*/

		private class MyWebViewClient extends WebViewClient {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		    	Log.i(LOGTAG, "shouldOverrideUrlLoading: "+url);
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
		    }
		}

		final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
	        new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}