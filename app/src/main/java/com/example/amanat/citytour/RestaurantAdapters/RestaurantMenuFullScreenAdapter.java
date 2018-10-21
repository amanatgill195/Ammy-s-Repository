package com.example.amanat.citytour.RestaurantAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.amanat.citytour.Adapters.RestaurantMenuFullScreen;
import com.example.amanat.citytour.Model.ImageModel;
import com.example.amanat.citytour.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantMenuFullScreenAdapter extends PagerAdapter {
    private ArrayList<ImageModel> imageModelArrayList;
    private RestaurantMenuFullScreen restaurantMenuFullScreen;
    private LayoutInflater layoutInflater;

    public RestaurantMenuFullScreenAdapter(ArrayList<ImageModel> imageModelArrayList, RestaurantMenuFullScreen restaurantMenuFullScreen) {
        this.imageModelArrayList = imageModelArrayList;
        this.restaurantMenuFullScreen = restaurantMenuFullScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) restaurantMenuFullScreen.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.menu_full_screen_view, container, false);
        ZoomageView im_slider = view.findViewById(R.id.imageZoom);
        Button btClose = (Button) view.findViewById(R.id.btClose);
        Picasso.get()
                .load(imageModelArrayList.get(position).getImage())
                .fit()
                .into(im_slider);

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantMenuFullScreen.finish();
            }
        });
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
