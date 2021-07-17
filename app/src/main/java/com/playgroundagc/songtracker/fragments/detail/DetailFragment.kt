package com.playgroundagc.songtracker.fragments.detail

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.playgroundagc.songtracker.R
import com.playgroundagc.songtracker.activities.MainActivity
import com.playgroundagc.songtracker.activities.VideoActivity
import com.playgroundagc.songtracker.databinding.FragmentDetailBinding
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.model.SongCategory
import com.playgroundagc.songtracker.model.SongStatus
import com.playgroundagc.songtracker.util.Authentication
import com.playgroundagc.songtracker.viewmodel.SongViewModel
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast


class DetailFragment : Fragment() {
    private val args by navArgs<DetailFragmentArgs>()

    companion object {
        private lateinit var binding: FragmentDetailBinding
        private var viewModel: SongViewModel = MainActivity.viewModel
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = ViewModelProvider(this).get(SongViewModel::class.java)
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
        assignTab()

        binding.buttonSongUpdate.setOnClickListener {
            updateSong(viewModel.currentSong.value!!)
        }

        binding.buttonSongUpdateCancel.setOnClickListener {
            setCurrentSongDetail()
        }

        //region Navigation transition
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 300
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 300
        }
        //endregion

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
                        player.cueVideo(getVideoID())
                        player.setShowFullscreenButton(false)
//                        player.setOnFullscreenListener {
//                            navigate()
//                        }
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

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.song_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.song_play_menu_btn -> {
                navigate()
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
        binding.songLayoutDetail.visibility = View.VISIBLE
        binding.songLayoutUpdate.visibility = View.GONE
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

        viewModel.assignTabSelected(tabSelected)
    }
    //endregion

    //region Song Update Setup
    /**
     * Show update layout
     * */
    private fun setCurrentSongUpdate() {
        setHasOptionsMenu(false)
        binding.songLayoutDetail.visibility = View.GONE
        binding.songLayoutUpdate.visibility = View.VISIBLE
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
    //endregion

    //region Youtube Player
    private fun navigate() {
        val intent = Intent(requireContext(), VideoActivity::class.java)
        intent.putExtra("videoID", intentSong())
        startActivity(intent)
    }

    private fun intentSong(): String {
        return when (viewModel.currentSong.value?.name) {
            "Mariage d'amour" -> {
                "O1EYacuAwMQ"
            }
            "Il Vento d'Oro" -> {
                "UMiW3G1USHg"
            }
            "Prelude in C" -> {
                "https://www.youtube.com/watch?v=frxT2qB1POQ&list=PL5xsjcJ2WpmV5UUnmpubRfZ3UhblXbEgx&index=2"
            }
            "changes" -> {
                "https://www.youtube.com/watch?v=NO8KIx8wsec&list="
            }
            else -> {
                "nothing"
            }
        }
    }

    private fun getVideoID(): String {
        val videoID = viewModel.currentSong.value?.link

        return if (videoID != null) {
            when (videoID.length) {
                0 -> {
                    ""
                }
                11 -> {
                    videoID
                }
                else -> {
                    videoID.getVideoID()
                }
            }
        } else {
            ""
        }
    }

    private fun String.getVideoID(): String {
        return when {
            this.length > 43 -> {
                this.removePrefix(this.substringBefore("=")).removePrefix("=")
                    .removeSuffix(this.substringAfter("&")).removeSuffix("&")
            }
            this.length == 28 -> {
                this.removePrefix(this.substringBeforeLast("/")).removePrefix("/")
            }
            else -> this.removePrefix(this.substringBefore("=")).removePrefix("=")
        }
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

        if (inputCheck(name, artist)) {
            val updatedSong = Song(song.id, name, artist, status, category, link)

            viewModel.assignSong(updatedSong)
            viewModel.updateSong()

            toast("${updatedSong.name} updated")

            updateCheck(false)
        } else {
            toast("Fill all fields first!")
        }
    }

    private fun inputCheck(name: String, artist: String): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(artist))
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