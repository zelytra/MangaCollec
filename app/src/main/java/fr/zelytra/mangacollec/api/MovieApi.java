package fr.zelytra.mangacollec.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("discover/movie")
    Call<RestMovieResponse> getMoviesResponse(@Query("api_key") String key, @Query("language") String langage, @Query("sort_by") String popularity, @Query("include_adult") Boolean includeAdult, @Query("include_video") Boolean includeVideo, @Query("page") int page, @Query("with_watch_monetization_types") String monetization);

    @GET("authentication/guest_session/new")
    Call<RestGuestResponse> getGuestToken(@Query("api_key") String key);
}
