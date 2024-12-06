package com.bangkit.pedulibumil.ui.home

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("news_results")
	val newsResults: List<NewsResultsItem>,

	@field:SerializedName("menu_links")
	val menuLinks: List<MenuLinksItem>,

	@field:SerializedName("search_metadata")
	val searchMetadata: SearchMetadata,

	@field:SerializedName("search_parameters")
	val searchParameters: SearchParameters
)

data class Source(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("authors")
	val authors: List<String>
)

data class SearchParameters(

	@field:SerializedName("q")
	val q: String,

	@field:SerializedName("hl")
	val hl: String,

	@field:SerializedName("engine")
	val engine: String
)

data class MenuLinksItem(

	@field:SerializedName("topic_token")
	val topicToken: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("serpapi_link")
	val serpapiLink: String
)

data class NewsResultsItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("thumbnail")
	val thumbnail: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("position")
	val position: Int,

	@field:SerializedName("source")
	val source: Source,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("stories")
	val stories: List<StoriesItem>,

	@field:SerializedName("topic_token")
	val topicToken: String,

	@field:SerializedName("serpapi_link")
	val serpapiLink: String
)

data class StoriesItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("thumbnail")
	val thumbnail: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("position")
	val position: Int,

	@field:SerializedName("source")
	val source: Source,

	@field:SerializedName("title")
	val title: String
)

data class SearchMetadata(

	@field:SerializedName("raw_html_file")
	val rawHtmlFile: String,

	@field:SerializedName("google_news_url")
	val googleNewsUrl: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("processed_at")
	val processedAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("total_time_taken")
	val totalTimeTaken: Any,

	@field:SerializedName("json_endpoint")
	val jsonEndpoint: String,

	@field:SerializedName("status")
	val status: String
)