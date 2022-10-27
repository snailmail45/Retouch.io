package com.evan.scanenhancer.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.evan.scanenhancer.data.LocalRepository;
import com.evan.scanenhancer.data.RemoteRepository;
import com.evan.scanenhancer.data.model.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final LocalRepository localRepository;
    private final List<Result> listResults = new ArrayList<>();
    private final MutableLiveData<Boolean> liveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(LocalRepository localRepository) {
        this.localRepository = localRepository;

       getData();
    }

    public List<Result> getRecentActions() {
        return listResults;
    }

    public MutableLiveData<Boolean> getLiveData() {
        return liveData;
    }

    public void getData(){
        listResults.clear();
        localRepository.getRecentActions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recentActions -> {
                    listResults.addAll(recentActions);
                    liveData.setValue(true);
                }, err -> {
                    Timber.e(err, "MainViewModel: Failed to get recent actions");
                });
    }

}
