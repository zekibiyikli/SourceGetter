package com.zbiyikli.sourcegetter

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class PlayerActivity : AppCompatActivity() {

    var player: SimpleExoPlayer? = null
    var mediaDataSourceFactory: DataSource.Factory? = null
    var url = ""
    var playerView: PlayerView? = null
    var mediaSource: MediaSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)

        url = intent.getStringExtra("url")!!

        player = ExoPlayerFactory.newSimpleInstance(this)
        mediaDataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "ExoPlayerDemo")
        )

        when (Util.inferContentType(Uri.parse(url))) {
            C.TYPE_HLS -> {
                mediaSource = HlsMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse(url))
            }
            C.TYPE_DASH -> {
                mediaSource = DashMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse(url))
            }
            C.TYPE_SS->{
                mediaSource = SsMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse(url))
            }
            C.TYPE_OTHER->{
                mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse(url))
            }
        }

        player!!.prepare(mediaSource, false, false)
        player!!.setPlayWhenReady(true)
        playerView!!.setPlayer(player)
        playerView!!.requestFocus()


    }

    override fun onBackPressed() {
        player!!.stop()
        super.onBackPressed()

    }
}