package co.touchlab.dogify;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import co.touchlab.dogify.databinding.ActivityMainBinding;
import co.touchlab.dogify.view.BreedListFragment;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private BreedListFragment breedListFragment;
    private static final String BREED_LIST_FRAGMENT = "breed_list_fragment";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        breedListFragment = (BreedListFragment) fragmentManager.findFragmentByTag(BREED_LIST_FRAGMENT);

        if (breedListFragment == null) {
            breedListFragment = BreedListFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, breedListFragment, BREED_LIST_FRAGMENT)
                    .commit();
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

}
