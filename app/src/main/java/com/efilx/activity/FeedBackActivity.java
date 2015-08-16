package com.efilx.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
* Created by Dinesh on 4/19/2015.
*/
public class FeedBackActivity extends ActionBarActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context = this;
    }
}
