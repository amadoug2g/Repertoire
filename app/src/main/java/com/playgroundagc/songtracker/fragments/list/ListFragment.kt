package com.playgroundagc.songtracker.fragments.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.google.android.material.tabs.TabLayout
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.activities.MainActivity
import com.playgroundagc.songtracker.activities.MainActivity.Companion.navController
import com.playgroundagc.songtracker.databinding.FragmentListBinding
import com.playgroundagc.songtracker.extension.copyToClipboard
import com.playgroundagc.songtracker.extension.setAllEnabled
import com.playgroundagc.songtracker.extension.setInactive
import com.playgroundagc.songtracker.fragments.list.adapters.ListAdapter
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber

class ListFragment : Fragment() {

    companion object {
        private lateinit var binding: FragmentListBinding
        private var viewModel: SongViewModel = MainActivity.viewModel
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.viewModel = viewModel

        binding.floatingActionButton.setOnClickListener {
            navigate()
        }

        binding.songCount.okInfoBtn.setOnClickListener {
            binding.songCount.songCountLayout.visibility = View.GONE
            binding.listLayout.setAllEnabled(false)
            binding.floatingActionButton.setInactive(true)
        }

        binding.songCount.copyInfoBtn.setOnClickListener {
            copySongsToClipboard()
        }

        setUpRecyclerView(0)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setUpRecyclerView(tab!!.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.recyclerview.smoothScrollToPosition(0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
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
            R.id.song_info_menu_btn -> {
                songInfo()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("LogNotTimber")
    override fun onResume() {
        super.onResume()

        try {
            binding.tabLayout.getTabAt(viewModel.tabSelect.value!!)!!.select()
        } catch (e: Exception) {
            Timber.e("Error: $e")
            Log.e("LIST", "Error: ${viewModel.tabSelect.value}")
        }

        setUpRecyclerView(viewModel.tabSelect.value)
    }

    override fun onStop() {
        super.onStop()

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.change_image_transform)
    }
    //endregion

    //region Song Info
    private fun songInfo() {
        binding.songCount.songCountLayout.visibility = View.VISIBLE
        binding.listLayout.setAllEnabled(true)
        binding.floatingActionButton.setInactive(false)
    }
    //endregion

    //region RecyclerView
    private fun setUpRecyclerView(status: Int?) {
        if (status != null) {
//            viewModel.assignTabSelected(status)

            val adapter = ListAdapter()
            adapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

            lifecycleScope.launchWhenResumed {
                when (status) {
                    0 -> {
                        viewModel.readStatusNotStartedDataDESC.collect { value ->
                            adapter.setData(value)
                            viewModel.assignTabSelected(status)
                        }
                    }
                    1 -> {
                        viewModel.readStatusInProgressDataDESC.collect { value ->
                            adapter.setData(value)
                            viewModel.assignTabSelected(status)
                        }
                    }
                    else -> {
                        viewModel.readStatusLearnedDataDESC.collect { value ->
                            adapter.setData(value)
                            viewModel.assignTabSelected(2)
                        }
                    }
                }
            }
        } else {
            Timber.e("Tab position is null")
        }
    }
    //endregion

    //region Navigation
    private fun navigate() {
        navController.navigate(R.id.listFragmentToAddFragment)
    }
    //endregion

    //region Song Count
    private fun copySongsToClipboard() {
        toast("Copying to clipboard...")
        lifecycleScope.launchWhenResumed {
            var result = ""
            viewModel.readAllSongs.collect { value ->
                value.forEach {
                    val current = "${it.name}, ${it.artist}, ${it.status}\n"
                    result += current
                }

                requireContext().copyToClipboard(result)
            }
        }
    }
    //endregion
}