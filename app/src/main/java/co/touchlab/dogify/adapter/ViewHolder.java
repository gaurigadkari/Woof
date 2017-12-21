package co.touchlab.dogify.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.touchlab.dogify.R;

/**
 * Created by Gauri Gadkari on 12/21/17.
 */

class ViewHolder extends RecyclerView.ViewHolder {
    TextView nameText;
    ImageView breedImage;

    ViewHolder(View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.name);
        breedImage = itemView.findViewById(R.id.image);
    }
}
