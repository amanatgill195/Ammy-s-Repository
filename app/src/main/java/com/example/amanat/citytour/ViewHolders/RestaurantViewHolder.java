package com.example.amanat.citytour.ViewHolders;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.amanat.citytour.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    public ImageView ibRestrauntImage;
    public TextView restrauntName, cousineType1, cousineType2, dot, restaurantDistance;
    public RatingBar ratingBar;
    public Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "quicksand_regular.ttf");

    public RestaurantViewHolder(View itemView) {
        super(itemView);

        ibRestrauntImage = itemView.findViewById(R.id.ibRestrauntImage);
        restrauntName = itemView.findViewById(R.id.restrauntName);
        restaurantDistance = itemView.findViewById(R.id.restaurantDistance);
        ratingBar = itemView.findViewById(R.id.restaurantRatingBarMini);
        restaurantDistance.setTypeface(font);

        //cousineType1 = itemView.findViewById(R.id.cousineType1);
        //cousineType2 = itemView.findViewById(R.id.cousineType2);
        //dot = itemView.findViewById(R.id.dot);
        restrauntName.setTypeface(font, Typeface.BOLD);
        //cousineType1.setTypeface(font);
        //cousineType2.setTypeface(font);
        //dot.setTypeface(font);

    }
}
