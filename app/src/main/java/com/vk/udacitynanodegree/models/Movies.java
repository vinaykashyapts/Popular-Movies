package com.vk.udacitynanodegree.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Vinay on 14/7/16.
 */
public class Movies implements Parcelable {
    public int page;
    public ArrayList<MovieItem> results;
    public int total_results;
    public int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MovieItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieItem> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    protected Movies(Parcel in) {
        page = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<>();
            in.readList(results, MovieItem.class.getClassLoader());
        } else {
            results = null;
        }
        total_results = in.readInt();
        total_pages = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        dest.writeInt(total_results);
        dest.writeInt(total_pages);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
