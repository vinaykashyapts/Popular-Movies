package nanodegree.udacity.vk.com.udacitynanodegree.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nanodegree.udacity.vk.com.udacitynanodegree.R;
import nanodegree.udacity.vk.com.udacitynanodegree.activities.MoviesDetailActivity;
import nanodegree.udacity.vk.com.udacitynanodegree.models.MovieItem;
import nanodegree.udacity.vk.com.udacitynanodegree.network.RestAPI;
import nanodegree.udacity.vk.com.udacitynanodegree.utils.Constants;

/**
 * Created by Vinay on 19/4/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder> {

    public Context context;
    public ArrayList<MovieItem> movieItems;

    public MoviesListAdapter(Context c, ArrayList<MovieItem> moviesLists1) {
        this.context = c;
        this.movieItems = moviesLists1;
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
        Glide.with(context)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.moviePic);
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

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, MoviesDetailActivity.class);
            i.putExtra(Constants.KEY_MOVIE_ITEM, movieItems.get(getAdapterPosition()));
            context.startActivity(i);
        }
    }
}
