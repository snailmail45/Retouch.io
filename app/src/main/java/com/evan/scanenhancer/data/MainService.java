package com.evan.scanenhancer.data;

import com.evan.scanenhancer.data.model.Result;

import io.reactivex.rxjava3.core.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MainService {
    @Headers("accept: application/json")
    @POST("upscale")
    Single<Result> upscaleImage(
            @Body RequestBody requestBody,
            @Header("X-Picsart-API-Key") String apiKey
    );

    @Headers("accept: application/json")
    @POST("removebg")
    Single<Result> removeBackground(
            @Body RequestBody body,
            @Header("X-Picsart-API-Key") String apiKey
    );

    @Headers("accept: application/json")
    @POST("upscale/ultra")
    Single<Result> upscaleUltra(
            @Body RequestBody body,
            @Header("X-Picsart-API-Key") String apiKey
    );

    @Headers("accept: application/json")
    @POST("enhance/face")
    Single<Result> enhanceFace(
            @Body RequestBody body,
            @Header("X-Picsart-API-Key") String apiKey
    );

    @Headers("accept: application/json")
    @POST("vectorizer")
    Single<Result> vectorizeImage(
            @Body RequestBody body,
            @Header("X-Picsart-API-Key") String apiKey
    );

    @Headers("accept: application/json")
    @POST("upload")
    Single<Result> uploadImage(
            @Body RequestBody body,
            @Header("X-Picsart-API-Key") String apiKey
    );

    @Headers("accept: application/json")
    @POST("background/texture")
    Single<Result> contentGeneration(
            @Body RequestBody body,
            @Header("X-Picsart-API-Key") String apiKey
    );







}
