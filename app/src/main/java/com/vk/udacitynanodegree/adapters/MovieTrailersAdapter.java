package com.vk.udacitynanodegree.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.models.MovieTrailers;
import com.vk.udacitynanodegree.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 9/12/16.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder> {

    private  MovieTrailers trailers;

    public MovieTrailersAdapter(MovieTrailers trailers) {
        this.trailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        final MovieTrailers.Trailer trailer = trailers.getResults().get(position);
        holder.trailerName.setText(trailer.getName());
        holder.watchTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.watchYoutubeVideo(v.getContext(), trailer.getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.getResults().size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.trailerName) TextView trailerName;
        @Bind(R.id.watchTrailer) ImageView watchTrailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
