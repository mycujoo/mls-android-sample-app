package tv.mycujoo.mlssampleapp


import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video.*
import tv.mycujoo.mls.api.MyCujooLiveService
import tv.mycujoo.mls.model.ConfigParams

class VideoActivity : AppCompatActivity() {

    lateinit var myCujooLiveService: MyCujooLiveService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        myCujooLiveService = MyCujooLiveService.Builder()
            .withContext(this)
            .defaultPlayerController(true)
            .build()

        myCujooLiveService.loadVideo(Uri.parse("https://playlists.mycujoo.football/eu/ck8u05tfu1u090hew2kgobnud/master.m3u8"))

    }

    override fun onStart() {
        super.onStart()
        myCujooLiveService.onStart(playerViewWrapper)
    }

    override fun onResume() {
        super.onResume()
        myCujooLiveService.onResume(playerViewWrapper)
    }

    override fun onPause() {
        super.onPause()
        myCujooLiveService.onPause()
    }

    override fun onStop() {
        super.onStop()
        myCujooLiveService.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        myCujooLiveService.onConfigurationChanged(
            ConfigParams(newConfig, hasPortraitActionBar = true, hasLandscapeActionBar = false),
            window.decorView,
            supportActionBar
        )
    }
}
