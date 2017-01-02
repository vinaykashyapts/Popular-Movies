package com.vk.udacitynanodegree.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.pnikosis.materialishprogress.ProgressWheel;
import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.network.RestService;

import org.w3c.dom.Text;

/**
 * Created by Vinay on 19/4/16.
 */
public class BaseActivity extends AppCompatActivity {

    private View baseView;
    protected Context mContext;
    public RestService restService;
    private ProgressWheel progressWheel;
    public Toolbar baseToolbar;
    public TextView emptyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        restService = RestService.getInstance(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        baseView = (View) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) baseView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        baseToolbar = (Toolbar) baseView.findViewById(R.id.defaultToolbar);
        progressWheel = (ProgressWheel) baseView.findViewById(R.id.progress_wheel);
        emptyData = (TextView) baseView.findViewById(R.id.emptyData);
        super.setContentView(baseView);
        setToolbar();
    }

    public String getApiUrl(int api, int movieId) {
        return restService.getApiUrl(api, movieId);
    }

    public void showProgress() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if(progressWheel != null)
            progressWheel.setVisibility(View.GONE);
    }

    public void setToolbar() {
        setSupportActionBar(baseToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void showEmptyData(String message) {
        emptyData.setText(message);
        emptyData.setVisibility(View.VISIBLE);
    }

    public void hideEmptyData() {
        emptyData.setVisibility(View.GONE);
    }
}
