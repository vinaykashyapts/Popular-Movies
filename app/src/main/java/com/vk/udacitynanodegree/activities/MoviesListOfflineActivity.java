package com.vk.udacitynanodegree.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.adapters.MoviesListAdapter;
import com.vk.udacitynanodegree.models.MovieItem;
import com.vk.udacitynanodegree.models.Movies;
import com.vk.udacitynanodegree.network.RestAPI;
import com.vk.udacitynanodegree.utils.Constants;
import com.vk.udacitynanodegree.utils.Logger;
import com.vk.udacitynanodegree.utils.PreferenceManager;
import com.vk.udacitynanodegree.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 19/4/16.
 */
public class MoviesListOfflineActivity extends BaseActivity {

    private ArrayList<MovieItem> movieItems;

    @Bind(R.id.moviesView)
    RecyclerView moviesView;

    protected RecyclerView.LayoutManager gridLayoutManager;

    private int currentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.movies) + " " + getString(R.string.offline));
        baseToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_sort));
        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        moviesView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (currentType > 0) {
            if (currentType == RestAPI.MOVIES_FAVORITE)
                displayMovies(currentType);
            else
                showcaseMovies(currentType);
        } else {
            showcaseMovies(RestAPI.MOVIES_POPULAR);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showcaseMovies(int type) {
        if (PreferenceManager.with(this).getBoolean(String.valueOf(type), false)) {
            displayMovies(type);
        } else {
            fetchMovies(type);
        }
    }

    private void fetchMovies(final int type) {
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(this, getString(R.string.noInternet));
            return;
        }
        moviesView.setAdapter(null);
        showProgress();

        String url = getApiUrl(type, -1);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hideProgress();

                loadMovies(response.toString(), type);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(MoviesListOfflineActivity.this, getString(R.string.fetchFailure));
                showEmptyData(getString(R.string.fetchFailure));
                hideProgress();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest.setTag(RestAPI.MOVIES_POPULAR);
        restService.addToRequestQueue(jsonObjectRequest);
    }

    private void loadMovies(String response, int type) {
        Movies movies = new Gson().fromJson(response.toString(), Movies.class);
        movieItems = movies.getResults();

        // save to DB
        MovieItem movieItem = new MovieItem();
        movieItem.insertFromServer(this, movieItems, type);

        displayMovies(type);
    }

    private void displayMovies(int type) {
        currentType = type;

        // get movies from DB
        MovieItem item = new MovieItem();
        movieItems = item.getMovieItems(this, type);

        boolean isOffline = true;
        if (type == RestAPI.MOVIES_FAVORITE)
            isOffline = false;

        MoviesListAdapter moviesListAdapter = new MoviesListAdapter(MoviesListOfflineActivity.this, movieItems, isOffline);
        moviesView.setAdapter(moviesListAdapter);

        if (movieItems.size() > 0)
            hideEmptyData();
        else
            showEmptyData(getString(R.string.noResults));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_movie_popular:
                showcaseMovies(RestAPI.MOVIES_POPULAR);
                return true;

            case R.id.action_movie_toprated:
                showcaseMovies(RestAPI.MOVIES_TOP_RATED);
                return true;

            case R.id.action_movie_favourite:
                displayMovies(RestAPI.MOVIES_FAVORITE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_CURRENT_TYPE, currentType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentType = savedInstanceState.getInt(Constants.KEY_CURRENT_TYPE);
    }
}
