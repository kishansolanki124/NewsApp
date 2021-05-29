package com.excuseme.newsapp.dto

data class IntroResponseModel(
    var home_banner: List<HomeBanner> = listOf(),
    var msg: String = "",
    var logo: String = "",
    var success: Boolean = false
) {
    data class HomeBanner(
        var id: String = "",
        var name: String = "",
        var up_pro_img: String = "",
        var url: String = ""
    )
}