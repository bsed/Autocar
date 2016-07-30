package com.huanyun.autocar.activity;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.Utils.PayResult;
import com.huanyun.autocar.Utils.SignUtils;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.huanyun.autocar.network.Api;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.huanyun.autocar.widget.HTML5WebView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity {

	public static final String TAG = OrderDetailsActivity.class
			.getSimpleName();
	public static OrderDetailsActivity roadHelpActivity;
	private CommonTitleBar commonTitleBar;

	private String name,price,tel,count,type,id;
	private Double totalPrice;
	private String orderId;

	// 商户PID
	public static final String PARTNER = "2088321007602760";
	// 商户收款账号
	public static final String SELLER = "bjtcgj6789@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANiCXkorfJ5zw2+" +
			"Lnr/MrNpzO72h5r/Nh54vxxl7EOhTP4tre7yT1wa4WHkRWB4ABkS7vJuG382ABrqMAG" +
			"Vf1eZbtbt7KIcw6kl75FTLowSYQXcQuPTc6JSV11yzD1VF+L+q7CR81cuIIYi+tokt7Rb" +
			"6IpDv4pyafGbI4e5rgHfDAgMBAAECgYEAi0K/t2FlVN4NNTBPOftCq3g3UvBSZcijlMIShH" +
			"c+FtGFK/5EprVJN89XSPg4/4LwhUFkPX1xctMIgPEQSCRuhgBDzd/u2F6glWqKqXOMzvEfM" +
			"gohDQ6xyihwmk+GDs3G5554uQFKJ1fFz5chZhaAI+SBHuBFBucWQtp2h2O+/GECQQDwnmrHgkk/peKEn" +
			"MlZNCCDeeKiFIXPwr9OYr4oyKkDzo78vT3p8HvhCDBH38+oCI2xEWfshMBb5XZPg9dXwXI7AkEA5llpoHdxGr" +
			"crP+lmzSlpxZ+pRD8RlwLfNo5Qx/NvTIoJlh65uAG3duvTkduD05CLcYf5vq6TPAtGBpR7Bj/wGQJAHMjWvUc4v" +
			"AR8NKEM9T7nJNNP6VPLLi9980KcvQREM9BiCpoY7gN2zlTMccy0asWbLbOHWyQGXvf0T07WVAkwQQJAY++szOql" +
			"DnMjd3H6oM3zzsKLPEmkhwC++LtbvrQVuH421IN2DafpAdYkjyZOli4i4Vmd96VjyWwWsGogycBZmQJBANZVGXq" +
			"Kb52DYmhn8Ho0b5R4a7uVjh4LxHmz9eCpSz2o9ILa2sH+e6wMUx1gtcQiHOXUcnQ6RJOsDaqhguzWmZ8=";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "";
	private static final int SDK_PAY_FLAG = 1;

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);
					/**
					 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
					 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
					 * docType=1) 建议商户依赖异步通知
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息

					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(OrderDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
						addOrder();
					} else {
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(OrderDetailsActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(OrderDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

						}
					}
					break;
				}
				default:
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		roadHelpActivity = this;
		setContentView(R.layout.activity_order_details);
		initView();
		initData();
	}

	private void initData(){
		if(getIntent()!=null){
			name = getIntent().getExtras().getString("name");
			price = getIntent().getExtras().getString("price");
			tel = getIntent().getExtras().getString("tel");
			count = getIntent().getExtras().getString("count");
			type = getIntent().getExtras().getString("type");
			id = getIntent().getExtras().getString("id");
			totalPrice = Double.valueOf(price)*Integer.valueOf(count);

			if(type.equals("1")){
				name = name+" 洗车";
			}else if(type.equals("2")){
				name = name+" 维修";
			}else if(type.equals("3")){
				name = name+" 保养";
			}

			((TextView) findViewById(R.id.shop_name_tv)).setText(name);
			((TextView)findViewById(R.id.phone_tv)).setText(tel);
			((TextView)findViewById(R.id.price_tv)).setText(price+"元");
			((TextView)findViewById(R.id.count_tv)).setText(count);

			((TextView)findViewById(R.id.total_tv)).setText(totalPrice+"元");
			findView(R.id.comform_order_tv).setOnClickListener(this);
		}
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("订单详情");
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
	}

	private void addOrder(){
		beginLoading(OrderDetailsActivity.this);
		Api api = new Api(OrderDetailsActivity.this, new RequestCallBack<Object>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				LogUtils.i("返回其他邀请-->" + arg0.result);
				endLoading();
				Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
				String error = (String)dataMap.get("error");
				if(error==null||error.equals("")){
					if(BuyShopActivity.buyShopActivity!=null){
						BuyShopActivity.buyShopActivity.finish();
					}
                    finish();
					loadNext(MyOrderActivity.class);
				}else{
					Toast.makeText(OrderDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				endLoading();
			}
		});
		api.addOrder(CommonSettingProvider.getUserId(OrderDetailsActivity.this), id, type, price, count, name, orderId);
	}


	/**
	 * call alipay sdk pay. 调用SDK支付
	 *
	 */
	public void pay(View v) {
		String orderInfo = getOrderInfo(name, name, totalPrice+"");

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(OrderDetailsActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}


	/**
	 * create the order info. 创建订单信息
	 *
	 */
	private String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		orderId = getOutTradeNo();

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 *
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 *
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}


	private boolean isInstallAlipay(String packagename){
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(
					packagename, PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo ==null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.comform_order_tv:
				if(CommonSettingProvider.getUserId(OrderDetailsActivity.this).equals("")){
					Toast.makeText(OrderDetailsActivity.this,"请重新登录后购买",Toast.LENGTH_SHORT).show();
					return;
				}
				if(isInstallAlipay("com.eg.android.AlipayGphone")){
					pay(v);
				}else {
					Toast.makeText(OrderDetailsActivity.this,"请先安装支付宝",Toast.LENGTH_SHORT).show();
				}
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
}
