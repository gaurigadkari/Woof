package co.touchlab.dogify.dagger.component;

import android.app.Application;

import co.touchlab.dogify.dagger.module.AppModule;
import co.touchlab.dogify.dagger.module.MainActivityModule;
import co.touchlab.dogify.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Gauri Gadkari on 12/21/17.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);
}
