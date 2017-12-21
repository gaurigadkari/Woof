package co.touchlab.dogify;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import co.touchlab.dogify.adapter.BreedAdapter;
import co.touchlab.dogify.databinding.ActivityMainBinding;
import co.touchlab.dogify.models.Breed;
import co.touchlab.dogify.models.ImageResult;
import co.touchlab.dogify.models.NameResult;
import co.touchlab.dogify.view.BreedListFragment;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager fragmentManager;
    BreedListFragment breedListFragment;
    private static final String BREED_LIST_FRAGMENT = "breed_list_fragment";

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

}
