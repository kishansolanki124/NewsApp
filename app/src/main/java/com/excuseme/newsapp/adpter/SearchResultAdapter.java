package com.excuseme.newsapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.excuseme.newsapp.AppUtilsKt;
import com.excuseme.newsapp.activities.NewsDetail;
import com.excuseme.newsapp.activities.Search;
import com.excuseme.newsapp.constant.Constant;
import com.excuseme.newsapp.R;
import com.excuseme.newsapp.model.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {
    private final List<News.Data> newsList;
    private final List<News.Data> trendingNewsList;
    private final Context context;

    public SearchResultAdapter(List<News.Data> newsList, List<News.Data> trendingNewsList, Context context) {
        this.newsList = newsList;
        this.trendingNewsList = trendingNewsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search_result, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setTrendingNews(ArrayList<News.Data> trendingNewsList) {
        this.trendingNewsList.clear();
        this.trendingNewsList.addAll(trendingNewsList);
    }

//    public void add(News.Data news) {
//        newsList.add(news);
//        notifyItemInserted(newsList.size() - 1);
//    }
//
//    public void addAll(List<News.Data> newsList) {
//        for (News.Data result : newsList) {
//            add(result);
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (newsList.get(position).getView_type() == 0) {
            //if (moviesList.get(position).getName() != null) {
            holder.lin_view.setTag(position);
            holder.txt_name.setText(newsList.get(position).getUpload_by());
            holder.txt_title.setText(newsList.get(position).getName());
            holder.txt_tag.setText(newsList.get(position).getKeywords().toUpperCase());
            if (null != newsList.get(position).getTags() && !newsList.get(position).getTags().isEmpty()) {
                holder.txt_first_tag.setText(AppUtilsKt.commaSeparatedStringtoArrayList(newsList.get(position).getTags()).get(0));
                holder.txt_first_tag.setVisibility(View.VISIBLE);
            } else {
                holder.txt_first_tag.setVisibility(View.GONE);
            }

            if (newsList.get(position).getKeywords().isEmpty()) {
                holder.txt_tag.setVisibility(View.GONE);
            }
            holder.txt_tag.setOnClickListener(view -> Search.gotoSearchPage(context, "", newsList.get(position).getCid(), "", "", "", newsList.get(position).getKeywords()));
            holder.txt_first_tag.setOnClickListener(view -> Search.gotoSearchPage(
                    context, "", "",
                    AppUtilsKt.commaSeparatedStringtoArrayList(newsList.get(position).getTags()).get(0), "",
                    "",
                    AppUtilsKt.commaSeparatedStringtoArrayList(newsList.get(position).getTags()).get(0)));
//        if (!moviesList.get(position).getUp_pro_img().equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(Constant.POST + newsList.get(position).getUp_pro_img())
                    .error(R.drawable.error_load)
                    .placeholder(R.drawable.loading)
                    .into(holder.img_photo);

            holder.img_photo.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(Constant.AUTHER + newsList.get(position).getAuthor_img())
                    .error(R.drawable.user_icon)
                    .placeholder(R.drawable.user_icon)
                    .into(holder.img_propic);
            holder.lin_view.setOnClickListener(view -> {
                News.Data newsData = newsList.get((Integer) view.getTag());
                //context.startActivity(new Intent(context, DetailScreen.class).putExtra("newsDetails", (Serializable) newsData));
                context.startActivity(new Intent(context, NewsDetail.class).putExtra(Constant.NEWS_ID, Integer.parseInt(newsData.getId())));
            });
            ArrayList<Object> obj = new ArrayList<>();
            obj.add(holder.img_bookmark);
            obj.add(position);
            holder.txt_date.setText(newsList.get(position).getPdate());
            holder.img_bookmark.setTag(obj);
            holder.img_bookmark.setOnClickListener(view -> {
                ArrayList<Object> obj1 = (ArrayList<Object>) view.getTag();
                Constant.saveBookmark(context, newsList.get((Integer) obj1.get(1)).getId(), Constant.get_sp(context, "mobile"), "");
                if (newsList.get((Integer) obj1.get(1)).getIsbookmark().equalsIgnoreCase("0")) {
                    ((ImageView) view).setImageResource(R.drawable.ic_baseline_bookmark_24);
                    newsList.get((Integer) obj1.get(1)).setIsbookmark("1");
                } else {
                    ((ImageView) view).setImageResource(R.drawable.ic_baseline_bookmark_gray_border_24);
                    newsList.get((Integer) obj1.get(1)).setIsbookmark("0");
                }
            });
            if (newsList.get(position).getIsbookmark().equalsIgnoreCase("1")) {
                holder.img_bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
            }
            holder.img_share.setTag(position);
            holder.img_share.setOnClickListener(view -> {
                String shareBody = "*" + HtmlCompat.fromHtml(newsList.get(position).getName(), HtmlCompat.FROM_HTML_MODE_COMPACT) + "*\n\n" + HtmlCompat.fromHtml(newsList.get(position).getDescription().substring(0, Math.min(newsList.get(position).getDescription().length(), Constant.ShareDescWords)) + "...\n\n" + Constant.get_sp(context, Constant.Postsharemsg), HtmlCompat.FROM_HTML_MODE_COMPACT);
                Constant.shareImage(context, shareBody, Constant.POST + newsList.get(position).getUp_pro_img(), null);
            });
            holder.lin_trading_story.setVisibility(View.GONE);
            holder.ivAd.setVisibility(View.GONE);
            holder.lin_mainlayout.setVisibility(View.VISIBLE);
            holder.img_photo.setVisibility(View.VISIBLE);
            holder.rlImageBottom.setVisibility(View.VISIBLE);
            holder.txt_title.setVisibility(View.VISIBLE);
            holder.llNewsPostedBy.setVisibility(View.VISIBLE);
        } else if (newsList.get(position).getView_type() == 2) {
            holder.txt_first_tag.setVisibility(View.GONE);
            holder.lin_mainlayout.setVisibility(View.VISIBLE);
            holder.ivAd.setVisibility(View.VISIBLE);
            holder.img_photo.setVisibility(View.GONE);
            holder.lin_trading_story.setVisibility(View.GONE);
            holder.rlImageBottom.setVisibility(View.GONE);
            holder.txt_title.setVisibility(View.GONE);
            holder.llNewsPostedBy.setVisibility(View.GONE);

            Picasso.with(context)
                    .load(Constant.BANNER + newsList.get(position).getUp_pro_img())
                    .error(R.drawable.error_load)
                    .placeholder(R.drawable.loading)
                    .into(holder.ivAd);
            holder.ivAd.setOnClickListener(view -> AppUtilsKt.browserIntent(context, newsList.get(position).getUrl()));
        } else {
            holder.txt_first_tag.setVisibility(View.GONE);
            holder.ivAd.setVisibility(View.GONE);
            holder.lin_mainlayout.setVisibility(View.GONE);
            holder.lin_trading_story.setVisibility(View.VISIBLE);
            if (trendingNewsList.size() == 0) {
                holder.lin_trading_story.setVisibility(View.GONE);
            }
            TrendingNewsAdapter mAdapter = new TrendingNewsAdapter(trendingNewsList, context);
            holder.rec_tranding.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.rec_tranding.setItemAnimator(new DefaultItemAnimator());
            holder.rec_tranding.setAdapter(mAdapter);
        }

        if (holder.lin_mainlayout.getVisibility() == View.GONE) {
            holder.lin_trading_story.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_title, txt_tag, txt_first_tag, txt_date;
        public ImageView img_photo, ivAd, img_share, img_bookmark, img_propic;
        public LinearLayout lin_view, lin_mainlayout, lin_trading_story, llNewsPostedBy;
        public RelativeLayout rlImageBottom;
        RecyclerView rec_tranding;

        public MyViewHolder(View view) {
            super(view);
            txt_name = view.findViewById(R.id.txt_name);
            txt_title = view.findViewById(R.id.txt_title);
            txt_tag = view.findViewById(R.id.txt_tag);
            txt_first_tag = view.findViewById(R.id.txt_first_tag);
            txt_date = view.findViewById(R.id.txt_date);
            img_photo = view.findViewById(R.id.img_photo);
            ivAd = view.findViewById(R.id.ivAd);
            img_share = view.findViewById(R.id.img_share);
            img_propic = view.findViewById(R.id.img_propic);
            img_bookmark = view.findViewById(R.id.img_bookmark);
            lin_view = view.findViewById(R.id.lin_view);
            lin_mainlayout = view.findViewById(R.id.lin_mainlayout);
            lin_trading_story = view.findViewById(R.id.lin_trading_story);
            rec_tranding = view.findViewById(R.id.rec_tranding);
            rlImageBottom = view.findViewById(R.id.rlImageBottom);
            llNewsPostedBy = view.findViewById(R.id.llNewsPostedBy);
        }
    }
}