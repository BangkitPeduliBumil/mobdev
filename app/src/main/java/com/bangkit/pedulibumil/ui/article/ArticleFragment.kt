package com.bangkit.pedulibumil.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.pedulibumil.databinding.FragmentArticleBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://serpapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ArticleService::class.java)

        // Setup ViewModel with Factory
        val factory = ArticleViewModelFactory(service)
        viewModel = ViewModelProvider(this, factory)[ArticleViewModel::class.java]

        setupRecyclerView()

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                binding.rvArticles.adapter = ArticleAdapter(articles) { article ->
                    // Handle item click
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.fetchArticles("hamil, ibu, bayi")
    }

    private fun setupRecyclerView() {
        binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticles.setHasFixedSize(true)
    }
}
