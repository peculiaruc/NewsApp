package com.peculiaruc.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.peculiaruc.newsapp.R
import com.peculiaruc.newsapp.ui.activity.MainActivity
import com.peculiaruc.newsapp.util.Resource
import com.peculiaruc.newsapp.viewModel.NewsVeiwModel
import com.peculiaruc.thenews.ui.adapter.NewsAdapter
import kotlinx.android.synthetic.main.fragment_recents_news.*


class RecentsNewsFragment : Fragment(R.layout.fragment_recents_news) {

    private lateinit var viewModel: NewsVeiwModel
    private lateinit var newsAdapter: NewsAdapter

    val TAG = "RecentNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        //put the article into a bundle and attach the bundle it to the navigation component
        //the navigation component will then handle the transition and pass the argument to the articlefragment
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_recentsNewsFragment_to_articleFragment,
                bundle
            )
        }

        //call recent news lifedata
        viewModel.recentNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is  Resource.Success ->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

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
        pageProgressBar.visibility = View.VISIBLE
    }


    private fun showProgressBar() {
        pageProgressBar.visibility = View.VISIBLE
    }

    //setup recyclerView
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        recyclerView_recent.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}