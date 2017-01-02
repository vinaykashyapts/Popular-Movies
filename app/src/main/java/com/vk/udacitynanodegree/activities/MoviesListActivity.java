package com.vk.udacitynanodegree.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.vk.udacitynanodegree.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 19/4/16.
 */
public class MoviesListActivity extends BaseActivity {

    private ArrayList<MovieItem> movieItems;

    @Bind(R.id.moviesView)
    RecyclerView moviesView;

    protected RecyclerView.LayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.movies));
        baseToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_sort));
        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        moviesView.setLayoutManager(gridLayoutManager);

        fetchMovies(RestAPI.MOVIES_POPULAR);
    }

    private void fetchMovies(int type) {

        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(this, getString(R.string.noInternet));
            return;
        }
        moviesView.setAdapter(null);
        showProgress();

        String url;

        if (type == RestAPI.MOVIES_POPULAR)
            url = getApiUrl(RestAPI.MOVIES_POPULAR, -1);
        else
            url = getApiUrl(RestAPI.MOVIES_TOP_RATED, -1);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hideProgress();

                Movies movies = new Gson().fromJson(response.toString(), Movies.class);
                movieItems = movies.getResults();
                MoviesListAdapter moviesListAdapter = new MoviesListAdapter(MoviesListActivity.this, movieItems, false);
                moviesView.setAdapter(moviesListAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(MoviesListActivity.this, getString(R.string.fetchFailure));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        MenuItem item = menu.findItem(R.id.action_movie_favourite);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_movie_popular:
                fetchMovies(RestAPI.MOVIES_POPULAR);
                return true;

            case R.id.action_movie_toprated:
                fetchMovies(RestAPI.MOVIES_TOP_RATED);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
