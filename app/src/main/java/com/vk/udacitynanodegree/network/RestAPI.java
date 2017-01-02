package com.vk.udacitynanodegree.network;

/**
 * Created by Vinay on 4/11/15.
 */
public class RestAPI {

    // API Names
    public static final int MOVIES_POPULAR = 1;
    public static final int MOVIES_TOP_RATED = 2;
    public static final int MOVIES_FAVORITE = 3;
    public static final int MOVIES_TRAILERS = 4;
    public static final int MOVIES_REVIEWS = 5;

    // API extensions
    public final static String API_KEY                   = "?api_key=";
    public final static String API_MOVIE_POPULAR         = "popular";
    public final static String API_MOVIE_TOP_RATED       = "top_rated";
    public final static String API_MOVIE_TRAILERS        = "/videos";
    public final static String API_MOVIE_REVIEWS         = "/reviews";

    // URL Endpoints
    public final static String URL_MOVIES_SERVER         = "https://api.themoviedb.org/3/movie/";
    public final static String URL_IMAGES_SERVER         = "http://image.tmdb.org/t/p/w780";
}
