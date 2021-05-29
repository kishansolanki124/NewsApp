package com.excuseme.newsapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.excuseme.newsapp.R;
import com.excuseme.newsapp.activities.DetailScreen;
import com.excuseme.newsapp.constant.Constant;
import com.excuseme.newsapp.model.News;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {
	
	// list for storing urls of images.
	private final List<News.Data> mSliderItems;
	Context context;
	// Constructor
	public SliderAdapter(Context context, ArrayList<News.Data> sliderDataArrayList) {
		this.mSliderItems = sliderDataArrayList;
		this.context=context;
	}

	// We are inflating the slider_layout
	// inside on Create View Holder method.
	@Override
	public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
		View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_slider, null);
		return new SliderAdapterViewHolder(inflate);
	}

	// Inside on bind view holder we will
	// set data to item of Slider View.
	@Override
	public void onBindViewHolder(SliderAdapterViewHolder holder, int position) {

		final News.Data sliderItem = mSliderItems.get(position);
		holder.txt_tag.setText(sliderItem.getKeywords());
		holder.txt_title.setText(sliderItem.getName());
		Picasso.with(context)
				.load(Constant.POST+mSliderItems.get(position).getUp_pro_img())
				.error(R.drawable.error_load)
				.placeholder(R.drawable.loading)
				.into(holder.img_photo);
		holder.itemView.setTag(position);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				News.Data newsData = mSliderItems.get((Integer) view.getTag());
				context.startActivity(new Intent(context, DetailScreen.class).putExtra("newsDetails", (Serializable) newsData));
			}
		});

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
		TextView txt_tag,txt_title;
		public SliderAdapterViewHolder(View itemView) {
			super(itemView);
			img_photo = itemView.findViewById(R.id.img_photo);
			txt_tag = itemView.findViewById(R.id.txt_tag);
			txt_title = itemView.findViewById(R.id.txt_title);
			this.itemView = itemView;
		}
	}
}
