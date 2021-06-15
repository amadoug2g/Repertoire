package com.playgroundagc.songtracker.fragments.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.data.SongStatus
import com.playgroundagc.songtracker.databinding.FragmentUpdateBinding
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import org.jetbrains.anko.support.v4.toast

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    companion object {
        private lateinit var binding: FragmentUpdateBinding
        private lateinit var viewModel: SongViewModel
    }

    //region Override Methods
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
            R.layout.fragment_update,
            container,
            false
        )

        setCurrentSong()
        setupStatusSpinner()
        statusSelect()

        binding.buttonSongUpdate.setOnClickListener {
//            updateSong()
        }

        return binding.root
    }
    //endregion

    //region Song Setup
    private fun setCurrentSong() {
        binding.song = args
    }
    //endregion

    //region Spinner Setup
    private fun setupStatusSpinner() {
        binding.spinnerSongStatusUpdate.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_layout_file, SongStatus.values())
    }

    private fun statusSelect() {
        binding.spinnerSongStatusUpdate.setSelection(when (args.currentSong.status) {
            SongStatus.Not_Started -> {
                0
            }
            SongStatus.In_Progress -> {
                1
            }
            SongStatus.Learned -> {
                2
            }
        })
    }
    //endregion

    //region Update Song
    private fun updateSong() {
        val name = binding.songNameInputUpdate.text.toString()
        val artist = binding.songArtistInputUpdate.text.toString()
        val status = when (binding.spinnerSongStatusUpdate.selectedItemPosition) {
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

        if (inputCheck(name, artist)) {
            val updatedSong = Song(args.currentSong.id, name, artist, status)

            viewModel.updateSong(updatedSong)

            toast("${updatedSong.name} updated")

            findNavController().navigate(R.id.updateFragmentToDetailFragment)
        } else {
            toast("Fill all fields first!")
        }
    }
    //endregion

    //region Field Check
    private fun inputCheck(name: String, artist: String) : Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(artist))
    }
    //endregion
}