package com.newsapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.R;
import com.newsapp.activities.DetailScreen;
import com.newsapp.activities.Home;
import com.newsapp.activities.Search;
import com.newsapp.constant.Constant;
import com.newsapp.model.News;
import com.newsapp.model.OtherStories;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.newsapp.constant.Constant.POST;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder>  {
    private List<News.Data> moviesList,moviesList_tranding;
    private Context context;
    public SearchResultAdapter(List<News.Data> moviesList,List<News.Data> moviesList_tranding, Context context) {
        this.moviesList = moviesList;
        this.moviesList_tranding= moviesList_tranding;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search_result, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if(moviesList.get(position).getName()!=null) {
            holder.lin_view.setTag(position);
            holder.txt_name.setText(moviesList.get(position).getUpload_by());
            holder.txt_title.setText(moviesList.get(position).getName());
            holder.txt_tag.setText(moviesList.get(position).getKeywords().toUpperCase());
            if(moviesList.get(position).getKeywords().isEmpty()){
                holder.txt_tag.setVisibility(View.GONE);
            }
            holder.txt_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Search.gotoSearchPage(context,"",moviesList.get(position).getCid(),"","","",moviesList.get(position).getKeywords());
                }
            });
//        if (!moviesList.get(position).getUp_pro_img().equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(Constant.POST+moviesList.get(position).getUp_pro_img())
                    .error(R.drawable.error_load)
                    .placeholder(R.drawable.loading)
                    .into(holder.img_photo);
//        }

            Picasso.with(context)
                    .load(Constant.AUTHER+moviesList.get(position).getAuthor_img())
                    .error(R.drawable.user_icon)
                    .placeholder(R.drawable.user_icon)
                    .into(holder.img_propic);
            holder.lin_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News.Data newsData = moviesList.get((Integer) view.getTag());
                    context.startActivity(new Intent(context, DetailScreen.class).putExtra("newsDetails", (Serializable) newsData));
                }
            });
            ArrayList<Object> obj=new ArrayList<Object>();
            obj.add(holder.img_bookmark);
            obj.add(position);
            holder.txt_date.setText(moviesList.get(position).getPdate());
            holder.img_bookmark.setTag(obj);
            holder.img_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Object> obj= (ArrayList<Object>) view.getTag();
                    Constant.saveBookmark(context, moviesList.get((Integer)obj.get(1)).getId(), Constant.get_sp(context, "mobile"), "");
                    if (moviesList.get((Integer)obj.get(1)).getIsbookmark().equalsIgnoreCase("0")) {
                        ((ImageView) view).setImageResource(R.drawable.ic_baseline_bookmark_24);
                        moviesList.get((Integer)obj.get(1)).setIsbookmark("1");
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.ic_baseline_bookmark_gray_border_24);
                        moviesList.get((Integer)obj.get(1)).setIsbookmark("0");
                    }
                }
            });
            if (moviesList.get(position).getIsbookmark().equalsIgnoreCase("1")) {
                holder.img_bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
            }
            holder.img_share.setTag(position);
            holder.img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String shareBody = "*"+HtmlCompat.fromHtml(moviesList.get(position).getName(), HtmlCompat.FROM_HTML_MODE_COMPACT) + "*\n\n" + HtmlCompat.fromHtml(moviesList.get(position).getDescription().substring(0, Math.min(moviesList.get(position).getDescription().length(), Constant.ShareDescWords))+"...\n\n"+Constant.get_sp(context,Constant.Postsharemsg), HtmlCompat.FROM_HTML_MODE_COMPACT);
                    Constant.shareImage(context,shareBody,Constant.POST+moviesList.get(position).getUp_pro_img(),null);
                }
            });
            holder.lin_mainlayout.setVisibility(View.VISIBLE);
            holder.lin_trading_story.setVisibility(View.GONE);
        }else{
            holder.lin_mainlayout.setVisibility(View.GONE);
            holder.lin_trading_story.setVisibility(View.VISIBLE);
            if(moviesList_tranding.size()==0){
                holder.lin_trading_story.setVisibility(View.GONE);
            }
            TrandingAdapter mAdapter = new TrandingAdapter(moviesList_tranding, context);
            holder.rec_tranding.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.rec_tranding.setItemAnimator(new DefaultItemAnimator());
            holder.rec_tranding.setAdapter(mAdapter);
        }
        if(holder.lin_mainlayout.getVisibility()==View.GONE){
            holder.lin_trading_story.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_title,txt_tag,txt_date;
        public ImageView img_photo,img_share,img_bookmark,img_propic;
        public LinearLayout lin_view,lin_mainlayout,lin_trading_story;
        RecyclerView rec_tranding;
        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_tag = (TextView) view.findViewById(R.id.txt_tag);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            img_photo = (ImageView) view.findViewById(R.id.img_photo);
            img_share = (ImageView) view.findViewById(R.id.img_share);
            img_propic = (ImageView) view.findViewById(R.id.img_propic);
            img_bookmark = (ImageView) view.findViewById(R.id.img_bookmark);
            lin_view = (LinearLayout) view.findViewById(R.id.lin_view);
            lin_mainlayout= (LinearLayout) view.findViewById(R.id.lin_mainlayout);
            lin_trading_story= (LinearLayout) view.findViewById(R.id.lin_trading_story);
            rec_tranding =(RecyclerView)view.findViewById(R.id.rec_tranding);
        }
    }
}
