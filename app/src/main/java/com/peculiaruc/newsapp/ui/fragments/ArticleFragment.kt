package com.peculiaruc.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peculiaruc.newsapp.R
import com.peculiaruc.newsapp.ui.activity.MainActivity
import com.peculiaruc.newsapp.viewModel.NewsVeiwModel


class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var viewModel: NewsVeiwModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel


    }
}