package com.huanyun.autocar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huanyun.autocar.R;

/**
 * Created by admin on 2016/1/19.
 */
public class CarsManageFragment extends BaseFragment {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_cars_manage, container, false);
        }
        initView(rootView);
        return rootView;
    }

    private void initView(View view){


    }


}
