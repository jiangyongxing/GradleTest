package cn.fengrong.gradletest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var textview: TextView = findViewById(R.id.ma_login)
        textview.setOnClickListener{
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
        }

        BuildConfig.UMENG_SHARE_WEIXIN_ID
        BuildConfig.VERSION_NAME
    }
}
