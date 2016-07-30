package com.huanyun.autocar.network;

import android.content.Context;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class HttpUtilClient {

	private static HttpUtils hu = new HttpUtils();

	static {
		hu.configTimeout(15000);
	}

	public static RequestParams genJsonBodyParams(Map<String, Object> map) {
		String body = new Gson().toJson(map).toString();
		LogUtils.i("json转换--->" + body);
		RequestParams params = new RequestParams();
		params.addBodyParameter("body", body);
		return params;
	}

	public static String genJsonBody(Map<String, Object> map) {
		String body = new Gson().toJson(map);
		LogUtils.i("json转换--->" + body);
		return body;
	}


	public static HttpHandler<Object> get(Context context, String url, Map<String, Object> map,
			RequestCallBack<Object> callBack) {
		RequestParams params = new RequestParams("UTF-8");
		params.addHeader("Content-Type", "application/json");
		LogUtils.i("路径-->" + url);
		return hu.send(HttpMethod.GET, url, params, callBack);
	}

	public static HttpHandler<Object> post(Context context, String url, Map<String, Object> map,
			RequestCallBack<Object> callBack) {
		
		RequestParams params = new RequestParams("UTF-8");
		params.setContentType("application/json");
		params.addHeader("Content-Type", "application/json");
		try {
			params.setBodyEntity(new StringEntity(genJsonBody(map), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtils.i("路径-->" + url);
		hu.configCurrentHttpCacheExpiry(1000 * 0);
		hu.configTimeout(15000);
		return hu.send(HttpMethod.POST, url, params, callBack);
	}

	public static HttpHandler<Object> postJsonArray(Context context, String url, JSONObject paramsObj,
			RequestCallBack<Object> callBack) {
		LogUtils.i("JSONArray转换--->" + paramsObj.toString());
		RequestParams params = new RequestParams("UTF-8");
		params.setContentType("application/json");
		params.addHeader("Content-Type", "application/json");

		try {
			params.setBodyEntity(new StringEntity(paramsObj.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtils.i("路径-->" + url);
		hu.configCurrentHttpCacheExpiry(1000*0);
		hu.configTimeout(15000);
		return hu.send(HttpMethod.POST, url, params, callBack);
	}
	
	

	public static HttpHandler<Object> put(Context context, String url, Map<String, Object> map,
			RequestCallBack<Object> callBack) {

		RequestParams params = new RequestParams("UTF-8");
		params.setContentType("application/json");
		params.addHeader("Content-Type", "application/json");

		try {
			params.setBodyEntity(new StringEntity(genJsonBody(map), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtils.i("路径-->" + url);
		hu.configTimeout(15000);
		return hu.send(HttpMethod.PUT, url, params, callBack);
	}

	public static HttpHandler<Object> delete(Context context, String url, Map<String, Object> map,
			RequestCallBack<Object> callBack) {

		RequestParams params = new RequestParams("UTF-8");
		params.setContentType("application/json");
		params.addHeader("Content-Type", "application/json");

		try {
			params.setBodyEntity(new StringEntity(genJsonBody(map), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtils.i("路径-->" + url);
		hu.configTimeout(15000);
		return hu.send(HttpMethod.DELETE, url, params, callBack);
	}

	public static HttpHandler<File> download(Context context,String url,String filePaht,Map<String, Object> map,
			RequestCallBack<File> callBack){
		return hu.download(url,filePaht, true, true, callBack);
	}

}
