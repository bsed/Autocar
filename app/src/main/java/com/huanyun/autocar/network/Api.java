package com.huanyun.autocar.network;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.huanyun.autocar.network.HttpUtilClient;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

public class Api {

	public Context mContext;

	public RequestCallBack<Object> oRequestCallBack;

	public Api(Context context, RequestCallBack<Object> callBack) {
		this.oRequestCallBack = callBack;
		this.mContext = context;
	}

	/**
	 * 统一处理的回调接口
	 */
	RequestCallBack<Object> mRequestCallBack = new RequestCallBack<Object>() {

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			super.onLoading(total, current, isUploading);
			oRequestCallBack.onLoading(total, current, isUploading);
		}

		@Override
		public void onStart() {
			super.onStart();
			oRequestCallBack.onStart();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			oRequestCallBack.onFailure(arg0, arg1);
		}

		@Override
		public void onSuccess(ResponseInfo<Object> arg0) {
			oRequestCallBack.onSuccess(arg0);
		}
	};
	public final static String BASE_HOST_QA = "http://graduationdesign.sinaapp.com";
	public static final String PORT = "";

	public final static String BASE_URL = BASE_HOST_QA+PORT; // 更换此处切换环境

	private final static String HOST = BASE_URL + "/autocar";

	public final static String QINIU_URL = "http://freefood.qiniudn.com/";


	/**
	 * ------------------------------------------
	 */

	//注册
	public final static String REGISTER_USER = HOST + "/index.php?s=/User/register";

	public HttpHandler<Object> register(String phonenum, String password,
									 String djdate,String lqdate) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("phonenum",phonenum);
		paramMap.put("password1",password);
		paramMap.put("password2",password);
		paramMap.put("djdate",djdate);
		paramMap.put("lqdate",lqdate);

		return HttpUtilClient.post(mContext,
				REGISTER_USER, paramMap,
				mRequestCallBack);
	}

	//忘记密码
	public final static String FORGET_PWD_USER = HOST + "/index.php?s=/User/findPassword";

	public HttpHandler<Object> forgetPwd(String phonenum, String password) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("phonenum",phonenum);
		paramMap.put("password1",password);
		paramMap.put("password2",password);

		return HttpUtilClient.post(mContext,
				FORGET_PWD_USER, paramMap,
				mRequestCallBack);
	}

	//短息验证
	public final static String VERIFI_SMS = HOST + "/index.php?s=/User/verificationSMS/phonenum/%s";

	public HttpHandler<Object> verificationSMS(String phone) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return HttpUtilClient.get(mContext,
				String.format(VERIFI_SMS, phone), paramMap,
				mRequestCallBack);
	}

	//登录
	public final static String LOGIN_USER = HOST + "/index.php?s=/User/login";

	public HttpHandler<Object> login(String phonenum, String password) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("phonenum",phonenum);
		paramMap.put("password",password);

		return HttpUtilClient.post(mContext,
				LOGIN_USER, paramMap,
				mRequestCallBack);
	}

	//洗车，保养，维修的接口
	public final static String SHOP_PLIST = HOST + "/index.php?s=/Shop/plist/pageNo/%s/longitude/%s/latitude/%s/type/%s";

	public HttpHandler<Object> shopList(int pageNo, String longitude,String latitude,int type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return HttpUtilClient.get(mContext,
				String.format(SHOP_PLIST, pageNo, longitude, latitude, type), paramMap,
				mRequestCallBack);
	}

	//二手车
	public final static String USED_CAR_LIST = HOST + "/index.php?s=/Shop/usedcar/pageNo/%s";

	public HttpHandler<Object> usedCarList(int pageNo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return HttpUtilClient.get(mContext,
				String.format(USED_CAR_LIST,pageNo), paramMap,
				mRequestCallBack);
	}

	//轮播图
	public final static String SHOW_AD_LIST = HOST + "/index.php?s=/Shop/ad/type/%s";

	public HttpHandler<Object> showAdList(int type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return HttpUtilClient.get(mContext,
				String.format(SHOW_AD_LIST,type), paramMap,
				mRequestCallBack);
	}

	//获取token
	public final static String QINIU_TOKEN = HOST + "/index.php?s=/User/getQiniuToken";

	public HttpHandler<Object> getQiniuToken( ) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return HttpUtilClient.get(mContext,
				QINIU_TOKEN, paramMap,
				mRequestCallBack);
	}

	//上传车辆信息
	public final static String ADD_CAR_INFO = HOST + "/index.php?s=/Shop/addcarinfo";

	public HttpHandler<Object> addCarInfo(String userId,String cpnum,String phone,String idcard1,String idcard2,
	String xscard1,String xscard2,String jscard1,String jscard2) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("cpnum", cpnum);
		paramMap.put("phone", phone);
		paramMap.put("idcard1", idcard1);
		paramMap.put("idcard2", idcard2);
		paramMap.put("xscard1", xscard1);
		paramMap.put("xscard2", xscard2);
		paramMap.put("jscard1", jscard1);
		paramMap.put("jscard2", jscard2);
		return HttpUtilClient.post(mContext,
				ADD_CAR_INFO, paramMap,
				mRequestCallBack);
	}

	//获取车辆信息
	public final static String GET_CAR_INFO = HOST + "/index.php?s=/Shop/carinfo";

	public HttpHandler<Object> getCarInfo(String userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		return HttpUtilClient.post(mContext,
				GET_CAR_INFO, paramMap,
				mRequestCallBack);
	}

	//道路救援
	public final static String SHOP_HELP = HOST + "/index.php?s=/Shop/rescue";

	public HttpHandler<Object> shopHelp(String userId,String address,String longitude,String latitude) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("address", address);
		paramMap.put("longitude", longitude);
		paramMap.put("latitude", latitude);
		return HttpUtilClient.post(mContext,
				SHOP_HELP, paramMap,
				mRequestCallBack);
	}

	//审车
	public final static String SHEN_CHE = HOST + "/index.php?s=/Shop/shenche";

	public HttpHandler<Object> shenChe(String userId,String scdate) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("scdate", scdate);
		return HttpUtilClient.post(mContext,
				SHEN_CHE, paramMap,
				mRequestCallBack);
	}


	//支付成功后提交订单
	public final static String ADD_ORDER = HOST + "/index.php?s=/Shop/addOrder";

	public HttpHandler<Object> addOrder(String userId,String shopId,String type,String price,String num,String name,String orderId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("shopId", shopId);
		paramMap.put("type", type);
		paramMap.put("price", price);
		paramMap.put("num", num);
		paramMap.put("name", name);
		paramMap.put("orderId", orderId);
		return HttpUtilClient.post(mContext,
				ADD_ORDER, paramMap,
				mRequestCallBack);
	}

	//我的订单
	public final static String MY_ORDER = HOST + "/index.php?s=/Shop/myOrder";

	public HttpHandler<Object> getMyOrder(String userId,int pageNo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("pageNo", pageNo);
		return HttpUtilClient.post(mContext,
				MY_ORDER, paramMap,
				mRequestCallBack);
	}


/*	//上传图片
	public final static String METHOD_UPLOADE_IMG = HOST + "/fuploadfapiao.action?modifyUser.userName=%s";

	public HttpHandler<Object> uploadMethod(String userName,String filePath) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("images", FileHelper.base64Encode(filePath));

		LogUtils.i("METHOD_storeRecharge基础参数:" + paramMap.toString());

		return HttpUtilClient.uploadMethod(mContext, String.format(METHOD_UPLOADE_IMG, userName), filePath, paramMap,
				mRequestCallBack);
	}*/

	//数据查询
	public final static String METHOD_QUERY = HOST + "/saomiaoList.action?modifyUser.userName=%s&page=%s&saomiaotuVO.flag=%s";

	public HttpHandler<Object> gueryData(String userName,int page, int flag) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		return HttpUtilClient.get(mContext,
				String.format(METHOD_QUERY, userName, page, flag), paramMap,
				mRequestCallBack);
	}


	//获取详情数据
	public final static String METHOD_QUERY_DETAILS = HOST + "/shoujiDetail.action?id=%s";

	public HttpHandler<Object> getDataDetails(String tupianid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		return HttpUtilClient.get(mContext,
				String.format(METHOD_QUERY_DETAILS, tupianid), paramMap,
				mRequestCallBack);
	}
	//提交补录
	public final static String SHOUJI_EDIT_SAVE = HOST + "/shoujiEditSave.action?" +
			"smt.tupianid=%s&smt.sfapiaodama=%s&smt.sfapiaohaoma=%s&smt.sbsrsbh=%s&smt.skaipiaoriqi=%s&smt.sjine=%s&smt.smima=%s&smt.sdjine=%s&smt.spm=%s&smt.sjiqima=%s&smt.sshuikongma=%s";

	public HttpHandler<Object> shoujiEditSave(String tupianid,
											  String sfapiaodama,
											  String sfapiaohaoma,
											  String sbsrsbh,
											  String skaipiaoriqi,
											  String sjine,
											  String smima,
											  String sdjine,
											  String spm,
											  String sjiqima,
											  String sshuikongma
	) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return HttpUtilClient.get(mContext,
				String.format(SHOUJI_EDIT_SAVE,tupianid, sfapiaodama,sfapiaohaoma,sbsrsbh,skaipiaoriqi,sjine,smima,sdjine,spm,sjiqima,sshuikongma), paramMap,
				mRequestCallBack);
	}


	public final static String METHOD_NASHUIDAIWEI = HOST + "/nashuidanwei.action?nsVO.nashuirenhaoma=%s&nsVO.nashuirenmigncheng=%s";
	public HttpHandler<Object> SearchNashuidanwei(String search_sbsrsbhString, String danweimingString) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		return HttpUtilClient.get(mContext,
				String.format(METHOD_NASHUIDAIWEI, search_sbsrsbhString, danweimingString), paramMap,
				mRequestCallBack);
	}


	public final static String METHOD_CHANGE_PWD = HOST + "/modifyPassword.action?modifyUser.userName=%s&oldpassword=%s&newpassword=%s&confirmpassword=%s";
	public HttpHandler<Object> changePwd(String userName, String oldpassword,String newpassword,String confirmpassword ) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		return HttpUtilClient.get(mContext,
				String.format(METHOD_CHANGE_PWD, userName, oldpassword,newpassword,confirmpassword), paramMap,
				mRequestCallBack);
	}


	public final static String METHOD_saomiaoList = HOST + "/saomiaoList.action?modifyUser.userName=%s&page=%s&saomiaotuVO.flag=0&saomiaotuVO.fenxijieguo=%s&saomiaotuVO.fapiaodaima=%s&saomiaotuVO.fapiaohaoma=%s&saomiaotuVO.fapiaohaomasaomiaotuVO.nsrsbh=%s&saomiaotuVO.startsTime=%s&saomiaotuVO.endsTime=%s";
	public HttpHandler<Object> getSaomiaoList(String userName, int page,String fenxijieguo,String fapiaodaima,String fapiaohaoma,String nsrsbh,String startsTime,String endsTime) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		return HttpUtilClient.get(mContext,
				String.format(METHOD_saomiaoList, userName, page,fenxijieguo,fapiaodaima,fapiaohaoma,nsrsbh,startsTime,endsTime), paramMap,
				mRequestCallBack);
	}

	//	版本更新
	public final static String METHOD_UPDATE_APP = HOST + "/version.action";
	public HttpHandler<Object> updateApp() {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		return HttpUtilClient.get(mContext,METHOD_UPDATE_APP, paramMap,
				mRequestCallBack);
	}



}
