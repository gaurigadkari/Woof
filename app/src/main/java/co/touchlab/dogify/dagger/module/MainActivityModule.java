package co.touchlab.dogify.dagger.module;

import co.touchlab.dogify.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();
}
