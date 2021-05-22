package com.newsapp.activities

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.newsapp.R
import com.newsapp.constant.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_display_picture.*

class DisplayPictureActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private var mAlbumList: ArrayList<String>? = null
    private var mSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_picture)
        init()
    }

    private fun init() {
        mAlbumList =
            intent.getSerializableExtra(Constant.IMAGE_LIST) as ArrayList<String>
        val pos = intent.getIntExtra(Constant.IMAGE_POSITION, 0)

        viewPager.addOnPageChangeListener(this)
        viewPager.adapter = PictureAdapter(this)
        mSize = if (mAlbumList != null) mAlbumList!!.size else 0
        if (pos < mSize) {
            viewPager.currentItem = pos
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private inner class PictureAdapter(context: Context) :
        PagerAdapter() {
        private val mInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return if (mAlbumList != null) mAlbumList!!.size else 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = mInflater.inflate(R.layout.item_zoom_image, container, false)

            val tivNewsImage =
                itemView.findViewById<com.github.chrisbanes.photoview.PhotoView>(R.id.tivNewsImage)

//            val circularProgressDrawable = CircularProgressDrawable(context)
//            circularProgressDrawable.strokeWidth = 5f
//            circularProgressDrawable.centerRadius = 30f
//            circularProgressDrawable.start()

            val circularProgressDrawable = CircularProgressDrawable(this@DisplayPictureActivity)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                circularProgressDrawable.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(
                        this@DisplayPictureActivity,
                        R.color.white
                    ), BlendMode.SRC_ATOP
                )
            } else {
                circularProgressDrawable.setColorFilter(
                    ContextCompat.getColor(
                        this@DisplayPictureActivity,
                        R.color.white
                    ), PorterDuff.Mode.SRC_ATOP
                )
            }

            Picasso.with(this@DisplayPictureActivity)
                .load(Constant.POST + mAlbumList!![position])
//                .apply(
//                    RequestOptions()
//                        .placeholder(circularProgressDrawable)
//                    //.error(R.drawable.default_image_placeholder)
//                )
                .into(tivNewsImage)

            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, anyType: Any) {
            container.removeView(anyType as LinearLayout)
        }
    }
}