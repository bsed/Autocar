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
import com.huanyun.autocar.activity.MyWebViewActivity;

import java.util.List;
import java.util.Map;

public class CarUsedTradeAdapter extends BaseAdapter {

    private String TAG = CarUsedTradeAdapter.class.getSimpleName();
    private Activity activity;
	private List<Map<String, Object>> datas;
    private int size;
	private int width ,height;

    public CarUsedTradeAdapter(Activity activity, List<Map<String, Object>> datas) {
		this.activity = activity;
		this.datas = datas;
		this.size = datas.size();
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);

		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
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
			convertView = View.inflate(activity, R.layout.item_car_user_trade, null);
		    holder = new Holder();
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);
			holder.car_name = (TextView)convertView.findViewById(R.id.car_name_tv);
			holder.car_price = (TextView)convertView.findViewById(R.id.car_price_tv);
		    convertView.setTag(holder);
		} else {
		    holder = (Holder) convertView.getTag();
		}


		LoadLocalImageUtil.getInstance().displayImageForNet((String) dataMap.get("image"), holder.picture);
		holder.car_price.setText("价格：" + dataMap.get("price") + "万");
		holder.car_name.setText((String) dataMap.get("name"));


		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, MyWebViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("html_url","https://www.baidu.com/");
				bundle.putString("title_name",dataMap.get("name").toString());
				bundle.putString("htmlData",dataMap.get("content").toString());
				intent.putExtras(bundle);
				activity.startActivity(intent);
			}
		});

		return convertView;
    }
    
    private final class Holder {
		ImageView picture;
		TextView car_name,car_price;
		CardView cardView;
    }
    
}
