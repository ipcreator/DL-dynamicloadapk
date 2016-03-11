package com.ygsoft.pluginapp.pluginapp;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by yuting2 on 2016/3/10.
 */
public class SearchView extends LinearLayout {


    private Context context;
    private LayoutInflater mLayoutInflater;

    private EditText mKeyword;
    private Button mBtn;

    public SearchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        Resources resources = null;
        if (context instanceof BaseActivity) {
            resources = ((BaseActivity)context).getResources();
            Log.d(SearchView.class.getSimpleName(), "BaseActivity subclass");
        } else {
            resources = FullContext.getInstance().getmResources();
            Log.d(SearchView.class.getSimpleName(), "Not BaseActivity subclass");
        }

        XmlResourceParser parser = resources.getLayout(R.layout.enabled_text_view);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(parser, this, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addView(view, layoutParams);


        Log.d(SearchView.class.getSimpleName(), "View view=" + view);

//        mBtn = (Button) findViewById(R.id.btn);
//        mKeyword = (EditText) findViewById(R.id.keyword);
    }

}
