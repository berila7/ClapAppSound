package com.example.clap

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var runnable: Runnable
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var handler: Handler
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.sbProgress)
        handler = Handler(Looper.myLooper()!!)

        val pauseBtn = findViewById<FloatingActionButton>(R.id.fabPause)
        pauseBtn.setOnClickListener {
            mediaPlayer?.pause()
        }

        val playBtn = findViewById<FloatingActionButton>(R.id.fabPlay)
        playBtn.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.clap)
                innistialazeSeekBar()
            }
            mediaPlayer?.start()
        }

        val stopBtn = findViewById<FloatingActionButton>(R.id.fabStop)
        stopBtn.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0

        }

    }
    @SuppressLint("SetTextI18n")
    private fun innistialazeSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        val tvDuration = findViewById<TextView>(R.id.tvDue)
        val tvPlayedTime = findViewById<TextView>(R.id.tvPlay)
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            val play = mediaPlayer!!.currentPosition/1000
            tvPlayedTime.text = "$play sec"
            val duration = mediaPlayer!!.duration/1000
            val dueTime = duration - play
            tvDuration.text = "$dueTime sec"
            handler.postDelayed(runnable, 1000)
        }
            handler.postDelayed(runnable, 1000)
    }

}