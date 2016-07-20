package nanodegree.udacity.vk.com.udacitynanodegree.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import nanodegree.udacity.vk.com.udacitynanodegree.R;

import com.pnikosis.materialishprogress.ProgressWheel;

import nanodegree.udacity.vk.com.udacitynanodegree.network.RestService;

/**
 * Created by Vinay on 19/4/16.
 */
public class BaseActivity extends AppCompatActivity {

    private View baseView;
    protected Context mContext;
    public RestService restService;
    private ProgressWheel progressWheel;
    public Toolbar baseToolbar;

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
        super.setContentView(baseView);
        setToolbar();
    }

    public String getApiUrl(int api) {
        return restService.getApiUrl(api);
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
}
