package nanodegree.udacity.vk.com.udacitynanodegree.network;

/**
 * Created by Vinay on 4/11/15.
 */
public class RestAPI {

    // API Names
    public static final int MOVIES_POPULAR = 1;
    public static final int MOVIES_TOP_RATED = 2;

    // API extensions
    public final static String API_KEY                   = "?api_key=";
    public final static String API_MOVIE_POPULAR         = "movie/popular";
    public final static String API_MOVIE_TOP_RATED       = "movie/top_rated";

    // URL Endpoints
    public final static String URL_MOVIES_SERVER         = "https://api.themoviedb.org/3/";
    public final static String URL_IMAGES_SERVER         = "http://image.tmdb.org/t/p/w780";
}
