package com.example.he.alltest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.util.ArrayList;

/**
 * ================================================
 * 作    者：何云超
 * 版    本：
 * 创建日期：2018/1/5
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class CheckCodeView extends LinearLayout implements TextWatcher, View.OnKeyListener{

    private int mBoxNum = 6; //验证码的个数
    private int mBoxWidth = 80; //验证框的宽
    private int mBoxHeight = 80; //验证框的高
    private int mEditTextTopMargin = 14;
    private int mEditTextBottomMargin = 14;
    private int mEditTextLeftMargin = 14;
    private int mEditTextRightMargin = 14;
    private int currenntPositon = 0;
    private Drawable mBoxBgNormal = null;
    private Drawable mBoxBgFocus = null;
    private ArrayList<EditText> mEditTextList = new ArrayList<EditText>();

    public CheckCodeView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public CheckCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CheckCodeView);
        mBoxNum = typedArray.getInt(R.styleable.CheckCodeView_box_num, 2);
        mBoxWidth = (int) typedArray.getDimension(R.styleable.CheckCodeView_box_width, 80);
        mBoxHeight = (int) typedArray.getDimension(R.styleable.CheckCodeView_box_height, 80);
        mEditTextTopMargin = (int) typedArray.getDimension(R.styleable.CheckCodeView_mEditTextTopMargin, 14);
        mEditTextBottomMargin = (int) typedArray.getDimension(R.styleable.CheckCodeView_mEditTextBottomMargin, 14);
        mEditTextLeftMargin = (int) typedArray.getDimension(R.styleable.CheckCodeView_mEditTextLeftMargin, 14);
        mEditTextRightMargin = (int) typedArray.getDimension(R.styleable.CheckCodeView_mEditTextRightMargin, 14);
        mBoxBgNormal = typedArray.getDrawable(R.styleable.CheckCodeView_box_bg_normal);
        mBoxBgFocus = typedArray.getDrawable(R.styleable.CheckCodeView_box_bg_focus);
        typedArray.recycle();
        initView();
    }



    /**
     * 初始化验证码的布局
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        for (int i = 0; i <mBoxNum; i++) {
            EditText editText = new EditText(getContext());
            LayoutParams layoutParams = new LayoutParams(mBoxWidth, mBoxHeight);
            layoutParams.topMargin = mEditTextTopMargin;
            layoutParams.bottomMargin = mEditTextBottomMargin;
            layoutParams.leftMargin = mEditTextLeftMargin;
            layoutParams.rightMargin = mEditTextRightMargin;
            layoutParams.gravity = Gravity.CENTER;

            editText.setLayoutParams(layoutParams);
            if (i == 0) {
                setEditTextBackground(editText,true);
            } else {
                setEditTextBackground(editText,false);
            }
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.addTextChangedListener(this);
            editText.setOnKeyListener(this);
            addView(editText);
            mEditTextList.add(editText);
        }

    }

    /**
     * 设置验证框的背景色
     * @param editText
     * @param focus
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setEditTextBackground(EditText editText, Boolean focus) {
        if (mBoxBgNormal != null && focus) {

            editText.setBackground(mBoxBgFocus);
            editText.setCursorVisible(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            editText.setGravity(Gravity.CENTER_HORIZONTAL);

        } else if (mBoxBgFocus != null && !focus) {
            editText.setBackground(mBoxBgNormal);
            editText.setCursorVisible(false);
            editText.setFocusableInTouchMode(false);
            editText.setFocusable(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (start == 0 && count >= 1 && currenntPositon != mEditTextList.size() - 1 ) {
            setEditTextBackground(mEditTextList.get(currenntPositon),false);
            currenntPositon++;
            setEditTextBackground(mEditTextList.get(currenntPositon),true);
        }else if (currenntPositon == mEditTextList.size() -1 && !TextUtils.isEmpty(mEditTextList.get(currenntPositon).getText())) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mEditTextList.size(); i++) {
               stringBuilder.append( mEditTextList.get(i).getText());
            }
            if (mCheckcodeCompleteListene != null) {
                mCheckcodeCompleteListene.setInputCompleteListener(stringBuilder);
            }

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
        if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            return false;
        }
        if (!TextUtils.isEmpty(mEditTextList.get(currenntPositon).getText()) && keycode == KeyEvent.KEYCODE_DEL && currenntPositon == mEditTextList.size() - 1 ) {
            return false;
        }

        if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && currenntPositon> 0   ) {

            setEditTextBackground(mEditTextList.get(currenntPositon),false);
            currenntPositon--;
            setEditTextBackground(mEditTextList.get(currenntPositon),true);

                mEditTextList.get(currenntPositon).setText("");
        }
        return false;
    }

    public interface OnInputCompleteListener{
        void setInputCompleteListener(StringBuilder s);
    }

    private OnInputCompleteListener mCheckcodeCompleteListene;


    public void setOnCheckcodeCompleteListener(OnInputCompleteListener listener) {
        mCheckcodeCompleteListene = listener;
    }
}
