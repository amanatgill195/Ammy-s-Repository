package com.example.amanat.citytour.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.amanat.citytour.AttractionOnClick;
import com.example.amanat.citytour.Model.ImageModel;
import com.example.amanat.citytour.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AttractionImageSliderAdapter extends PagerAdapter {
    private ArrayList<ImageModel> imageModelArrayList;
    private AttractionOnClick attractionOnClick;
    private LayoutInflater layoutInflater;

    public AttractionImageSliderAdapter(ArrayList<ImageModel> imageModelArrayList, AttractionOnClick attractionOnClick) {
        this.imageModelArrayList = imageModelArrayList;
        this.attractionOnClick = attractionOnClick;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) attractionOnClick.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.image_slider_view, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.imageSlide);
        Picasso.get()
                .load(imageModelArrayList.get(position).getImage())
                .fit()
                .into(im_slider);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}