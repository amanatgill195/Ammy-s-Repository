package com.example.amanat.citytour.RestaurantAdapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amanat.citytour.Adapters.RestaurantMenuFullScreen;
import com.example.amanat.citytour.Model.ImageMenuModel;
import com.example.amanat.citytour.Model.MenuCollectionModel;
import com.example.amanat.citytour.R;
import com.example.amanat.citytour.RestaurantDetail;
import com.example.amanat.citytour.ViewHolders.ImageMenuViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantImageMenuAdapter extends RecyclerView.Adapter<ImageMenuViewHolder> {
    private ArrayList<ImageMenuModel> imageMenuModelArrayList;
    private RestaurantDetail restaurantDetail;
    private ArrayList<MenuCollectionModel> menuCollectionModelArrayList;

    public RestaurantImageMenuAdapter(ArrayList<ImageMenuModel> imageMenuModelArrayList, RestaurantDetail restaurantDetail, ArrayList<MenuCollectionModel> menuCollectionModelArrayList) {
        this.imageMenuModelArrayList = imageMenuModelArrayList;
        this.restaurantDetail = restaurantDetail;
        this.menuCollectionModelArrayList = menuCollectionModelArrayList;
    }

    @NonNull
    @Override
    public ImageMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(restaurantDetail.getBaseContext());
        View view = layoutInflater.inflate(R.layout.recycler_menu_view, parent, false);
        return new ImageMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageMenuViewHolder holder, final int position) {
        Picasso.get().load(imageMenuModelArrayList.get(position).getMenuImage()).fit().into(holder.imageMenu);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(restaurantDetail, RestaurantMenuFullScreen.class);
                Bundle bundle = new Bundle();
                for (int i = 0; i < menuCollectionModelArrayList.size(); i++) {
                    bundle.putString("collection", menuCollectionModelArrayList.get(i).getCollection());
                    bundle.putString("docId", menuCollectionModelArrayList.get(i).getDocId());
                }
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageMenuModelArrayList.size();
    }
}
