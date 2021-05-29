package com.excuseme.newsapp.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excuseme.newsapp.R;
import com.excuseme.newsapp.activities.Search;

import java.util.List;

public class NewsTagAdapter extends RecyclerView.Adapter<NewsTagAdapter.MyViewHolder> {
    private final List<String> newsTagList;
    private final Context context;

    public NewsTagAdapter(List<String> newsTagList, Context context) {
        this.newsTagList = newsTagList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_tag, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_tag.setText(newsTagList.get(position));
        holder.lin_view.setTag(newsTagList.get(position));
        holder.lin_view.setOnClickListener(view -> Search.gotoSearchPage(context, "", "",
                ((String) view.getTag()), "", "", newsTagList.get(position)));
    }

    @Override
    public int getItemCount() {
        return newsTagList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_tag;
        public LinearLayout lin_view;

        public MyViewHolder(View view) {
            super(view);
            txt_tag = view.findViewById(R.id.txt_tag);
            lin_view = view.findViewById(R.id.lin_view);
        }
    }
}