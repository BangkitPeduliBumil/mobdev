package com.bangkit.test.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ArticleViewModel(private val service: ArticleService) : ViewModel() {

    private val _articles = MutableLiveData<List<NewsResultsItem>>()
    val articles: LiveData<List<NewsResultsItem>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchArticles(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = service.getArticles(query = query)
                if (response.isSuccessful) {
                    _articles.value = response.body()?.newsResults ?: emptyList()
                } else {
                    _articles.value = emptyList()
                }
            } catch (e: Exception) {
                _articles.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
