package co.touchlab.dogify.dagger.module;

import co.touchlab.dogify.view.BreedListFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract BreedListFragment contributeBreedListFragment();
}

