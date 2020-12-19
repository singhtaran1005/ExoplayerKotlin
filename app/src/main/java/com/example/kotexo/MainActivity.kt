package com.example.kotexo

import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.media.MediaFormat
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
//import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.PlayerView
//import com.google.android.exoplayer2.util

import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes


//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

class MainActivity : AppCompatActivity() {

    //quality selector
    lateinit var btn_settings: PlayerView


    //private for exoplayer
    private lateinit var trackSelector: DefaultTrackSelector
    private var trackDialog: Dialog? = null

    //Initialize variable
    lateinit var playerView: PlayerView//? = null
    lateinit var progressBar: ProgressBar//? = null
    lateinit var btFullScreen: ImageView//? = null
    lateinit var btMenu: ImageView
    lateinit var btSpeedControl: ImageView//? = null
    lateinit var simpleExoPlayer: SimpleExoPlayer//? = null

    //    lateinit var exo_quality: Button
    var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Assign variable
        playerView = findViewById(R.id.player_view)
        progressBar = findViewById(R.id.progress_bar)
        btFullScreen = playerView.findViewById(R.id.bt_fullscreen)
        btSpeedControl = playerView.findViewById(R.id.speed_control)
        btn_settings = playerView.findViewById(R.id.btn_settings)

//        exo_quality = playerView.findViewById(R.id.exo_quality)
        btMenu = playerView.findViewById(R.id.img_menu)
        //Make activity full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //Video URL
        val videoUrl = Uri.parse("https://i.imgur.com/7bMqysJ.mp4")
        //        Uri videoUrl = Uri.parse("https://s3.amazonaws.com/_bc_dml/example-content/sintel_dash/sintel_vod.mpd");

        //Initialize load control
        val loadControl: LoadControl = DefaultLoadControl()
        //Initialize bandwidth meter
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        //Initialize track selector
        val trackSelector: TrackSelector =
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        //Initialize simple exo player
        simpleExoPlayer =
            ExoPlayerFactory.newSimpleInstance(this@MainActivity, trackSelector, loadControl)
        //Initialize data source factory
        val factory = DefaultHttpDataSourceFactory("exoplayer_video")
        //Initialize extractors factory
        val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
        //Initialize media source
        val mediaSource: MediaSource =
            ExtractorMediaSource(videoUrl, factory, extractorsFactory, null, null)
        //Set Player
        playerView.setPlayer(simpleExoPlayer)
        //keep screen on
        playerView.setKeepScreenOn(true)
        //Prepare media
        simpleExoPlayer!!.prepare(mediaSource)
        //Play video when ready
        simpleExoPlayer!!.playWhenReady = true
        simpleExoPlayer!!.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {}
            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                progressBar.setVisibility(View.GONE)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                //Check condition
                if (playbackState == Player.STATE_BUFFERING) {
                    //when buffering
                    //show progress bar
                    progressBar.setVisibility(View.GONE)
                }
            }

            override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {}
            override fun onIsPlayingChanged(isPlaying: Boolean) {}
            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: ExoPlaybackException) {}
            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
        btSpeedControl.setOnClickListener(View.OnClickListener {

//                PlaybackParameters param = new PlaybackParameters(2.0f);
//                simpleExoPlayer.setPlaybackParameters(param);
            val speed = arrayOf("1.0", "1.25", "1.5", "2.0")

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Select speed")
            builder.setItems(
                speed
            ) { dialog, which ->
                if ("1.0" == speed[which]) {
                    val param = PlaybackParameters(1.0f)
                    simpleExoPlayer!!.setPlaybackParameters(param)

                    Toast.makeText(this@MainActivity, "1.0", Toast.LENGTH_SHORT)
                        .show()
                } else if ("1.25" == speed[which]) {
                    val param = PlaybackParameters(1.25f)
                    simpleExoPlayer!!.setPlaybackParameters(param)

                    Toast.makeText(this@MainActivity, "1.25", Toast.LENGTH_SHORT)
                        .show()
                } else if ("1.5" == speed[which]) {
                    val param = PlaybackParameters(1.5f)
                    simpleExoPlayer!!.setPlaybackParameters(param)

                    Toast.makeText(this@MainActivity, "1.5", Toast.LENGTH_SHORT)
                        .show()
                } else if ("2.0" == speed[which]) {
                    val param = PlaybackParameters(2.0f)
                    simpleExoPlayer!!.setPlaybackParameters(param)

                    Toast.makeText(this@MainActivity, "2.0", Toast.LENGTH_SHORT).show()
                }
                // the user clicked on colors[which]
            }
            builder.show()
        })


        btMenu.setOnClickListener(View.OnClickListener {

//                PlaybackParameters param = new PlaybackParameters(2.0f);
//                simpleExoPlayer.setPlaybackParameters(param);
            val speed = arrayOf("Report", "Quality", "Caption", "Help")

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Select Option")
            builder.setItems(
                speed
            ) { dialog, which ->
                if ("Report" == speed[which]) {
                    Toast.makeText(this@MainActivity, "Report", Toast.LENGTH_SHORT).show()
                } else if ("Quality" == speed[which]) {
                    Toast.makeText(this@MainActivity, "Quality", Toast.LENGTH_SHORT).show()
                } else if ("Caption" == speed[which]) {
                    Toast.makeText(this@MainActivity, "Caption", Toast.LENGTH_SHORT).show()
                } else if ("Help" == speed[which]) {
                    Toast.makeText(this@MainActivity, "Help", Toast.LENGTH_SHORT).show()
                }
                // the user clicked on colors[which]
            }
            builder.show()
        })


        btFullScreen.setOnClickListener(View.OnClickListener {
            //check condition
            if (flag) {
                //When flag is true
                //Set enter full screen image
                btFullScreen.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen))

                //Set potrait orientation
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                //Set flag value false
                flag = false
            } else {
                //When flag is false
                //Set exit full screen image
                btFullScreen.setImageDrawable(getDrawable(R.drawable.ic_fullscreen_exit))
                //Set landscape orientation
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                //Set flag value is true
                flag = true
            }
        })
//Quality onclick listener
//        private fun ImageButton.setOnClickListener(mainActivity: MainActivity) {

//        btn_settings.setOnClickListener(View v {
//            public void onClick(View v) {
//            int i1 = v . getId();
//            if (i1 === R.id.btn_settings) {
//                val popup = PopupMenu(this@VideoPlayer, v)
//                popup.setOnMenuItemClickListener(object : OnMenuItemClickListener() {
//                    fun onMenuItemClick(item: MenuItem): Boolean {
//                        player.setSelectedTrack(0, item.getItemId() - 1)
//                        return false
//                    }
//                })
//                val menu: Menu = popup.menu
//                menu.add(Menu.NONE, 0, 0, "Video Quality")
//                for (i in 0 until player.getTrackCount(0)) {
//                    val format: MediaFormat = player.getTrackFormat(0, i)
//                    if (MimeTypes.isVideo(format.mimeType)) {
//                        if (format.adaptive) {
//                            menu.add(1, i + 1, i + 1, "Auto")
//                        } else {
//                            menu.add(1, i + 1, i + 1, format.width.toString() + "p")
//                        }
//                    }
//                }
//                menu.setGroupCheckable(1, true, true)
//                menu.findItem(player.getSelectedTrack(0) + 1).setChecked(true)
//                popup.show()
//            }
//        })
//        button.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                //your implementation goes here
//            }
//        })
//        }

            btn_settings.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v : View?){
//                int i1 = v . getId();

                var i1 = v?.getId();
            if (i1 === R.id.btn_settings) {
                val popup = PopupMenu(this@MainActivity, v)
                popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        simpleExoPlayer.setSelectedTrack(0, item.getItemId() - 1)
                        return false
                    }
                })
                val menu: Menu = popup.menu
                menu.add(Menu.NONE, 0, 0, "Video Quality")
                for (i in 0 until simpleExoPlayer.getTrackCount(0)) {
                    val format: MediaFormat = simpleExoPlayer.getTrackFormat(0,i)
                    if (MimeTypes.isVideo(format.mimeType)) {
                        if (format.adaptive) {
                            menu.add(1, i + 1, i + 1, "Auto")
                        } else {
                            menu.add(1, i + 1, i + 1, format.width.toString() + "p")
                        }
                    }
                }
                menu.setGroupCheckable(1, true, true)
                menu.findItem(simpleExoPlayer.getSelectedTrack(0) + 1).setChecked(true)
                popup.show()
            }
        }
                )

    }
    }

    override fun onPause() {
        super.onPause()
        //Stop video when ready
        simpleExoPlayer!!.playWhenReady = false
        //Get playback state
        simpleExoPlayer!!.playbackState
    }

    override fun onRestart() {
        super.onRestart()
        //Play video when ready
        simpleExoPlayer!!.playWhenReady = true
        //Get playback state
        simpleExoPlayer!!.playbackState
    }
}



//    // QUALITY SELECTOR
//
//    @SuppressLint("StringFormatInvalid")
//    private fun initPopupQuality() {
//        val mappedTrackInfo = trackSelector.currentMappedTrackInfo
//        var videoRenderer : Int? = null
//
//        if(mappedTrackInfo == null) return else exo_quality.visibility = View.VISIBLE
//
//        for(i in 0 until mappedTrackInfo.rendererCount){
//            if(isVideoRenderer(mappedTrackInfo, i)){
//                videoRenderer = i
//            }
//        }
//
//        if(videoRenderer == null){
//            exo_quality.visibility = View.GONE
//            return
//        }
//
////        val trackSelectionDialogBuilder = TrackSelectionDialogBuilder(this, getString(com.google.android.exoplayer2.R.string.qualitySelector), trackSelector, videoRenderer)
////        trackSelectionDialogBuilder.setTrackNameProvider{
////            // Override function getTrackName
////            getString(com.google.android.exoplayer2.R.string.exo_track_resolution_pixel, it.height)
////        }
////        trackDialog = trackSelectionDialogBuilder.build()
//    }
//
//    private fun isVideoRenderer(
//        mappedTrackInfo: MappingTrackSelector.MappedTrackInfo,
//        rendererIndex: Int
//    ): Boolean {
//        val trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
//        if (trackGroupArray.length == 0) {
//            return false
//        }
//        val trackType = mappedTrackInfo.getRendererType(rendererIndex)
//        return C.TRACK_TYPE_VIDEO == trackType
//    }
//
//
//}