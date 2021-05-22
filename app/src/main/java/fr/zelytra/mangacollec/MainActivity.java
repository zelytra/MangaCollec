package fr.zelytra.mangacollec;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.zelytra.mangacollec.api.Movie;
import fr.zelytra.mangacollec.api.MovieApi;
import fr.zelytra.mangacollec.api.RestMovieResponse;
import fr.zelytra.mangacollec.list.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showList();
        Log.d("app", "making api call");
        apiCall();
    }

    private void showList() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
        }

        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ListAdapter(input);
        recyclerView.setAdapter(listAdapter);
    }

    private void apiCall() {
        final String BASE_URL = "https://api.themoviedb.org/3/";
        final String apiKey = "3e2de8ad359733ffda6986280aa735dd";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

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
                    for (Movie movie:movieList){
                        Log.d("app",movie.getTitle());
                    }
                }else {
                    Toast.makeText(MainActivity.this, "API failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestMovieResponse> call, Throwable t) {
                Log.e("app","error");
            }
        });
    }
}