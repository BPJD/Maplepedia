package com.wicom.maplepedia

import android.app.TabActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import org.w3c.dom.Text
import java.io.InputStreamReader

class RefL_Union : TabActivity() {
    // TODO - Adknowva SDK Library
    lateinit var bav: BannerAdView
    private var mAdView: AdManagerAdView? = null
    // TODO - Adknowva SDK Library

    private var itemStrs: Array<String?> = arrayOfNulls<String>(8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide_link_union)

        var tabHost = this.tabHost

        var tabskillI = tabHost.newTabSpec("Tab1").setIndicator("기본 스탯")
        tabskillI.setContent(R.id.ref_linkUnion_1)
        tabHost.addTab(tabskillI)

        var tabskillII = tabHost.newTabSpec("Tab2").setIndicator("공격 스탯")
        tabskillII.setContent(R.id.ref_linkUnion_2)
        tabHost.addTab(tabskillII)

        var tabskillIII = tabHost.newTabSpec("Tab3").setIndicator("기타 스탯")
        tabskillIII.setContent(R.id.ref_linkUnion_3)
        tabHost.addTab(tabskillIII)
        tabHost.currentTab = 0

        findViewById<TextView>(R.id.ref_info_name).text = "유니온 공격대원 정보"

        val text1 : TextView = findViewById(R.id.ref_linkUnion_1_Name)
        val text1_1 : TextView = findViewById(R.id.ref_linkUnion_11_Name)
        val text1_2 : TextView = findViewById(R.id.ref_linkUnion_12_Name)
        val text1_3 : TextView = findViewById(R.id.ref_linkUnion_13_Name)
        val text1_4 : TextView = findViewById(R.id.ref_linkUnion_14_Name)
        val text1_5 : TextView = findViewById(R.id.ref_linkUnion_15_Name)
        val text2 : TextView = findViewById(R.id.ref_linkUnion_2_Name)
        val text3 : TextView = findViewById(R.id.ref_linkUnion_3_Name)

        text1.text = "STR"
        text1_1.text = "DEX"
        text1_2.text = "INT"
        text1_3.text = "LUK"
        text1_4.text = "STR, DEX, LUK"
        text1_5.text = "HP, MP"
        text2.text = "공격 능력치"
        text3.text = "기타 효과"

        text1_1.visibility = View.VISIBLE
        text1_2.visibility = View.VISIBLE
        text1_3.visibility = View.VISIBLE
        text1_4.visibility = View.VISIBLE
        text1_5.visibility = View.VISIBLE

        // TODO - Adknowva SDK Library
        setHuvleAD() // 애드노바 sdk init - Activity onCreate 부분에 적용해준다.
        // TODO - Adknowva SDK Library

        skillLoad()
    }


    private fun skillLoad() { //스킬 읽어오기
        val assetManager = this.assets
        val inputStream = assetManager.open("ref_union.csv") //5차 공용 scv
        val reader = CSVReader(InputStreamReader(inputStream))
        try {
            var nextRecord: Array<String>?
            // we are going to read data line by line
            while (reader.readNext().also { nextRecord = it } != null) {
                for(j in 0..7) {
                    itemStrs[j] = nextRecord?.get(j)//스킬 정보 1개 가져오기
                    //Log.i("skill", skillStrs[j].toString())
                }
                skillAdd() //스킬 하나 레이아웃 생성하고, 거기에 가져온 스킬 정보 붙이기.
            } //해당 과정을 테이블 행의 수만큼 반복
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun skillAdd() {

        var unionLayout: LinearLayout = findViewById(R.id.ref_linkUnion_1_Pos)
        when(itemStrs[1]?.toInt()) {
            1 -> unionLayout = findViewById(R.id.ref_linkUnion_1_Pos)
            2 -> unionLayout = findViewById(R.id.ref_linkUnion_11_Pos)
            3 -> unionLayout = findViewById(R.id.ref_linkUnion_12_Pos)
            4 -> unionLayout = findViewById(R.id.ref_linkUnion_13_Pos)
            5 -> unionLayout = findViewById(R.id.ref_linkUnion_14_Pos)
            6 -> unionLayout = findViewById(R.id.ref_linkUnion_15_Pos)
            7 -> unionLayout = findViewById(R.id.ref_linkUnion_2_Pos)
            8 -> unionLayout = findViewById(R.id.ref_linkUnion_3_Pos)
        }

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_ref_union, null) //스킬 레이아웃 생성
        unionLayout!!.addView(rowView, unionLayout!!.childCount)

        val textClassName: TextView = rowView.findViewById(R.id.ref_union_info_name)
        val textUnionInfo: TextView = rowView.findViewById(R.id.ref_union_info_desc)
        val textUnionB: TextView = rowView.findViewById(R.id.ref_union_info_b)
        val textUnionA: TextView = rowView.findViewById(R.id.ref_union_info_a)
        val textUnionS: TextView = rowView.findViewById(R.id.ref_union_info_s)
        val textUnionSS: TextView = rowView.findViewById(R.id.ref_union_info_ss)
        val textUnionSSS: TextView = rowView.findViewById(R.id.ref_union_info_sss)

        textClassName.text = itemStrs[0]
        textUnionInfo.text = itemStrs[2]
        textUnionB.text = itemStrs[3]
        textUnionA.text = itemStrs[4]
        textUnionS.text = itemStrs[5]
        textUnionSS.text = itemStrs[6]
        textUnionSSS.text = itemStrs[7]
    }


    // TODO - Adknowva SDK Library
    private fun setHuvleAD() {
        /*
            정적 구현부와 동적구현부는 참고하시어 하나만 적용하시기 바랍니다.(With checking the implementation guide below, please apply Implementation either only Dynamic or Static.)
            initBannerView 아이디 "test" 값은 http://ssp.huvle.com/ 에서 가입 > 매체생성 > zoneid 입력후 테스트 하시고, release시점에 허블에 문의주시면 인증됩니다. 배너사이즈는 변경하지 마세요.(For the “test” value below, please go to http://ssp.huvle.com/ to sign up > create media > Test your app after typing zoneid. Next, contact Huvle before releasing your app for authentication. You must not change the banner size.)
        */

//        // 동적으로 구현시(When if apply Dynamic Implementation) BannerAdView Start
//        bav = BannerAdView(this)
//        layout = findViewById<View>(R.id.adview_container) as RelativeLayout
//        val layoutParams = RelativeLayout.LayoutParams(
//            RelativeLayout.LayoutParams.WRAP_CONTENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT
//        )
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
//        bav!!.layoutParams = layoutParams
//        layout!!.addView(bav)

        // 정적으로 구현시(When if apply Static Implementation) BannerAdView Start
        bav = findViewById(R.id.banner_view)
        bav.placementID = "Z916x23725" // 320*50 banner testID , 300*250 banner test ID "testbig", 32050 Z916x23725,  300250 Zzo598u6rz
        bav.shouldServePSAs = false
        bav.clickThroughAction = ANClickThroughAction.OPEN_DEVICE_BROWSER
        bav.setAdSize(320, 50) //bav.setAdSize(300, 250);
        // Resizes the container size to fit the banner ad
        bav.resizeAdToFitContainer = true
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
                bav.visibility = View.GONE
                //setGoogleAD()
            }
            override fun onAdLoaded(ba: com.adknowva.adlib.AdView) {
                Log.v("HuvleBANNER", "The Ad Loaded!")
                bav.visibility = View.VISIBLE
            }
            override fun onAdLoaded(nativeAdResponse: NativeAdResponse) {}
            override fun onAdExpanded(bav: com.adknowva.adlib.AdView) {}
            override fun onAdCollapsed(bav: com.adknowva.adlib.AdView) {}
            override fun onAdClicked(bav: com.adknowva.adlib.AdView) {}
            override fun onAdClicked(adView: com.adknowva.adlib.AdView, clickUrl: String) {}
            override fun onLazyAdLoaded(adView: com.adknowva.adlib.AdView) {}
        }
        bav.adListener = adListener
        bav.loadAd()

    }


    private fun setGoogleAD() {
        MobileAds.initialize(
            this
        ) { }
        mAdView = findViewById<AdManagerAdView>(R.id.adManagerAdView)
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)
        mAdView!!.setAdListener(object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                Log.v("GoogleAD", "The Ad Loaded!")
                mAdView!!.setVisibility(View.VISIBLE)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.v("GoogleAD", "The Ad failed!")
            }

            override fun onAdOpened() {}
            override fun onAdClicked() {}
            override fun onAdClosed() {}
        })
    }
}