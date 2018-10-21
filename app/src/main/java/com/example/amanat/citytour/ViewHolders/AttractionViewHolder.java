package com.example.amanat.citytour.ViewHolders;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amanat.citytour.R;
import com.google.firebase.firestore.DocumentReference;

public class AttractionViewHolder extends RecyclerView.ViewHolder {
    public ImageView hotspotImage;
    public TextView hotspotName;
    public DocumentReference docRef;
    public Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "lobster.ttf");

    public AttractionViewHolder(View itemView) {
        super(itemView);

        hotspotImage = itemView.findViewById(R.id.hotspotImage);
        hotspotName = itemView.findViewById(R.id.txtHotspotName);
        hotspotName.setTypeface(font, Typeface.BOLD);
    }
}
