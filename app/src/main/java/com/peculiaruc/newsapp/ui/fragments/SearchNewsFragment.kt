package com.peculiaruc.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.peculiaruc.newsapp.R
import com.peculiaruc.newsapp.ui.activity.MainActivity
import com.peculiaruc.newsapp.util.Constant.Companion.DELAY_NEWS_SEARCH
import com.peculiaruc.newsapp.util.Resource
import com.peculiaruc.newsapp.viewModel.NewsVeiwModel
import com.peculiaruc.thenews.ui.adapter.NewsAdapter
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.*


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private lateinit var viewModel: NewsVeiwModel
    private lateinit var newAdapter: NewsAdapter

    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        //handle search functionality. Add a little delay while search. Use coroutine job for this

        var job: Job? = null
        searchIcon.addTextChangedListener { editsearch ->
            job?.cancel()
            job = MainScope().launch {
                delay(200L)
                editsearch?.let {
                    if (editsearch.toString().isNotEmpty()) {
                        viewModel.searchNews(editsearch.toString())
                    }
                }
            }
        }

        //call recent news lifedata
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is  Resource.Success ->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newAdapter.differ.submitList(newsResponse.articles)

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error has occured: $message" )
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
    }


    private fun showProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
    }

    //setup recyclerView
    private fun setUpRecyclerView() {
        newAdapter = NewsAdapter()
        recyclerView_search.apply {
            adapter = newAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}

