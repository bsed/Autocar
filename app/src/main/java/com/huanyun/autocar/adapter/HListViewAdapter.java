package com.huanyun.autocar.adapter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.huanyun.autocar.R;
import com.huanyun.autocar.Utils.LoadLocalImageUtil;
import com.huanyun.autocar.activity.MyWebViewActivity;

import java.util.List;

public class HListViewAdapter extends BaseAdapter {

    private String TAG = HListViewAdapter.class.getSimpleName();
    private Activity activity;
	private List<String> hlistviewUrls;
    private int size;

    public HListViewAdapter(Activity activity, List<String> hlistviewUrls) {
		this.activity = activity;
		this.hlistviewUrls = hlistviewUrls;
		size = hlistviewUrls.size();
    }
    
    public void refreshData(List<String> hlistviewUrls) {
    	this.hlistviewUrls = hlistviewUrls;
    	size = hlistviewUrls.size();
    	notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
		// TODO Auto-generated method stub
		return hlistviewUrls.size();
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
//		final Map<String , Object>  dataMap = datas.get(position);
		final Holder holder;
		if (convertView == null) {
		    convertView = View.inflate(parent.getContext(), R.layout.item_ykyin_choiced_picture, null);
		    holder = new Holder();
		    convertView.setTag(holder);

			holder.picture = (ImageView)convertView.findViewById(R.id.image_choice);
		} else {
		    holder = (Holder) convertView.getTag();
		}

		LoadLocalImageUtil.getInstance().displayImageForNet(hlistviewUrls.get(position), holder.picture);

//		convertView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(activity, MyWebViewActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("html_url", "https://www.baidu.com/");
//				bundle.putString("title_name", "webview");
//				intent.putExtras(bundle);
//				activity.startActivity(intent);
//			}
//		});

		return convertView;
	   
    }
    
    private final class Holder {
		ImageView picture;
    }
}
