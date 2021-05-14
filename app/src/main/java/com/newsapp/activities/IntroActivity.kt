package com.newsapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.newsapp.R
import com.newsapp.adpter.IntroAdapter
import com.newsapp.api.ApiListeners
import com.newsapp.constant.Constant
import com.newsapp.dto.IntroResponseModel
import kotlinx.android.synthetic.main.activity_intro.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class IntroActivity : AppCompatActivity() {

    var apiListeners: ApiListeners? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

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

    private fun fetchIntroImages() {
        //pb.show();
        val client = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        apiListeners = retrofit.create(ApiListeners::class.java)

        val getUserInfoVoCall: Call<IntroResponseModel> = apiListeners!!.introBanner()

        getUserInfoVoCall.enqueue(object : Callback<IntroResponseModel?> {
            override fun onResponse(call: Call<IntroResponseModel?>, response: Response<IntroResponseModel?>) {
//                pb.dismiss();
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        setupOffersViewPager(response.body()!!.home_banner)
                    } else {
                        Toast.makeText(this@IntroActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<IntroResponseModel?>, t: Throwable) {
//                pb.dismiss();
                Toast.makeText(this@IntroActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupOffersViewPager(homeBanner: List<IntroResponseModel.HomeBanner>) {
        val adapter = IntroAdapter()
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
        }, 1000, 2000)

        introViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })
    }
}