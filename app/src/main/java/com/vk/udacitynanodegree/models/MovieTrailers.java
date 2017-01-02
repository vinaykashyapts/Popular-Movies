package com.vk.udacitynanodegree.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Vinay on 6/12/16.
 */

public class MovieTrailers implements Parcelable {

    public ArrayList<Trailer> results;

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }

    public class Trailer implements Parcelable {

        public String id;
        public String iso_639_1;
        public String iso_3166_1;
        public String key;
        public String name;
        public String site;
        public String size;
        public String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        protected Trailer(Parcel in) {
            id = in.readString();
            iso_639_1 = in.readString();
            iso_3166_1 = in.readString();
            key = in.readString();
            name = in.readString();
            site = in.readString();
            size = in.readString();
            type = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(iso_639_1);
            dest.writeString(iso_3166_1);
            dest.writeString(key);
            dest.writeString(name);
            dest.writeString(site);
            dest.writeString(size);
            dest.writeString(type);
        }

        @SuppressWarnings("unused")
        public final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
            @Override
            public Trailer createFromParcel(Parcel in) {
                return new Trailer(in);
            }

            @Override
            public Trailer[] newArray(int size) {
                return new Trailer[size];
            }
        };
    }

    protected MovieTrailers(Parcel in) {
        if (in.readByte() == 0x01) {
            results = new ArrayList<Trailer>();
            in.readList(results, Trailer.class.getClassLoader());
        } else {
            results = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieTrailers> CREATOR = new Parcelable.Creator<MovieTrailers>() {
        @Override
        public MovieTrailers createFromParcel(Parcel in) {
            return new MovieTrailers(in);
        }

        @Override
        public MovieTrailers[] newArray(int size) {
            return new MovieTrailers[size];
        }
    };
}
