package com.wicom.maplepedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class DeveloperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)
    }


    fun Developer_play(v: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.wicom.maplepedia"))
        startActivity(browserIntent)
    }

    fun Developer_blog(v: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/judeizzang"))
        startActivity(browserIntent)
    }

    fun Developer_email(v: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("judei1028@gmail.com"))
        startActivity(browserIntent)
    }

}