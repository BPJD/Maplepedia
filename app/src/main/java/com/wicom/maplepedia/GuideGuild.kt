package com.wicom.maplepedia

import android.app.TabActivity
import android.os.Bundle
import android.widget.TabHost

class GuideGuild : TabActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide_guildskill)
        tabHost.setup()
        val spec1 = tabHost.newTabSpec("Tab1").setContent(R.id.Tab1).setIndicator("길드 스킬")
        tabHost.addTab(spec1)
        val spec2 = tabHost.newTabSpec("Tab2").setContent(R.id.Tab2).setIndicator("노블레스 스킬")
        tabHost.addTab(spec2)
        val spec3 = tabHost.newTabSpec("Tab3").setContent(R.id.Tab3).setIndicator("보스 별 기여도")
        tabHost.addTab(spec3)



        tabHost.currentTab = 0
    }
}