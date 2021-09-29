package com.example.restaurantreview

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.restaurantreview.databinding.ActivityMainBinding
import com.example.restaurantreview.models.CustomerReviewsItem
import com.example.restaurantreview.models.PostReviewResponse
import com.example.restaurantreview.models.Restaurant
import com.example.restaurantreview.models.RestaurantResponse
import com.example.restaurantreview.service.ApiConfig
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        findRestaurant()

        binding.btnSend.setOnClickListener { view ->
            postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }

    private fun postReview(text: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding",text)

        client.enqueue(object : retrofit2.Callback<PostReviewResponse> {
            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}", )
            }

            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null) {
                    setReviewData(responseBody.customerReviews)
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}", )
                }
            }
        })
    }

    private fun findRestaurant() {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailRestaurant(RESTAURANT_ID)


        client.enqueue(object : retrofit2.Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                showLoading(false)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        setRestaurantData(responseBody.restaurant)
                        setReviewData(responseBody.restaurant.customerReviews)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    private fun setRestaurantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description

        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPicture)
    }

    private fun setReviewData(consumerReview: List<CustomerReviewsItem>) {
        val listReview = ArrayList<String>();

        for(review in consumerReview) {
            listReview.add(
                """
                ${review.review}
                - ${review.name}
                """.trimIndent()
            )
        }

        val adapter = ReviewAdapter(listReview)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")

    }
    private fun showLoading(value: Boolean) {
        if(value) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}