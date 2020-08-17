package tv.mycujoo.mlssampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        withEventIdButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    VideoActivity::class.java
                )
            )
        }
        withEventListButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    VideoActivityWithEventList::class.java
                )
            )
        }


    }
}
