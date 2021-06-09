package com.playgroundagc.songtracker.fragments.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import com.playgroundagc.songtracker.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collect

class ListFragment : Fragment() {

    companion object {
        private lateinit var binding: FragmentListBinding
        private lateinit var viewModel: SongViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SongViewModel::class.java)
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

    private fun setUpRecyclerView() {
        val adapter = ListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenResumed {
            viewModel.readAllData.collect { value ->
                adapter.setData(value)
//                value.forEach {
//                    Log.i("Console"," data = $it")
//                }
            }
        }
    }
}