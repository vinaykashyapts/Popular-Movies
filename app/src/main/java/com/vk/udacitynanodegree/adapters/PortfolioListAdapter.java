package com.vk.udacitynanodegree.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.activities.MoviesListActivity;
import com.vk.udacitynanodegree.activities.MoviesListOfflineActivity;
import com.vk.udacitynanodegree.utils.Constants;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 3/2/16.
 */
public class PortfolioListAdapter extends RecyclerView.Adapter<PortfolioListAdapter.PortfolioViewHolder> {

    private Activity mActivity;
    private List<String> projectTitles;
    private List<String> projectDescriptions;

    public PortfolioListAdapter(Activity activity) {
        mActivity = activity;
        projectTitles = Arrays.asList(mActivity.getResources().getStringArray(R.array.projectTitles));
        projectDescriptions = Arrays.asList(mActivity.getResources().getStringArray(R.array.projectDescription));
    }

    @Override
    public PortfolioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_list_item, parent, false);
        PortfolioViewHolder pvh = new PortfolioViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PortfolioViewHolder holder, int position) {
        int projectNumber = position + 1;
        holder.projectNumber.setText(mActivity.getString(R.string.project) + projectNumber);
        holder.projectName.setText(projectTitles.get(position));
        holder.projectDescription.setText(projectDescriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return projectTitles.size();
    }

    public class PortfolioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.projectNumber) TextView projectNumber;
        @Bind(R.id.projectName) TextView projectName;
        @Bind(R.id.projectDescription) TextView projectDescription;

        public PortfolioViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(getAdapterPosition() == 0) {
                mActivity.startActivity(new Intent(mActivity, MoviesListActivity.class));
             } else if(getAdapterPosition() == 1) {
                checkPermission();
            } else {
                Toast.makeText(mActivity, projectTitles.get(getAdapterPosition()) + mActivity.getString(R.string.projectToast), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.WRITE_CONTACTS_PERMISSION);
        } else {
            mActivity.startActivity(new Intent(mActivity, MoviesListOfflineActivity.class));
        }
    }
}
