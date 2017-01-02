package com.vk.udacitynanodegree.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.fragments.MovieReviewsFragment;
import com.vk.udacitynanodegree.fragments.MovieTrailersFragment;
import com.vk.udacitynanodegree.models.MovieItem;
import com.vk.udacitynanodegree.models.MovieTrailers;
import com.vk.udacitynanodegree.network.RestAPI;
import com.vk.udacitynanodegree.utils.Constants;
import com.vk.udacitynanodegree.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 14/7/16.
 */
public class MoviesDetailOfflineActivity extends AppCompatActivity implements View.OnClickListener {

    MovieItem movieItem;

    @Bind(R.id.collapsingToolbar)
    Toolbar toolbar;
    @Bind(R.id.backdrop)
    ImageView backDrop;
    @Bind(R.id.originalTitle)
    TextView originalTitle;
    @Bind(R.id.releaseDate)
    TextView releaseDate;
    @Bind(R.id.userRating)
    TextView userRating;
    @Bind(R.id.synopsis)
    TextView synopsis;
    @Bind(R.id.isFavorite)
    ImageView isFavorite;
    @Bind(R.id.scroll)
    NestedScrollView nestedScrollView;
    @Bind(R.id.trailersContainer)
    LinearLayout trailersContainer;
    @Bind(R.id.reviewsContainer)
    LinearLayout reviewsContainer;
    private MenuItem mMenuItemShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail_offline);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieItem = getIntent().getParcelableExtra(Constants.KEY_MOVIE_ITEM);

        initLayout();
    }

    public void initLayout() {

        getSupportActionBar().setTitle(movieItem.getTitle());

        String imagePath = RestAPI.URL_IMAGES_SERVER + movieItem.getBackdropPath();
        Glide.with(this)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(backDrop);
        originalTitle.setText(movieItem.getOriginalTitle());
        releaseDate.setText(movieItem.getReleaseDate());
        userRating.setText(String.valueOf(movieItem.getVoteAverage()));
        synopsis.setText(movieItem.getOverview());
        isFavorite.setOnClickListener(this);
        refreshFavorite();
        setMovieInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        mMenuItemShare = menu.findItem(R.id.menu_share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_share:
                if(Utils.isNetworkAvailable(MoviesDetailOfflineActivity.this)) {
                    MovieTrailersFragment fragment = (MovieTrailersFragment) getSupportFragmentManager().findFragmentByTag(MovieTrailersFragment.class.getSimpleName());
                    if(fragment != null && fragment.trailers != null && fragment.trailers.getResults() != null)
                        Utils.shareTrailer(MoviesDetailOfflineActivity.this, fragment.trailers.getResults().get(0));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.isFavorite:
                setFavorite();
                break;
        }
    }

    private void setFavorite() {
        if (movieItem.isFavorite == 1)
            movieItem.setIsFavorite(0);
        else
            movieItem.setIsFavorite(1);

        movieItem.updateFavorite(this);
        refreshFavorite();
    }

    private void refreshFavorite() {
        if (movieItem.isFavorite == 1)
            isFavorite.setColorFilter(getResources().getColor(R.color.app_theme));
        else
            isFavorite.setColorFilter(getResources().getColor(R.color.white));
    }

    private void setMovieInfo() {
        nestedScrollView.setFillViewport(true);

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_MOVIE_ID, movieItem.getMovieId());

        Fragment fragment = new MovieTrailersFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.trailersContainer, fragment, MovieTrailersFragment.class.getSimpleName())
                .commit();

        fragment = new MovieReviewsFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.reviewsContainer, fragment, MovieReviewsFragment.class.getSimpleName())
                .commit();
    }
}
