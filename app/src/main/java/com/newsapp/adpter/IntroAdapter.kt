package com.newsapp.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.newsapp.R
import com.newsapp.constant.Constant
import com.newsapp.dto.IntroResponseModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.intro_item.view.*

class IntroAdapter(private val itemClick: (IntroResponseModel.HomeBanner) -> Unit) : RecyclerView.Adapter<IntroAdapter.HomeOffersViewHolder>() {

    private var list: List<IntroResponseModel.HomeBanner> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(
                parent
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bind(list[position],itemClick)
    }

    fun setItem(list: List<IntroResponseModel.HomeBanner>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup) : this(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.intro_item,
                        parent, false
                )
        )

        fun bind(
            introImageModel: IntroResponseModel.HomeBanner,
            itemClick: (IntroResponseModel.HomeBanner) -> Unit
        ) {
            val circularProgressDrawable = CircularProgressDrawable(itemView.introImage.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            itemView.setOnClickListener {
                itemClick(introImageModel)
            }

            Picasso.with(itemView.introImage.context)
                    .load(Constant.BANNER + introImageModel.up_pro_img)
                    .error(R.drawable.error_load)
                    //.placeholder(R.drawable.loading)
                    .placeholder(circularProgressDrawable)
                    .into(itemView.introImage)
        }
    }
}