package com.bangkit.test.ui.article

import com.bangkit.test.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {
    @GET("search.json")
    suspend fun getArticles(
        @Query("engine") engine: String = "google_news",
        @Query("q") query: String,
        @Query("hl") language: String = "id",
        @Query("api_key") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<ArticleResponse>
}
