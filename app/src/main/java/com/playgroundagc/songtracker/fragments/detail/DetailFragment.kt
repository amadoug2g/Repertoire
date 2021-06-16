package com.playgroundagc.songtracker.fragments.detail

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.data.SongStatus
import com.playgroundagc.songtracker.databinding.FragmentDetailBinding
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import org.jetbrains.anko.support.v4.toast

class DetailFragment : Fragment() {
    private val args by navArgs<DetailFragmentArgs>()

    companion object {
        private lateinit var binding: FragmentDetailBinding
        private lateinit var viewModel: SongViewModel
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SongViewModel::class.java)
        setHasOptionsMenu(true)

        viewModel.assignSong(args.currentSong)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        viewModel.currentSong.observe(viewLifecycleOwner, { song ->
            binding.song = song
        })

        setCurrentSongDetail()

        binding.buttonSongUpdate.setOnClickListener {
            updateSong(viewModel.currentSong.value!!)
        }

        binding.buttonSongUpdateCancel.setOnClickListener {
            setCurrentSongDetail()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.song_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.song_update_menu_btn -> {
                updateCheck(true)
            }
            R.id.song_delete_menu_btn -> {
                deleteAlert()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //endregion

    //region Song Detail Setup
    private fun setCurrentSongDetail() {
        setHasOptionsMenu(true)
        binding.songLayoutDetail.visibility = View.VISIBLE
        binding.songLayoutUpdate.visibility = View.GONE
    }
    //endregion

    //region Song Update Setup
    private fun setCurrentSongUpdate() {
        setHasOptionsMenu(false)
        binding.songLayoutDetail.visibility = View.GONE
        binding.songLayoutUpdate.visibility = View.VISIBLE
    }

    private fun loadStatusUpdateSpinner() {
        binding.spinnerSongStatusUpdate.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_layout_file, SongStatus.values())
    }

    private fun setStatusSelect(song: Song) {
        binding.spinnerSongStatusUpdate.setSelection(when (song.status) {
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

    //region Update Option
    private fun updateCheck(updating: Boolean) {
        when (updating) {
            true -> {
                setCurrentSongUpdate()
                loadStatusUpdateSpinner()
                setStatusSelect(viewModel.currentSong.value!!)
            }
            false -> {
                setCurrentSongDetail()
            }
        }
    }

    private fun updateSong(song: Song) {
        val name = binding.songNameUpdate.text.toString()
        val artist = binding.songArtistUpdate.text.toString()
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
            val updatedSong = Song(song.id, name, artist, status)

            viewModel.assignSong(updatedSong)
            viewModel.updateSong()

            toast("${updatedSong.name} updated")

            updateCheck(false)
        } else {
            toast("Fill all fields first!")
        }
    }

    private fun inputCheck(name: String, artist: String) : Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(artist))
    }
    //endregion

    //region Delete Option
    private fun deleteAlert() {
        val titleAlert = getString(R.string.delete_song) + " ${viewModel.currentSong.value?.name}"
        val messageAlert = getString(R.string.delete_song_message) + " ${viewModel.currentSong.value?.name}"

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleAlert)
            .setMessage(getString(R.string.delete_song_message))
            .setPositiveButton(R.string.confirm_message) { _: DialogInterface, _: Int ->
                deleteSong()
            }
            .setNegativeButton(R.string.cancel_message) { _: DialogInterface, _: Int ->
            }.show()
    }

    private fun deleteSong() {
        viewModel.deleteSong()
        requireActivity().onBackPressed()
        toast("Song deleted successfully !")
    }
    //endregion
}