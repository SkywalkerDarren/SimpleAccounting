package io.github.skywalkerdarren.simpleaccounting.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillDetailViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillEditViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillListViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.ChartViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.ClassifyViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.DiscoveryViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.EmptyListViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.FeedBackViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.JournalViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;
    private final AppRepository mRepository;

    private ViewModelFactory(AppRepository repository) {
        mRepository = repository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(AppRepository.getInstance(new AppExecutors(), application));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public AppRepository getRepository() {
        return mRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountViewModel.class)) {
            //noinspection unchecked
            return (T) new AccountViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(BillDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new BillDetailViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(BillEditViewModel.class)) {
            //noinspection unchecked
            return (T) new BillEditViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(BillListViewModel.class)) {
            //noinspection unchecked
            return (T) new BillListViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(ClassifyViewModel.class)) {
            //noinspection unchecked
            return (T) new ClassifyViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(JournalViewModel.class)) {
            //noinspection unchecked
            return (T) new JournalViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(ChartViewModel.class)) {
            //noinspection unchecked
            return (T) new ChartViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(DiscoveryViewModel.class)) {
            //noinspection unchecked
            return (T) new DiscoveryViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(EmptyListViewModel.class)) {
            //noinspection unchecked
            return (T) new EmptyListViewModel();
        } else if (modelClass.isAssignableFrom(FeedBackViewModel.class)) {
            //noinspection unchecked
            return (T) new FeedBackViewModel();
        }

        throw new IllegalArgumentException("no this ViewModel");
    }
}
