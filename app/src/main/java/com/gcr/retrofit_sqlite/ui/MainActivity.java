package com.gcr.retrofit_sqlite.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RvAdapter.ItemClickListener {

    //

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

                    progressBar.setVisibility(View.GONE);
                    rvFlowers.setVisibility(View.VISIBLE);
                    rvAdapter = new RvAdapter(response.body(), MainActivity.this);
                    rvFlowers.setAdapter(rvAdapter);
                    assert response.body() != null;

                    for (FlowerResponse flor :
                            response.body()) {
                        Log.d(TAG, "onResponse: "+flor.getPhoto());
                        SaveIntoDb saveIntoDb = new SaveIntoDb();
                        saveIntoDb.execute(flor);
                    }
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public class SaveIntoDb extends AsyncTask<FlowerResponse, FlowerResponse, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(FlowerResponse... flowerResponses) {
            FlowerResponse flower = flowerResponses[0];
            try {
                InputStream stream = new URL("https://services.hanselandpetal.com/photos/" + flower.getPicture()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                flower.setPicture(bitmap);
                publishProgress(flower);
            }catch (Exception e){
                Log.d(TAG, "doInBackground: "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(FlowerResponse... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: "+values[0].getName());
            flowerDatabase.addFlower(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    //https://github.com/filippella/Retrofit2-RestApiDemo/blob/master/app/src/main/java/org/dalol/retrofit2_restapidemo/ui/MainActivity.java
    //https://www.youtube.com/watch?v=xRRkOHhcUPk&t=2566s
}