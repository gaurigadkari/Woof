package co.touchlab.dogify.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.dogify.models.Breed;
import co.touchlab.dogify.R;

/**
 * Created by Gauri Gadkari on 12/21/17.
 */

public class BreedAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<Breed> breeds = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_breed, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Breed breed = breeds.get(position);
        holder.nameText.setText(breed.getName());
        Picasso.with(holder.breedImage.getContext())
                .load(breed.getImageURL())
                .fit()
                .centerCrop()
                .into(holder.breedImage);
    }

    @Override
    public int getItemCount() {
        return breeds.size();
    }

    public void add(Breed breed) {
        breeds.add(breed);
        notifyItemInserted(breeds.size() - 1);
    }

    public void clear() {
        int size = breeds.size();
        breeds.clear();
        notifyItemRangeRemoved(0, size);
    }
}