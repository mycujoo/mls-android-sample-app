package tv.mycujoo.mlssampleapp


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video.*
import tv.mycujoo.mls.api.MLS
import tv.mycujoo.mls.api.MLSBuilder
import tv.mycujoo.mls.api.MLSConfiguration
import tv.mycujoo.mls.api.PlayerEventsListener
import tv.mycujoo.mls.core.UIEventListener
import tv.mycujoo.mls.widgets.PlayerViewWrapper

class VideoActivity : AppCompatActivity() {

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

        MLS = MLSBuilder().publicKey("3HFCBP4EQJME2EH8H0SBH9RCST0IR269")
            .withActivity(this)
            .setPlayerEventsListener(playerEventsListener)
            .setUIEventListener(uiEventListener)
            .setConfiguration(MLSConfiguration())
            .build()


        testPlayButton.setOnClickListener {
            MLS.getVideoPlayer().playVideo("1g52suLu2ktx4Rvwt0WnCke0d9a")
        }


        val dataProvider = MLS.getDataProvider()
        dataProvider.fetchEvents(10, fetchEventCallback = { Log.i("VideoActivity", it.toString()) })

    }

    override fun onStart() {
        super.onStart()
        MLS.onStart(playerViewWrapper)
    }

    override fun onResume() {
        super.onResume()
        MLS.onResume(playerViewWrapper)
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
            playerViewWrapper.screenMode(PlayerViewWrapper.ScreenMode.Landscape(PlayerViewWrapper.RESIZE_MODE_FILL))
        } else {
            playerViewWrapper.screenMode(PlayerViewWrapper.ScreenMode.Portrait(PlayerViewWrapper.RESIZE_MODE_FIT))
        }
    }
}
