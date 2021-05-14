package com.newsapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import com.newsapp.activities.Search;
import com.newsapp.activities.SearchResult;
import com.newsapp.model.Categories;
import com.newsapp.model.Keyword;

import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.MyViewHolder>  {
    private List<Keyword.Data> moviesList;
    private Context context;
    public KeywordAdapter(List<Keyword.Data> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_popular_keyword, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_tag.setText(moviesList.get(position).getName().toUpperCase());
        holder.lin_view.setTag(moviesList.get(position).getName());
        holder.lin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.gotoSearchPage(context,"","",((String) view.getTag()),"","",moviesList.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_tag;
        public LinearLayout lin_view;
        public MyViewHolder(View view) {
            super(view);
            txt_tag = (TextView) view.findViewById(R.id.txt_tag);
            lin_view = (LinearLayout) view.findViewById(R.id.lin_view);
        }
    }
}
