package com.playgroundagc.songtracker.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.databinding.FragmentAddBinding
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.model.SongCategory
import com.playgroundagc.songtracker.model.SongStatus
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import org.jetbrains.anko.support.v4.toast

class AddFragment : Fragment() {

    companion object {
        private lateinit var binding: FragmentAddBinding
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
            R.layout.fragment_add,
            container,
            false
        )

        setupStatusSpinner()

        binding.buttonSongAdd.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }
    //endregion

    //region Spinner Setups
    private fun setupStatusSpinner() {
        binding.spinnerSongStatusAdd.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_layout_file, SongStatus.values())

        binding.spinnerSongCategoryAdd.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_layout_file, SongCategory.values())
    }
    //endregion

    //region Add Song
    private fun insertDataToDatabase() {
        val name = binding.songNameInputAdd.text.toString()
        val artist = binding.songArtistInputAdd.text.toString()
        val status = binding.spinnerSongStatusAdd.selectedItem as SongStatus
        val category = binding.spinnerSongCategoryAdd.selectedItem as SongCategory
        val link = binding.songLinkInputAdd.text.toString()

        if (inputCheck(name, artist)) {
            val song = Song(0, name, artist, status, category, link)

            viewModel.addSong(song)

            toast("${song.name} added")

            requireActivity().onBackPressed()
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