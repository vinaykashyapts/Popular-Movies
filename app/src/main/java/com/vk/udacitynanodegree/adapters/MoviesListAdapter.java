package com.vk.udacitynanodegree.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.activities.MoviesDetailActivity;
import com.vk.udacitynanodegree.activities.MoviesDetailOfflineActivity;
import com.vk.udacitynanodegree.models.MovieItem;
import com.vk.udacitynanodegree.network.RestAPI;
import com.vk.udacitynanodegree.utils.Constants;
import com.vk.udacitynanodegree.utils.Logger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 19/4/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder> {

    public Activity mActivity;
    public ArrayList<MovieItem> movieItems;
    private boolean isOffline;

    public MoviesListAdapter(Activity activity, ArrayList<MovieItem> moviesLists1, boolean isOffline) {
        this.mActivity = activity;
        this.movieItems = moviesLists1;
        this.isOffline = isOffline;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
        MoviesViewHolder mvh = new MoviesViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

        MovieItem list = movieItems.get(position);
        holder.movieName.setText(list.getTitle());
        String imagePath = RestAPI.URL_IMAGES_SERVER + list.getPosterPath();
        Glide.with(mActivity)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.moviePic);

        if (list.isFavorite == 1) {
            holder.isFavorite.setColorFilter(mActivity.getResources().getColor(R.color.app_theme));
        } else {
            holder.isFavorite.setColorFilter(mActivity.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.moviePic)
        ImageView moviePic;
        @Bind(R.id.movieName)
        TextView movieName;
        @Bind(R.id.isFavorite)
        ImageView isFavorite;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (isOffline) {
                isFavorite.setVisibility(View.VISIBLE);
                isFavorite.setOnClickListener(this);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.isFavorite) {
                setFavorite();
            } else {
                MovieItem item = movieItems.get(getAdapterPosition());
                Intent i;
                if(isOffline)
                    i = new Intent(mActivity, MoviesDetailOfflineActivity.class);
                else
                    i = new Intent(mActivity, MoviesDetailActivity.class);
                i.putExtra(Constants.KEY_MOVIE_ITEM, item);
                mActivity.startActivity(i);
            }
        }

        private void setFavorite() {
            MovieItem movieItem = movieItems.get(getAdapterPosition());
            if (movieItem.isFavorite == 1)
                movieItem.setIsFavorite(0);
            else
                movieItem.setIsFavorite(1);

            movieItem.updateFavorite(mActivity);
            movieItems.set(getAdapterPosition(), movieItem);
            notifyDataSetChanged();
        }
    }
}
