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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.LoadLocalImageUtil;
import com.huanyun.autocar.activity.BuyShopActivity;
import com.huanyun.autocar.activity.GPSActivity;
import com.huanyun.autocar.activity.LoginActivity;
import com.huanyun.autocar.activity.MeActivity;
import com.huanyun.autocar.activity.MyWebViewActivity;
import com.huanyun.autocar.constant.CommonSettingProvider;

import java.util.List;
import java.util.Map;

public class CarWashAdapter extends BaseAdapter {

    private String TAG = CarWashAdapter.class.getSimpleName();
    private Activity activity;
	private List<Map<String, Object>> datas;
    private int size;
	private int width ,height;
	private int type;

    public CarWashAdapter(Activity activity, List<Map<String, Object>> datas,int type) {
		this.activity = activity;
		this.datas = datas;
		this.size = datas.size();
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);

		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		this.type = type;
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
			convertView = View.inflate(activity, R.layout.item_car_wash, null);
		    holder = new Holder();
			holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			holder.distance_tv = (TextView) convertView.findViewById(R.id.distance_tv);
			holder.desc_tv = (TextView) convertView.findViewById(R.id.desc_tv);
			holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);
			holder.assessment_tv = (TextView) convertView.findViewById(R.id.assessment_tv);
			holder.daohang_tv = (TextView)convertView.findViewById(R.id.daohang_tv);
			holder.goumai_tv = (TextView)convertView.findViewById(R.id.goumai_tv);
		    convertView.setTag(holder);
		} else {
		    holder = (Holder) convertView.getTag();
		}


		LoadLocalImageUtil.getInstance().displayImageForNet((String) dataMap.get("image"), holder.picture);
		holder.name_tv.setText((String) dataMap.get("name"));
		holder.distance_tv.setText("距离："+(Double) dataMap.get("distance"));
		holder.desc_tv.setText((String) dataMap.get("desc"));
		holder.price_tv.setText("价格：￥"+ dataMap.get("price"));
		holder.assessment_tv.setText("评价："+ dataMap.get("score")+"分");

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, MyWebViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("html_url", "https://www.baidu.com/");
				bundle.putString("title_name", dataMap.get("name").toString());
				bundle.putString("htmlData", dataMap.get("content").toString());
				intent.putExtras(bundle);
				activity.startActivity(intent);
			}
		});

		holder.daohang_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(activity, GPSActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("longitude", dataMap.get("longitude").toString());
				bundle.putString("latitude", dataMap.get("latitude").toString());
				bundle.putString("address", dataMap.get("address").toString());
				intent.putExtras(bundle);
				activity.startActivity(intent);
			}
		});

		holder.goumai_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(CommonSettingProvider.getUserId(activity).equals("")) {
					Intent intent = new Intent(activity, LoginActivity.class);
					activity.startActivity(intent);
				}else{
					Intent intent = new Intent(activity, BuyShopActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("name", dataMap.get("name").toString());
					bundle.putString("price", dataMap.get("price").toString());
					bundle.putString("tel", dataMap.get("tel").toString());
					bundle.putString("type", type+"");
					bundle.putString("id", dataMap.get("id").toString());
					bundle.putString("pcontent", dataMap.get("pcontent").toString());
					intent.putExtras(bundle);
					activity.startActivity(intent);
				}
			}
		});

		return convertView;
    }
    
    private final class Holder {
		ImageView picture;
		TextView name_tv,distance_tv,desc_tv,price_tv,assessment_tv,daohang_tv,goumai_tv;
		CardView cardView;
    }
    
}
