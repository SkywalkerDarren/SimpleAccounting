package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;

public class DiscoveryViewModel extends ViewModel {
    private AppRepository mRepository;
    private MutableLiveData<String> cumulativeDays = new MutableLiveData<>();

    public DiscoveryViewModel(AppRepository repository) {
        mRepository = repository;
    }

    public MutableLiveData<String> getCumulativeDays() {
        return cumulativeDays;
    }

    public void setCumulativeDays(String days) {
        cumulativeDays.setValue(days);
    }
}
