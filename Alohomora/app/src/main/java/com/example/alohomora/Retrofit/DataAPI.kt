package com.example.alohomora.Retrofit

import android.content.ClipData
import io.reactivex.Observable
import retrofit2.http.GET

interface DataAPI {
    @get:GET("posts")
    val posts:Observable<List<ClipData.Item>>
}