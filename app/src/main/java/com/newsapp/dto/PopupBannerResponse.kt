package com.newsapp.dto

data class PopupBannerResponse(
    var msg: String = "",
    var delay_time: String = "",
    var initial_time: String = "",
    var popup_banner: List<PopupBanner> = listOf(),
    var success: Boolean = false
) {
    data class PopupBanner(
        var id: String = "",
        var name: String = "",
        var screen: String = "",
        var status: String = "",
        var up_pro_img: String = "",
        var url: String = ""
    )
}