package com.evan.scanenhancer.di;

import com.evan.scanenhancer.viewmodels.FilterViewModel;

import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface FilterViewModelFactory {
    FilterViewModel create(String imageUrl);
}
