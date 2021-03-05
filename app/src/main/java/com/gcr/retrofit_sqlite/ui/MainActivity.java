package com.gcr.retrofit_sqlite.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gcr.retrofit_sqlite.R;
import com.gcr.retrofit_sqlite.api.RetrofitClient;
import com.gcr.retrofit_sqlite.db.FlowerDatabase;
import com.gcr.retrofit_sqlite.models.FlowerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RvAdapter.ItemClickListener {

    private RetrofitClient retrofitClient;
    private FlowerDatabase flowerDatabase;
    private RvAdapter rvAdapter;
    private RecyclerView rvFlowers;
    private ProgressBar progressBar;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowerDatabase = new FlowerDatabase(this,"flores",null,1);

        setUpViews();
        //getDataFromDb();
       getDataFromService();
    }


    private void getDataFromDb() {
        List<FlowerResponse> list = flowerDatabase.getDataFromDb();
        rvAdapter = new RvAdapter(list, this);
        rvFlowers.setAdapter(rvAdapter);
    }

    private void setUpViews() {
        progressBar = findViewById(R.id.progress);
        rvFlowers = findViewById(R.id.rvFlowers);
        rvFlowers.setHasFixedSize(true);
        rvFlowers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getDataFromService() {
        FlowerDatabase database = new FlowerDatabase(
                this,
                "flores",
                null,
                1);

        progressBar.setVisibility(View.VISIBLE);
        retrofitClient = new RetrofitClient();
        Call<List<FlowerResponse>> flowerResponse =
                retrofitClient
                        .retrofitClient()
                        .getDataFromServer();
        flowerResponse.enqueue(new Callback<List<FlowerResponse>>() {
            @Override
            public void onResponse(Call<List<FlowerResponse>> call, Response<List<FlowerResponse>> response) {
                if (response.isSuccessful()) {
                    SQLiteDatabase sqLiteDatabase = flowerDatabase.getWritableDatabase();

                    progressBar.setVisibility(View.GONE);
                    rvFlowers.setVisibility(View.VISIBLE);
                    rvAdapter = new RvAdapter(response.body(), MainActivity.this);
                    rvFlowers.setAdapter(rvAdapter);
                    assert response.body() != null;

                    for (FlowerResponse flor :
                            response.body()) {
                        flowerDatabase.addFlower(flor);
                    }
                    sqLiteDatabase.close();
                }
            }

            @Override
            public void onFailure(Call<List<FlowerResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                rvFlowers.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void itemClickListener(FlowerResponse flower) {
        Toast.makeText(this, flower.getName(), Toast.LENGTH_SHORT).show();
    }
}