package com.huanyun.autocar.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.LoadLocalImageUtil;
import com.huanyun.autocar.activity.BuyShopActivity;
import com.huanyun.autocar.activity.GPSActivity;
import com.huanyun.autocar.activity.LoginActivity;
import com.huanyun.autocar.activity.MyWebViewActivity;
import com.huanyun.autocar.constant.CommonSettingProvider;

import java.util.List;
import java.util.Map;

public class MyOrderAdapter extends BaseAdapter {

    private String TAG = MyOrderAdapter.class.getSimpleName();
    private Activity activity;
	private List<Map<String, Object>> datas;
    private int size;

    public MyOrderAdapter(Activity activity, List<Map<String, Object>> datas) {
		this.activity = activity;
		this.datas = datas;
		this.size = datas.size();
    }
    
    public void refreshData(List<Map<String, Object>> datas) {
    	this.datas = datas;
    	this.size = datas.size();
    	notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
		// TODO Auto-generated method stub
		return size;
    }

    @Override
    public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;//bankData.get(position);
    }

    @Override
    public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Map<String , Object>  dataMap = datas.get(position);
		final Holder holder;
		if (convertView == null) {
			convertView = View.inflate(activity, R.layout.item_my_order, null);
		    holder = new Holder();
			holder.order_id_tv = (TextView) convertView.findViewById(R.id.order_id_tv);
			holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
			holder.count_tv = (TextView) convertView.findViewById(R.id.count_tv);
			holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
			holder.total_tv = (TextView) convertView.findViewById(R.id.total_tv);
		    convertView.setTag(holder);
		} else {
		    holder = (Holder) convertView.getTag();
		}


		holder.order_id_tv.setText("订单号："+(String) dataMap.get("orderId"));
		holder.time_tv.setText(dataMap.get("created_time").toString());
		holder.price_tv.setText("单价："+(String) dataMap.get("price")+"元");
		holder.count_tv.setText("数量：" + dataMap.get("num"));
		holder.total_tv.setText("总价：" + dataMap.get("total_price")+"元");

		return convertView;
    }
    
    private final class Holder {
		TextView order_id_tv,time_tv,price_tv,count_tv,total_tv;
    }
    
}
