package com.vk.udacitynanodegree.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.adapters.MovieReviewsAdapter;
import com.vk.udacitynanodegree.models.MovieReviews;
import com.vk.udacitynanodegree.network.RestAPI;
import com.vk.udacitynanodegree.network.RestService;
import com.vk.udacitynanodegree.utils.Constants;
import com.vk.udacitynanodegree.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 14/11/16.
 */

public class MovieReviewsFragment extends Fragment {

    @Bind(R.id.reviewsView)
    RecyclerView reviewsView;
    @Bind(R.id.reviewsProgress)
    ProgressWheel reviewsProgress;
    @Bind(R.id.reviewsEmptyData)
    TextView reviewsEmptyData;

    private MovieReviews reviews;

    public MovieReviewsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            reviews = savedInstanceState.getParcelable(MovieReviews.class.getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(reviews != null) displayReviewsInfo(reviews);
        else fetchReviewsInfo();
    }

    private void fetchReviewsInfo() {
        showProgress();
        if (!Utils.isNetworkAvailable(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.noInternet));
            showEmptyData(getString(R.string.fetchFailure));
            return;
        }

        RestService restService = RestService.getInstance(getActivity());
        String url = restService.getApiUrl(RestAPI.MOVIES_REVIEWS, getArguments().getInt(Constants.KEY_MOVIE_ID));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MovieReviews reviews = new Gson().fromJson(response.toString(), MovieReviews.class);
                displayReviewsInfo(reviews);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showEmptyData(getString(R.string.fetchFailure));
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest.setTag(RestAPI.MOVIES_TRAILERS);
        restService.addToRequestQueue(jsonObjectRequest);
    }

    private void displayReviewsInfo(MovieReviews reviews) {
        hideProgress();
        hideEmptyData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        reviewsView.setLayoutManager(linearLayoutManager);
        MovieReviewsAdapter reviewsAdapter = new MovieReviewsAdapter(getActivity(), reviews);
        reviewsView.setAdapter(reviewsAdapter);
        if(reviews.getResults().size() == 0)
            showEmptyData(getString(R.string.noResults));
    }

    public void showProgress() {
        reviewsProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        reviewsProgress.setVisibility(View.GONE);
    }

    public void showEmptyData(String message) {
        hideProgress();
        reviewsEmptyData.setText(message);
        reviewsEmptyData.setVisibility(View.VISIBLE);
    }

    public void hideEmptyData() {
        reviewsEmptyData.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (reviews != null) outState.putParcelable(MovieReviews.class.getSimpleName(), reviews);
    }
}