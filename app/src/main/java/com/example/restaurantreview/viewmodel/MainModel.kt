package com.example.restaurantreview.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreview.MainActivity
import com.example.restaurantreview.models.CustomerReviewsItem
import com.example.restaurantreview.models.PostReviewResponse
import com.example.restaurantreview.models.Restaurant
import com.example.restaurantreview.models.RestaurantResponse
import com.example.restaurantreview.service.ApiConfig
import retrofit2.Call
import retrofit2.Response

class MainModel: ViewModel() {
    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant

    private val _listReview = MutableLiveData<List<CustomerReviewsItem>>()
    val listReview: LiveData<List<CustomerReviewsItem>> = _listReview

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    init {
        findRestaurant()
    }

    private fun findRestaurant() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailRestaurant(RESTAURANT_ID)
        client.enqueue(object : retrofit2.Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()

                if(response.isSuccessful && responseBody != null) {
                    _restaurant.value = responseBody?.restaurant
                    _listReview.value = responseBody?.restaurant.customerReviews
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun postReview(text: String) {

        _isLoading.value = true
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Mahmud",text)

        client.enqueue(object : retrofit2.Callback<PostReviewResponse> {

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}", )
            }

            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {

                _isLoading.value = false
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    _listReview.value = responseBody.customerReviews

                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }
        })
    }
}