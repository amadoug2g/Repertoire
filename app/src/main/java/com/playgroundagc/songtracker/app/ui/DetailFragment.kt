package com.playgroundagc.songtracker.app.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.databinding.FragmentDetailBinding
import com.playgroundagc.songtracker.domain.Song
import com.playgroundagc.songtracker.domain.SongCategory
import com.playgroundagc.songtracker.domain.SongStatus
import com.playgroundagc.songtracker.extension.getVideo
import com.playgroundagc.songtracker.extension.inputCheck
import com.playgroundagc.songtracker.extension.visibility
import com.playgroundagc.songtracker.util.Authentication
import org.jetbrains.anko.support.v4.toast

class DetailFragment : Fragment() {
    private val args by navArgs<DetailFragmentArgs>()

    companion object {
        private lateinit var binding: FragmentDetailBinding
        private var viewModel: SongViewModel = MainActivity.viewModel
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

            binding.noLinkAvailableLayout.visibility = if (song.link == "") View.VISIBLE else View.GONE

            //region Youtube Player
            val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.youtube_fragment, youTubePlayerFragment)
            transaction.commit()

            youTubePlayerFragment.initialize(
                Authentication.API_KEY,
                object : YouTubePlayer.OnInitializedListener {

                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider?,
                        player: YouTubePlayer?,
                        wasRestored: Boolean
                    ) {
                        if (!wasRestored && player != null) {
                            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                            player.setFullscreen(false)
                            player.cueVideo(getVideo(song.link))
                            player.setShowFullscreenButton(false)
                        }
                    }

                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider?,
                        error: YouTubeInitializationResult?
                    ) {
                        toast("Error: $error")
                    }
                })
            //endregion
        })

        setCurrentSongDetail()

        binding.buttonSongUpdate.setOnClickListener {
            updateSong(viewModel.currentSong.value!!)
        }

        binding.buttonSongUpdateCancel.setOnClickListener {
            setCurrentSongDetail()
        }

        binding.addALinkText.setOnClickListener {
            addYoutubeLink()
        }

        //region Navigation transition
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 300
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 300
        }
        //endregion

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.song_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.song_play_menu_btn -> {
                if (viewModel.currentSong.value?.link!!.isNotEmpty()) {
                    navigate()
                } else {
                    val titleAlert = "Youtube link missing"
                    val messageAlert = "This song has no youtube link! Add one to access fullscreen"

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(titleAlert)
                        .setMessage(messageAlert)
                        .setPositiveButton(R.string.add_a_link_text) { _: DialogInterface, _: Int ->
                            addYoutubeLink()
                        }
                        .setNegativeButton(R.string.ok_message) { _: DialogInterface, _: Int ->
                        }.show()
                }
            }
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
    /**
     * Show detail layout & set current [Song] details
     * */
    private fun setCurrentSongDetail() {
        setHasOptionsMenu(true)
        binding.songLayoutDetail.visibility(true)
        binding.songLayoutUpdate.visibility(false)
    }

    private fun assignTab() {
        val tabSelected = when (viewModel.currentSong.value?.status) {
            SongStatus.Not_Started -> {
                0
            }
            SongStatus.In_Progress -> {
                1
            }
            else -> {
                2
            }
        }

//        viewModel.assignTabSelected(tabSelected)
    }
    //endregion

    //region Song Update Setup
    /**
     * Show update layout
     * */
    private fun setCurrentSongUpdate() {
        setHasOptionsMenu(false)
        binding.songLayoutDetail.visibility(false)
        binding.songLayoutUpdate.visibility(true)
    }

    private fun loadStatusUpdateSpinner() {
        binding.spinnerSongStatusUpdate.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_layout_file, SongStatus.values())
        binding.spinnerSongCategoryUpdate.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_layout_file, SongCategory.values())
    }

    private fun setStatusSelect(song: Song) {
        binding.spinnerSongStatusUpdate.setSelection(
            when (song.status) {
                SongStatus.Not_Started -> {
                    0
                }
                SongStatus.In_Progress -> {
                    1
                }
                SongStatus.Learned -> {
                    2
                }
            }
        )
    }

    private fun setCategorySelect(song: Song) {
        binding.spinnerSongCategoryUpdate.setSelection(
            when (song.category) {
                SongCategory.Music -> {
                    0
                }
                SongCategory.Movie_Shows -> {
                    1
                }
                SongCategory.Game -> {
                    2
                }
                SongCategory.Anime -> {
                    3
                }
            }
        )
    }

    private fun addYoutubeLink() {
        updateCheck(true)
        binding.songLinkLayoutUpdate.requestFocus()
    }
    //endregion

    //region Youtube Player Option
    private fun navigate() {
        val intent = Intent(requireContext(), VideoActivity::class.java)
        intent.putExtra("videoID", viewModel.currentSong.value?.link)
        startActivity(intent)
    }
    //endregion

    //region Update Option
    private fun updateCheck(updating: Boolean) {
        when (updating) {
            true -> {
                setCurrentSongUpdate()
                loadStatusUpdateSpinner()
                setStatusSelect(viewModel.currentSong.value!!)
                setCategorySelect(viewModel.currentSong.value!!)
            }
            false -> {
                setCurrentSongDetail()
            }
        }
    }

    private fun updateSong(song: Song) {
        val name = binding.songNameUpdate.text.toString()
        val artist = binding.songArtistUpdate.text.toString()
        val status = binding.spinnerSongStatusUpdate.selectedItem as SongStatus
        val category = binding.spinnerSongCategoryUpdate.selectedItem as SongCategory
        val link = binding.songLinkUpdate.text.toString()

        if (name.inputCheck() && artist.inputCheck()) {
            val updatedSong = Song(song.id, name, artist, status, category)

            if (link.inputCheck()) updatedSong.link = link

            viewModel.updateSong(updatedSong)

            toast("${updatedSong.name} updated")

            updateCheck(false)
        } else {
            toast("Fill all fields first!")
        }
    }
    //endregion

    //region Delete Option
    private fun deleteAlert() {
        val titleAlert = getString(R.string.delete_song) + " ${viewModel.currentSong.value?.name}"
        val messageAlert = getString(R.string.delete_song_message)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleAlert)
            .setMessage(messageAlert)
            .setPositiveButton(R.string.confirm_message) { _: DialogInterface, _: Int ->
                deleteSong()
            }
            .setNegativeButton(R.string.cancel_message) { _: DialogInterface, _: Int ->
            }.show()
    }

    private fun deleteSong() {
        val songName = viewModel.currentSong.value?.name ?: "Song"
        viewModel.deleteSong()
        requireActivity().onBackPressed()
        toast("$songName deleted successfully !")
    }
    //endregion
}