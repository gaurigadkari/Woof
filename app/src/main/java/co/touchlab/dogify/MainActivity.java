package co.touchlab.dogify;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import co.touchlab.dogify.adapter.BreedAdapter;
import co.touchlab.dogify.models.Breed;
import co.touchlab.dogify.models.ImageResult;
import co.touchlab.dogify.models.NameResult;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView breedList;
    BreedAdapter adapter = new BreedAdapter();
    GetBreedsTask getBreeds = new GetBreedsTask(this);
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        breedList = findViewById(R.id.breed_list);
        breedList.setLayoutManager(new GridLayoutManager(this, 2));
        breedList.setAdapter(adapter);
        getBreeds.execute();
    }

    @Override
    protected void onDestroy() {
        getBreeds.cancel(false);
        super.onDestroy();
    }

    private void showSpinner(Boolean show) {
        spinner.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private static class GetBreedsTask extends AsyncTask<Void, Breed, Boolean> {
        private WeakReference<MainActivity> activityRef;

        GetBreedsTask(MainActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            MainActivity activity = activityRef.get();
            if (activity != null) {
                activity.showSpinner(true);
                activity.adapter.clear();
            }
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
                activity.adapter.add(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            MainActivity activity = this.activityRef.get();
            if (activity != null) {
                activity.showSpinner(false);
            }
        }
    }
}
