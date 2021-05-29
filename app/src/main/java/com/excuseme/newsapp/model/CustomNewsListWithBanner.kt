package com.excuseme.newsapp.model

data class CustomNewsListWithBanner(
        var item_type: Int = 0,
        var isbookmark: String,
        var author: String,
        var description: String,
        var keywords: String,
        var slider: String,
        var trending_news: String,
        var upload_by: String,
        var user_mobile: String,
        var author_img: String,
        var cid: String? = null,
        var id: String? = null,
        var name: String? = null,
        var pdate: String? = null,
        var up_pro_img: String? = null,
        var banner_id: String? = null,
        var banner_name: String? = null,
        var banner_up_pro_img: String? = null,
        var banner_url: String? = null
)