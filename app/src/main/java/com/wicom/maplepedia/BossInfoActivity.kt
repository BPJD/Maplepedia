package com.wicom.maplepedia

import android.app.TabActivity
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.adknowva.adlib.ANClickThroughAction
import com.adknowva.adlib.AdListener
import com.adknowva.adlib.BannerAdView
import com.adknowva.adlib.NativeAdResponse
import com.adknowva.adlib.ResultCode
import com.gomfactory.adpie.sdk.AdPieError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import java.io.FileReader
import java.io.InputStreamReader


class BossInfoActivity : TabActivity() {

    // TODO - AdNetWork
    var isAdLoaded: Boolean = false
    private var adpieView: com.gomfactory.adpie.sdk.AdView? = null
    lateinit var adknowvaView: BannerAdView
    private var admobView: AdManagerAdView? = null
// TODO - AdNetWork

    val bossStats: Array<Int> = arrayOf(
        R.id.boss_stat_nameANDdiff,
        R.id.boss_stat_reqQuest,
        R.id.boss_stat_reqLevel,
        R.id.boss_stat_bossLevel,
        R.id.boss_stat_HP,
        R.id.boss_stat_HP_perPhase,
        R.id.boss_stat_isRevision,
        R.id.boss_stat_bossForce,
        R.id.boss_stat_bossForce
    )

    val bossInfos: Array<Int> = arrayOf(
        0,
        R.id.boss_info_name,
        R.id.boss_info_desc,
        R.id.boss_info_identity_1,
        R.id.boss_info_identity_2,
        R.id.boss_info_map,
    )

    val dataName: Array<String> = arrayOf(
       "TAG", "bossName", "bossDesc", "bossIdentity1", "bossIdentity2", "bossMap"
    )


    var rewardPos: LinearLayout? = null
    var patternPos: LinearLayout? = null

    private var tipPos: LinearLayout? = null

    var bossStrs: Array<String?> = arrayOfNulls<String>(24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss_info)

        tipPos = findViewById(R.id.boss_info_tipPos)
        val bossimg: ImageView = findViewById(R.id.boss_info_img)
        val mDrawableName = "bossimg_" + intent.getStringExtra(dataName[0])
        val resImgID = resources.getIdentifier(mDrawableName, "drawable", packageName)
        bossimg.setImageResource(resImgID)

        for (i in 1..5) {
            val bossinfoText: TextView = findViewById(bossInfos[i])
            bossinfoText.text = intent.getStringExtra(dataName[i])
        }

        CSVLoadANDTabHost()

        adknowvaView = findViewById(R.id.banner_view)
        admobView = findViewById(R.id.adManagerAdView)
        adpieView = findViewById(R.id.adpie_Tbanner)
        setAdpieAD()
    }

    fun CSVLoadANDTabHost() {
        var diff: Int = 0
        var tabHost = this.tabHost

        val assetManager = this.assets
        val inputStream = assetManager.open("boss_" + intent.getStringExtra(dataName[0]) + ".csv")//boss_ 로 시작하는 csv파일 이름. 뒤에 붙는 숫자는 직업 TAG 값
        var reader = CSVReader(InputStreamReader(inputStream))

        reader.readNext()

        try {
            var nextRecord: Array<String>?
            // we are going to read data line by line
            while (reader.readNext().also { nextRecord = it } != null) {
                for(j in 0..10) {
                    bossStrs[j] = nextRecord?.get(j)//보스 정보 1개 가져오기
                    //Log.i("boss", bossStrs[j].toString())
                }

                var tabBossStat = tabHost.newTabSpec(bossStrs[1].toString()).setIndicator(bossStrs[1].toString())
                when(diff) {
                    0 -> tabBossStat.setContent(R.id.boss_info_Tab1)
                    1 -> tabBossStat.setContent(R.id.boss_info_Tab2)
                    2 -> tabBossStat.setContent(R.id.boss_info_Tab3)
                    3 -> tabBossStat.setContent(R.id.boss_info_Tab4)
                }

                //Log.i("boss", bossStrs[0].toString())
                if(bossStrs[0]!!.isNotEmpty()) { //보스 정보가 비어있으면 스킵
                    tabHost.addTab(tabBossStat)
                    BossAdd(diff)
                    diffRewardAdd(diff)
                    diff++
                }


            } //해당 과정을 테이블 행의 수만큼 반복

            tabHost.currentTab = 0
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun diffRewardAdd(diff: Int) {


        val assetManager = this.assets
        val inputStream = assetManager.open("boss_" + intent.getStringExtra(dataName[0]) + ".csv")//boss_ 로 시작하는 csv파일 이름. 뒤에 붙는 숫자는 직업 TAG 값
        var reader = CSVReader(InputStreamReader(inputStream))

        reader.readNext()

        var nexxtrecord: Array<String>?
        // we are going to read data line by line
        while (reader.readNext().also { nexxtrecord = it } != null) {
            bossStrs[11] = nexxtrecord?.get(11 + (diff * 2))//보상 아이템 아이콘
            if(bossStrs[11]!!.isNotEmpty()) {
                bossStrs[12] = nexxtrecord?.get(12 + (diff * 2))//보상 아이템 이름
                RewardAdd(bossStrs[11].toString(), bossStrs[12].toString())
            }
            bossStrs[13] = nexxtrecord?.get(19 + diff)//패턴 정보
            if(bossStrs[13]!!.isNotEmpty()) {
                PatternAdd(bossStrs[13].toString())
            }

            bossStrs[23] = nexxtrecord?.get(23)
            if(diff == 0) { //팁 정보는 한 번만 호출
                TipAdd(bossStrs[23]!!.toString())
            }
        }

    }

    private fun BossAdd(_diff:Int) {

        var bossTab: LinearLayout = findViewById(R.id.boss_info_Tab1)

        when(_diff) {
            0 -> bossTab = findViewById(R.id.boss_info_Tab1)
            1 -> bossTab = findViewById(R.id.boss_info_Tab2)
            2 -> bossTab = findViewById(R.id.boss_info_Tab3)
            3 -> bossTab = findViewById(R.id.boss_info_Tab4)
        }

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_boss_stat, null) //스킬 레이아웃 생성
        bossTab!!.addView(rowView, bossTab!!.childCount)



        val textBossName: TextView = rowView.findViewById(R.id.boss_stat_nameANDdiff)
        val textBossQuest: TextView = rowView.findViewById(R.id.boss_stat_reqQuest)
        val textBossReqLevel: TextView = rowView.findViewById(R.id.boss_stat_reqLevel)
        val textBossLevel: TextView = rowView.findViewById(R.id.boss_stat_bossLevel)
        val textBossHP: TextView = rowView.findViewById(R.id.boss_stat_HP)
        val textBossHPperPhase: TextView = rowView.findViewById(R.id.boss_stat_HP_perPhase)
        val textBossRevision: TextView = rowView.findViewById(R.id.boss_stat_isRevision)
        val textBossDef: TextView = rowView.findViewById(R.id.boss_stat_def)
        val textBossForce: TextView = rowView.findViewById(R.id.boss_stat_bossForce)

        textBossName.text = bossStrs[1] + ' ' + bossStrs[0]
        textBossQuest.text = "선행 퀘스트 : " + bossStrs[2]
        textBossReqLevel.text = "입장 제한 레벨 : " + bossStrs[3]
        textBossLevel.text = "보스 몬스터 레벨 : " + bossStrs[4]
        textBossHP.text = "보스 체력 : " + bossStrs[5] + " (" + bossStrs[6] + ')'
        if(!bossStrs[7].equals("0")) {
            textBossHPperPhase.text = bossStrs[7]?.replace("\\n", "\n")
        }
        else {
            textBossHPperPhase.text = ""
            textBossHPperPhase.height = 0
        }
        if(bossStrs[8]?.toInt() != 0) {
            textBossRevision.text = "모든 속성 공격에 반감"
        }
        else {
            textBossRevision.text = ""
            textBossRevision.height = 0
        }
        textBossDef.text = "방어율 : " + bossStrs[9] + '%'
        textBossForce.text = bossStrs[10]?.replace("\\n", "\n")

        rewardPos = rowView.findViewById(R.id.boss_info_rewardPos)
        patternPos = rowView.findViewById(R.id.boss_info_patternPos)
    }

    fun RewardAdd(icon:String, name:String) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_boss_reward, null) //스킬 레이아웃 생성

        rewardPos!!.addView(rowView, rewardPos!!.childCount)

        val itemIcon: ImageView = rowView.findViewById(R.id.rewardIcon)
        val mDrawableName = "item_$icon"
        val resID = resources.getIdentifier(mDrawableName, "drawable", packageName)
        itemIcon.setImageResource(resID)

        val itemName: TextView = rowView.findViewById((R.id.rewardName))
        itemName.text = name
    }

    fun PatternAdd(patt:String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_boss_pattern, null) //스킬 레이아웃 생성
        patternPos!!.addView(rowView, patternPos!!.childCount)

        val pattern: TextView = rowView.findViewById((R.id.boss_pattern_text))
        pattern.text = patt.replace("\\n", "\n")
        if(patt == "기본 패턴" || patt == "1페이즈" || patt == "2페이즈" || patt == "3페이즈" || patt == "4페이즈" || patt == "맵 패턴" || patt == "공멸형 패턴") {
            pattern.setPadding(12, 32, 0, 0)
            pattern.setTextSize(18f)
        }

    }

    fun TipAdd(tips:String) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_boss_pattern, null) //팁 레이아웃 생성
        tipPos!!.addView(rowView, tipPos!!.childCount)

        val tip: TextView = rowView.findViewById((R.id.boss_pattern_text))
        tip.text = tips

    }

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