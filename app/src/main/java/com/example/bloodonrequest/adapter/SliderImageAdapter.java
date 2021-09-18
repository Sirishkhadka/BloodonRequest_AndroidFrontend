package com.example.bloodonrequest.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bloodonrequest.R;
import com.example.bloodonrequest.model.Image;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SliderImageAdapter extends PagerAdapter {

    private ArrayList<Image> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;

    public SliderImageAdapter(Context context, ArrayList<Image> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    public SliderImageAdapter(ArrayList<Image> imageModelArrayList, LayoutInflater inflater, Context context) {
        this.imageModelArrayList = imageModelArrayList;
        this.inflater = inflater;
        this.context = context;
    }

    public SliderImageAdapter(ArrayList<Image> imageModelArrayList, Context context) {
        this.imageModelArrayList = imageModelArrayList;
        this.context = context;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slider_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.sliderImageView);


        imageView.setImageResource(imageModelArrayList.get(position).getImage_drawable());

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {

    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }
}
