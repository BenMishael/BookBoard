package com.example.bookboard.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bookboard.R;

public class ImageLoader {

    private static ImageLoader instance;
    private static Context appContext;

    private ImageLoader(Context context) {
        this.appContext = context;
    }

    public static ImageLoader getInstance() {
        return instance;
    }

    public static ImageLoader initImageLoader(Context context) {
        if (instance == null)
            instance = new ImageLoader(context);
        return instance;
    }

    public void loadProfileImage(String imageURL, ImageView imageView) {
        Glide
                .with(appContext)
                .load(imageURL)
                .placeholder(R.mipmap.ic_launcher_round)
                .centerCrop()
                .into(imageView);
    }

    public void loadImage(String imageURL, ImageView imageView) {
        Glide
                .with(appContext)
                .load(imageURL)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    public void loadImage(Drawable drawable, ImageView imageView) {
        Glide
                .with(appContext)
                .load(drawable)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
    }
}