package com.excuseme.newsapp.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.excuseme.newsapp.R
import com.excuseme.newsapp.adpter.SearchResultAdapter
import com.excuseme.newsapp.api.ApiListeners
import com.excuseme.newsapp.constant.Constant
import com.excuseme.newsapp.model.News
import com.excuseme.newsapp.model.NewsBanner
import kotlinx.android.synthetic.main.activity_demo.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class Demo : AppCompatActivity() {

    private var newsBannerAdCurrentIndex = 0
    var isLoading = false
    private lateinit var apiListeners: ApiListeners
    private var totalPage = 0
    private var page = 1
    private lateinit var rvLayoutManager: LinearLayoutManager
    private lateinit var newsListAdapter: SearchResultAdapter
    private var regularNewsList: ArrayList<News.Data> = ArrayList()
    private var newsBannerList: ArrayList<NewsBanner> = ArrayList()
    private var trendingNewsList: ArrayList<News.Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        rvLayoutManager = LinearLayoutManager(this)
        rec_for_you.layoutManager = rvLayoutManager
        newsListAdapter = SearchResultAdapter(regularNewsList, trendingNewsList, this)
        rec_for_you.adapter = newsListAdapter

        //rec_for_you.addOnScrollListener(recyclerViewOnScrollListener)
        nsvRoot.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    //code to fetch more data for endless scrolling
                    if (!isLoading && totalPage >= page) {
                        pbNews.visibility = View.VISIBLE
                        getRegularNews()
                    }
                }
            }
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(100, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiListeners = retrofit.create(ApiListeners::class.java)
        getTrendingNews()
    }

//    private var recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
//        object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val visibleItemCount: Int = rvLayoutManager.childCount
//                val totalItemCount: Int = rvLayoutManager.itemCount
//                val firstVisibleItemPosition: Int = rvLayoutManager.findFirstVisibleItemPosition()
//                if (!isLoading && totalPage >= page) {
//                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                    ) {
//                        pbNews.visibility = View.VISIBLE
//                        getRegularNews()
//                    }
//                }
//            }
//        }

    fun getRegularNews() {
        isLoading = true
        val getUserInfoVoCall: Call<News> = apiListeners.getNews(
            "",
            "",
            "",
            "",
            "",
            "",
            Constant.get_sp(this@Demo, "mobile"),
            page
        )
        getUserInfoVoCall.enqueue(object : Callback<News?> {
            override fun onResponse(call: Call<News?>, response: Response<News?>) {
                pbNews.visibility = View.INVISIBLE
                pbTop.visibility = View.GONE
                isLoading = false
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        totalPage = response.body()!!.total_page
                        page = response.body()!!.current_page.toInt()
                        page++

                        if (response.body()!!.news_banner.isNotEmpty()) {
                            newsBannerList = java.util.ArrayList()
                            newsBannerList.addAll(response.body()!!.news_banner)
                        }

                        for (i in response.body()!!.data.indices) {
                            if (i == 2 && page == 2) {
                                val obj = News.Data()
                                obj.view_type = 1
                                regularNewsList.add(obj)
                            }

                            if (newsBannerList.isNotEmpty()) {
                                if (i == 1) {
                                    if (newsBannerList.size - 1 < newsBannerAdCurrentIndex) {
                                        newsBannerAdCurrentIndex = 0
                                    }
                                    val obj = News.Data()
                                    obj.up_pro_img =
                                        newsBannerList[newsBannerAdCurrentIndex].up_pro_img
                                    obj.url = newsBannerList[newsBannerAdCurrentIndex].url
                                    obj.view_type = 2
                                    regularNewsList.add(obj)
                                    newsBannerAdCurrentIndex += 1
                                }
                            }

                            val obj = News.Data()
                            obj.isbookmark = response.body()!!.data[i].isbookmark
                            obj.author = response.body()!!.data[i].author
                            obj.cid = response.body()!!.data[i].cid
                            obj.description = response.body()!!.data[i].description
                            obj.city = response.body()!!.data[i].city
                            obj.id = response.body()!!.data[i].id
                            obj.keywords = response.body()!!.data[i].keywords
                            obj.name = response.body()!!.data[i].name
                            obj.pdate = response.body()!!.data[i].pdate
                            obj.slider = response.body()!!.data[i].slider
                            obj.status = response.body()!!.data[i].status
                            obj.trending_news = response.body()!!.data[i].trending_news
                            obj.up_pro_img = response.body()!!.data[i].up_pro_img
                            obj.upload_by = response.body()!!.data[i].upload_by
                            obj.user_mobile = response.body()!!.data[i].user_mobile
                            obj.author_img = response.body()!!.data[i].author_img
                            regularNewsList.add(obj)
                        }
                        newsListAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@Demo, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
                pbNews.visibility = View.INVISIBLE
                isLoading = false
                Toast.makeText(this@Demo, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTrendingNews() {
        val getUserInfoVoCall = apiListeners.getNews(
            "",
            "",
            "",
            "yes",
            "",
            "",
            Constant.get_sp(this, "mobile"),
            page
        )
        getUserInfoVoCall.enqueue(object : Callback<News?> {
            override fun onResponse(call: Call<News?>, response: Response<News?>) {
                if (response.body() != null) {
                    if (response.body()!!.success) {
                        for (i in response.body()!!.data.indices) {
                            val data = response.body()!!.data[i]
                            data.setnumRecords((i + 1).toString() + "/" + response.body()!!.data.size)
                            trendingNewsList.add(data)
                        }
                    } else {
                        Toast.makeText(this@Demo, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                }
                getRegularNews()
                pbNews.visibility = View.GONE
                pbTop.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
                Toast.makeText(this@Demo, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}