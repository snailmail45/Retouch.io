package com.evan.scanenhancer.data;

import com.evan.scanenhancer.Constants;
import com.evan.scanenhancer.data.model.Result;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RemoteRepository {

    private final MainService mainService;

    @Inject
    public RemoteRepository(MainService mainService) {
        this.mainService = mainService;
    }

    public Single<Result> upscaleImage(String imageUrl) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image_url", imageUrl)
                .addFormDataPart("upscale_factor", "x2")
                .addFormDataPart("format", "JPG")
                .build();
        return mainService.upscaleImage(body, Constants.API_KEY);
    }

    public Single<Result> removeBackground(String imageUrl) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("bg_blur", "0")
                .addFormDataPart("scale", "fit")
                .addFormDataPart("image_url", imageUrl)
                .addFormDataPart("format", "PNG")
                .addFormDataPart("output_type", "cutout")
                .build();
        return mainService.removeBackground(body, Constants.API_KEY);
    }

    public Single<Result> upscaleUltra(String imageUrl) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("unit", "px")
                .addFormDataPart("image_url", imageUrl)
                .addFormDataPart("format", "JPG")
                .addFormDataPart("upscale_factor", "2")
                .build();
        return mainService.upscaleUltra(body, Constants.API_KEY);
    }

    public Single<Result> enhanceFace(String imageUrl) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image_url", imageUrl)
                .addFormDataPart("format", "JPG")
                .build();
        return mainService.enhanceFace(body, Constants.API_KEY);
    }

    public Single<Result> vectorizeImage(String imageUrl) {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image_url", imageUrl)
                .addFormDataPart("downscale_to", "-1")
                .build();
        return mainService.vectorizeImage(body, Constants.API_KEY);
    }

    public Single<Result> contentGeneration(String imageUrl){
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("width","1024")
                .addFormDataPart("height","1024")
                .addFormDataPart("scale","1")
                .addFormDataPart("image_url",imageUrl)
                .addFormDataPart("offset_x","0")
                .addFormDataPart("offset_y","0")
                .addFormDataPart("pattern","hex")
                .addFormDataPart("format","JPG")
                .addFormDataPart("rotate","0")
                .build();
        return mainService.contentGeneration(body, Constants.API_KEY);
    }

    public Single<Result> uploadImage(String pathToImage) {
        File file = new File(pathToImage);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "image",
                        file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file)
                ).build();
        return mainService.uploadImage(body, Constants.API_KEY);
    }


}
