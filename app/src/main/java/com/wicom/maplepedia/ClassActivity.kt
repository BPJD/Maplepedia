package com.wicom.maplepedia

import android.annotation.SuppressLint
import android.app.TabActivity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.adknowva.adlib.ANAdResponseInfo
import com.adknowva.adlib.ANClickThroughAction
import com.adknowva.adlib.AdListener
import com.adknowva.adlib.BannerAdView
import com.adknowva.adlib.NativeAdEventListener
import com.adknowva.adlib.NativeAdRequest
import com.adknowva.adlib.NativeAdRequestListener
import com.adknowva.adlib.NativeAdResponse
import com.adknowva.adlib.NativeAdSDK
import com.adknowva.adlib.ResultCode
import com.gomfactory.adpie.sdk.AdPieError
import com.gomfactory.adpie.sdk.AdPieSDK
import com.gomfactory.adpie.sdk.AdView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.opencsv.CSVReader
import java.io.InputStreamReader
import java.util.LinkedList


class ClassActivity : TabActivity() {

// TODO - AdNetWork
    var isAdLoaded: Boolean = false
    private var adpieView: AdView? = null
    lateinit var adknowvaView: BannerAdView
    private var admobView: AdManagerAdView? = null
// TODO - AdNetWork

    val dataName: Array<String> = arrayOf(
        "TAG", "className", "classFaction", "classType", "mainWeapon",
        "subWeapon", "mainStat", "subStat", "criRate", "unionInfo",
        "unionB", "unionA", "unionS", "unionSS", "unionSSS",
        "linkIcon", "linkName", "linkInfo", "linkDesc",
        "identityIcon", "identityDesc", "identityIcon2", "identityDesc2", "identityIcon3", "identityDesc3",
        "colorUpR", "colorUpG", "colorUpB", "colorDownR", "colorDownG", "colorDownB"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        var tabHost = this.tabHost

        var tabFaction = tabHost.newTabSpec("Faction").setIndicator("직업군")
        tabFaction.setContent(R.id.class_Tab1)
        tabHost.addTab(tabFaction)

        var tabType = tabHost.newTabSpec("Type").setIndicator("직업 계열")
        tabType.setContent(R.id.class_Tab2)
        tabHost.addTab(tabType)

        tabHost.currentTab = 0

        // TODO - Adknowva SDK Library
        //setHuvleAD() // 애드노바 sdk init - Activity onCreate 부분에 적용해준다.
        // TODO - Adknowva SDK Library
        adknowvaView = findViewById(R.id.banner_view)
        admobView = findViewById(R.id.adManagerAdView)
        adpieView = findViewById(R.id.adpie_Tbanner)

        // 광고 연동을 위한 슬롯 ID를 필수로 입력한다.
        reqAd(0)
    }

    // TODO - Adknowva SDK Library
    private fun setHuvleAD() {


        // 정적으로 구현시(When if apply Static Implementation) BannerAdView Start
        adknowvaView.placementID = "Z916x23725" // 320*50 banner testID , 300*250 banner test ID "testbig", 32050 Z916x23725,  300250 Zzo598u6rz
        adknowvaView.shouldServePSAs = false
        adknowvaView.clickThroughAction = ANClickThroughAction.OPEN_DEVICE_BROWSER
        adknowvaView.setAdSize(320, 50) //bav.setAdSize(300, 250);
        // Resizes the container size to fit the banner ad
        adknowvaView.resizeAdToFitContainer = true
//        bav.setExpandsToFitScreenWidth(true)
        val adListener: AdListener = object : AdListener {
            override fun onAdRequestFailed(
                bav: com.adknowva.adlib.AdView,
                errorCode: ResultCode
            ) {
                if (errorCode == null) {
                    Log.v("HuvleBANNER", "Call to loadAd failed")
                } else {
                    Log.v("HuvleBANNER", "Ad request failed: $errorCode")
                }
                bav.visibility = View.INVISIBLE
                if(!isAdLoaded) {
                }
                //adpieView?.visibility = View.GONE
                //setAdpieAD()
            }
            override fun onAdLoaded(ba: com.adknowva.adlib.AdView) {
                isAdLoaded = true
                Log.v("HuvleBANNER", "The Ad Loaded!")
                adpieView?.visibility = View.INVISIBLE
                adknowvaView.visibility = View.VISIBLE
                admobView!!.visibility = View.INVISIBLE
            }
            override fun onAdLoaded(nativeAdResponse: NativeAdResponse) {}
            override fun onAdExpanded(bav: com.adknowva.adlib.AdView) {}
            override fun onAdCollapsed(bav: com.adknowva.adlib.AdView) {}
            override fun onAdClicked(bav: com.adknowva.adlib.AdView) {}
            override fun onAdClicked(adView: com.adknowva.adlib.AdView, clickUrl: String) {}
            override fun onLazyAdLoaded(adView: com.adknowva.adlib.AdView) {}
        }
        adknowvaView.adListener = adListener
        adknowvaView.loadAd()

    }

    private fun reqAd(adNet : Int) {
        when (adNet) {
            0 -> setAdpieAD()
            1 -> setHuvleAD()
            2 -> setGoogleAD()
        }
    }


    private fun setGoogleAD() {
        MobileAds.initialize(
            this
        ) { }
        val adRequest = AdRequest.Builder().build()
        admobView!!.loadAd(adRequest)
        admobView!!.setAdListener(object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                isAdLoaded = true
                Log.v("GoogleAD", "The Ad Loaded!")
                adpieView?.visibility = View.INVISIBLE
                adknowvaView.visibility = View.INVISIBLE
                admobView!!.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.v("GoogleAD", "The Ad failed!")
                reqAd(1)
            }

            override fun onAdOpened() {}
            override fun onAdClicked() {}
            override fun onAdClosed() {}
        })
    }


    private fun setAdpieAD() {
        adpieView?.load()
        adpieResult()
    }

    private fun adpieResult() {
        adpieView!!.setAdListener(object : AdView.AdListener {
            override fun onAdLoaded() {
                isAdLoaded = true
                // 광고 표출 성공 후 이벤트 발생
                adpieView?.visibility = View.VISIBLE
                adknowvaView.visibility = View.INVISIBLE
                admobView!!.visibility = View.INVISIBLE
                Log.i("adPie", "AdPie Loaded!")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // 광고 요청 또는 표출 실패 후 이벤트 발생
                val errorMessage = AdPieError.getMessage(errorCode)
                Log.e("adPie", errorMessage)

                if(!isAdLoaded) {
                    reqAd(2)
                }
            }

            override fun onAdClicked() {
                // 광고 클릭 후 이벤트 발생
            }
        })
    }

    override fun onDestroy() {

        admobView?.destroy()
        adknowvaView?.destroy()

        if (adpieView != null) {
            adpieView!!.destroy();
            adpieView = null;
        }

        super.onDestroy()
    }

    fun classButtonClicked(v: View) {

        var classDatas: Array<String?> = arrayOfNulls<String>(31)
        val assetManager = this.assets
        val inputStream = assetManager.open("classInfoTable.csv")
        val reader = CSVReader(InputStreamReader(inputStream))

        val buttonClassNumber: Int = v.tag.toString().toInt()
        val mToast = Toast.makeText(applicationContext,buttonClassNumber.toString(), Toast.LENGTH_SHORT)

        val nextIntent = Intent(this, ClassInfoActivity::class.java)

        try {
            var nextRecord: Array<String>?

            // we are going to read data line by line
            var i: Int = 0;
            while (reader.readNext().also { nextRecord = it } != null) {
                if(i == buttonClassNumber){
                    var j: Int = 0
                    for(name in 0..dataName.size){
                        nextIntent.putExtra(dataName[j], nextRecord?.get(j))
                        j++
                    }
                }
                i++
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        startActivity(nextIntent);

        //mToast.show()
    }





}
