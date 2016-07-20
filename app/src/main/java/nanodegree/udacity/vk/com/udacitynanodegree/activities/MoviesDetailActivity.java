package nanodegree.udacity.vk.com.udacitynanodegree.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import nanodegree.udacity.vk.com.udacitynanodegree.R;
import nanodegree.udacity.vk.com.udacitynanodegree.models.MovieItem;
import nanodegree.udacity.vk.com.udacitynanodegree.network.RestAPI;
import nanodegree.udacity.vk.com.udacitynanodegree.utils.Constants;

/**
 * Created by Vinay on 14/7/16.
 */
public class MoviesDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieItem = getIntent().getParcelableExtra(Constants.KEY_MOVIE_ITEM);
        getSupportActionBar().setTitle(movieItem.getTitle());
        initLayout();
    }

    public void initLayout() {
        String imagePath = RestAPI.URL_IMAGES_SERVER + movieItem.getBackdropPath();
        Glide.with(this)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(backDrop);
        originalTitle.setText(movieItem.getOriginalTitle());
        releaseDate.setText(movieItem.getReleaseDate());
        userRating.setText(String.valueOf(movieItem.getVoteAverage()));
        synopsis.setText(movieItem.getOverview());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
