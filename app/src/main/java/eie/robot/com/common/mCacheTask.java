package eie.robot.com.common;

import android.content.ComponentName;
import android.content.Intent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.view.RxToast;

import eie.robot.com.accessibilityservice.AccessibilityHelper;

public class mCacheTask {

    //清理手机缓存
    public static void ClearPhoneCacheTask(){
        try{
            if(!mCommonTask.ThreadTaskOpenStatus){
                return;
            }
//            while (true){
//                if(mCommonTask.AppTaskCloseSuccessStatus){
//                    break;
//                }
//            }
            mFunction.sleep(mConfig.clickSleepTime);
            String UniqueSerialNumber = RxDeviceTool.getUniqueSerialNumber().toLowerCase();
            int index = -1;
            index = UniqueSerialNumber.toLowerCase().indexOf("xiaomi");
            //小米手机版清理数据
            if(index > -1){
                ClearXiaoMiPhoneCache();
            }
            index = UniqueSerialNumber.toLowerCase().indexOf("lenovo");
            index = UniqueSerialNumber.toLowerCase().indexOf("huawei");
        }catch (Exception ex){
            RxToast.success("清理内存报错:"+ex.getMessage());
        }


    }
    //清楚小米手机的应用缓存
    private static void ClearXiaoMiPhoneCache(){

        if(!mFunction.loopOpenApp("安全中心")){
            return;
        }
        RxToast.success("开始清理手机内存");

        AccessibilityNodeInfo nodeInfo = null;
        int count = 3 * mConfig.loopCount;
        while (count > 0) {
            nodeInfo = AccessibilityHelper.findNodeInfosByText("选中垃圾");
            if ( nodeInfo != null ) {
                break;
            }
            nodeInfo = AccessibilityHelper.findNodeInfosById("com.miui.securitycenter:id/iv_icon1");
            if ( nodeInfo != null ) {
                mGestureUtil.performClick(nodeInfo);
                mFunction.sleep(2*mConfig.clickSleepTime);
                continue;
            }
            count--;
            mFunction.click_sleep();
        }
        if(nodeInfo != null){
            mGestureUtil.click(nodeInfo);
            mFunction.sleep(15*1000);
        }
        RxToast.success("清理手机内存完成");
    }

    //清理手机内存
    public static void ClearPhoneROMTask(){
        try{
            if(!mCommonTask.ThreadTaskOpenStatus){
                return;
            }
            AccessibilityHelper.performHome();
            mFunction.sleep(mConfig.clickSleepTime);
            String UniqueSerialNumber = RxDeviceTool.getUniqueSerialNumber().toLowerCase();
            int index = -1;
            index = UniqueSerialNumber.toLowerCase().indexOf("xiaomi");

            //小米手机版清理数据
            if(index > -1){
                ClearXiaoMiPhoneROM();
            }
            index = UniqueSerialNumber.toLowerCase().indexOf("lenovo");
            if(index > -1){
                ClearLenovoPhoneROM();
            }
            index = UniqueSerialNumber.toLowerCase().indexOf("huawei");
            if(index > -1){
                ClearHuaWeiPhoneROM();
            }
            index = UniqueSerialNumber.toLowerCase().indexOf("4g-4g");
            if(index > -1){
                Clear4G4GPhoneROM();
            }
        }catch (Exception ex){
            RxToast.success("清理内存报错:"+ex.getMessage());
        }


    }
    //清除小米手机的内存
    private static void ClearXiaoMiPhoneROM(){
        AccessibilityHelper.performRecents();
        mFunction.click_sleep();
        AccessibilityNodeInfo nodeInfo = AccessibilityHelper.findNodeInfosById("com.android.systemui:id/clearAnimView");
        if(nodeInfo != null){
            mGestureUtil.click(nodeInfo);
        }
        mGestureUtil.scroll_down_screen();
        mGestureUtil.clickByResourceId("com.android.systemui:id/dismiss_view");
        mGestureUtil.clickByResourceId("com.android.systemui:id/clear_all_button");


    }
    //清除联想手机的内存
    private static void ClearLenovoPhoneROM(){
        AccessibilityHelper.performRecents();
        mFunction.click_sleep();
        AccessibilityNodeInfo nodeInfo = AccessibilityHelper.findNodeInfosById("com.android.systemui:id/recents_cleanup");
        if(nodeInfo != null){
            mGestureUtil.click(nodeInfo);
        }

        mGestureUtil.scroll_down_screen();
        nodeInfo = AccessibilityHelper.findNodeInfosById("com.android.systemui:id/notification_cleanup");
        if(nodeInfo != null){
            mGestureUtil.click(nodeInfo);
        }
    }
    //清除华为手机的内存
    private static void ClearHuaWeiPhoneROM(){
        AccessibilityHelper.performRecents();
        mFunction.click_sleep();
        AccessibilityNodeInfo nodeInfo = AccessibilityHelper.findNodeInfosById("com.android.systemui:id/clear_all_recents_image_button");
        if(nodeInfo != null){
            mGestureUtil.click(nodeInfo);
        }
        mGestureUtil.scroll_down_screen();
        nodeInfo = AccessibilityHelper.findNodeInfosById("com.android.systemui:id/delete");
        if(nodeInfo != null){
            mGestureUtil.click(nodeInfo);
        }
    }
    //清除公司购买的400元手机
    private static void Clear4G4GPhoneROM(){
        mGestureUtil.scroll_down_screen();
        AccessibilityNodeInfo nodeInfo = AccessibilityHelper.findNodeInfosById("com.android.systemui:id/dismiss_text");
        if(nodeInfo != null){
            mGestureUtil.click(nodeInfo);
        }
    }
}
