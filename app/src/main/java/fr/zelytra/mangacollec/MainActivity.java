package fr.zelytra.mangacollec;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.zelytra.mangacollec.api.Movie;
import fr.zelytra.mangacollec.api.MovieApi;
import fr.zelytra.mangacollec.api.RestMovieResponse;
import fr.zelytra.mangacollec.list.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("moviesList", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();

        List<Movie> movieList = getDataFromLocal();
        if(movieList!=null)
            showList(movieList);
        else
            apiCall();
    }

    private List<Movie> getDataFromLocal() {
        String jsonMovies = sharedPreferences.getString("jsonMovie",null);
        if(jsonMovies==null)
            return null;
        else
            return gson.fromJson(jsonMovies,new TypeToken<List<Movie>>(){}.getType());

    }

    private void showList(List<Movie> movieList) {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ListAdapter(movieList);
        recyclerView.setAdapter(listAdapter);
    }

    private void apiCall() {
        final String BASE_URL = "https://api.themoviedb.org/3/";
        final String apiKey = "3e2de8ad359733ffda6986280aa735dd";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MovieApi movieApi = retrofit.create(MovieApi.class);


        Call<RestMovieResponse> call = movieApi.getMoviesResponse(apiKey, "fr-FR", "popularity.desc", false, false, 1, "flatrate");
        call.enqueue(new Callback<RestMovieResponse>() {
            @Override
            public void onResponse(Call<RestMovieResponse> call, Response<RestMovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movieList = response.body().getResults();
                    Toast.makeText(MainActivity.this, "API succed", Toast.LENGTH_SHORT).show();
                    saveList(movieList);
                } else {
                    Toast.makeText(MainActivity.this, "API failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestMovieResponse> call, Throwable t) {
                Log.e("app", "error");
            }
        });
    }

    private void saveList(List<Movie> movieList) {
        String jsonString = gson.toJson(movieList);
        sharedPreferences
                .edit()
                .putString("jsonMovie",jsonString)
                .apply();

    }

    public static MainActivity getInstance(){
        return instance;
    }

}