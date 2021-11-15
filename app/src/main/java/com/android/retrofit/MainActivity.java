package com.android.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.retrofit.databinding.ActivityMainBinding;
import com.android.retrofit.models.Repo;
import com.android.retrofit.services.GithubService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Retrofit retrofit;
    GithubService service;
    TextView alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        alert = (TextView) findViewById(R.id.alert);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GithubService.class);

        Call<List<Repo>> repos = service.listRepos("seshadwi");

    }

    public void handleSend(View view){
        Call<List<Repo>> repos = service.listRepos(binding.getUsername().toString());
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if(response.isSuccessful()){
                    if(response.body().isEmpty()){
                        alert.setVisibility(View.VISIBLE);
                        binding.setRepo(null);
                    } else {
                        binding.setRepo(response.body().get(0));
                        alert.setVisibility(View.GONE);
                    }
                } else {
                    alert.setVisibility(View.VISIBLE);
                    binding.setRepo(null);
                }
                binding.setUsername("");
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                alert.setVisibility(View.VISIBLE);
                binding.setUsername("");
                progress.dismiss();
            }
        });
    }
}