package com.newsapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.newsapp.constant.Constant
import com.newsapp.dto.PopupBannerResponse
import com.squareup.picasso.Picasso

fun Context.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

private var pgDialog: Dialog? = null
private var newsBannerAdCurrentIndex = 0

fun showProgressDialog(context: Context, popup_banner: List<PopupBannerResponse.PopupBanner>) {
    if (pgDialog == null) {
        if (!(context as Activity).isFinishing) {
            pgDialog = getProgressDialog(context, popup_banner)
            pgDialog?.show()
        }
    } else if (null != pgDialog && (!pgDialog!!.isShowing)) {
        if (!(context as Activity).isFinishing) {
            pgDialog?.show()
        }
    }
}

fun getProgressDialog(
    context: Context,
    popup_banner: List<PopupBannerResponse.PopupBanner>
): Dialog {
    var currentIndex = 0
    val progressDialog = Dialog(context)
    val view: View = View.inflate(context, R.layout.dialog_progress, null)
    //LayoutInflater.from(context).inflate(R.layout.dialog_progress, parent, false)

    val ivAppBanner = view.findViewById<ImageView>(R.id.ivAppBanner)
    val ivClose = view.findViewById<ImageView>(R.id.ivClose)

    ivClose.setOnClickListener {
        dismissProgressDialog()
    }

    if (popup_banner.size - 1 < newsBannerAdCurrentIndex) {
        newsBannerAdCurrentIndex = 0
    }
    currentIndex = newsBannerAdCurrentIndex

    ivAppBanner.setOnClickListener {
        browserIntent(context, popup_banner[currentIndex].url)
        dismissProgressDialog()
    }

    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        circularProgressDrawable.colorFilter = BlendModeColorFilter(
            ContextCompat.getColor(
                context,
                R.color.white
            ), BlendMode.SRC_ATOP
        )
    } else {
        circularProgressDrawable.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.white
            ), PorterDuff.Mode.SRC_ATOP
        )
    }

    circularProgressDrawable.start()

    Picasso.with(ivAppBanner.context)
        .load(Constant.BANNER + popup_banner[currentIndex].up_pro_img)
        .error(R.drawable.error_load)
        .placeholder(circularProgressDrawable)
        .into(ivAppBanner)

    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    progressDialog.setContentView(view)
    progressDialog.setCancelable(true)
    progressDialog.setCanceledOnTouchOutside(false)
    val window = progressDialog.window
    if (window != null) {
        window.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                android.R.color.transparent
            )
        )
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    newsBannerAdCurrentIndex += 1
    return progressDialog
}

fun dismissProgressDialog() {
    if (pgDialog != null) {
        pgDialog?.dismiss()
        pgDialog = null
    }
}

fun browserIntent(context: Context, url: String) {
    var webpage = Uri.parse(url)
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        webpage = Uri.parse("http://$url")
    }
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}