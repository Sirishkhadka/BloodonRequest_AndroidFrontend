package com.example.bloodonrequest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodonrequest.R;

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    int images[];
    String[] country;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, int[] images, String[] country, LayoutInflater inflater) {
        this.context = context;
        this.images = images;
        this.country = country;
        this.inflater = inflater;
    }

    public SpinnerAdapter(Context applicationContext, int[] images, String[] country) {
        this.context = applicationContext;
        this.images = images;
        this.country = country;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_register_layout, null);
        ImageView imageView = convertView.findViewById(R.id.imageViewSpinner);
        TextView textView = convertView.findViewById(R.id.textViewSpinner);
        imageView.setImageResource(images[position]);
        textView.setText(country[position]);
        return convertView;
    }
}
