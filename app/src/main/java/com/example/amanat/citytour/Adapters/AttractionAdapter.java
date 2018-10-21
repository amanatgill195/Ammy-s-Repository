package com.example.amanat.citytour.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amanat.citytour.AttractionOnClick;
import com.example.amanat.citytour.Attractions;
import com.example.amanat.citytour.Model.AttractionModel;
import com.example.amanat.citytour.R;
import com.example.amanat.citytour.ViewHolders.AttractionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionViewHolder> {
    private ArrayList<AttractionModel> attractionModelArrayList;
    private Attractions attractions;
    private Bundle bundle;

    public AttractionAdapter(ArrayList<AttractionModel> attractionModelArrayList, Attractions attractions) {
        this.attractionModelArrayList = attractionModelArrayList;
        this.attractions = attractions;
    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(attractions.getBaseContext());
        View view = layoutInflater.inflate(R.layout.attraction_view, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttractionViewHolder holder, final int position) {
        holder.hotspotName.setText(attractionModelArrayList.get(position).getHotspotName());
        Picasso.get().load(attractionModelArrayList.get(position).hotspotImage).fit().into(holder.hotspotImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(attractions, AttractionOnClick.class);
                Bundle bundle = new Bundle();
                bundle.putString("image", attractionModelArrayList.get(position).getHotspotImage());
                bundle.putString("name", attractionModelArrayList.get(position).getHotspotName());
                bundle.putString("timing", attractionModelArrayList.get(position).getTxtTime());
                bundle.putString("aboutPlace", attractionModelArrayList.get(position).getAboutPlace());
                bundle.putString("documentId", attractionModelArrayList.get(position).getDocumentId());

                intent.putExtras(bundle);
                v.getContext().startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return attractionModelArrayList.size();
    }
}