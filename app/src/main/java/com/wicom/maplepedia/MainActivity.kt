package com.wicom.maplepedia

import android.Manifest
import android.app.AlarmManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.drawerlayout.widget.DrawerLayout
import com.adknowva.adlib.ANClickThroughAction
import com.adknowva.adlib.AdListener
import com.adknowva.adlib.BannerAdView
import com.adknowva.adlib.NativeAdResponse
import com.adknowva.adlib.ResultCode
import com.byappsoft.sap.browser.Sap_BrowserActivity
import com.byappsoft.sap.browser.Sap_MainActivity
import com.byappsoft.sap.launcher.Sap_act_main_launcher
import com.byappsoft.sap.utils.Sap_Func
import com.gomfactory.adpie.sdk.AdPieError
import com.gomfactory.adpie.sdk.AdPieSDK
import com.gomfactory.adpie.sdk.AdView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    private var isDrawerOpened = false;

    // TODO - AdNetWork
    var isAdLoaded: Boolean = false
    private var adpieView: AdView? = null
    lateinit var adknowvaView: BannerAdView
    lateinit var adknowvaPop: BannerAdView
    private var admobView: AdManagerAdView? = null
// TODO - AdNetWork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        AdPieSDK.getInstance()
            .initialize(getApplicationContext(), "653874688c8d1d335d680645"); //애드파이의 매체식별

        val toolbar: Toolbar = findViewById(R.id.toolbar) // toolBar를 통해 App Bar 생성
        setSupportActionBar(toolbar) // 툴바 적용

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(true) // 툴바에 타이틀 보이게

        // 네비게이션 드로어 생성
        drawerLayout = findViewById(R.id.drawer_layout)

        // 네비게이션 드로어 내에있는 화면의 이벤트를 처리하기 위해 생성
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this) //navigation 리스너


        //안드로이드 13

        //layoutNative = findViewById(R.id.layout_native);

        //adRequest = setupNativeAd();
        //adRequest.loadAd();

        // 안드로이드 13 이상 알림권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkPermission()) {
                requestSapPermissions()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                    val pref = getSharedPreferences("alarm", MODE_PRIVATE)
                    val prefBool : Boolean = pref.getBoolean("alarm", false)
                    if(!prefBool) {
                        checkExactAlarm()

                    }

                }
            }
        }

        adknowvaView = findViewById(R.id.banner_view)
        admobView = findViewById(R.id.adManagerAdView)
        adpieView = findViewById(R.id.adpie_Tbanner)

        // 광고 연동을 위한 슬롯 ID를 필수로 입력한다.
        reqAd(0)

    }



    // TODO - Adknowva SDK Library
    private fun setHuvlePop(dialog : Dialog) {

        adknowvaPop = dialog.findViewById(R.id.dialog_pop_view)

        Log.v("HuvlePOP", adknowvaPop.toString())

        // 정적으로 구현시(When if apply Static Implementation) BannerAdView Start
        adknowvaPop.placementID = "testbig" // 320*50 banner testID , 300*250 banner test ID "testbig", 32050 Z916x23725,  300250 Zzo598u6rz
        adknowvaPop.shouldServePSAs = false
        adknowvaPop.clickThroughAction = ANClickThroughAction.OPEN_DEVICE_BROWSER
        adknowvaPop.setAdSize(300, 250) //bav.setAdSize(300, 250);
        // Resizes the container size to fit the banner ad
        adknowvaPop.resizeAdToFitContainer = true
//        bav.setExpandsToFitScreenWidth(true)
        val adListener: AdListener = object : AdListener {
            override fun onAdRequestFailed(
                bav: com.adknowva.adlib.AdView,
                errorCode: ResultCode
            ) {
                if (errorCode == null) {
                    Log.v("HuvlePOP", "Call to loadAd failed")
                } else {
                    Log.v("HuvlePOP", "Ad request failed: $errorCode")
                }
                bav.visibility = View.INVISIBLE

                //adpieView?.visibility = View.GONE
                //setAdpieAD()
            }
            override fun onAdLoaded(ba: com.adknowva.adlib.AdView) {
                Log.v("HuvlePOP", "The Ad Loaded!")
            }
            override fun onAdLoaded(nativeAdResponse: NativeAdResponse) {}
            override fun onAdExpanded(bav: com.adknowva.adlib.AdView) {}
            override fun onAdCollapsed(bav: com.adknowva.adlib.AdView) {}
            override fun onAdClicked(bav: com.adknowva.adlib.AdView) {}
            override fun onAdClicked(adView: com.adknowva.adlib.AdView, clickUrl: String) {}
            override fun onLazyAdLoaded(adView: com.adknowva.adlib.AdView) {}
        }
        adknowvaPop.adListener = adListener
        adknowvaPop.loadAd()

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
                    reqAd(2)
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
                //reqAd(1)
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

    fun customExitDialog() {
        // creating custom dialog
        val dialog = Dialog(this)

        // setting content view to dialog
        dialog.setContentView(R.layout.dialog_profile)

        adknowvaPop = dialog.findViewById(R.id.dialog_pop_view)




        // getting reference of TextView
        val dialogButtonYes = dialog.findViewById(R.id.dialog_yes) as TextView
        val dialogButtonNo = dialog.findViewById(R.id.dialog_no) as TextView

        // click listener for No
        dialogButtonNo.setOnClickListener { // dismiss the dialog
            dialog.dismiss()
        }
        // click listener for Yes
        dialogButtonYes.setOnClickListener { // dismiss the dialog and exit the exit
            dialog.dismiss()
            finish()
        }

        // show the exit dialog
        dialog.show()

        //setHuvlePop(dialog)
    }
    //THis code is written by Ujjwal kumar bhardwaj

    override fun onBackPressed() {   // 뒤로가기 누르면 다이얼로그 생성
        customExitDialog()
    }

    fun exit() { // 종료
        super.onBackPressed()
    }

    // 툴바 메뉴 버튼이 클릭 됐을 때 실행하는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        isDrawerOpened = true;

        // 클릭한 툴바 메뉴 아이템 id 마다 다르게 실행하도록 설정
        when(item!!.itemId){
            android.R.id.home->{
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // 드로어 내 아이템 클릭 이벤트 처리하는 함수
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var nextIntent: Intent? = null;

        when(item.itemId){
            R.id.menu_class-> {
                nextIntent = Intent(this, ClassActivity::class.java)
            }

            R.id.menu_boss-> {
                nextIntent = Intent(this, BossActivity::class.java)
            }

            R.id.menu_refdata-> {
                nextIntent = Intent(this, ReferenceActivity::class.java)
            }

            R.id.menu_dev-> {
                nextIntent = Intent(this, DeveloperActivity::class.java)
            }

            R.id.menu_combat-> {
                nextIntent = Intent(this, SimulatorCombat::class.java)
            }

//            R.id.menu_genesis-> {
//                nextIntent = Intent(this, SimulatorGenesis::class.java)
//            }

            R.id.menu_huvle-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://www.huvle.com/")
            }


            R.id.menu_maplegg-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://maple.gg/")
            }

            R.id.menu_stat-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://maplescouter.com/")
            }

            R.id.menu_starforce-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://mesu.live/sim/starforce")
            }



            R.id.menu_notice-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://m.maplestory.nexon.com/News/Notice")
            }
            R.id.menu_update-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://m.maplestory.nexon.com/News/Update")
            }
            R.id.menu_event-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://m.maplestory.nexon.com/News/Event")
            }
            R.id.menu_community-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://m.maplestory.nexon.com/Community/N23Free")
            }
            R.id.menu_ranking-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://m.maplestory.nexon.com/N23Ranking/World/Total")
            }
            R.id.menu_tserver-> {
                nextIntent = Intent(this, Sap_MainActivity::class.java)
                nextIntent.putExtra(Sap_BrowserActivity.PARAM_OPEN_URL, "https://maplestory.nexon.com/Testworld/Main")
            }

        }

        startActivity(nextIntent);
        return false
    }


    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkPermission()) {
                if (Build.VERSION.SDK_INT >= 34) {
                    //Sap_Func.setNotiBarState(this, true)
                    //Sap_Func.setServiceState(this, true) //notibar 사용 시 true 변경
                }
                huvleView()
            }
        } else {
            huvleView()
        }
    }

    private fun huvleView() {

        // TODO-- huvleView apply
        Sap_Func.setNotiBarLockScreen(this,false)
        Sap_act_main_launcher.initsapStart(this,"judei1028",true,true,
            object : Sap_act_main_launcher.OnLauncher {
                override fun onDialogOkClicked() {
                    checkDrawOverlayPermission()
                }

                override fun onDialogCancelClicked() {
                }

                override fun onInitSapStartapp() {
                }

                override fun onUnknown() {
                }

            })
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun requestSapPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) requestPermissions(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ), 0
            )
        } catch (ignored: Exception) {
        }
    }

    private	fun checkDrawOverlayPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        return if (!Settings.canDrawOverlays(this)) {
            AlertDialog.Builder(this)
                .setTitle("다른앱 위에 그리기")
                .setMessage("허블뷰 사용을 위한 권한입니다.\n앱 사용에 필수는 아니므로\n허용하지 않아도 됩니다.")
                .setPositiveButton("확인") { dialog, which ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                    val uri = Uri.parse("package:$packageName")
                    intent.data = uri
                    startActivity(intent)
                }
                .setNegativeButton(
                    "취소"
                ) { dialog, which -> dialog.cancel() }
                .create()
                .show()
            false
        } else {
            true
        }
    }

    fun checkExactAlarm(): Boolean {

        val pref = getSharedPreferences("alarm", MODE_WORLD_READABLE)
        val edit = pref.edit()

        edit.putBoolean("alarm", true)
        edit.apply()

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            return true
        }
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val canScheduleExactAlarms = alarmManager.canScheduleExactAlarms()
        return if (!canScheduleExactAlarms) {
            AlertDialog.Builder(this)
                .setTitle("알람 및 리마인더 허용")
                .setMessage("알람 및 리마인더 권한을 허용해주세요.\n앱 사용에 필수는 아니므로\n허용하지 않아도 됩니다.")
                .setPositiveButton("확인") { dialog, which ->
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }
                .setNegativeButton(
                    "취소"
                ) { dialog, which -> dialog.cancel() }
                .create()
                .show()
            false
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
//            if (Settings.canDrawOverlays(this)) {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                val pref = getSharedPreferences("alarm", MODE_PRIVATE)
                val prefBool : Boolean = pref.getBoolean("alarm", false)
                if(!prefBool) {
                    checkExactAlarm()
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // 이 부분이 추가되었습니다.

        if (requestCode == 0) {
            if (checkPermission()) {
                // Post notification 권한이 허용된 경우를 확인합니다.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                    val pref = getSharedPreferences("alarm", MODE_PRIVATE)
                    val prefBool : Boolean = pref.getBoolean("alarm", false)
                    if(!prefBool) {
                        checkExactAlarm()
                    }

                }
            }
        }
    }


}


