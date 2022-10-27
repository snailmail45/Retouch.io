package com.evan.scanenhancer.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.evan.scanenhancer.R;
import com.evan.scanenhancer.data.LocalRepository;
import com.evan.scanenhancer.data.RemoteRepository;
import com.evan.scanenhancer.data.model.FilterItem;
import com.evan.scanenhancer.data.model.Result;
import com.evan.scanenhancer.data.model.ServiceResult;
import com.evan.scanenhancer.di.FilterViewModelFactory;
import com.evan.scanenhancer.util.Filter;
import com.evan.scanenhancer.util.Status;

import java.util.ArrayList;
import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class FilterViewModel extends ViewModel {

    private final MutableLiveData<ServiceResult> liveData = new MutableLiveData<>();
    private final List<FilterItem> list = new ArrayList<>();
    private final RemoteRepository remoteRepository;
    private final LocalRepository localRepository;
    private String uploadedImageUrl;
    private final String localImagePath;
    private FilterItem filterItem;

    @AssistedInject
    public FilterViewModel(
            RemoteRepository remoteRepository,
            LocalRepository localRepository,
            @Assisted String localImagePath
    ) {
        this.remoteRepository = remoteRepository;
        this.localRepository = localRepository;
        this.localImagePath = localImagePath;

        list.add(new FilterItem("Upscale", R.drawable.ic_devportal_upscale, Filter.UPSCALE));
        list.add(new FilterItem("Remove\nBackground", R.drawable.ic_devportal_remove_background, Filter.REMOVE_BGR));
        list.add(new FilterItem("Upscale\nUltra", R.drawable.ic_devportal_ultra_upscale, Filter.UPSCALE_ULTRA));
        list.add(new FilterItem("Enhance\nFace", R.drawable.ic_devportal_ultra_enhance, Filter.ENHANCE_FACE));
        list.add(new FilterItem("Vectorize\nImage", R.drawable.ic_devportal_image_vectorizer, Filter.VECTORIZE));
        list.add(new FilterItem("Content\nGeneration", R.drawable.ic_devportal_texture_generator, Filter.CONTENT_GENERATION));

    }

    @SuppressWarnings("unchecked")
    public static ViewModelProvider.Factory provideFactory(
            FilterViewModelFactory assistedFactory,
            String imageUrl
    ) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) assistedFactory.create(imageUrl);
            }
        };
    }

    public List<FilterItem> getList() {
        return list;
    }

    public void onItemClick(FilterItem filterItem) {
        this.filterItem = filterItem;
        liveData.setValue(new ServiceResult(null, null, Status.IN_PROGRESS));

        remoteRepository.uploadImage(localImagePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    uploadedImageUrl = result.getData().getUrl();
                    if (result.getStatus().equals("success")) {
                        switch (filterItem.getFilter()) {
                            case UPSCALE:
                                upscale(uploadedImageUrl);
                                break;

                            case REMOVE_BGR:
                                removeBackground(uploadedImageUrl);
                                break;

                            case UPSCALE_ULTRA:
                                upscaleUltra(uploadedImageUrl);
                                break;

                            case ENHANCE_FACE:
                                enhanceFace(uploadedImageUrl);
                                break;

                            case VECTORIZE:
                                vectorize(uploadedImageUrl);
                                break;

                            case CONTENT_GENERATION:
                                contentGeneration(uploadedImageUrl);
                                break;
                        }
                    } else {
                        Timber.d("Upload failed.");
                        liveData.setValue(new ServiceResult("Failed to upload image", null, Status.FAILED));
                    }
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to call service");
                    liveData.setValue(new ServiceResult("Failed to call service", null, Status.FAILED));
                });

    }

    private void contentGeneration(String imageUrl) {
        ResultWrapper resultWrapper = new ResultWrapper();

        remoteRepository.contentGeneration(imageUrl)
                .flatMap(result -> {
                    resultWrapper.setResult(result);
                    result.setDateCreated(System.currentTimeMillis());
                    result.setActionType(filterItem.getFilter().toString());
                    return insertRecentResult(result);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSuccessful -> {
                    liveData.setValue(new ServiceResult("Success", resultWrapper.getResult(), Status.SUCCESS));
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to call service");
                });
    }

    private void upscale(String imageUrl) {
        ResultWrapper resultWrapper = new ResultWrapper();

        remoteRepository.upscaleImage(imageUrl)
                .flatMap(result -> {
                    resultWrapper.setResult(result);
                    result.setDateCreated(System.currentTimeMillis());
                    result.setActionType(filterItem.getFilter().toString());
                    return insertRecentResult(result);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSuccessful -> {
                    liveData.setValue(new ServiceResult("Success", resultWrapper.getResult(), Status.SUCCESS));
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to call service");
                });
    }

    private class ResultWrapper{
        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

    private void removeBackground(String uploadedImageUrl) {
        ResultWrapper resultWrapper = new ResultWrapper();

        remoteRepository.removeBackground(uploadedImageUrl)
                .flatMap(result -> {
                    resultWrapper.setResult(result);
                    result.setDateCreated(System.currentTimeMillis());
                    result.setActionType(filterItem.getFilter().toString());
                    return insertRecentResult(result);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSuccessful -> {
                    liveData.setValue(new ServiceResult("Success", resultWrapper.getResult(), Status.SUCCESS));
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to call service");
                });
    }

    private void upscaleUltra(String uploadedImageUrl) {
        ResultWrapper resultWrapper = new ResultWrapper();

        remoteRepository.upscaleUltra(uploadedImageUrl)
                .flatMap(result -> {
                    resultWrapper.setResult(result);
                    result.setDateCreated(System.currentTimeMillis());
                    result.setActionType(filterItem.getFilter().toString());
                    return insertRecentResult(result);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSuccessful -> {
                    liveData.setValue(new ServiceResult("Success", resultWrapper.getResult(), Status.SUCCESS));
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to call service");
                });
    }

    private void enhanceFace(String uploadedImageUrl) {
        ResultWrapper resultWrapper = new ResultWrapper();

        remoteRepository.enhanceFace(uploadedImageUrl)
                .flatMap(result -> {
                    resultWrapper.setResult(result);
                    result.setDateCreated(System.currentTimeMillis());
                    result.setActionType(filterItem.getFilter().toString());
                    return insertRecentResult(result);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSuccessful -> {
                    liveData.setValue(new ServiceResult("Success", resultWrapper.getResult(), Status.SUCCESS));
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to call service");
                });
    }

    private void vectorize(String uploadedImageUrl) {
        ResultWrapper resultWrapper = new ResultWrapper();

        remoteRepository.vectorizeImage(uploadedImageUrl)
                .flatMap(result -> {
                    resultWrapper.setResult(result);
                    result.setDateCreated(System.currentTimeMillis());
                    result.setActionType(filterItem.getFilter().toString());
                    return insertRecentResult(result);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSuccessful -> {
                    liveData.setValue(new ServiceResult("Success", resultWrapper.getResult(), Status.SUCCESS));
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to call service");
                });
    }

    private Single<Long> insertRecentResult(Result result) {
        return localRepository.insertResult(result);
    }

    public MutableLiveData<ServiceResult> getLiveData() {
        return liveData;
    }
}
