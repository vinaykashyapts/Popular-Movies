package com.vk.udacitynanodegree.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Vinay on 18/12/16.
 */

public class MovieReviews implements Parcelable {

    public int id;
    public int page;
    public ArrayList<MovieReviews.Review> results;
    public int total_pages;
    public int total_results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public class Review implements Parcelable {

        public String id;
        public String author;
        public String content;
        public String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        protected Review(Parcel in) {
            id = in.readString();
            author = in.readString();
            content = in.readString();
            url = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(author);
            dest.writeString(content);
            dest.writeString(url);
        }

        @SuppressWarnings("unused")
        public final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };
    }

    protected MovieReviews(Parcel in) {
        id = in.readInt();
        page = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<MovieReviews.Review>();
            in.readList(results, MovieReviews.Review.class.getClassLoader());
        } else {
            results = null;
        }
        total_pages = in.readInt();
        total_results = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(page);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        dest.writeInt(total_pages);
        dest.writeInt(total_results);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieReviews> CREATOR = new Parcelable.Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };
}
