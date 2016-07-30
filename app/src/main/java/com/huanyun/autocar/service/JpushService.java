package com.huanyun.autocar.service;


import org.json.JSONException;
import org.json.JSONObject;
import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huanyun.autocar.activity.MessageActivity;

/***
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 * 
 * @author lion
 * 
 */
public class JpushService extends BroadcastReceiver {

	private static final String TAG = "MyReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: "
				+ printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收Registration Id : " + regId);
		}
//		else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
//			String regId = bundle
//					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//			Log.d(TAG, "接收UnRegistration Id : " + regId);
//		}
		else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);


		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "用户点击打开了通知");
//
//			String extraString = bundle.getString(JPushInterface.EXTRA_EXTRA);
//
//			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);

			if(content!=null&&!content.equals("")){
				Intent intent1 = new Intent(context, MessageActivity.class);
				Bundle bundle1 = new Bundle();
				bundle1.putString("message",content);
				intent1.putExtras(bundle1);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent1);
			}

//			handleNotification(context, bundle);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	// onReceive - cn.jpush.android.intent.NOTIFICATION_RECEIVED, extras:
	// key:cn.jpush.android.NOTIFICATION_CONTENT_TITLE, value:占便宜
	// key:cn.jpush.android.EXTRA, value:{}
	// key:cn.jpush.android.NOTIFICATION_ID, value:1702117272
	// key:cn.jpush.android.ALERT, value:测试文本消息测试文本消息测试文本消息测试文本消息
	// 接收到推送下来的通知
	// 接收到推送下来的通知的ID: 1702117272
	//
	// onReceive - cn.jpush.android.intent.NOTIFICATION_RECEIVED, extras:
	// key:cn.jpush.android.NOTIFICATION_CONTENT_TITLE, value:占便宜
	// key:cn.jpush.android.EXTRA, value:{"p":2}
	// key:cn.jpush.android.NOTIFICATION_ID, value:1365903167
	// key:cn.jpush.android.ALERT, value:测试商品消息测试商品消息测试商品消息测试商品消息
	// 接收到推送下来的通知
	// 接收到推送下来的通知的ID: 1365903167
	//
	// onReceive - cn.jpush.android.intent.NOTIFICATION_RECEIVED, extras:
	// key:cn.jpush.android.NOTIFICATION_CONTENT_TITLE, value:占便宜
	// key:cn.jpush.android.EXTRA, value:{"s":1,"a":10}
	// key:cn.jpush.android.NOTIFICATION_ID, value:1365903573
	// key:cn.jpush.android.ALERT, value:测试活动消息测试活动消息测试活动消息测试活动消息
	// 接收到推送下来的通知
	// 接收到推送下来的通知的ID: 1365903573

	private void handleNotification(Context context, Bundle bundle) {
		String rawPayload = bundle.getString(JPushInterface.EXTRA_EXTRA);


	}

	private void loadProduct(Context context, Bundle bundle, String prodId) {
//		loadNewTaskActivity(context, ProductInfoActivity.class, bundle,
//				new String[] { "productid", prodId });
	}

	private void loadActivity(Context context, Bundle bundle, String storeId,
			String activityId) {
//		loadNewTaskActivity(context, StoreActivityNew.class, bundle,
//				new String[] { "storeid", storeId },
//				// new String[]{"storename",store.getName()},
//				new String[] { "activityid", activityId }
//		// new String[]{"activityname",store.getDefActivityName()},
//		// new String[]{"isfavorite",""+store.isFavorite()}
//		);
	}

	private void loadMsgDetails(Context context, Bundle bundle) {
		// 打开自定义的Activity
//		Intent i = new Intent(context, SimpleNotificationDetailsActivity.class);
//		i.putExtras(bundle);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//				| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//		context.startActivity(i);
	}

	private void loadNewTaskActivity(Context context, Class<?> cls,
			Bundle bundle, String[]... pairs) {
		Intent intent = new Intent(context, cls);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		for (String[] pair : pairs) {
			intent.putExtra(pair[0], pair[1]);
		}
		context.startActivity(intent);
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// //send msg to MainActivity
	// private void processCustomMessage(Context context, Bundle bundle) {
	// if (MainActivity.isForeground) {
	// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	// Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
	// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
	// if (!ExampleUtil.isEmpty(extras)) {
	// try {
	// JSONObject extraJson = new JSONObject(extras);
	// if (null != extraJson && extraJson.length() > 0) {
	// msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
	// }
	// } catch (JSONException e) {
	//
	// }
	//
	// }
	// context.sendBroadcast(msgIntent);
	// }
	// }

}
