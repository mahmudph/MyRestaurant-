package com.example.restaurantreview.service

import com.example.restaurantreview.models.PostReviewResponse
import com.example.restaurantreview.models.RestaurantResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("detail/{id}")
    fun getDetailRestaurant( @Path("id") id: String ): Call<RestaurantResponse>

    @FormUrlEncoded
    @Headers("Authorization: token 12345")
    @POST("review")
    fun postReview(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("review") review : String,
    ): Call<PostReviewResponse>
}