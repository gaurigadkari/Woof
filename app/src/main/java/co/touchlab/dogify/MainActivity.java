package co.touchlab.dogify;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    RecyclerView breedList;
    BreedAdapter  adapter   = new BreedAdapter();
    GetBreedsTask getBreeds = new GetBreedsTask(this);
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        breedList = findViewById(R.id.breed_list);
        breedList.setLayoutManager(new GridLayoutManager(this, 2));
        breedList.setAdapter(adapter);
        getBreeds.execute();
    }

    @Override
    protected void onDestroy()
    {
        getBreeds.cancel(false);
        super.onDestroy();
    }

    private void showSpinner(Boolean show)
    {
        spinner.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private static class BreedAdapter extends RecyclerView.Adapter<BreedAdapter.ViewHolder>
    {

        List<Breed> data = new ArrayList<>();

        @Override
        public BreedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_breed, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            Breed breed = data.get(position);
            holder.nameText.setText(breed.name);
            Picasso.with(holder.breedImage.getContext())
                    .load(breed.imageURL)
                    .fit()
                    .centerCrop()
                    .into(holder.breedImage);
        }

        @Override
        public int getItemCount()
        {
            return data.size();
        }

        void add(Breed breed)
        {
            data.add(breed);
            notifyItemInserted(data.size() - 1);
        }

        void clear()
        {
            int size = data.size();
            data.clear();
            notifyItemRangeRemoved(0, size);
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView nameText;

            ImageView breedImage;

            ViewHolder(View itemView)
            {
                super(itemView);
                nameText = itemView.findViewById(R.id.name);
                breedImage = itemView.findViewById(R.id.image);
            }
        }
    }

    private static class GetBreedsTask extends AsyncTask<Void, Breed, Boolean>
    {
        private WeakReference<MainActivity> activityRef;

        GetBreedsTask(MainActivity activity)
        {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute()
        {
            MainActivity activity = activityRef.get();
            if(activity != null)
            {
                activity.showSpinner(true);
                activity.adapter.clear();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dog.ceo/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            DogService service = retrofit.create(DogService.class);

            if(isCancelled())
            {
                return false;
            }

            List<String> breedNames = getBreedNames(service);

            for(String name : breedNames)
            {
                if(isCancelled())
                {
                    return false;
                }

                Breed breed = new Breed(name, getBreedImage(service, name));
                publishProgress(breed);
            }
            return true;
        }

        private List<String> getBreedNames(DogService service)
        {
            try
            {
                NameResult result = service.getBreeds().execute().body();
                if(result != null && result.message != null)
                {
                    return result.message;
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return Collections.emptyList();
        }

        private @Nullable
        String getBreedImage(DogService service, String breedName)
        {
            try
            {
                ImageResult result = service.getImage(breedName).execute().body();
                if(result != null)
                {
                    return result.message;
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Breed... values)
        {
            MainActivity activity = this.activityRef.get();
            if(activity != null && values.length > 0)
            {
                activity.adapter.add(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            MainActivity activity = this.activityRef.get();
            if(activity != null)
            {
                activity.showSpinner(false);
            }
        }
    }
}
