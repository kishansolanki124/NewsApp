package com.newsapp.activities

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.newsapp.R
import com.newsapp.adpter.IntroAdapter
import com.newsapp.api.ApiListeners
import com.newsapp.browserIntent
import com.newsapp.constant.Constant
import com.newsapp.dto.IntroResponseModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_intro.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class IntroActivity : AppCompatActivity() {

    private lateinit var apiListeners: ApiListeners

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        //setupLogo()
        fetchIntroImages()

        btGetStarted.setOnClickListener {
            if (Constant.get_sp(this@IntroActivity, "mobile").isNotEmpty()) {
                val intent = Intent(this@IntroActivity, Home::class.java)
                startActivity(intent)
            } else {
                startActivity(Intent(this, Signup::class.java))
            }
            finish()
        }
    }

    private fun setupLogo(logo: String) {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            circularProgressDrawable.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.pink
                ), BlendMode.SRC_ATOP
            )
        } else {
            circularProgressDrawable.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.pink
                ), PorterDuff.Mode.SRC_ATOP
            )
        }

        circularProgressDrawable.start()

        Constant.save_sp_genral(applicationContext, Constant.APP_LOGO, logo)

        Picasso.with(applicationContext)
            .load(logo)
            .error(R.drawable.error_load)
            .placeholder(circularProgressDrawable)
            .into(ivAppLogo)
    }

    private fun fetchIntroImages() {
        pbIntro.visibility = View.VISIBLE
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(100, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiListeners = retrofit.create(ApiListeners::class.java)

        val getUserInfoVoCall: Call<IntroResponseModel> = apiListeners.introBanner()

        getUserInfoVoCall.enqueue(object : Callback<IntroResponseModel?> {
            override fun onResponse(
                call: Call<IntroResponseModel?>,
                response: Response<IntroResponseModel?>
            ) {
                pbIntro.visibility = View.GONE
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        setupOffersViewPager(response.body()!!.home_banner)
                        setupLogo(response.body()!!.logo)
                    } else {
                        Toast.makeText(
                            this@IntroActivity,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<IntroResponseModel?>, t: Throwable) {
                pbIntro.visibility = View.GONE
                Toast.makeText(this@IntroActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupOffersViewPager(homeBanner: List<IntroResponseModel.HomeBanner>) {
        val adapter = IntroAdapter {
            browserIntent(this, it.url)
        }
        adapter.setItem(homeBanner)
        introViewpager.adapter = adapter

        TabLayoutMediator(introTabLayout, introViewpager as ViewPager2) { _, _ ->
        }.attach()

        var currentPage = 0
        val handler = Handler(Looper.getMainLooper())

        val update = Runnable {
            if (currentPage == homeBanner.size) {
                currentPage = 0
            }
            introViewpager.setCurrentItem(currentPage++, true)
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 2000, 3500)

        introViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })
    }
}