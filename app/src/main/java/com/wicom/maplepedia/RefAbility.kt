package com.wicom.maplepedia

import android.app.TabActivity
import android.os.Bundle

class RefAbility : TabActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide_ability)
        tabHost.setup()
        val spec1 = tabHost.newTabSpec("Tab1").setContent(R.id.Tab1).setIndicator("레전드리")
        tabHost.addTab(spec1)
        val spec2 = tabHost.newTabSpec("Tab2").setContent(R.id.Tab2).setIndicator("유니크")
        tabHost.addTab(spec2)
        val spec3 = tabHost.newTabSpec("Tab3").setContent(R.id.Tab3).setIndicator("에픽")
        tabHost.addTab(spec3)

        tabHost.currentTab = 0
    }
}