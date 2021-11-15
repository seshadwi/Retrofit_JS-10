package com.android.retrofit.services;

import com.android.retrofit.models.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubService {

    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);
}
