package com.example.alohomora.Retrofit

import com.example.alohomora.Model.Item
import io.reactivex.Observable
import retrofit2.http.GET

interface DataAPI {
    @get:GET("customers/customers")
    val posts:Observable<List<Item>>
}