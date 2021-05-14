package com.newsapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.R;
import com.newsapp.activities.DetailScreen;
import com.newsapp.constant.Constant;
import com.newsapp.model.News;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class TrandingAdapter extends RecyclerView.Adapter<TrandingAdapter.MyViewHolder>  {
    private List<News.Data> moviesList;
    private Context context;
    public TrandingAdapter(List<News.Data> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tranding_search_result, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.lin_view.setTag(position);
        holder.txt_title.setText(moviesList.get(position).getName());
        holder.txt_numnews.setText(moviesList.get(position).getnumRecords());
//        if (!moviesList.get(position).getUp_pro_img().equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(Constant.POST+moviesList.get(position).getUp_pro_img())
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.img_photo);
//        }
        holder.lin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News.Data newsData=moviesList.get((Integer) view.getTag());
                context.startActivity(new Intent(context, DetailScreen.class).putExtra("newsDetails", (Serializable) newsData));
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title,txt_numnews;
        public ImageView img_photo;
        public LinearLayout lin_view;
        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_numnews = (TextView) view.findViewById(R.id.txt_numnews);
            img_photo = (ImageView) view.findViewById(R.id.img_photo);
            lin_view = (LinearLayout) view.findViewById(R.id.lin_view);
        }
    }
}
