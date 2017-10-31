package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.utils.DensityUtils;

/**
 * xiaJue 2017/9/20创建
 */
public class SearchEditText extends RelativeLayout implements TextWatcher {
    private TextView mTextView;
    private TextView mButtonTextView;
    private EditText mEditText;

    private String mText = "点击打开";
    private String mTextHint = "请输入..";
    private float mTextSize = 15;

    //    private int mTextColor;
    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        setView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .SearchEditText, defStyleAttr, 0);
        mText = typedArray.getString(R.styleable.SearchEditText_text);
        mTextHint = typedArray.getString(R.styleable.SearchEditText_textHint);
        mTextSize = typedArray.getInt(R.styleable.SearchEditText_textSize, 15);
//        mTextColor = typedArray.getInt(R.styleable.SearchEditText_textColor, Color.GRAY);
    }


    private void setView(final Context context) {
        LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams
                        .WRAP_CONTENT);
        mTextView = new TextView(context);
        mTextView.setText(mText);
        mTextView.setTextSize(mTextSize);
//        mTitleTv.setTextColor(mTextColor);

        mEditText = new ClearEditText(context);
        mEditText.setLayoutParams(lp);
        mEditText.setHint(mTextHint);
        mEditText.setBackgroundDrawable(null);
        mEditText.setTextSize(mTextSize);
        mEditText.setVisibility(GONE);//默认隐藏editText
        mEditText.addTextChangedListener(this);
        mEditText.setMaxLines(1);
//        mEditText.setTextColor(mTextColor);

        mButtonTextView = new TextView(context);
        mButtonTextView.setText("关闭");
//        mButtonTextView.setTextColor(mTextColor);
        LayoutParams buttonLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//居右
        mButtonTextView.setLayoutParams(buttonLp);
        mButtonTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable
                .fragment_home_search_but_bg));
        mButtonTextView.setClickable(true);
        mButtonTextView.setVisibility(GONE);
        mButtonTextView.setTextSize(mTextSize);
        int padding = DensityUtils.dp2px(context, 6);
        mButtonTextView.setPadding(padding, padding, padding, padding);
        mButtonTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClose) {
                    performClick();
                    if (mButtonClickListener != null) {
                        mButtonClickListener.onButtonClose();
                    }
                    return;
                }
                if (mButtonClickListener != null) {
                    mButtonClickListener.onButtonClick(v, mEditText.getText().toString());
                }
            }
        });

        addView(mTextView);
        addView(mEditText);
        addView(mButtonTextView);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果显示,则隐藏,反之亦然
                boolean shown = mEditText.isShown();
                mEditText.setVisibility(shown ? GONE : VISIBLE);
                mButtonTextView.setVisibility(shown ? GONE : VISIBLE);
                mTextView.setVisibility(shown ? VISIBLE :
                        GONE);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    private boolean isClose = true;

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().isEmpty()) {
//            mButtonTextView.setVisibility(GONE);
            mButtonTextView.setText("关闭");
            isClose = true;
        } else {
//            mButtonTextView.setVisibility(VISIBLE);
            mButtonTextView.setText("确定");
            isClose = false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mButtonClickListener = listener;
    }

    private OnButtonClickListener mButtonClickListener;

    public EditText getEditText() {
        return mEditText;
    }

    public void setEditState(boolean b) {
        mEditText.setVisibility(b ? GONE : VISIBLE);
        performClick();
    }

    public boolean isEditState() {
        return mEditText.isShown();
    }

    public interface OnButtonClickListener {
        void onButtonClick(View view, String text);

        void onButtonClose();
    }

}
