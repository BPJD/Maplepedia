package com.wicom.maplepedia

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
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
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

class SimulatorCombat : AppCompatActivity() {

    private var myLevelEdt : EditText? = null
    private var myForceEdt : EditText? = null
    private var mobForceEdt : EditText? = null
    private var mobLevelEdt : EditText? = null

    var forceRbts : Array<RadioButton?> = arrayOfNulls(3)

    private var selectedRbt : Int = 0

    private var resultTxt_lvDesc : TextView? = null
    private var resultTxt_forceDesc : TextView? = null
    private var resultTxt_sendDamage : TextView? = null
    private var resultTxt_exp : TextView? = null

    // TODO - AdNetWork
    var isAdLoaded: Boolean = false
    private var adpieView: com.gomfactory.adpie.sdk.AdView? = null
    lateinit var adknowvaView: BannerAdView
    private var admobView: AdManagerAdView? = null
// TODO - AdNetWork

    private var vCorePosition : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simulate_force_revision)

        myLevelEdt = findViewById(R.id.sim_rev_myLevel)
        myForceEdt = findViewById(R.id.sim_rev_myForce)
        mobForceEdt = findViewById(R.id.sim_rev_mobForce)
        mobLevelEdt = findViewById(R.id.sim_rev_mobLevel)

        forceRbts = arrayOf(findViewById(R.id.sim_rev_rbtStar), findViewById(R.id.sim_rev_rbtArcane), findViewById(R.id.sim_rev_rbtAscentic))

        resultTxt_lvDesc = findViewById(R.id.sim_rev_descLv)
        resultTxt_forceDesc = findViewById(R.id.sim_rev_descForce)
        resultTxt_sendDamage = findViewById(R.id.sim_rev_sendDamage)
        resultTxt_exp = findViewById(R.id.sim_rev_exp)

        vCorePosition = findViewById(R.id.sim_rev_preset)

        val toggleButton: ToggleButton = findViewById(R.id.sim_rev_presetBtn)
        toggleButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                vCorePosition!!.visibility = View.VISIBLE

            } else {
                vCorePosition!!.visibility = View.GONE
            }
        })

        adknowvaView = findViewById(R.id.banner_view)
        admobView = findViewById(R.id.adManagerAdView)
        adpieView = findViewById(R.id.adpie_Tbanner)
        setAdpieAD()
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.sim_rev_rbtStar -> {
                    selectedRbt = 1
                }
                R.id.sim_rev_rbtArcane -> {
                    selectedRbt = 2
                }
                R.id.sim_rev_rbtAscentic -> {
                    selectedRbt = 3
                }
            }
            editTextSet(selectedRbt)
        }
    }

    private fun editTextSet(_selectedRbt : Int) {
        when(_selectedRbt) {
            1 -> {
                myForceEdt?.hint = "내 스타포스"
                mobForceEdt?.hint = "몬스터 스타포스"
            }
            2 -> {
                myForceEdt?.hint = "내 아케인포스"
                mobForceEdt?.hint = "몬스터 아케인포스"
            }
            3 -> {
                myForceEdt?.hint = "내 어센틱포스"
                mobForceEdt?.hint = "몬스터 어센틱포스"
            }
        }
    }

    fun applyClicked (v : View) {
        sendDamage = 0f
        exp = 0

        if(myLevelEdt!!.text.isEmpty()) {
            myLevelEdt?.setText("0")
        }
        if(myForceEdt!!.text.isEmpty()) {
            myForceEdt?.setText("0")
        }
        if(mobLevelEdt!!.text.isEmpty()) {
            mobLevelEdt?.setText("0")
        }
        if(mobForceEdt!!.text.isEmpty()) {
            mobForceEdt?.setText("0")
            myForceEdt?.setText("0")
        }

        if(myLevelEdt!!.text.toString().toInt() >= 300) {
            myLevelEdt?.setText("300")
        }

        val iMyLevel : Int = myLevelEdt?.text.toString().toInt()
        val iMyForce : Int = myForceEdt?.text.toString().toInt()
        val iMobLevel : Int = mobLevelEdt?.text.toString().toInt()
        val iMobForce : Int = mobForceEdt?.text.toString().toInt()

        val resLv : Int = iMyLevel - iMobLevel

        setLevelDesc(resLv)
        setForceDesc(iMyForce, iMobForce)
        setExpDesc(resLv)


        val damageForTxt : Float = sendDamage
        if(sendDamage < 0f) {
            resultTxt_sendDamage!!.text = "주는 최종데미지 " + String.format("%.1f", abs(damageForTxt)) + "% 감소"
        }
        else {
            resultTxt_sendDamage!!.text = "주는 최종데미지 " + String.format("%.1f", damageForTxt) + "% 증가"
        }
    }

    var sendDamage : Float = 0f
    var exp : Int = 0

    private fun setLevelDesc(disLv : Int) {
        if(disLv <= -40) {
            resultTxt_lvDesc!!.text = "캐릭터 레벨이 몬스터보다 " + 40 +" 이상 낮습니다."
            sendDamage += -100f
        }
        else if(disLv < 0) {
            resultTxt_lvDesc!!.text = "캐릭터 레벨이 몬스터보다 " + abs(disLv) +" 낮습니다."
            if(disLv >= -4) {
                sendDamage += 10f + -(5f * abs(disLv))
            }
            else {
                sendDamage += 10f + -(2.5f * abs(disLv))
            }
        }
        else if(disLv == 0) {
            resultTxt_lvDesc!!.text = "캐릭터 레벨이 몬스터와 같습니다."
            sendDamage += 10f
        }
        else if(disLv in 1..4) {
            resultTxt_lvDesc!!.text = "캐릭터 레벨이 몬스터보다 " + abs(disLv) +" 높습니다."

            sendDamage += (10f + (2f * abs(disLv)))
        }
        else if(disLv >= 5) {
            resultTxt_lvDesc!!.text = "캐릭터 레벨이 몬스터보다 " + 5 +" 이상 높습니다."
            sendDamage += 20f
        }
    }

    private fun setForceDesc(myForce : Int, mobForce : Int) {


        if(mobForce == 0) {
            resultTxt_forceDesc!!.text = "적에게 포스가 없습니다."
        }
        else {
            when(selectedRbt) {
                1 -> {
                    val disStar : Float = (myForce.toFloat() / mobForce.toFloat())

                    if(disStar < 0.1f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 10% 미만입니다."
                        sendDamage += -100f
                    }
                    else if(disStar < 0.3f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 30% 미만입니다."
                        sendDamage += -90f
                    }
                    else if(disStar < 0.5f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 50% 미만입니다."
                        sendDamage += -70f
                    }
                    else if(disStar < 0.7f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 70% 미만입니다."
                        sendDamage += -50f
                    }
                    else if(disStar < 1f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 100% 미만입니다."
                        sendDamage += -30f
                    }
                    else if(disStar.roundToInt() == 1) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스와 같습니다."
                        sendDamage += 0f
                    }
                    else {
                        if((myForce - mobForce) <= 19) {
                            resultTxt_forceDesc!!.text = "내 포스가 적 포스보다 " + (myForce - mobForce) + " 높습니다."
                            sendDamage += (myForce - mobForce)
                        }
                        else {
                            resultTxt_forceDesc!!.text = "내 포스가 적 포스보다 20 이상 높습니다."
                            sendDamage += 20f
                        }
                    }

                }

                2 -> {
                    val disArc : Float = (myForce.toFloat() / mobForce.toFloat())

                    if(disArc < 0.1f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 10% 미만입니다."
                        sendDamage += -90f
                    }
                    else if(disArc < 0.3f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 30% 미만입니다."
                        sendDamage += -70f
                    }
                    else if(disArc < 0.5f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 50% 미만입니다."
                        sendDamage += -40f
                    }
                    else if(disArc < 0.7f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 70% 미만입니다."
                        sendDamage += -30f
                    }
                    else if(disArc < 1f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 100% 미만입니다."
                        sendDamage += -20f
                    }
                    else if(disArc < 1.1f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 100% 를 초과합니다."
                        sendDamage += 0f
                    }
                    else if(disArc < 1.3f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 110% 를 초과합니다."
                        sendDamage += 10f
                    }
                    else if(disArc < 1.5f) {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 130% 를 초과합니다."
                        sendDamage += 30f
                    }
                    else {
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스의 150% 를 초과합니다."
                        sendDamage += 50f
                    }

                }

                3 -> {
                    val disAsc : Int = myForce - mobForce


                    if(disAsc <= -95) {
                        sendDamage += -95f
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스보다 95 이상 낮습니다."
                    }
                    else if(disAsc >= 50) {
                        sendDamage += 25f
                        resultTxt_forceDesc!!.text = "내 포스가 적 포스보다 50 이상 높습니다."
                    }
                    else {
                        if(disAsc < 0) {
                            val toFlt : Int = floor(abs(disAsc) * 0.1f).toInt()
                            sendDamage += -(10f * toFlt)
                            resultTxt_forceDesc!!.text = "내 포스가 적 포스보다" + abs(disAsc) + 0 + " 낮습니다."

                        }
                        else if(disAsc == 0) {
                            resultTxt_forceDesc!!.text = "내 포스가 적 포스와 같습니다."
                            sendDamage += 0f
                        }
                        else if(disAsc >= 10) {
                            if(disAsc >= 50) {
                                sendDamage += 25f
                                resultTxt_forceDesc!!.text = "내 포스가 적 포스보다 50 이상 높습니다."
                            }
                            else {
                                val toFlt : Int = floor(disAsc * 0.1f).toInt()
                                sendDamage += (5f * toFlt)
                                resultTxt_forceDesc!!.text = "내 포스가 적 포스보다" + abs(disAsc) + 0 + " 높습니다."
                            }
                        }
                    }


                }
            }
        }

    }

    private fun setExpDesc(disLv : Int) {

        if(disLv >= 40) {
            exp = 70
        }
        else if(disLv > 20) {
            exp = (90 - (disLv - 20))
        }
        else if(disLv >= 19) {
            exp = 95
        }
        else if(disLv >= 17) {
            exp = 96
        }
        else if(disLv >= 15) {
            exp = 97
        }
        else if(disLv >= 13) {
            exp = 98
        }
        else if(disLv >= 11) {
            exp = 99
        }
        else if(disLv >= 10) {
            exp = 100
        }
        else if(disLv >= 5) {
            exp = 105
        }
        else if(disLv >= -4) {
            exp = 110
        }
        else if(disLv >= -9) {
            exp = 105
        }
        else if(disLv >= -20) {
            exp = 100 - (abs(disLv) - 10)
        }
        else if(disLv >= -35) {
            exp = 70 - ((abs(disLv) - 20) * 4)
        }
        else if(disLv >= -39) {
            exp = (10)
        }

        if(disLv <= -40) {
            resultTxt_exp!!.text = "획득 경험치 100 고정"
        }
        else {
            val expForTxt : Int = exp - 100
            if(expForTxt < 0) {
                resultTxt_exp!!.text = "경험치 획득량 " + abs(expForTxt) + "% 감소"
            }
            else {
                resultTxt_exp!!.text = "경험치 획득량 " + abs(expForTxt) + "% 증가"
            }
        }


    }

    fun presetClicked (v : View) {
        forceRbts[1]?.isChecked = true
        when(v.tag.toString().toInt()) {
            0 -> {
                mobLevelEdt?.setText("210")
                mobForceEdt?.setText("0")
                selectedRbt = 2
            }
            1 -> {
                mobLevelEdt?.setText("230")
                mobForceEdt?.setText("360")
                selectedRbt = 2
            }
            2 -> {
                mobLevelEdt?.setText("235")
                mobForceEdt?.setText("560")
                selectedRbt = 2
            }
            3 -> {
                mobLevelEdt?.setText("250")
                mobForceEdt?.setText("760")
                selectedRbt = 2
            }
            4 -> {
                mobLevelEdt?.setText("255")
                mobForceEdt?.setText("730")
                selectedRbt = 2
            }
            5 -> {
                mobLevelEdt?.setText("265")
                mobForceEdt?.setText("850")
                selectedRbt = 2
            }
            6 -> {
                mobLevelEdt?.setText("250")
                mobForceEdt?.setText("820")
                selectedRbt = 2
            }
            7 -> {
                mobLevelEdt?.setText("250")
                mobForceEdt?.setText("900")
                selectedRbt = 2
            }
        }
        editTextSet(selectedRbt)
    }

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
                    setGoogleAD()
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
                    setHuvleAD()
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