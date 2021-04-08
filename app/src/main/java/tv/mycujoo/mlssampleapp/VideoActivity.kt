package tv.mycujoo.mlssampleapp


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_video.*
import tv.mycujoo.domain.entity.EventEntity
import tv.mycujoo.mcls.api.MLS
import tv.mycujoo.mcls.api.MLSBuilder
import tv.mycujoo.mcls.api.MLSConfiguration
import tv.mycujoo.mcls.api.PlayerEventsListener
import tv.mycujoo.mcls.core.UIEventListener
import tv.mycujoo.mcls.entity.msc.VideoPlayerConfig
import tv.mycujoo.mcls.widgets.MLSPlayerView

/**
 * This sample shows how to display an Event by using the event's id.
 */
class VideoActivity : AppCompatActivity() {

    lateinit var MLS: MLS
    var isFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        // apply constraint to MLSPlayerView
        constraintMLSPlayerView(resources.configuration.orientation)

        val playerEventsListener = object : PlayerEventsListener {

            override fun onIsPlayingChanged(playing: Boolean) {
                Log.i("PlayerEvents", "onIsPlayingChanged() $playing")
            }

            override fun onPlayerStateChanged(playbackState: Int) {
                Log.i("PlayerEvents", "onPlayerStateChanged() $playbackState")
            }
        }

        val uiEventListener = object : UIEventListener {
            override fun onFullScreenButtonClicked(fullScreen: Boolean) {
                Log.d("uiEventListener", "onFullScreenButtonClicked $fullScreen")
                isFullScreen = fullScreen
                requestedOrientation = if (fullScreen) {
                    ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                }
            }
        }

        // Customize configuration, or use default values
        val videoPlayerConfig = VideoPlayerConfig(
            primaryColor = "#FFFF00",
            secondaryColor = "#32CD32",
            autoPlay = false,
            enableControls = true,
            showPlayPauseButtons = true,
            showBackForwardsButtons = true,
            showSeekBar = true,
            showTimers = true,
            showFullScreenButton = true,
            showLiveViewers = true,
            showEventInfoButton = true
        )
        val mlsConfiguration =
            MLSConfiguration(seekTolerance = 1000L, videoPlayerConfig = videoPlayerConfig)

        // create MLS component
        MLS = MLSBuilder().publicKey("YOUR_PUBLIC_KEY_HERE")
            .withActivity(this)
            .setPlayerEventsListener(playerEventsListener)
            .setUIEventListener(uiEventListener)
            .setConfiguration(mlsConfiguration) // customize MLSConfiguration by providing
            .build()

        // use VideoPlayer to play video
        playButton.setOnClickListener {
            MLS.getVideoPlayer().playVideo("EVENT_ID_HERE")
        }

        // use Data-Provider to fetch events
        val dataProvider = MLS.getDataProvider()
        dataProvider.fetchEvents(
            10,
            fetchEventCallback = { eventList: List<EventEntity>, previousPageToken: String, nextPageToken: String ->
                // eventList contains all events that matches search criteria
            })

    }


    override fun onStart() {
        super.onStart()
        MLS.onStart(mlsPlayerView)
    }

    override fun onResume() {
        super.onResume()
        MLS.onResume(mlsPlayerView)
    }

    override fun onPause() {
        MLS.onPause()
        super.onPause()
    }

    override fun onStop() {
        MLS.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        MLS.onDestroy()
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        constraintMLSPlayerView(newConfig.orientation)
    }

    private fun constraintMLSPlayerView(orientation: Int) {
        // constraint MLSVideoPlayer view based on device orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(mainActivityRootLayout)
            constraintSet.constrainWidth(mlsPlayerView.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
            constraintSet.constrainHeight(mlsPlayerView.id, 800)

            constraintSet.applyTo(mainActivityRootLayout)


            mlsPlayerView.setScreenResizeMode(resizeMode = MLSPlayerView.ResizeMode.RESIZE_MODE_FIXED_HEIGHT)
            mlsPlayerView.setFullscreen(isFullscreen = true)
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(mainActivityRootLayout)
            constraintSet.constrainWidth(mlsPlayerView.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
            constraintSet.constrainHeight(mlsPlayerView.id, 800)

            constraintSet.applyTo(mainActivityRootLayout)


            mlsPlayerView.setScreenResizeMode(resizeMode = MLSPlayerView.ResizeMode.RESIZE_MODE_FIT)
            mlsPlayerView.setFullscreen(isFullscreen = false)
        }
    }

}
