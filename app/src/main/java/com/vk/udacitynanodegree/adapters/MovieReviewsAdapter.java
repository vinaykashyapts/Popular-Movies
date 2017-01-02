package com.vk.udacitynanodegree.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.models.MovieReviews;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 27/12/16.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder> {

    private Activity mActivity;
    private MovieReviews reviews;

    public MovieReviewsAdapter(Activity activity, MovieReviews reviews) {
        this.mActivity = activity;
        this.reviews = reviews;
    }

    @Override
    public MovieReviewsAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new MovieReviewsAdapter.ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieReviewsAdapter.ReviewViewHolder holder, int position) {
        final MovieReviews.Review review = reviews.getResults().get(position);
        holder.reviewMsg.setText(review.getContent());
        holder.reviewBy.setText(review.getAuthor());
        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.getResults().size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.reviewMsg)
        TextView reviewMsg;
        @Bind(R.id.reviewBy)
        TextView reviewBy;
        @Bind(R.id.readMore)
        TextView readMore;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
