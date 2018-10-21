package com.example.amanat.citytour.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.amanat.citytour.R;

public class ImageMenuViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageMenu;

    public ImageMenuViewHolder(View itemView) {
        super(itemView);
        imageMenu = itemView.findViewById(R.id.imageMenu);
    }
}
