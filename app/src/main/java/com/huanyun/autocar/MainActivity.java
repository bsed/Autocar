package com.huanyun.autocar;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.huanyun.autocar.Utils.JsonUtil;
import com.huanyun.autocar.activity.AddCarInfoActivity;
import com.huanyun.autocar.activity.CarRepairActivity;
import com.huanyun.autocar.activity.CarUpkeepActivity;
import com.huanyun.autocar.activity.CarUsedTradeActivity;
import com.huanyun.autocar.activity.CarWashActivity;
import com.huanyun.autocar.activity.LoginActivity;
import com.huanyun.autocar.activity.MeActivity;
import com.huanyun.autocar.activity.MyWebViewActivity;
import com.huanyun.autocar.activity.RoadHelpActivity;
import com.huanyun.autocar.activity.ViolateRulesActivity;
import com.huanyun.autocar.activity.WhoseCarActivity;
import com.huanyun.autocar.adapter.CarWashAdapter;
import com.huanyun.autocar.adapter.HListViewAdapter;
import com.huanyun.autocar.adapter.PopPicturesAdapter;
import com.huanyun.autocar.constant.CommonSettingProvider;
import com.huanyun.autocar.network.Api;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.huanyun.autocar.widget.LoopViewPager;
import com.huanyun.autocar.widget.RefreshLayout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

public class MainActivity extends BaseActivity{

    private LoopViewPager loopViewPager;
    private HListView hListView;
    private HListViewAdapter hListViewAdapter;
    private PopPicturesAdapter popPicturesAdapter;

    List<String> listPics2 = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getAdList(1);
        getAdList(2);
        addListener();

        JPushInterface.setAlias(this, CommonSettingProvider.getUserName(MainActivity.this), new TagAliasCallback() {

            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initView(){
        findViewById(R.id.title_right_area).setOnClickListener(this);
        findViewById(R.id.title_ref_area).setOnClickListener(this);
        findView(R.id.add_car_info_line).setOnClickListener(this);
        findView(R.id.violate_rules_line).setOnClickListener(this);
        findView(R.id.more_tv).setOnClickListener(this);
        findView(R.id.whose_car_line).setOnClickListener(this);
        findView(R.id.car_wash_line).setOnClickListener(this);
        findView(R.id.car_repair_line).setOnClickListener(this);
        findView(R.id.car_upkeep_line).setOnClickListener(this);
        findView(R.id.help_line).setOnClickListener(this);
        findView(R.id.car_user_trade_lin).setOnClickListener(this);
        findView(R.id.more_but).setOnClickListener(this);

        loopViewPager = findView(R.id.loop_vp);
        loopViewPager.setPointPosition(LoopViewPager.POINT_CENTER);

        hListView = findView(R.id.hlistview);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(CommonSettingProvider.getUserId(MainActivity.this).equals("")){
//            commonTitleBar.visbleLeftBtn();
//        }else {
//            commonTitleBar.hideRightBtn();
//        }
        JPushInterface.onResume(MainActivity.this);
    }

    private void addListener(){

        loopViewPager.setOnPagerClickLisenter(new LoopViewPager.OnPagerClickLisenter() {
            @Override
            public void onPagerClickLisenter(int clickPosition) {
                if(dataList1!=null&&dataList1.size()>0){
                    Intent intent = new Intent(MainActivity.this, MyWebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("html_url","https://www.baidu.com/");
                    bundle.putString("title_name",dataList1.get(clickPosition).get("title").toString());
                    bundle.putString("htmlData",dataList1.get(clickPosition).get("content").toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(dataList2!=null&&dataList2.size()>0){
                    Intent intent = new Intent(MainActivity.this, MyWebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("html_url","https://www.baidu.com/");
                    bundle.putString("title_name",dataList2.get((int) l).get("title").toString());
                    bundle.putString("htmlData",dataList2.get((int) l).get("content").toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }


    List<Map<String, Object>> dataList1,dataList2;
    private void getAdList(final int type){
        Api api = new Api(MainActivity.this, new RequestCallBack<Object>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> arg0) {
                LogUtils.i("返回其他邀请-->" + arg0.result);
                Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.jsonToMap((String) arg0.result);
                if (dataMap == null) {
                    return;
                }

                String error = (String) dataMap.get("error");
                if (error == null || error.equals("")) {

                    if (type == 1) {
                        dataList1 = (List<Map<String, Object>>) dataMap.get("list");
                        if (dataList1 != null) {
                            List<String> listPics = new ArrayList<String>();
                            for (int i = 0; i < dataList1.size(); i++) {
                                Map<String, Object> map = dataList1.get(i);
                                listPics.add(map.get("image").toString());
                            }
                            loopViewPager.initPageView(listPics, null, false, R.drawable.lunbo);
                        }

                    }
                    if (type == 2) {
                        dataList2 = (List<Map<String, Object>>) dataMap.get("list");
                        if (dataList2 != null) {
                            listPics2 = new ArrayList<String>();
                            for (int i = 0; i < dataList2.size(); i++) {
                                Map<String, Object> map = dataList2.get(i);
                                listPics2.add(map.get("image").toString());
                            }
                            if(hListViewAdapter==null){
                                hListViewAdapter = new HListViewAdapter(MainActivity.this,listPics2);
                            }
                            hListView.setAdapter(hListViewAdapter);
                        }

                    }

                }else{
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
            }
        });
        api.showAdList(type);
    }

    private PopupWindow selectPopupWindow;
    private View mPopView;
    public void showClickPopupWindow() {
        if (mPopView != null) {
            mPopView = null;
        }
        mPopView = LayoutInflater.from(this).inflate(R.layout.popup_collect,
                null);
        if (selectPopupWindow == null) {
            selectPopupWindow = new PopupWindow(mPopView,
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        selectPopupWindow.setOutsideTouchable(true);
        selectPopupWindow.setFocusable(true);
        selectPopupWindow.setTouchable(true);
        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        selectPopupWindow.showAtLocation(mPopView, Gravity.CENTER, 0, 0);

        ImageView delete_img = (ImageView) mPopView.findViewById(R.id.delete_img);
        delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPopupWindow.dismiss();
            }
        });

        GridView mGrid = (GridView) mPopView.findViewById(R.id.mGrid);
        if(popPicturesAdapter==null){
            popPicturesAdapter = new PopPicturesAdapter(MainActivity.this,listPics2);
        }
        mGrid.setAdapter(popPicturesAdapter);

        mGrid.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if(dataList2!=null&&dataList2.size()>0){
                    Intent intent = new Intent(MainActivity.this, MyWebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("html_url","https://www.baidu.com/");
                    bundle.putString("title_name",dataList2.get(position).get("title").toString());
                    bundle.putString("htmlData",dataList2.get(position).get("content").toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_right_area:
                if(CommonSettingProvider.getUserId(MainActivity.this).equals("")) {
                    loadNext(LoginActivity.class);
                }else{
//                    Toast.makeText(MainActivity.this,"用户已登录",Toast.LENGTH_SHORT).show();
                    loadNext(MeActivity.class);
                }
                break;
            case R.id.title_ref_area:
                getAdList(1);
                getAdList(2);
                break;
            case R.id.add_car_info_line:
                loadNext(AddCarInfoActivity.class);
                break;
            case R.id.violate_rules_line:
//                loadNext(ViolateRulesActivity.class);

                Intent intent = new Intent(MainActivity.this, MyWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("html_url", "http://125.46.53.194:88/Lyzjj/Default.aspx");
                bundle.putString("title_name", "违章查询");
                bundle.putString("htmlData", "http://125.46.53.194:88/Lyzjj/Default.aspx");
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.more_tv:
                if(listPics2.size()>0){
                    showClickPopupWindow();
                }
                break;
            case R.id.more_but:
                if(listPics2.size()>0){
                    showClickPopupWindow();
                }
                break;
            case R.id.whose_car_line:
                loadNext(WhoseCarActivity.class);
                break;
            case R.id.car_wash_line:
                loadNext(CarWashActivity.class);
                break;
            case R.id.car_repair_line:
                loadNext(CarRepairActivity.class);
                break;
            case R.id.car_upkeep_line:
                loadNext(CarUpkeepActivity.class);
                break;
            case R.id.help_line:
                loadNext(RoadHelpActivity.class);
                break;
            case R.id.car_user_trade_lin:
                loadNext(CarUsedTradeActivity.class);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(MainActivity.this);
    }
}
