package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.ViewModel;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;

public class DiscoveryViewModel extends ViewModel {
    private AppRepository mRepository;

    public DiscoveryViewModel(AppRepository repository) {
        mRepository = repository;
    }
}
