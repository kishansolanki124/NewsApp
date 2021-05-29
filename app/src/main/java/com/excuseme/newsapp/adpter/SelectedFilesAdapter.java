package com.excuseme.newsapp.adpter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excuseme.newsapp.R;

import java.util.List;

public class SelectedFilesAdapter extends RecyclerView.Adapter<SelectedFilesAdapter.MyViewHolder>  {
    private List<Uri> moviesList;
    private Context context;
    public SelectedFilesAdapter(List<Uri> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_multipe_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.img_selected_image.setImageURI(moviesList.get(position));
        holder.img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moviesList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_selected_image,img_remove;
        public MyViewHolder(View view) {
            super(view);
            img_selected_image = (ImageView) view.findViewById(R.id.img_selected_image);
            img_remove = (ImageView) view.findViewById(R.id.img_remove);
        }
    }
}
