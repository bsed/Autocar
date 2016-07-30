package com.huanyun.autocar.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

	private boolean isCanScroll = false;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setIsCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	// @Override
	// public void scrollTo(int x, int y) {
	// if (isCanScroll) {
	// super.scrollTo(x, y);
	// }
	// }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		try {
			if (isCanScroll) {
				return super.onInterceptTouchEvent(arg0);
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			return super.onTouchEvent(arg0);
		}

		return false;
	}
}
