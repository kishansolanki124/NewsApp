package com.newsapp.dto

data class NewsDetailResponse(
    var `data`: List<Data> = listOf(),
    var msg: String = "",
    var success: Boolean = false
) {
    data class Data(
        var author: String = "",
        var author_img: String = "",
        var cid: String = "",
        var city: String = "",
        var created_at: String = "",
        var description: String = "",
        var id: String = "",
        var keywords: String = "",
        var name: String = "",
        var pdate: String = "",
        var post_publish_type: String = "",
        var slider_news: String = "",
        var status: String = "",
        var trending_news: String = "",
        var up_pro_img: String = "",
        var upload_by: String = "",
        var user_mobile: String = "",
        var web_link: String = "",
        var isbookmark: String = ""
    )
}