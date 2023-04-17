package com.kosim97.mulgaTalkTalk.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var favoriteBinding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val favoriteAdapter = FavoriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentFavoriteBinding.inflate(inflater).also {
            favoriteBinding = it
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = favoriteViewModel
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView() {
        favoriteBinding.favoriteRv.also {
            it.adapter = favoriteAdapter
            it.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            it.addItemDecoration(DividerItemDecoration(context, 1))
        }
    }

    private fun initObserver() {
        favoriteViewModel.getAll()
        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.getRoomData().collectLatest {
                    favoriteAdapter.submitData(it)
                }
            }
        }
    }
}