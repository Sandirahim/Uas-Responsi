package id.AbdulMizwar.movieapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.provider.Settings;

import id.AbdulMizwar.movieapp.Adapter.MoviesAdapter;
import id.AbdulMizwar.movieapp.Model.Movie;
import id.AbdulMizwar.movieapp.Model.MovieResponse;
import id.AbdulMizwar.movieapp.Rest.ApiClient;
import id.AbdulMizwar.movieapp.Rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private Button button;
    private static final String TAG = MainActivity.class.getSimpleName();
    // isi api key the movie db
    private final static String API_KEY = "7e5f1e6eabc0dc07fbc2e011e5cd4383";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.buttonData);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivitydata();
            }
        });


        // ganti bahasa
        Button button = (Button) findViewById(R.id.button);

//        Button btn = (Button) findViewById(R.id.notifybtn);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notificationcall();
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
            }

        });
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "api key kosong bos", Toast.LENGTH_LONG).show();
            return;
        }
        tampilData();
    }

    public void openactivitydata() {
         Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

//    private void notificationcall() {
//        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setSmallIcon(R.drawable.stlm)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.stlm))
//                .setContentTitle("Notification From Sanktips")
//                .setContentText("Hello and welcome to Sanktips ");
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notificationBuilder.build());
//
//    }


    private void tampilData() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getNowPlayingMovie(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                final List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));

                /*perintah klik recyclerview*/
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                        public boolean onSingleTapUp(MotionEvent e){
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)){
                            int position = rv.getChildAdapterPosition(child);
//                            Toast.makeText(getApplicationContext(), "Id : " + movies.get(position).getId() + " selected", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(MainActivity.this, DetailActivity.class);
                            i.putExtra("title", movies.get(position).getTitle());
                            i.putExtra("date", movies.get(position).getReleaseDate());
                            i.putExtra("vote", movies.get(position).getVoteAverage().toString());
                            i.putExtra("overview", movies.get(position).getPopularity());
                            i.putExtra("bg", movies.get(position).getBackdropPath());
                            MainActivity.this.startActivity(i);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }

                });
            }
            @Override
            public void onFailure(Call<MovieResponse>call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}