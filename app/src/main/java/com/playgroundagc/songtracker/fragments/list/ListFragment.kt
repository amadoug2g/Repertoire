package com.playgroundagc.songtracker.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import com.playgroundagc.songtracker.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.support.v4.toast

class ListFragment : Fragment() {

    companion object {
        private lateinit var binding: FragmentListBinding
        private lateinit var viewModel: SongViewModel
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SongViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list,
            container,
            false
        )

        setUpRecyclerView()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.listFragmentToAddFragment)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.song_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.song_sort_menu_btn -> {
                sortSongs()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortSongs() {
        toast("Feature not implemented... yet")
    }
    //endregion

    //region RecyclerView
    private fun setUpRecyclerView() {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenResumed {
            viewModel.readAllData.collect { value ->
                adapter.setData(value)
            }
        }
    }
    //endregion
}