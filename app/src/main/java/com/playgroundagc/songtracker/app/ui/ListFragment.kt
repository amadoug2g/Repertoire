package com.playgroundagc.songtracker.app.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.google.android.material.tabs.TabLayout
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.app.ui.MainActivity.Companion.navController
import com.playgroundagc.songtracker.databinding.FragmentListBinding
import com.playgroundagc.songtracker.extension.setAllEnabled
import com.playgroundagc.songtracker.extension.setInactive
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber

class ListFragment : Fragment() {

    private val viewModel by activityViewModels<SongViewModel>()
//    private lateinit var viewModel: SongViewModel

    companion object {
        private lateinit var binding: FragmentListBinding

        //        private var viewModel: SongViewModel = MainActivity.viewModel
        private lateinit var adapter: ListAdapter
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

//        viewModel = ViewModelProvider(requireActivity()).get(SongViewModel::class.java)

        try {
            Log.i("Test value", "${viewModel.tabSelect.value}")
        } catch (e: Exception) {
//            Log.e("Test value", "${viewModel.tabSelect.value}")
            Log.e("Test value", "Error: $e")
        }

        setUpAdapter()

        viewModel.currentSongList.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        binding.viewModel = viewModel

        binding.floatingActionButton.setOnClickListener {
            navigate()
        }

        binding.songCount.okInfoBtn.setOnClickListener {
            try {
                viewModel.countSongs()
                Log.i("Test value", "${viewModel.countLearnedSongs.value}")
            } catch (e: Exception) {
                Timber.e("Error: $e")
                Log.e("SongCount", "Error: $e")
            }
            displaySongCount(false)
        }

        binding.songCount.copyInfoBtn.setOnClickListener {
            copySongsToClipboard()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateRecyclerView(tab?.position)
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

    override fun onResume() {
        super.onResume()

        viewModel.countSongs()

        try {
            binding.tabLayout.getTabAt(viewModel.tabSelect.value!!)!!.select()
        } catch (e: Exception) {
            toast("Error: $e")
        }

        updateRecyclerView(viewModel.tabSelect.value)
    }

    override fun onStop() {
        super.onStop()

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.change_image_transform)
    }
    //endregion

    //region Song Info
    private fun songInfo() {
        displaySongCount(true)
    }
    //endregion

    //region RecyclerView
    private fun setUpAdapter() {
        adapter = ListAdapter()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        updateRecyclerView(0)
    }

    private fun updateRecyclerView(status: Int?) {
        if (status != null) {
            viewModel.getSongList(status)
        } else {
            toast("Tab position is null")
        }
    }
    //endregion

    //region Navigation
    private fun navigate() {
        navController.navigate(R.id.listFragmentToAddFragment)
    }
    //endregion

    //region Song Count
    private fun displaySongCount(displayCount: Boolean) {
        when (displayCount) {
            true -> {
                binding.songCount.songCountLayout.visibility = View.VISIBLE
                binding.listLayout.setAllEnabled(true)
                binding.floatingActionButton.setInactive(false)
            }
            false -> {
                binding.songCount.songCountLayout.visibility = View.GONE
                binding.listLayout.setAllEnabled(false)
                binding.floatingActionButton.setInactive(true)
            }
        }

    }

    private fun copySongsToClipboard() {
        toast("Copying...")
        viewModel.copySongs(requireContext())
        toast("Songs copied to clipboard!")
    }
    //endregion
}