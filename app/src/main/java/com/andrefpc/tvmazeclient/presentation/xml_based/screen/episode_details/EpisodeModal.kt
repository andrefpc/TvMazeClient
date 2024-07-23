package com.andrefpc.tvmazeclient.presentation.xml_based.screen.episode_details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.andrefpc.tvmazeclient.databinding.ModalEpisodeBinding
import com.andrefpc.tvmazeclient.presentation.model.EpisodeViewState
import com.andrefpc.tvmazeclient.util.ScreenUtil
import com.andrefpc.tvmazeclient.util.extensions.ImageViewExtensions.loadImage
import com.andrefpc.tvmazeclient.util.extensions.StringExtensions.removeHtmlTags

/**
 * Modal for episode info
 */
class EpisodeModal : DialogFragment() {
    companion object {
        const val EPISODE = "episode"

        /**
         * Initialize the instance for a fragment
         * (It's recommended to prevent memory leak)
         */
        fun newInstance(
            episode: EpisodeViewState
        ) = EpisodeModal().apply {
            arguments = bundleOf(EPISODE to episode)
        }
    }

    private var _binding: ModalEpisodeBinding? = null
    private val binding get() = _binding!!

    /**
     * Lifecycle method used to create the fragment view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = ModalEpisodeBinding.inflate(
            layoutInflater,
            null,
            false
        )
        activity?.let {
            val width = ScreenUtil.getScreenWidth(it) - ScreenUtil.dpToPX(40, it)
            binding.card.layoutParams =
                LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
        return binding.root
    }

    /**
     * Lifecycle method called when the fragment is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    /**
     * Init the views
     */
    private fun initViews() {
        val episode = arguments?.get(EPISODE) as EpisodeViewState

        binding.apply {
            this.image.loadImage(episode.image)
            this.name.text = episode.name
            this.episode.text = episode.seasonEpisode
            this.date.text = episode.airdate
            this.time.text = episode.time
            this.summary.text = episode.summary.removeHtmlTags()
        }
    }

    /**
     * Lifecycle method called when the fragment is destroyed
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}