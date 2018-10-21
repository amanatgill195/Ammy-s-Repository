package com.example.amanat.citytour.RestaurantAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amanat.citytour.Model.RestaurantCommentModel;
import com.example.amanat.citytour.R;
import com.example.amanat.citytour.RestaurantDetail;
import com.example.amanat.citytour.ViewHolders.RestaurantCommentViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantCommentAdapter extends RecyclerView.Adapter<RestaurantCommentViewHolder> {
    private ArrayList<RestaurantCommentModel> commentModels;
    private RestaurantDetail restaurantDetail;

    public RestaurantCommentAdapter(ArrayList<RestaurantCommentModel> commentModels, RestaurantDetail restaurantDetail) {
        this.commentModels = commentModels;
        this.restaurantDetail = restaurantDetail;
    }

    @NonNull
    @Override
    public RestaurantCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(restaurantDetail.getBaseContext());
        View view = layoutInflater.inflate(R.layout.customer_comments, parent, false);
        return new RestaurantCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantCommentViewHolder holder, int position) {
        holder.commentUserName.setText(commentModels.get(position).getUserName());
        holder.userComment.setText(commentModels.get(position).getUserComment());
        Picasso.get().load(commentModels.get(position).getUserImage()).fit().into(holder.commentUserImage);
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }
}
