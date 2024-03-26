package com.byappsoft.sap;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.byappsoft.sap.browser.Sap_BrowserActivity;
import com.byappsoft.sap.browser.Sap_MainActivity;
import com.byappsoft.sap.launcher.NotibarConfig;
import com.wicom.maplepedia.MainActivity;
import com.wicom.maplepedia.R;
import com.wicom.maplepedia.RefWeaponOptionActivity;

public class CustomNotibarConfig extends NotibarConfig {

/*
    동의창 이미지 수정시 아래 주석 해제후 앱사 현황에 맞게 수정.
    동의창 내용은 언어별로 총 5가지가 제공되며 사이즈는 800(w)X650(h) 입니다.

    또는 return R.drawable.res_sap_notiba_img;
    drawable-ko, drawable-vi...... 폴더 만들어서 동일한 이름으로 언어별 셋팅
*/
//    @Override
//    public int getNotibarPopupBg() {
//        switch (Locale.getDefault().getLanguage()) {
//            case "ko":
//                return R.drawable.res_sap_notiba_img_cn;
//            case "zh":
//                return R.drawable.res_sap_notiba_img_cn;
//            case "vi":
//                return R.drawable.res_sap_notiba_img_vi;
//            case "ja":
//                return R.drawable.res_sap_notiba_img_ja;
//            default:
//                return R.drawable.res_sap_notiba_img_en;
//        }
//    }


/*
    노티바 수정시 1번~5번 아이콘까지 수정이 가능합니다.
    아래 주석 해제후 실행하시면 수정된 노티바를 확인하실 수 있습니다.
*/

    public int getNotibarIcon1() { return R.mipmap.ic_launcher; }
    public int getNotibarString1() { return R.string.noti_1; }
    public void callNotibar1(Activity activity, String nt) {
        try{
            Intent callIntent = new Intent(activity, MainActivity.class);
            activity.startActivity(callIntent);
            activity.finish();
        }catch (Exception e){
            showNotfoundPackagePopup(activity, nt);
        }
    }





    public int getNotibarIcon2() { return R.drawable.noti_profile; }
    public int getNotibarString2() { return R.string.noti_2; }
    public void callNotibar2(Activity activity, String nt) {
        try{
            Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maple.gg/"));
            activity.startActivity(callIntent);
            activity.finish();

        }catch (Exception e){
            showNotfoundPackagePopup(activity, nt);
        }
    }

    public int getNotibarIcon3() { return R.drawable.noti_stat; }
    public int getNotibarString3() { return R.string.noti_3; }
    public void callNotibar3(Activity activity, String nt) {
        try{
            Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maplescouter.com/"));
            activity.startActivity(callIntent);
            activity.finish();
        }catch (Exception e){
            showNotfoundPackagePopup(activity, nt);
        }
    }


    public int getNotibarIcon4() { return R.drawable.noti_weapon; }
    public int getNotibarString4() { return R.string.noti_4; }
    public void callNotibar4(Activity activity, String nt) {
        try{
            Intent callIntent = new Intent(activity, RefWeaponOptionActivity.class);
            activity.startActivity(callIntent);
            activity.finish();
        }catch (Exception e){
            showNotfoundPackagePopup(activity, nt);
        }
    }
//

    public int getNotibarIcon5() { return R.drawable.noti_setting; }
    public int getNotibarString5() { return R.string.noti_5; }


    public int getNotibarIcon6() { return R.drawable.noti_search; }
    public int getNotibarString6() { return R.string.noti_6; }

//    public void callNotibar5(Activity activity, String nt) {
//        try{
//            Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.daum.net"));
//            activity.startActivity(callIntent);
//            activity.finish();
//        }catch (Exception e){
//            showNotfoundPackagePopup(activity, nt);
//        }
//    }
}
