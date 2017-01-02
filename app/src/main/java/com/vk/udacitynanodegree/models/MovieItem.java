package com.vk.udacitynanodegree.models;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;
import com.vk.udacitynanodegree.activities.MoviesListOfflineActivity;
import com.vk.udacitynanodegree.db.CpContent;
import com.vk.udacitynanodegree.db.CpProvider;
import com.vk.udacitynanodegree.db.metaData.DataType;
import com.vk.udacitynanodegree.db.metaData.TableColumn;
import com.vk.udacitynanodegree.network.RestAPI;
import com.vk.udacitynanodegree.utils.Constants;
import com.vk.udacitynanodegree.utils.Logger;
import com.vk.udacitynanodegree.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 22/3/16.
 */
public class MovieItem extends CpContent implements Parcelable {

    private static final String TAG = MovieItem.class.getSimpleName();
    public static final String TABLE_NAME = Constants.TABLE_MOVIES;
    public static final Uri CONTENT_URI = Uri.parse(CpContent.CONTENT_URI + "/" + TABLE_NAME);
    public static final String TYPE_TABLE = "vnd.android.cursor.dir/cp-movies";
    public static final String TYPE_ID = "vnd.android.cursor.item/cp-movies";

    public static final TableColumn tc_id = new TableColumn(BaseColumns._ID, DataType.INTEGER, true, true);
    public static final TableColumn tc_title = new TableColumn("title", DataType.TEXT);
    public static final TableColumn tc_originalTitle = new TableColumn("originalTitle", DataType.TEXT);
    public static final TableColumn tc_overview = new TableColumn("overview", DataType.TEXT);
    public static final TableColumn tc_posterPath = new TableColumn("posterPath", DataType.TEXT);
    public static final TableColumn tc_backdropPath = new TableColumn("backdropPath", DataType.TEXT);
    public static final TableColumn tc_releaseDate = new TableColumn("releaseDate", DataType.TEXT);
    public static final TableColumn tc_voteAverage = new TableColumn("voteAverage", DataType.DOUBLE);
    public static final TableColumn tc_movieType = new TableColumn("movieType", DataType.INTEGER);
    public static final TableColumn tc_isFavorite = new TableColumn("isFavorite", DataType.INTEGER);
    public static final TableColumn tc_movieId = new TableColumn("movieId", DataType.INTEGER);

    @SerializedName("poster_path")
    public String posterPath;
    public boolean adult;
    public String overview;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("genre_ids")
    public List<Integer> genreIds = new ArrayList<Integer>();
    public int _id;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("original_language")
    public String originalLanguage;
    public String title;
    @SerializedName("backdrop_path")
    public String backdropPath;
    public float popularity;
    public int voteCount;
    public boolean video;
    @SerializedName("vote_average")
    public double voteAverage;
    public int movieType;
    public int isFavorite;
    @SerializedName("id")
    public int movieId;

    public MovieItem() {
        super(TABLE_NAME);
        columns = getMovieColumns();
    }

    public static ArrayList<TableColumn> getMovieColumns() {
        ArrayList<TableColumn> columns = new ArrayList<>();
        columns.add(tc_id);
        columns.add(tc_title);
        columns.add(tc_originalTitle);
        columns.add(tc_overview);
        columns.add(tc_posterPath);
        columns.add(tc_backdropPath);
        columns.add(tc_releaseDate);
        columns.add(tc_voteAverage);
        columns.add(tc_movieType);
        columns.add(tc_isFavorite);
        columns.add(tc_movieId);
        return columns;
    }


    protected MovieItem(Parcel in) {
        super(TABLE_NAME);
        movieId = in.readInt();
        posterPath = in.readString();
        adult = in.readByte() != 0x00;
        overview = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0x01) {
            genreIds = new ArrayList<Integer>();
            in.readList(genreIds, Integer.class.getClassLoader());
        } else {
            genreIds = null;
        }
        _id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readFloat();
        voteCount = in.readInt();
        video = in.readByte() != 0x00;
        voteAverage = in.readDouble();
        movieType = in.readInt();
        isFavorite = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        if (genreIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genreIds);
        }
        dest.writeInt(_id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
        dest.writeDouble(voteAverage);
        dest.writeInt(movieType);
        dest.writeInt(isFavorite);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getMovieType() {
        return movieType;
    }

    public void setMovieType(int movieType) {
        this.movieType = movieType;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void updateFavorite(Context context) {
        ContentValues mUpdateValues = new ContentValues();
        mUpdateValues.put(tc_isFavorite.getColumnName(), isFavorite);

        String selection = tc_id.getColumnName() + "=?";
        context.getContentResolver().update(CONTENT_URI, mUpdateValues, selection, new String[]{String.valueOf(get_id())});
    }

    public ArrayList<MovieItem> getMovieItems(Context mContext, int type) {

        if(type == RestAPI.MOVIES_FAVORITE) {
            String selection = tc_isFavorite.getColumnName() + " =? ";
            Cursor c = mContext.getContentResolver().query(CONTENT_URI, getProjection(), selection, new String[]{String.valueOf(1)}, null);
            return traverseCursor(c);
        } else {
            String selection = tc_movieType.getColumnName() + " =? ";
            Cursor c = mContext.getContentResolver().query(CONTENT_URI, getProjection(), selection, new String[]{String.valueOf(type)}, null);
            return traverseCursor(c);
        }
    }

    private ArrayList<MovieItem> traverseCursor(Cursor c) {
        ArrayList<MovieItem> items = new ArrayList<>();
        while (c.moveToNext()) {
            MovieItem item = new MovieItem();
            item.set_id(c.getInt(c.getColumnIndexOrThrow(tc_id.getColumnName())));
            item.setTitle(c.getString(c.getColumnIndexOrThrow(tc_title.getColumnName())));
            item.setOriginalTitle(c.getString(c.getColumnIndexOrThrow(tc_originalTitle.getColumnName())));
            item.setOverview(c.getString(c.getColumnIndexOrThrow(tc_overview.getColumnName())));
            item.setPosterPath(c.getString(c.getColumnIndexOrThrow(tc_posterPath.getColumnName())));
            item.setBackdropPath(c.getString(c.getColumnIndexOrThrow(tc_backdropPath.getColumnName())));
            item.setReleaseDate(c.getString(c.getColumnIndexOrThrow(tc_releaseDate.getColumnName())));
            item.setVoteAverage(c.getDouble(c.getColumnIndexOrThrow(tc_voteAverage.getColumnName())));
            item.setMovieType(c.getInt(c.getColumnIndexOrThrow(tc_movieType.getColumnName())));
            item.setIsFavorite(c.getInt(c.getColumnIndexOrThrow(tc_isFavorite.getColumnName())));
            item.setMovieId(c.getInt(c.getColumnIndexOrThrow(tc_movieId.getColumnName())));
            items.add(item);
        }
        c.close();
        return items;
    }

    public void insertFromServer(Context context, ArrayList<MovieItem> movieItems, int type) {

        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        for (int i = 0; i < movieItems.size(); i++) {
            MovieItem movieItem = movieItems.get(i);
            movieItem.setMovieType(type);
            operationList.add(ContentProviderOperation.newInsert(CONTENT_URI).withValues(toContentValues(movieItem)).build());
        }

        applyBatch(context, operationList);

        PreferenceManager
                .with(context)
                .edit()
                .putBoolean(String.valueOf(type), true)
                .apply();
    }

    public ContentValues toContentValues(MovieItem movieItem) {
        ContentValues values = new ContentValues();
        values.put(tc_title.getColumnName(), movieItem.getTitle());
        values.put(tc_originalTitle.getColumnName(), movieItem.getOriginalTitle());
        values.put(tc_overview.getColumnName(), movieItem.getOverview());
        values.put(tc_posterPath.getColumnName(), movieItem.getPosterPath());
        values.put(tc_backdropPath.getColumnName(), movieItem.getBackdropPath());
        values.put(tc_releaseDate.getColumnName(), movieItem.getReleaseDate());
        values.put(tc_voteAverage.getColumnName(), movieItem.getVoteAverage());
        values.put(tc_movieType.getColumnName(), movieItem.getMovieType());
        values.put(tc_isFavorite.getColumnName(), 0);
        values.put(tc_movieId.getColumnName(), movieItem.getMovieId());

        return values;
    }

    private void applyBatch(Context context, ArrayList<ContentProviderOperation> operationList) {
        if (!operationList.isEmpty()) {
            try {
                context.getContentResolver().applyBatch(CpProvider.PROVIDER_NAME, operationList);
            } catch (Exception e) {
                Logger.logException("Exception", e);
            }
            operationList.clear();
        }
    }
}