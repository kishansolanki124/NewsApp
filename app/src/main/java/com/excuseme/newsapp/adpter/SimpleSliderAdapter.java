package com.excuseme.newsapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.excuseme.newsapp.activities.DisplayPictureActivity;
import com.excuseme.newsapp.R;
import com.excuseme.newsapp.constant.Constant;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleSliderAdapter extends SliderViewAdapter<SimpleSliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<String> mSliderItems;
    Context context;

    // Constructor
    public SimpleSliderAdapter(Context context, ArrayList<String> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = View.inflate(context, R.layout.adapter_simple_slider, null);
        //View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_simple_slider, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder holder, final int position) {

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        String sliderItem = mSliderItems.get(position);
        Picasso.with(context)
                .load(Constant.POST + sliderItem)
                .error(R.drawable.error_load)
                //.placeholder(R.drawable.loading)
                .placeholder(circularProgressDrawable)
                .into(holder.img_photo);

        holder.img_photo.setOnClickListener(view -> context.startActivity(new Intent(context, DisplayPictureActivity.class)
                .putExtra(Constant.IMAGE_POSITION, position)
                .putExtra(Constant.IMAGE_LIST, (Serializable) mSliderItems)));
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView img_photo;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            img_photo = itemView.findViewById(R.id.img_photo);
            this.itemView = itemView;
        }
    }
}