package co.touchlab.dogify.view;


import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import co.touchlab.dogify.DogService;
import co.touchlab.dogify.MainActivity;
import co.touchlab.dogify.R;
import co.touchlab.dogify.adapter.BreedAdapter;
import co.touchlab.dogify.databinding.FragmentBreedListBinding;
import co.touchlab.dogify.models.Breed;
import co.touchlab.dogify.models.ImageResult;
import co.touchlab.dogify.models.NameResult;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BreedListFragment extends Fragment {
    RecyclerView breedList;
    BreedAdapter adapter = new BreedAdapter();
    GetBreedsTask getBreeds = new GetBreedsTask((MainActivity) getActivity());
    ProgressBar spinner;
    FragmentBreedListBinding binding;
    public BreedListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_breed_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = binding.spinner;
        breedList = binding.breedList;
        breedList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        breedList.setAdapter(adapter);
        getBreeds.execute();
    }

    public static BreedListFragment newInstance() {
        Bundle args = new Bundle();
        BreedListFragment breedListFragment = new BreedListFragment();
        breedListFragment.setArguments(args);
        return breedListFragment;
    }

    private static class GetBreedsTask extends AsyncTask<Void, Breed, Boolean> {
        private WeakReference<MainActivity> activityRef;

        GetBreedsTask(MainActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {

//            MainActivity activity = activityRef.get();
//            if (activity != null) {
//                activity.showSpinner(true);
//                activity.adapter.clear();
//            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dog.ceo/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            DogService service = retrofit.create(DogService.class);

            if (isCancelled()) {
                return false;
            }

            List<String> breedNames = getBreedNames(service);

            for (String name : breedNames) {
                if (isCancelled()) {
                    return false;
                }

                Breed breed = new Breed(name, getBreedImage(service, name));
                publishProgress(breed);
            }
            return true;
        }

        private List<String> getBreedNames(DogService service) {
            try {
                NameResult nameResult = service.getBreeds().execute().body();
                if (nameResult != null && nameResult.getMessage() != null) {
                    return nameResult.getMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        }

        private @Nullable
        String getBreedImage(DogService service, String breedName) {
            try {
                ImageResult imageResult = service.getImage(breedName).execute().body();
                if (imageResult != null) {
                    return imageResult.getMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Breed... values) {
            MainActivity activity = this.activityRef.get();
            if (activity != null && values.length > 0) {
                //activity.adapter.add(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            MainActivity activity = this.activityRef.get();
            if (activity != null) {
                //activity.showSpinner(false);
            }
        }
    }

    private void showSpinner(Boolean show) {
        spinner.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
