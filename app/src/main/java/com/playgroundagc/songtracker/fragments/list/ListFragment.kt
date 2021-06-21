package com.playgroundagc.songtracker.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
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
import timber.log.Timber
import java.lang.Exception

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
                        viewModel.assignSelection(tab.position)
                    }
                    1 -> {
                        setUpRecyclerViewInProgress()
                        viewModel.assignSelection(tab.position)
                    }
                    2 -> {
                        setUpRecyclerViewLearned()
                        viewModel.assignSelection(tab.position)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 300
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 300
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.song_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.song_sort_menu_btn -> {
//                sortSongs()
//            }
            R.id.song_sort_order_menu_btn -> {
                sortList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        try {
            binding.tabLayout.getTabAt(viewModel.tabSelect.value!!)?.select()
        } catch (e: Exception) {
            Timber.e("Error: $e")
        }

        when (viewModel.tabSelect.value) {
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

    override fun onStop() {
        super.onStop()

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.change_image_transform)
    }
    //endregion

    //region Ascending / Descending
    private fun sortList() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        setUpRecyclerViewNotStarted()
                        toast("1")
                    }
                    1 -> {
                        setUpRecyclerViewInProgress()
                        toast("2")
                    }
                    2 -> {
                        setUpRecyclerViewLearned()
                        toast("3")
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })
    }
    //endregion

    //region RecyclerView
    private fun setUpRecyclerViewNotStarted() {
        val adapter = ListAdapter()
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

//        try {
//            viewModel.assignNotStartedSelect(!viewModel.notStartedSelect.value!!)
//        } catch (e: Exception) {
//            Timber.e("Error: $e")
//        }

//        when (viewModel.notStartedSelect.value) {
//            true -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readStatusNotStartedDataDESC.collect { value ->
                        adapter.setData(value)
                    }
                }
//            }
//            false -> {
//                lifecycleScope.launchWhenResumed {
//                    viewModel.readStatusNotStartedDataASC.collect { value ->
//                        adapter.setData(value)
//                    }
//                }
//            }
//        }
    }

    private fun setUpRecyclerViewInProgress() {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

//        try {
//            viewModel.assignInProgressSelect(!viewModel.inProgressSelect.value!!)
//        } catch (e: Exception) {
//            Timber.e("Error: $e")
//        }

//        when (viewModel.inProgressSelect.value) {
//            true -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readStatusInProgressDataDESC.collect { value ->
                        adapter.setData(value)
                    }
                }
//            }
//            false -> {
//                lifecycleScope.launchWhenResumed {
//                    viewModel.readStatusInProgressDataASC.collect { value ->
//                        adapter.setData(value)
//                    }
//                }
//            }
//        }
    }

    private fun setUpRecyclerViewLearned() {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

//        try {
//            viewModel.assignLearnedSelect(!viewModel.learnedSelect.value!!)
//        } catch (e: Exception) {
//            Timber.e("Error: $e")
//        }
//
//        when (viewModel.learnedSelect.value) {
//            true -> {
                lifecycleScope.launchWhenResumed {
                    viewModel.readStatusLearnedDataDESC.collect { value ->
                        adapter.setData(value)
                    }
                }
//            }
//            false -> {
//                lifecycleScope.launchWhenResumed {
//                    viewModel.readStatusLearnedDataASC.collect { value ->
//                        adapter.setData(value)
//                    }
//                }
//            }
//        }
    }
    //endregion
}