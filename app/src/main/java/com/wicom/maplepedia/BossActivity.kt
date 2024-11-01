package com.wicom.maplepedia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adknowva.adlib.ANClickThroughAction
import com.adknowva.adlib.AdListener
import com.adknowva.adlib.BannerAdView
import com.adknowva.adlib.NativeAdResponse
import com.adknowva.adlib.ResultCode
import com.gomfactory.adpie.sdk.AdPieError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import java.io.FileReader
import java.io.InputStreamReader
import java.util.function.Consumer


class BossActivity : AppCompatActivity() {

    // TODO - AdNetWork
    var isAdLoaded: Boolean = false
    private var adpieView: com.gomfactory.adpie.sdk.AdView? = null
    lateinit var adknowvaView: BannerAdView
    private var admobView: AdManagerAdView? = null
// TODO - AdNetWork

    val dataName: Array<String> = arrayOf(
       "TAG", "bossName", "bossDesc", "bossIdentity1", "bossIdentity2", "bossMap"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss)

        CsvAndButtonLoad()

        // 광고 연동을 위한 슬롯 ID를 필수로 입력한다.
        adknowvaView = findViewById(R.id.banner_view)
        admobView = findViewById(R.id.adManagerAdView)
        adpieView = findViewById(R.id.adpie_Tbanner)
        setAdpieAD()
    }

    fun CsvAndButtonLoad() {
        val assetManager = this.assets
        val inputStream = assetManager.open("bossPublic.csv")
        var reader = CSVReader(InputStreamReader(inputStream))

        try {
            var nextRecord: Array<String>?
            // we are going to read data line by line

            while (reader.readNext().also { nextRecord = it } != null) {

                //skillStrs[j] = nextRecord?.get(j)//스킬 정보 1개 가져오기
                //Log.i("boss", nextRecord?.get(0).toString())
                BossButtonMake(nextRecord!!.get(0).toInt() - 2, nextRecord?.get(1).toString())
            } //해당 과정을 테이블 행의 수만큼 반복
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun BossButtonMake(tag:Int, name: String) {

        val bossButtonPos: LinearLayout = findViewById(R.id.bossAct_buttonPos)


        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_boss_button, null) //보스 버튼 레이아웃 생성
        bossButtonPos!!.addView(rowView, bossButtonPos!!.childCount)

        val bossBut: Button = rowView.findViewById(R.id.BossButton)

        bossBut.tag = tag
        bossBut.text = name
    }

    fun bossButtonClicked(v : View) {
        val assetManager = this.assets
        val inputStream = assetManager.open("bossPublic.csv")
        val reader = CSVReader(InputStreamReader(inputStream))

        val buttonBossNumber: Int = v.getTag().toString().toInt();

        val nextIntent = Intent(this, BossInfoActivity::class.java)

        try {
            var nextRecord: Array<String>?

            // we are going to read data line by line
            var i: Int = 0;
            while (reader.readNext().also { nextRecord = it } != null) {
                if(i == buttonBossNumber){
                    var j: Int = 0
                    for(name in dataName.indices){
                        nextIntent.putExtra(dataName[j], nextRecord?.get(j))
                        Log.i("boss", nextRecord!!.get(j))
                        j++
                    }
                }
                i++
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        startActivity(nextIntent);

    }


    private fun setHuvleAD() {

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
                setHuvleAD()
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
        adpieView!!.setAdListener(object : com.gomfactory.adpie.sdk.AdView.AdListener {
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
                    setGoogleAD()
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

}