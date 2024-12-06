package com.bangkit.pedulibumil.ui.home

import com.bangkit.pedulibumil.BuildConfig
import com.bangkit.pedulibumil.ui.home.ArticleResponse
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
