package es.salvaaoliiver.secondevproject.main.menu.multimedia

import android.media.MediaPlayer
import android.widget.MediaController
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentMultimediaBinding
import es.salvaaoliiver.secondevproject.main.bottombar.home.HomeFragment

class MultimediaFragment : Fragment() {
    private lateinit var binding: FragmentMultimediaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.menuFragmentoContainer, HomeFragment())
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMultimediaBinding.inflate(inflater, container, false)


        binding.btnLoad.setOnClickListener {
            if (!binding.inputText.text.isNullOrEmpty()){
                val video = getVideo(binding.inputText.text.toString())
                if (video != 0){
                    binding.videoView.setVideoURI(
                        Uri.parse("android.resource://${context?.packageName}/" + video)
                    )

                    val mediaController = MediaController(context)
                    mediaController.setAnchorView(binding.videoView)
                    mediaController.setMediaPlayer(binding.videoView)
                    binding.videoView.setMediaController(mediaController)

                    binding.videoView.start()
                } else {
                    Toast.makeText(context, "Ese video no existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Rellena los campos", Toast.LENGTH_SHORT).show()
            }
        }

        val mediaPlayer = MediaPlayer.create(context, R.raw.song1)

        binding.btnPlayPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.btnPlayPause.text = "Play"
            } else {
                mediaPlayer.start()
                binding.btnPlayPause.text = "Pause"
            }
        }

        binding.btnStart.setOnClickListener {
            mediaPlayer.start()
        }

        binding.btnStop.setOnClickListener {
            mediaPlayer.stop()
        }

        return binding.root
    }

    private fun getVideo(video: String): Int {
        return when (video) {
            "video1" -> R.raw.video1
            "video2" -> R.raw.video2
            "video3" -> R.raw.video3
            "video4" -> R.raw.video4
            else -> 0
        }
    }
}