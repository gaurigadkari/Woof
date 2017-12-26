package co.touchlab.dogify.dagger.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import co.touchlab.dogify.dagger.ViewModelKey;
import co.touchlab.dogify.view.BreedListViewModel;
import co.touchlab.dogify.view.BreedListViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Gauri Gadkari on 10/29/17.
 */

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(BreedListViewModel.class)
    abstract ViewModel bindBreedListViewModel(BreedListViewModel searchViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(BreedListViewModelFactory factory);
}
