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
import com.vk.udacitynanodegree.activities.BaseActivity;
import com.vk.udacitynanodegree.adapters.MovieTrailersAdapter;
import com.vk.udacitynanodegree.models.MovieTrailers;
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

public class MovieTrailersFragment extends Fragment {

    @Bind(R.id.trailersView)
    RecyclerView trailersView;
    @Bind(R.id.trailersProgress)
    ProgressWheel trailersProgress;
    @Bind(R.id.trailersEmptyData)
    TextView trailersEmptyData;

    public MovieTrailers trailers;

    public MovieTrailersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            trailers = savedInstanceState.getParcelable(MovieTrailers.class.getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(trailers != null) displayTrailerInfo(trailers);
        else fetchTrailerInfo();
    }

    private void fetchTrailerInfo() {
        showProgress();
        if (!Utils.isNetworkAvailable(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.noInternet));
            showEmptyData(getString(R.string.fetchFailure));
            return;
        }

        RestService restService = RestService.getInstance(getActivity());
        String url = restService.getApiUrl(RestAPI.MOVIES_TRAILERS, getArguments().getInt(Constants.KEY_MOVIE_ID));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                trailers = new Gson().fromJson(response.toString(), MovieTrailers.class);
                displayTrailerInfo(trailers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(getActivity() != null) showEmptyData(getString(R.string.fetchFailure));
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest.setTag(RestAPI.MOVIES_TRAILERS);
        restService.addToRequestQueue(jsonObjectRequest);
    }

    private void displayTrailerInfo(MovieTrailers trailers) {
        hideProgress();
        hideEmptyData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        trailersView.setLayoutManager(linearLayoutManager);
        MovieTrailersAdapter trailersAdapter = new MovieTrailersAdapter(trailers);
        trailersView.setAdapter(trailersAdapter);
        if(trailers.getResults().size() == 0 && getActivity() != null)
            showEmptyData(getString(R.string.noResults));
    }

    public void showProgress() {
        trailersProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        trailersProgress.setVisibility(View.GONE);
    }

    public void showEmptyData(String message) {
        hideProgress();
        trailersEmptyData.setText(message);
        trailersEmptyData.setVisibility(View.VISIBLE);
    }

    public void hideEmptyData() {
        trailersEmptyData.setVisibility(View.GONE);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (trailers != null) outState.putParcelable(MovieTrailers.class.getSimpleName(), trailers);
    }
}
