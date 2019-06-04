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
    //清楚小米手机的内存
    private static void ClearXiaoMiPhoneCache(){

        if(!mFunction.loopOpenApp("安全中心")){
            return;
        }
        RxToast.success("开始清理手机内存");

        AccessibilityNodeInfo nodeInfo = null;
        int count = 3 * mConfig.loopCount;
        while (count > 0) {
            nodeInfo = AccessibilityHelper.findNodeInfosByText("清理选中垃圾");
            if ( nodeInfo != null ) {
                break;
            }
            nodeInfo = AccessibilityHelper.findNodeInfosByText("垃圾清理");
            if ( nodeInfo != null ) {
                mGestureUtil.click(nodeInfo);
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
}