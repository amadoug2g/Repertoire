package com.playgroundagc.songtracker.fragments.detail

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.databinding.FragmentDetailBinding
import com.playgroundagc.songtracker.fragments.list.ListFragmentDirections
import org.jetbrains.anko.support.v4.toast

class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()

    companion object {
        private lateinit var binding: FragmentDetailBinding
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
            R.layout.fragment_detail,
            container,
            false
        )

        setCurrentSong()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.song_add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.song_update_menu_btn -> {
                updateSong()
            }
            R.id.song_delete_menu_btn -> {
                deleteAlert()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //endregion

    //region Song Setup
    private fun setCurrentSong() {
        binding.song = args
    }
    //endregion

    //region Update Option
    private fun updateSong() {
        val action = DetailFragmentDirections.detailFragmentToUpdateFragment(args.currentSong)
        findNavController().navigate(action)
    }
    //endregion

    //region Delete Option
    private fun deleteAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_song))
            .setMessage(getString(R.string.delete_song_message))
            .setPositiveButton(R.string.confirm_message) { _: DialogInterface, _: Int ->
                deleteSong()
            }
            .setNegativeButton(R.string.cancel_message) { _: DialogInterface, _: Int ->
            }.show()
    }

    private fun deleteSong() {
        toast("Deleting song...")
//                        findNavController().navigate(R.id.detailFragmentToListFragment)
    }
    //endregion
}