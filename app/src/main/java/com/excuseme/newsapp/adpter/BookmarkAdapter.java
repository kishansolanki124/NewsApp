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
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.excuseme.newsapp.AppUtilsKt;
import com.excuseme.newsapp.activities.DetailScreen;
import com.excuseme.newsapp.activities.Search;
import com.excuseme.newsapp.constant.Constant;
import com.excuseme.newsapp.R;
import com.excuseme.newsapp.model.News;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    private final List<News.Data> moviesList;
    private final Context context;

    public BookmarkAdapter(List<News.Data> moviesList, Context context) {
        this.moviesList = moviesList;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.lin_view.setTag(position);
        holder.txt_name.setText(moviesList.get(position).getUpload_by());
        holder.txt_title.setText(moviesList.get(position).getName());
        holder.txt_tag.setText(moviesList.get(position).getKeywords().toUpperCase());
        if (null != moviesList.get(position).getTags() && !moviesList.get(position).getTags().isEmpty()) {
            holder.txt_first_tag.setText(AppUtilsKt.commaSeparatedStringtoArrayList(moviesList.get(position).getTags()).get(0));
            holder.txt_first_tag.setVisibility(View.VISIBLE);
        } else {
            holder.txt_first_tag.setVisibility(View.GONE);
        }
        holder.txt_first_tag.setOnClickListener(view -> Search.gotoSearchPage(
                context, "", "",
                AppUtilsKt.commaSeparatedStringtoArrayList(moviesList.get(position).getTags()).get(0), "",
                "",
                AppUtilsKt.commaSeparatedStringtoArrayList(moviesList.get(position).getTags()).get(0)));
        holder.lin_view.setOnClickListener(view -> {
            News.Data newsData = moviesList.get((Integer) view.getTag());
            context.startActivity(new Intent(context, DetailScreen.class).putExtra("newsDetails", (Serializable) newsData));
        });
//        if (!moviesList.get(position).getUp_pro_img().equalsIgnoreCase("")) {
        Picasso.with(context)
                .load(Constant.POST + moviesList.get(position).getUp_pro_img())
                .error(R.drawable.error_load)
                .placeholder(R.drawable.loading)
                .into(holder.img_photo);
        Picasso.with(context)
                .load(Constant.AUTHER + moviesList.get(position).getAuthor_img())
                .error(R.drawable.user_icon)
                .placeholder(R.drawable.user_icon)
                .into(holder.img_propic);
//        }
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(holder.img_bookmark);
        obj.add(position);
        holder.img_bookmark.setTag(obj);
        holder.img_bookmark.setOnClickListener(view -> {
            ArrayList<Object> obj1 = (ArrayList<Object>) view.getTag();
            Constant.saveBookmark(context, moviesList.get((Integer) obj1.get(1)).getId(), Constant.get_sp(context, "mobile"), "");
//                if (moviesList.get((Integer)obj.get(1)).getIsbookmark().equalsIgnoreCase("0")) {
//                    ((ImageView) view).setImageResource(R.drawable.screen_icon_bookmark_active);
//                    moviesList.get((Integer)obj.get(1)).setIsbookmark("1");
//                } else {
//                    ((ImageView) view).setImageResource(R.drawable.screen_icon_bookmark);
//                    moviesList.get((Integer)obj.get(1)).setIsbookmark("0");
//                }
            moviesList.remove(position);
            notifyDataSetChanged();
        });
        if (moviesList.get(position).getIsbookmark().equalsIgnoreCase("1")) {
            holder.img_bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }
        holder.txt_date.setText(moviesList.get(position).getPdate());
        holder.img_share.setTag(position);
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = Constant.get_sp(context, Constant.Postsharemsg) + "\n\n*" + HtmlCompat.fromHtml(moviesList.get((Integer) view.getTag()).getName(), HtmlCompat.FROM_HTML_MODE_COMPACT) + "*\n\n" + HtmlCompat.fromHtml(moviesList.get((Integer) view.getTag()).getDescription().substring(0, Math.min(moviesList.get(position).getDescription().length(), Constant.ShareDescWords)), HtmlCompat.FROM_HTML_MODE_COMPACT);
                Constant.shareImage(context, shareBody, Constant.POST + moviesList.get(position).getUp_pro_img(), null);
            }
        });
        holder.lin_trading_story.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_title, txt_tag, txt_date, txt_first_tag;
        public ImageView img_photo, img_share, img_bookmark, img_propic;
        public LinearLayout lin_view, lin_trading_story;

        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_tag = (TextView) view.findViewById(R.id.txt_tag);
            txt_first_tag = (TextView) view.findViewById(R.id.txt_first_tag);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            img_photo = (ImageView) view.findViewById(R.id.img_photo);
            img_share = (ImageView) view.findViewById(R.id.img_share);
            img_propic = (ImageView) view.findViewById(R.id.img_propic);
            img_bookmark = (ImageView) view.findViewById(R.id.img_bookmark);
            lin_view = (LinearLayout) view.findViewById(R.id.lin_view);
            lin_trading_story = (LinearLayout) view.findViewById(R.id.lin_trading_story);
        }
    }
}