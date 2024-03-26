package com.wicom.maplepedia

import android.app.TabActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.adknowva.adlib.ANClickThroughAction
import com.adknowva.adlib.AdListener
import com.adknowva.adlib.BannerAdView
import com.adknowva.adlib.NativeAdResponse
import com.adknowva.adlib.ResultCode
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.opencsv.CSVReader
import java.io.InputStreamReader


class RefWeaponOptionActivity : TabActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide_weaponoption)

        var tabHost = this.tabHost

        var tabAbsol = tabHost.newTabSpec("Absol").setIndicator("앱솔")
        tabAbsol.setContent(R.id.TabAbsol)
        tabHost.addTab(tabAbsol)

        var tabArcane = tabHost.newTabSpec("Arcane").setIndicator("아케인")
        tabArcane.setContent(R.id.TabArcane)
        tabHost.addTab(tabArcane)

        var tabGenesis = tabHost.newTabSpec("Genesis").setIndicator("제네")
        tabGenesis.setContent(R.id.TabGenesis)
        tabHost.addTab(tabGenesis)

        var tabZero = tabHost.newTabSpec("Zero").setIndicator("제로")
        tabZero.setContent(R.id.TabZero)
        tabHost.addTab(tabZero)

        tabHost.currentTab = 0


        CsvAndButtonLoad()

    }

    var weaponStrs : Array<String?> = arrayOfNulls(18)

    fun CsvAndButtonLoad() {
        val assetManager = this.assets
        val inputStream = assetManager.open("weapon_option.csv")
        var reader = CSVReader(InputStreamReader(inputStream))

        try {
            var nextRecord: Array<String>?
            // we are going to read data line by line

            var i : Int = 1;
            while (reader.readNext().also { nextRecord = it } != null) {
                for (j in 0..17){
                    weaponStrs[j] = nextRecord?.get(j)//장비 정보 1개 가져오기
                }
                AddWeapon()
            } //해당 과정을 테이블 행의 수만큼 반복
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    fun AddWeapon() {

        var weaponOptionPosAbsol: LinearLayout = findViewById(R.id.TabAbsolPos1)
        var weaponOptionPosArcane : LinearLayout = findViewById(R.id.TabAbsolPos1)
        var weaponOptionPosGenesis : LinearLayout = findViewById(R.id.TabAbsolPos1)
        var weaponOptionPosZero : LinearLayout = findViewById(R.id.TabZeroPos)

        when(weaponStrs[2]!!.toInt()) {
            1 -> {
                weaponOptionPosAbsol = findViewById(R.id.TabAbsolPos1)
                weaponOptionPosArcane = findViewById(R.id.TabArcanePos1)
                weaponOptionPosGenesis = findViewById(R.id.TabGenesisPos1)
            }
            2 -> {
                weaponOptionPosAbsol = findViewById(R.id.TabAbsolPos2)
                weaponOptionPosArcane = findViewById(R.id.TabArcanePos2)
                weaponOptionPosGenesis = findViewById(R.id.TabGenesisPos2)
            }
            3 -> {
                weaponOptionPosAbsol = findViewById(R.id.TabAbsolPos3)
                weaponOptionPosArcane = findViewById(R.id.TabArcanePos3)
                weaponOptionPosGenesis = findViewById(R.id.TabGenesisPos3)
            }
            4 -> {
                weaponOptionPosAbsol = findViewById(R.id.TabAbsolPos4)
                weaponOptionPosArcane = findViewById(R.id.TabArcanePos4)
                weaponOptionPosGenesis = findViewById(R.id.TabGenesisPos4)
            }
            5 -> {
                weaponOptionPosAbsol = findViewById(R.id.TabAbsolPos5)
                weaponOptionPosArcane = findViewById(R.id.TabArcanePos5)
                weaponOptionPosGenesis = findViewById(R.id.TabGenesisPos5)
            }
            6 -> {

            }
        }

        if(weaponStrs[2]!!.toInt() != 6) { //제로무기가 아닐 때

            val inflater2 = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView2: View = inflater2.inflate(R.layout.layout_weapon_option, null)
            weaponOptionPosAbsol!!.addView(rowView2, weaponOptionPosAbsol!!.childCount)
            weaponOptionSet(1, rowView2)


            val inflater3 = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView3: View = inflater3.inflate(R.layout.layout_weapon_option, null)
            weaponOptionPosArcane!!.addView(rowView3, weaponOptionPosArcane!!.childCount)
            weaponOptionSet(2, rowView3)


            val inflater4 = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView4: View = inflater4.inflate(R.layout.layout_weapon_option, null)
            weaponOptionPosGenesis!!.addView(rowView4, weaponOptionPosGenesis!!.childCount)
            weaponOptionSet(3, rowView4)
        }
        else { //제로 무기일 때

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.layout_weapon_option, null)
            weaponOptionPosZero!!.addView(rowView, weaponOptionPosZero!!.childCount)
            weaponOptionSet(5, rowView)
        }
    }

    val weaponIconByTier : Array<String> = arrayOf("", "abs", "arc", "gen", "", "zero")

    var zeroStack : Int = 0

    fun weaponOptionSet(weaponTier : Int, lout : View) {
        val weaponName : TextView = lout.findViewById(R.id.ref_weaponName)

        val weaponIcon : ImageView = lout.findViewById(R.id.ref_weaponImg)
        val mDrawableName = "weapon_" + weaponIconByTier[weaponTier] + "_" + weaponStrs[1]
        val resID = resources.getIdentifier(mDrawableName, "drawable", packageName)
        weaponIcon.setImageResource(resID)

        val weaponT_5 : TextView = lout.findViewById((R.id.ref_weaponTier5))
        val weaponT_4 : TextView = lout.findViewById((R.id.ref_weaponTier4))
        val weaponT_3 : TextView = lout.findViewById((R.id.ref_weaponTier3))
        val weaponT_2 : TextView = lout.findViewById((R.id.ref_weaponTier2))
        val weaponT_1 : TextView = lout.findViewById((R.id.ref_weaponTier1))

        val zeroWeapon : Array<String> = arrayOf(" 7형", " 8형", " 9형", "")
        if(weaponStrs[1]!!.toInt() != 31) { //일반 무기
            weaponName.text = weaponStrs[0]
        }
        else { //제로 무기(7형 이상) 일 때
            weaponName.text = weaponStrs[0] + zeroWeapon[zeroStack]
            zeroStack++
            Log.i("weapon", zeroStack.toString())
        }

        if(weaponTier != 5) { //제로 무기가 아닐 때
            weaponT_5.text = weaponStrs[3 + (5 * (weaponTier - 1))]
            weaponT_4.text = weaponStrs[4 + (5 * (weaponTier - 1))]
            weaponT_3.text = weaponStrs[5 + (5 * (weaponTier - 1))]
            weaponT_2.text = weaponStrs[6 + (5 * (weaponTier - 1))]
            weaponT_1.text = weaponStrs[7 + (5 * (weaponTier - 1))]
        }
        else { //제로 무기일 때
            weaponT_5.text = weaponStrs[3]
            weaponT_4.text = weaponStrs[4]
            weaponT_3.text = weaponStrs[5]
            weaponT_2.text = weaponStrs[6]
            weaponT_1.text = weaponStrs[7]
        }
    }

}