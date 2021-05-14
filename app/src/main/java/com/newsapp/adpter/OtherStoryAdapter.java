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
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.R;
import com.newsapp.activities.DetailScreen;
import com.newsapp.constant.Constant;
import com.newsapp.model.News;
import com.newsapp.model.OtherStories;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OtherStoryAdapter extends RecyclerView.Adapter<OtherStoryAdapter.MyViewHolder>  {
    private List<News.Data> moviesList;
    private Context context;
    public OtherStoryAdapter(List<News.Data> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_otherstory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.lin_view.setTag(position);
        holder.txttitle.setText(moviesList.get(position).getName().trim());
        holder.txtlabel.setText(moviesList.get(position).getKeywords().trim().toUpperCase());
        holder.lin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News.Data newsData=moviesList.get((Integer) view.getTag());
                context.startActivity(new Intent(context, DetailScreen.class).putExtra("newsDetails",newsData));
            }
        });
//        if (!moviesList.get(position).getUp_pro_img().equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(Constant.POST+moviesList.get(position).getUp_pro_img())
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.img_photo);
//        }
        if(position==(moviesList.size()-1)){
            holder.lin_line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txttitle, txtlabel;
        public ImageView img_photo;
        LinearLayout lin_view,lin_line;
        public MyViewHolder(View view) {
            super(view);
            txttitle = (TextView) view.findViewById(R.id.txttitle);
            txtlabel = (TextView) view.findViewById(R.id.txtlabel);
            img_photo = (ImageView) view.findViewById(R.id.img_photo);
            lin_view = (LinearLayout) view.findViewById(R.id.lin_view);
            lin_line = (LinearLayout) view.findViewById(R.id.lin_line);
        }
    }
}
