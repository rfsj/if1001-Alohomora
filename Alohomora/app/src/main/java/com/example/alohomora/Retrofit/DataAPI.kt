package com.example.alohomora.Retrofit

import android.content.ClipData
import com.example.alohomora.Model.Item
import io.reactivex.Observable
import retrofit2.http.GET

interface DataAPI {
    @get:GET("posts")
    val posts:Observable<List<Item>>
}