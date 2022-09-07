package com.example.myapplication.retrofit;
import com.example.myapplication.models.APIResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInteface {

        @GET("/test-api/")
        Call<APIResponseModel> getResponseModel();

    }
