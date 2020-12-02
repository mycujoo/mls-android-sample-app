package tv.mycujoo.mlssampleapp

import android.app.Activity
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import tv.mycujoo.mls.api.MLSTVConfiguration
import tv.mycujoo.mls.enum.LogLevel
import tv.mycujoo.mls.tv.api.MLSTV
import tv.mycujoo.mls.tv.api.MLSTvBuilder

class VideoFragment : VideoSupportFragment() {
    lateinit var mlsTv: MLSTV

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // build MLSTV wherever you have access to activity
        // this can happen in the activity which hosts VideoSupportFragment
        mlsTv = MLSTvBuilder().withActivity(activity as Activity)
            .publicKey("YOUR_PUBLIC_KEY_HERE")
            .setConfiguration(MLSTVConfiguration())
            .setLogLevel(LogLevel.VERBOSE).build()
        // prepare video player by providing an instance of VideoSupportFragment
        mlsTv.preparePlayer(this)

        // use Video-player to play videos
        val videoPlayer = mlsTv.getVideoPlayer()

        // use Data-provider to fetch events
        val dataProvider = mlsTv.getDataProvider()
        dataProvider.fetchEvents(pageSize = 10) { eventList, previousPageToken, nextPageToken ->
            videoPlayer.playVideo(eventList.first())
        }
    }
}