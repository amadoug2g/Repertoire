package com.playgroundagc.songtracker.fragments.add

import android.content.Context
import android.graphics.ColorSpace
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.data.SongStatus
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import com.playgroundagc.songtracker.databinding.FragmentAddBinding
import org.jetbrains.anko.support.v4.toast


class AddFragment : Fragment() {

    companion object {
        private lateinit var binding: FragmentAddBinding
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add,
            container,
            false
        )

        binding.spinnerSongStatusAdd.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_layout_file, SongStatus.values())

        binding.buttonSongAdd.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    private fun insertDataToDatabase() {
        val name = binding.songNameInputAdd.text.toString()
        val artist = binding.songArtistInputAdd.text.toString()
        val status = when (binding.spinnerSongStatusAdd.selectedItemPosition) {
            0 -> {
                SongStatus.Not_Started
            }
            1 -> {
                SongStatus.In_Progress
            }
            else -> {
                SongStatus.Learned
            }
        }
        Log.i("Console Add"," name = $name")
        Log.i("Console Add"," artist = $artist")
        Log.i("Console Add"," status = $status")

        if (checkFields(name, artist)) {
            val song = Song(0, name, artist, status)

            viewModel.addSong(song)

            toast("${song.name} added")

            findNavController().navigate(R.id.addFragmentToListFragment)
        } else {
            toast("Fill all fields first!")
        }
    }

    private fun checkFields(name: String, artist: String) : Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(artist))
    }
}