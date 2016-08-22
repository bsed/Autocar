package com.huanyun.autocar.widget;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huanyun.autocar.R;

//import com.beumu.xiangyin.R;


/**
 *
 */
public class CommonTitleBar extends RelativeLayout {

    // 防重复点击时间
    private static final int BTN_LIMIT_TIME = 500;

    private TextView leftButton;
    private ImageView leftButtonImg;
    private TextView middleButton;
    private TextView rightButton;
    private ImageView rightButtonImg;

    private int              leftBtnIconId;
    private String leftBtnStr;
    private String titleTxtStr;
    private String rightBtnStr;
    private int              rightBtnIconId;

    private String bgStyle;
    private Context context;

    public CommonTitleBar(Context context) {
        super(context);
        this.context = context;
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
//        leftBtnStr = arr.getString(R.styleable.CommonTitleBar_leftBtnTxt);
//        leftBtnIconId = arr.getResourceId(R.styleable.CommonTitleBar_leftBtnIcon, 0);
//        titleTxtStr = arr.getString(R.styleable.CommonTitleBar_titleTxt);
//        rightBtnStr = arr.getString(R.styleable.CommonTitleBar_rightBtnTxt);
//        rightBtnIconId = arr.getResourceId(R.styleable.CommonTitleBar_rightBtnIcon, 0);
//        bgStyle = arr.getString(R.styleable.CommonTitleBar_baseStyle);
//        if (isInEditMode()) {
//            LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
//            return;
//        }
//        LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
//        findViewById(R.id.title_out_frame).setBackgroundResource(R.color.colorPrimary);
        this.context = context;

//        TypedArray arr = context.obtainStyledAttributes(attrs, new int[]{ResourceHelper.getIdByName(context, "attr", "CommonTitleBar")});
//        leftBtnStr = arr.getString(ResourceHelper.getIdByName(context, "styleable", "CommonTitleBar_leftBtnTxt"));
//        leftBtnIconId = arr.getResourceId(ResourceHelper.getIdByName(context, "styleable", "CommonTitleBar_leftBtnTxt"), 0);
//        titleTxtStr = arr.getString(ResourceHelper.getIdByName(context, "styleable", "CommonTitleBar_titleTxt"));
//        rightBtnStr = arr.getString(ResourceHelper.getIdByName(context, "styleable", "CommonTitleBar_rightBtnTxt"));
//        rightBtnIconId = arr.getResourceId(ResourceHelper.getIdByName(context, "styleable", "CommonTitleBar_rightBtnIcon"), 0);
//        bgStyle = arr.getString(ResourceHelper.getIdByName(context, "styleable", "CommonTitleBar_baseStyle"));
        if (isInEditMode()) {
            LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
            return;
        }

        LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
                findViewById(R.id.title_out_frame).setBackgroundResource(R.color.ykyin_white_normal);
//        arr.recycle();
    }

    protected void onFinishInflate() {
        if (isInEditMode()) {
            return;
        }

        leftButtonImg = (ImageView) findViewById(R.id.title_left_btn);
        leftButton = (TextView) findViewById(R.id.title_left);
        middleButton = (TextView) findViewById(R.id.title_middle);
        rightButtonImg = (ImageView) findViewById(R.id.title_right_btn);
        rightButton = (TextView) findViewById(R.id.title_right);

        if (leftBtnIconId != 0) {
            leftButtonImg.setImageResource(leftBtnIconId);
            leftButtonImg.setVisibility(View.VISIBLE);
        } else {
            leftButtonImg.setVisibility(View.GONE);
        }
        if (rightBtnIconId != 0) {
            rightButtonImg.setImageResource(rightBtnIconId);
            rightButtonImg.setVisibility(View.VISIBLE);
        } else {
            rightButtonImg.setVisibility(View.GONE);
        }
        setLeftTxtBtn(leftBtnStr);
        setTitleTxt(titleTxtStr);
        setRightTxtBtn(rightBtnStr);
    }

    public void setRightTxtBtn(String btnTxt) {
        if (!TextUtils.isEmpty(btnTxt)) {
            rightButton.setText(btnTxt);
            rightButton.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }
    }

    public void setRightImg(int id) {
        rightButtonImg.setVisibility(View.VISIBLE);
        rightButtonImg.setImageResource(id);
    }

    public void setLeftTxtBtn(String leftBtnStr) {
        if (!TextUtils.isEmpty(leftBtnStr)) {
            leftButton.setText(leftBtnStr);
            leftButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
        }
    }

    public void setTitleTxt(String title) {
        if (!TextUtils.isEmpty(title)) {
            middleButton.setText(title);
            middleButton.setVisibility(View.VISIBLE);
        } else {
            middleButton.setVisibility(View.GONE);
        }
    }

    public void hideLeftBtn() {
        leftButton.setVisibility(View.GONE);
        leftButtonImg.setVisibility(View.GONE);
        findViewById(R.id.title_left_area).setOnClickListener(null);
    }

    public void visbleLeftBtn() {
        leftButtonImg.setVisibility(View.VISIBLE);
    }

    public void hideRightBtn() {
        rightButton.setVisibility(View.GONE);
        rightButtonImg.setVisibility(View.GONE);
        findViewById(R.id.title_right_area).setOnClickListener(null);
    }

    public void hideTitleLine(){
        findViewById(R.id.title_line).setVisibility(View.GONE);
    }

    public void setLeftBtnOnclickListener(OnClickListener myListener) {
//        OnClickListener myListener = new GlobalLimitClickOnClickListener(listener, BTN_LIMIT_TIME);
        findViewById(R.id.title_left_area).setOnClickListener(myListener);
    }
//
    public void setRightBtnOnclickListener(OnClickListener myListener) {
//        OnClickListener myListener = new GlobalLimitClickOnClickListener(listener, BTN_LIMIT_TIME);
        findViewById(R.id.title_right_area).setOnClickListener(myListener);
    }

}
