package com.wicom.maplepedia

import android.app.TabActivity
import android.os.Bundle
import android.widget.TabHost

class GuideSoulskill : TabActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide_soulskill)
        tabHost.setup()
        val spec1 = tabHost.newTabSpec("Tab1").setContent(R.id.Tab1).setIndicator("매칭 보스")
        tabHost.addTab(spec1)
        val spec2 = tabHost.newTabSpec("Tab2").setContent(R.id.Tab2).setIndicator("일반 보스")
        tabHost.addTab(spec2)
        val spec3 = tabHost.newTabSpec("Tab3").setContent(R.id.Tab3).setIndicator("기타 보스")
        tabHost.addTab(spec3)
        val spec4 = tabHost.newTabSpec("Tab4").setContent(R.id.Tab4).setIndicator("이벤트")
        tabHost.addTab(spec4)
    }
}