package com.wicom.maplepedia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class ReferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reference)

    }

    fun referenceClicked(v : View) {

        val buttonRefNumber: Int = v.getTag().toString().toInt();
        var nextIntent = Intent(this, RefWeaponOptionActivity::class.java)

        when(buttonRefNumber) {
            0 -> nextIntent = Intent(this, RefWeaponOptionActivity::class.java)
            1 -> nextIntent = Intent(this, RefVskill::class.java)
            2 -> nextIntent = Intent(this, RefSeedring::class.java)
            3 -> nextIntent = Intent(this, RefAbility::class.java)
            4 -> nextIntent = Intent(this, RefLink::class.java)
            5 -> nextIntent = Intent(this, RefL_Union::class.java)
            6 -> nextIntent = Intent(this, RefScroll::class.java)
        }


        startActivity(nextIntent);

    }

}