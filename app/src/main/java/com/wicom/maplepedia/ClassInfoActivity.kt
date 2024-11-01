package com.wicom.maplepedia

import android.app.TabActivity
import android.content.Context
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TabHost
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
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
import com.opencsv.CSVReader
import java.io.InputStreamReader


class ClassInfoActivity : TabActivity() {

    // TODO - AdNetWork
    var isAdLoaded: Boolean = false
    private var adpieView: com.gomfactory.adpie.sdk.AdView? = null
    lateinit var adknowvaView: BannerAdView
    private var admobView: AdManagerAdView? = null
// TODO - AdNetWork

    private val infos: Array<Int> = arrayOf(
        0,
        R.id.class_info_name,
        R.id.class_info_faction,
        R.id.class_info_type,
        R.id.class_info_mainWeapon,
        R.id.class_info_subWeapon,
        R.id.class_info_mainStat,
        R.id.class_info_subStat,
        R.id.class_status_criRate,
        R.id.class_union_Desc,
        R.id.class_union_B,
        R.id.class_union_A,
        R.id.class_union_S,
        R.id.class_union_SS,
        R.id.class_union_SSS,
        R.id.class_info_linkIcon,
        R.id.class_info_linkName,
        R.id.class_info_linkInfo,
        R.id.class_info_linkDesc
    )

    private var vCorePosition : LinearLayout? = null

    private val dataName: Array<String> = arrayOf(
        "TAG", "className", "classFaction", "classType", "mainWeapon",
        "subWeapon", "mainStat", "subStat", "criRate", "unionInfo",
        "unionB", "unionA", "unionS", "unionSS", "unionSSS",
        "linkIcon", "linkName", "linkInfo", "linkDesc",
        "identityIcon", "identityDesc", "identityIcon2", "identityDesc2", "identityIcon3", "identityDesc3",
        "colorUpR", "colorUpG", "colorUpB", "colorDownR", "colorDownG", "colorDownB"
    )
    private var skillStrs: Array<String?> = arrayOfNulls<String>(8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_info)

        val intent = intent //전달할 데이터를 받을 Intent
        var tabHost = this.tabHost
        vCorePosition = findViewById(R.id.class_info_skillPos5_1)

        if(intent.getStringExtra(dataName[0])?.toInt() == 45) { //제로
            findViewById<TextView>(R.id.skillTierText1).text = "초월자 스킬"
            findViewById<TextView>(R.id.skillTierText2).text = "알파 스킬"
            findViewById<TextView>(R.id.skillTierText4).text = "베타 스킬"
            findViewById<TextView>(R.id.skillTierTextH).text = null
            findViewById<TextView>(R.id.skillTierText3).text = null
            findViewById<TextView>(R.id.skillTierTextH).height = 0
            findViewById<TextView>(R.id.skillTierText3).height = 0

            var tabskillI = tabHost.newTabSpec("O, I").setIndicator("공용")
            tabskillI.setContent(R.id.class_info_skillTab1)
            tabHost.addTab(tabskillI)

            var tabskillII = tabHost.newTabSpec("II, III").setIndicator("알파")
            tabskillII.setContent(R.id.class_info_skillTab2)
            tabHost.addTab(tabskillII)

            var tabskillIII = tabHost.newTabSpec("IV, H").setIndicator("베타")
            tabskillIII.setContent(R.id.class_info_skillTab3)
            tabHost.addTab(tabskillIII)

            var tabskillIV = tabHost.newTabSpec("V, VI").setIndicator("V~VI")
            tabskillIV.setContent(R.id.class_info_skillTab4)
            tabHost.addTab(tabskillIV)

            tabHost.currentTab = 0
        }
        else {

            if(intent.getStringExtra(dataName[0])?.toInt() == 12) { //듀블
                findViewById<TextView>(R.id.skillTierText1).text = "1 / 1+ 차 스킬"
                findViewById<TextView>(R.id.skillTierText2).text = "2 / 2+ 차 스킬"
            }

            var tabskillI = tabHost.newTabSpec("O, I").setIndicator("O~I")
            tabskillI.setContent(R.id.class_info_skillTab1)
            tabHost.addTab(tabskillI)

            var tabskillII = tabHost.newTabSpec("II, III").setIndicator("II~III")
            tabskillII.setContent(R.id.class_info_skillTab2)
            tabHost.addTab(tabskillII)

            var tabskillIII = tabHost.newTabSpec("IV, H").setIndicator("IV~H")
            tabskillIII.setContent(R.id.class_info_skillTab3)
            tabHost.addTab(tabskillIII)

            var tabskillIV = tabHost.newTabSpec("V, VI").setIndicator("V~VI")
            tabskillIV.setContent(R.id.class_info_skillTab4)
            tabHost.addTab(tabskillIV)

            tabHost.currentTab = 0


        }

        val toggleButton : ToggleButton = findViewById(R.id.VCoreHide)
        toggleButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                vCorePosition!!.visibility = View.VISIBLE

            } else {
                vCorePosition!!.visibility = View.GONE
            }
        })

        for (i in 1..30) {
            if(i == 15) { //링크 아이콘은 이미지뷰로 처리. 하는김에 직업 이미지까지 처리
                val linkImgVu: ImageView = findViewById(infos[i])
                val mDrawableName = "skill_" + intent.getStringExtra(dataName[i]) + "_icon"
                val resIconID = resources.getIdentifier(mDrawableName, "drawable", packageName)
                linkImgVu.setImageResource(resIconID)

                val classImgVu: ImageView = findViewById(R.id.class_info_img)
                val iDrawableName = "class_" + intent.getStringExtra(dataName[0])
                val resImgID = resources.getIdentifier(iDrawableName, "drawable", packageName)
                classImgVu.setImageResource(resImgID)
            }
            else if(i == 19 || i == 21 || i == 23) { // 직업 특성은 레이아웃 생성으로 처리

                if(intent.getStringExtra(dataName[i])?.toInt() != 0) {

                    val identityLayout: LinearLayout = findViewById(R.id.class_info_identityPos)
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val rowView: View = inflater.inflate(R.layout.layout_classinfo_identity, null) //스킬 레이아웃 생성
                    identityLayout!!.addView(rowView, identityLayout!!.childCount)

                    val identityIcon: ImageView = rowView.findViewById(R.id.class_info_identityIcon)
                    val mDrawableName = "skill_" + intent.getStringExtra(dataName[i]) + "_icon"
                    val resID = resources.getIdentifier(mDrawableName, "drawable", packageName)
                    identityIcon.setImageResource(resID)

                    val identityDescText:TextView = rowView.findViewById(R.id.class_info_identityDesc)
                    identityDescText.text = (intent.getStringExtra(dataName[i + 1]))//text 키값으로 데이터를 받는다. String을 받아야 하므로 getStringExtra()를 사용함

                }

            }
            else { //나머지는 텍스트로 처리
                if(i <= 19) {
                    val textVu:TextView = findViewById(infos[i])
                    textVu.text = intent.getStringExtra(dataName[i])?.replace("\\n", "\n")//text 키값으로 데이터를 받는다. String을 받아야 하므로 getStringExtra()를 사용함
                }
            }

            val backgroundUp: ImageView = findViewById(R.id.background_up)
            backgroundUp.setColorFilter(Color.rgb(intent.getStringExtra(dataName[25])!!.toInt(), intent.getStringExtra(dataName[26])!!.toInt(), intent.getStringExtra(dataName[27])!!.toInt()))

            val backgroundDown: ImageView = findViewById(R.id.background_down)
            backgroundDown.setColorFilter(Color.rgb(intent.getStringExtra(dataName[28])!!.toInt(), intent.getStringExtra(dataName[29])!!.toInt(), intent.getStringExtra(dataName[30])!!.toInt()))
            //Log.i("count", i.toString())
        }
        SkillLoad()

        adknowvaView = findViewById(R.id.banner_view)
        admobView = findViewById(R.id.adManagerAdView)
        adpieView = findViewById(R.id.adpie_Tbanner)
        setAdpieAD()
    }

    fun SkillLoad() { //스킬 읽어오기
        val assetManager = this.assets
        val inputStream = assetManager.open("skillOf_" + intent.getStringExtra(dataName[0]) + ".csv") //skillOf_ 로 시작하는 csv파일 이름. 뒤에 붙는 숫자는 직업 TAG 값
        val reader = CSVReader(InputStreamReader(inputStream))
        try {
            var nextRecord: Array<String>?
            // we are going to read data line by line
            while (reader.readNext().also { nextRecord = it } != null) {
                for(j in 0..7) {
                    skillStrs[j] = nextRecord?.get(j)//스킬 정보 1개 가져오기
                    //Log.i("skill", skillStrs[j].toString())
                }
                skillAdd() //스킬 하나 레이아웃 생성하고, 거기에 가져온 스킬 정보 붙이기.
            } //해당 과정을 테이블 행의 수만큼 반복
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun skillAdd() {

        var skillLayout: LinearLayout = findViewById(R.id.class_info_skillPos0)
        when(skillStrs[0]?.toInt()) {
            0 -> skillLayout = findViewById(R.id.class_info_skillPos0)
            1 -> skillLayout = findViewById(R.id.class_info_skillPos1)
            2 -> skillLayout = findViewById(R.id.class_info_skillPos2)
            3 -> skillLayout = findViewById(R.id.class_info_skillPos3)
            4 -> skillLayout = findViewById(R.id.class_info_skillPos4)
            5 -> skillLayout = findViewById(R.id.class_info_skillPosH)
            6 -> {
                if(skillStrs[3] == "스킬 강화") {
                    skillLayout = findViewById(R.id.class_info_skillPos5_1)
                }
                else {
                    skillLayout = findViewById(R.id.class_info_skillPos5)
                }
            }
            7 -> skillLayout = findViewById(R.id.class_info_skillPos6)
        }
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_classinfo_skill, null) //스킬 레이아웃 생성
        skillLayout!!.addView(rowView, skillLayout!!.childCount)

        val skillIcon: ImageView = rowView.findViewById(R.id.class_info_skillIcon)
        val mDrawableName = "skill_" + skillStrs[1] + "_icon"
        val resID = resources.getIdentifier(mDrawableName, "drawable", packageName)
        skillIcon.setImageResource(resID)

        val textSkillName: TextView = rowView.findViewById(R.id.class_info_skillName)
        val textSkillTypes: TextView = rowView.findViewById(R.id.class_info_skillType)
        val textSkillInfo: TextView = rowView.findViewById(R.id.class_info_skillInfo)
        val textSkillDesc: TextView = rowView.findViewById(R.id.class_info_skillDesc)
        val textCoolTime: TextView = rowView.findViewById(R.id.class_info_skillCoolTime)

        textSkillName.text = skillStrs[2]
        if(skillStrs[0]?.toInt() == 5) {
            textSkillTypes.text = "[" + skillStrs[3] + ", 필요 레벨 " + skillStrs[4] + "]"
        }
        else {
            textSkillTypes.text = "[" + skillStrs[3] + ", 마스터레벨 " + skillStrs[4] + "]"
        }

        textSkillInfo.text = skillStrs[5]?.replace("\\n", "\n")
        textSkillDesc.text = skillStrs[6]?.replace("\\n", "\n")
        if(skillStrs[7]?.toFloat() == 0f) {
            //textCoolTime.text = null
            //textCoolTime.height = 0
            textCoolTime.visibility = View.GONE
        }
        else {
            textCoolTime.text = "[" + "재사용 대기시간 : " + skillStrs[7] + "초" + "]"
        }
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