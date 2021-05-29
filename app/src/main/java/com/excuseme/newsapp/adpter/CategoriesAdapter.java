package com.excuseme.newsapp.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excuseme.newsapp.activities.Search;
import com.excuseme.newsapp.constant.Constant;
import com.excuseme.newsapp.R;
import com.excuseme.newsapp.model.Categories;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>  {
    private List<Categories.Data> moviesList;
    private Context context;
    public CategoriesAdapter(List<Categories.Data> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_categories, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_category.setText(moviesList.get(position).getName().toUpperCase());
        holder.lin_view.setTag(moviesList.get(position).getId());
        holder.lin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.gotoSearchPage(context,"",((String) view.getTag()),"","","",moviesList.get(position).getName());
            }
        });
        if (!moviesList.get(position).getUp_pro_img().equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(Constant.CATEGORY_IMAGE+moviesList.get(position).getUp_pro_img())
                    .error(R.drawable.error_load)
                    .placeholder(R.drawable.loading_icon)
                    .into(holder.img_icon);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_category;
        public ImageView img_icon;
        public LinearLayout lin_view;
        public MyViewHolder(View view) {
            super(view);
            txt_category = (TextView) view.findViewById(R.id.txt_category);
            img_icon = (ImageView) view.findViewById(R.id.img_icon);
            lin_view = (LinearLayout) view.findViewById(R.id.lin_view);
        }
    }
}
