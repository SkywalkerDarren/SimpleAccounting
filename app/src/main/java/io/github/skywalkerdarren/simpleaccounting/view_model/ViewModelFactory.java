package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;
    private final AppRepositry mRepositry;

    private ViewModelFactory(AppRepositry repositry) {
        mRepositry = repositry;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(AppRepositry.getInstance(new AppExecutors(), application));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public AppRepositry getRepositry() {
        return mRepositry;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountViewModel.class)) {
            //noinspection unchecked
            return (T) new AccountViewModel(mRepositry);
        } else if (modelClass.isAssignableFrom(BillDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new BillDetailViewModel(mRepositry);
        } else if (modelClass.isAssignableFrom(BillEditViewModel.class)) {
            //noinspection unchecked
            return (T) new BillEditViewModel(mRepositry);
        } else if (modelClass.isAssignableFrom(BillListViewModel.class)) {
            //noinspection unchecked
            return (T) new BillListViewModel(mRepositry);
        } else if (modelClass.isAssignableFrom(ClassifyViewModel.class)) {
            //noinspection unchecked
            return (T) new ClassifyViewModel(mRepositry);
        } else if (modelClass.isAssignableFrom(JournalViewModel.class)) {
            //noinspection unchecked
            return (T) new JournalViewModel(mRepositry);
        } else if (modelClass.isAssignableFrom(ChartViewModel.class)) {
            //noinspection unchecked
            return (T) new ChartViewModel(mRepositry);
        } else if (modelClass.isAssignableFrom(EmptyListViewModel.class)) {
            //noinspection unchecked
            return (T) new EmptyListViewModel();
        }

        throw new IllegalArgumentException("no this ViewModel");
    }
}
