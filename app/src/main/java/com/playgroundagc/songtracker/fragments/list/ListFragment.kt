package com.playgroundagc.songtracker.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.playgroundagc.songtracker.MainActivity
import com.playgroundagc.songtracker.MainActivity.Companion.navController
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import com.playgroundagc.songtracker.databinding.FragmentListBinding
import com.playgroundagc.songtracker.fragments.list.adapters.ListAdapter
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
//        setHasOptionsMenu(true)
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

        binding.floatingActionButton.setOnClickListener {
            navController.navigate(R.id.listFragmentToAddFragment)
        }
        setUpRecyclerViewNotStarted()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        setUpRecyclerViewNotStarted()
                    }
                    1 -> {
                        setUpRecyclerViewInProgress()
                    }
                    2 -> {
                        setUpRecyclerViewLearned()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }


        })

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
            R.id.song_sort_order_menu_btn -> {
                sortSongsByAlpha()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //endregion

    //region Sort Option
    private fun sortSongs() {
        val items = arrayOf("By Id", "By Name", "By Status")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.sort_song))
            .setItems(items) { _, which ->
                when (which) {
                    0 -> {
                        setUpRecyclerViewASC(0)
                    }
                    1 -> {
                        setUpRecyclerViewASC(1)
                    }
                    else -> {
                        setUpRecyclerViewASC(2)
                    }
                }
            }
            .show()
    }
    //endregion

    //region Sort By Alpha Option
    private fun sortSongsByAlpha() {
        viewModel.assignAlphaSelect(!viewModel.alphaSelect.value!!)
        when (viewModel.sortSelect.value) {
            0 -> {
                if (viewModel.alphaSelect.value!!)
                    setUpRecyclerViewASC(0)
                else
                    setUpRecyclerViewDESC(0)
            }
            1 -> {
                if (viewModel.alphaSelect.value!!)
                    setUpRecyclerViewASC(1)
                else
                    setUpRecyclerViewDESC(1)

            }
            else -> {
                if (viewModel.alphaSelect.value!!)
                    setUpRecyclerViewASC(2)
                else
                    setUpRecyclerViewDESC(2)
            }
        }
    }
    //endregion

    //region RecyclerView
    private fun setUpRecyclerViewASC(sortSelect: Int) {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        viewModel.assignSelection(sortSelect)

        when (sortSelect) {
            0 -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readAllDataASC.collect { value ->
                        adapter.setData(value)
                    }
                }
            }
            1 -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readAllDataByNameASC.collect { value ->
                        adapter.setData(value)
                    }
                }
            }
            else -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readAllDataByStatusASC.collect { value ->
                        adapter.setData(value)
                    }
                }
            }
        }
    }

    private fun setUpRecyclerViewDESC(sortSelect: Int) {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        viewModel.assignSelection(sortSelect)

        when (sortSelect) {
            0 -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readAllDataDESC.collect { value ->
                        adapter.setData(value)
                    }
                }
            }
            1 -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readAllDataByNameDESC.collect { value ->
                        adapter.setData(value)
                    }
                }
            }
            else -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readAllDataByStatusDESC.collect { value ->
                        adapter.setData(value)
                    }
                }
            }
        }
    }

    private fun setUpRecyclerViewNotStarted() {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenResumed {
            viewModel.readStatusNotStartedDataDESC.collect { value ->
                adapter.setData(value)
            }
        }
    }

    private fun setUpRecyclerViewInProgress() {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenResumed {
            viewModel.readStatusInProgressDataDESC.collect { value ->
                adapter.setData(value)
            }
        }
    }

    private fun setUpRecyclerViewLearned() {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenResumed {
            viewModel.readStatusLearnedDataDESC.collect { value ->
                adapter.setData(value)
            }
        }
    }
    //endregion
}