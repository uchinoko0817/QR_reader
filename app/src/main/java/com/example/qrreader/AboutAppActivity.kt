package com.example.qrreader

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class AboutAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        // Set version name
        findViewById<TextView>(R.id.textView_contents_version).text = BuildConfig.VERSION_NAME

        // Set event listener
        val licenceTextView = findViewById<TextView>(R.id.textView_licence)
        licenceTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        licenceTextView.setOnClickListener{
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
        }
    }
}