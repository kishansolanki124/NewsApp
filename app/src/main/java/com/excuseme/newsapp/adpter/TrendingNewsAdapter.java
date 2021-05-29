package com.excuseme.newsapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.excuseme.newsapp.activities.DetailScreen;
import com.excuseme.newsapp.constant.Constant;
import com.excuseme.newsapp.R;
import com.excuseme.newsapp.model.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrendingNewsAdapter extends RecyclerView.Adapter<TrendingNewsAdapter.MyViewHolder> {
    private final List<News.Data> moviesList;
    private final Context context;

    public TrendingNewsAdapter(List<News.Data> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trending_news_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.lin_view.setTag(position);
        holder.txt_title.setText(moviesList.get(position).getName());
        holder.txt_numnews.setText(moviesList.get(position).getnumRecords());
//        if (!moviesList.get(position).getUp_pro_img().equalsIgnoreCase("")) {

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Picasso.with(context)
                .load(Constant.POST + moviesList.get(position).getUp_pro_img())
                .error(R.mipmap.ic_launcher)
                .placeholder(circularProgressDrawable)
                .into(holder.img_photo);
//        }
        holder.lin_view.setOnClickListener(view -> {
            News.Data newsData = moviesList.get((Integer) view.getTag());
            context.startActivity(new Intent(context, DetailScreen.class).putExtra("newsDetails", newsData));
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title, txt_numnews;
        public ImageView img_photo;
        public LinearLayout lin_view;

        public MyViewHolder(View view) {
            super(view);
            txt_title = view.findViewById(R.id.txt_title);
            txt_numnews = view.findViewById(R.id.txt_numnews);
            img_photo = view.findViewById(R.id.img_photo);
            lin_view = view.findViewById(R.id.lin_view);
        }
    }
}