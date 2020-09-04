package tv.mycujoo.mlssampleapp


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video.*
import tv.mycujoo.domain.entity.EventEntity
import tv.mycujoo.mls.api.MLS
import tv.mycujoo.mls.api.MLSBuilder
import tv.mycujoo.mls.api.MLSConfiguration
import tv.mycujoo.mls.api.PlayerEventsListener
import tv.mycujoo.mls.core.UIEventListener
import tv.mycujoo.mls.widgets.MLSPlayerView

class VideoActivityWithEventList : AppCompatActivity() {

    lateinit var MLS: MLS
    var isFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

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

        MLS = MLSBuilder().publicKey("YOUR_PUBLIC_KEY_HERE")
            .withActivity(this)
            .setPlayerEventsListener(playerEventsListener)
            .setUIEventListener(uiEventListener)
            .setConfiguration(MLSConfiguration())
            .build()

        val dataProvider = MLS.getDataProvider()
        playButton.setOnClickListener {
            dataProvider.fetchEvents(
                10,
                fetchEventCallback = { eventList: List<EventEntity>, previousPageToken: String, nextPageToken: String ->
                    MLS.getVideoPlayer().playVideo(eventList.first())
                })
        }


    }

    override fun onStart() {
        super.onStart()
        MLS.onStart(playerView)
    }

    override fun onResume() {
        super.onResume()
        MLS.onResume(playerView)
    }

    override fun onPause() {
        super.onPause()
        MLS.onPause()
    }

    override fun onStop() {
        super.onStop()
        MLS.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (isFullScreen) {
            playerView.screenMode(MLSPlayerView.ScreenMode.Landscape(MLSPlayerView.RESIZE_MODE_FILL))
        } else {
            playerView.screenMode(MLSPlayerView.ScreenMode.Portrait(MLSPlayerView.RESIZE_MODE_FIT))
        }
    }
}
