package com.example.amanat.citytour.ViewHolders;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amanat.citytour.R;

public class RestaurantCommentViewHolder extends RecyclerView.ViewHolder {
    public TextView commentUserName, userComment;
    public ImageView commentUserImage;
    public Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "quicksand_regular.ttf");

    public RestaurantCommentViewHolder(View itemView) {
        super(itemView);
        commentUserName = itemView.findViewById(R.id.commentUserName);
        userComment = itemView.findViewById(R.id.txtUserComment);
        commentUserImage = itemView.findViewById(R.id.userCommentImage);
        commentUserName.setTypeface(font, Typeface.BOLD);
        userComment.setTypeface(font);

    }
}
