package com.huanyun.autocar.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huanyun.autocar.BaseActivity;
import com.huanyun.autocar.BaseFragmentActivity;
import com.huanyun.autocar.R;
import com.huanyun.autocar.fragment.CarsManageFragment;
import com.huanyun.autocar.fragment.DocumentQueryFragment;
import com.huanyun.autocar.widget.CommonTitleBar;
import com.huanyun.autocar.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 违章查询
 */
public class ViolateRulesActivity extends BaseFragmentActivity {

	public static final String TAG = ViolateRulesActivity.class
			.getSimpleName();
	public static ViolateRulesActivity violateRulesActivity;
	private CommonTitleBar commonTitleBar;
	private CustomViewPager viewPager;
	private RadioGroup radioGroup;
	private RadioButton radiob1,radiob2;
	private List<Fragment> allFragments;
	CarsManageFragment carsManageFragment;
	DocumentQueryFragment documentQueryFragment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		violateRulesActivity = this;
		setContentView(R.layout.activity_violate_rules);
		initData();
		initView();
		initFragment();
		initViewPager();
		initRadioButtonsListener();
	}

	private void initView() {
		commonTitleBar = (CommonTitleBar)findViewById(R.id.title_layout);
		commonTitleBar.setTitleTxt("查违章");
		commonTitleBar.setLeftTxtBtn("返回");
		commonTitleBar.visbleLeftBtn();
		commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commonTitleBar.setRightImg(R.mipmap.ic_launcher);
		commonTitleBar.setRightBtnOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
		viewPager = findView(R.id.viewpage);
		radioGroup = findView(R.id.picture_radio_group);
		radiob1 = findView(R.id.radiob_1);
		radiob2 = findView(R.id.radiob_2);
	}

	private void initFragment() {
		carsManageFragment = new CarsManageFragment();
		documentQueryFragment = new DocumentQueryFragment();
		if (allFragments == null) {
			allFragments = new ArrayList<Fragment>();
		} else {
			allFragments.clear();
		}
		allFragments.add(carsManageFragment);
		allFragments.add(documentQueryFragment);
	}

	private void initViewPager() {
		viewPager.setOnPageChangeListener(mPagerListener);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(mAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setIsCanScroll(true);
	}

	private void initData(){
	}


	private FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(
			getSupportFragmentManager()) {

		/** 仅执行一次 */
		@Override
		public Fragment getItem(int position) {
			Fragment result = allFragments.get(position);
			return result;
		}

		@Override
		public int getCount() {
			return allFragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	};

	private ViewPager.SimpleOnPageChangeListener mPagerListener = new ViewPager.SimpleOnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int state) {
			super.onPageScrollStateChanged(state);
		}

		public void onPageScrolled(int position, float positionOffset,
								   int positionOffsetPixels) {
		};

		@Override
		public void onPageSelected(int position) {
			switch (position) {
				case 0:
					radiob1.setChecked(true);
					break;
				case 1:
					radiob2.setChecked(true);
					break;
				default:
					break;
			}
		}
	};

	private void initRadioButtonsListener() {
		radioGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radiob_1:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.radiob_2:
                        viewPager.setCurrentItem(1);
                        break;

                }
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		violateRulesActivity = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

	}


}
