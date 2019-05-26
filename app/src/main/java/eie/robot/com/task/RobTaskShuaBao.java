package eie.robot.com.task;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.vondear.rxtool.view.RxToast;

import eie.robot.com.accessibilityservice.AccessibilityHelper;
import eie.robot.com.common.mCommonTask;
import eie.robot.com.common.mConfig;
import eie.robot.com.common.mFunction;
import eie.robot.com.common.mGestureUtil;
import eie.robot.com.common.mGlobal;
import eie.robot.com.common.mToast;

public class RobTaskShuaBao extends BaseRobotTask {

    public int SizeOffset = 40;

    /**
     * 构造函数
     */
    public RobTaskShuaBao() {
        super();
        this.AppName = "刷宝短视频";
    }


    /**
     * 执行刷单任务（领取时段奖励、定时刷新视频、查看文章）
     */
    @Override
    public boolean StartTask()  {
        super.StartTask();
        while (mCommonTask.AppTaskOpenStatus){
            try {
                //每次进行一项任务时，都先恢复到首页
                //如果APP未打开，则会自行打开,如果最后还是无法打开，则跳出这次循环，重新来。
                if(!returnHome()){
                    continue;
                }

                //签到
                //SignIn();
                //领取时段奖励
                //performTask_ShiDuanJiangLi();

                mFunction.openScreen();

                //阅读文章
                int RefreshCount =   mFunction.getRandom_10_20()+10;
                while (true){
                    if(!mCommonTask.AppTaskOpenStatus){ break;}
                    performTask_ShuaXiaoShiPing();
                    RefreshCount -- ;
                }
            }catch (Exception ex){
                RxToast.error(ex.getMessage());
            }
        }
        super.CloseTask();
        return false;
    }


    /**
     * 执行刷单任务（定时刷小视频）
     */
    private boolean performTask_ShuaXiaoShiPing(){

        //在主界面的情况下，点击底部导航【小视频】按钮，刷新小视频
        mGestureUtil.click(SizeOffset,mGlobal.mScreenHeight-SizeOffset);

        int VideoCount = mFunction.getRandom_10_20();
        while (VideoCount > 0){
            //点击视频的间隔
            int VideoInterval = 6+ mFunction.getRandom_6_12();//3;

            mGestureUtil.scroll_up_30();


            mToast.success("视频任务:浏览"+VideoInterval+"秒");
            if(VideoInterval > 16){
                AccessibilityNodeInfo nodeInfo = AccessibilityHelper.findNodeInfosByText("关注");
                if(nodeInfo != null){
                    mGestureUtil.click(nodeInfo);
                }else {
                    mGestureUtil.doubleClickInScreenCenter();
                }
            }
            if(VideoInterval == 18){
                mGestureUtil.doubleClickInScreenCenter();
            }

            mFunction.sleep( VideoInterval * 1000);
            VideoCount--;
        }
        return true;
    }

    /**
     * 执行刷单任务（领取时段奖励）
     */
    private boolean performTask_ShiDuanJiangLi(){
        mToast.success("时段奖励任务");
        if(!returnHome()){
            return false;
        }
        //点击第一个功能列表
        mGestureUtil.click(SizeOffset,mGlobal.mScreenHeight-SizeOffset);


        AccessibilityNodeInfo ScrollViewNodeInfo = AccessibilityHelper.findNodeInfosByClassName(
                mGlobal.mAccessibilityService.getRootInActiveWindow()
                ,"android.widget.HorizontalScrollView");
        if(ScrollViewNodeInfo == null){
            return false;
        }
        Rect rect = new Rect();
        ScrollViewNodeInfo.getBoundsInScreen(rect);
        if(rect.left < 1 || rect.top < 1){
            return false;
        }

        //点击时段按钮
        mGestureUtil.click(rect.left + SizeOffset,rect.top - SizeOffset);

        this.CloseDialog();

        return true;
    }


    //关闭APP弹出的所有可能弹框
    private void CloseDialog(){
        AccessibilityNodeInfo node = AccessibilityHelper.findNodeInfosById("com.jm.video:id/imgClose");
        if(node != null){
            mGestureUtil.click(node);
        }

    }


    /**
     * 执行刷单任务（看资讯）
     */
    private boolean performTask_KanZiXun(){
        mToast.success("新闻任务");
        if(!returnHome()){
            return false;
        }
        //点击第一个功能列表
        mGestureUtil.click(SizeOffset,mGlobal.mScreenHeight-SizeOffset);

        mGestureUtil.scroll_up();
        int NewsCount =   mFunction.getRandom_4_8();
        while (NewsCount > 0){
            if(!mCommonTask.AppTaskOpenStatus){break;}
            Task_KanZiXun();
            if(!returnHome()){
                continue;
            }
            mToast.info("首页滑动");
            mGestureUtil.scroll_up();
            NewsCount -- ;

        }
        //刷资讯
        return true;
    }


    private boolean Task_KanZiXun() {
        AccessibilityNodeInfo nodeInfo = AccessibilityHelper.findNodeInfosByClassName(
                mGlobal.mAccessibilityService.getRootInActiveWindow()
                ,"android.support.v7.widget.RecyclerView");
        if (nodeInfo == null) {
            return false;
        }
        int CountNews = (nodeInfo.getChildCount()/2);

        AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(CountNews);
        if(childNodeInfo == null){
            mToast.error("新闻为空，重新选择文章");
            return false;
        }

        mToast.success("阅读当页第"+CountNews+"条新闻");
        //过滤广告
        if(filterAdvertisement(childNodeInfo)){
            return false;
        }

        //点击新闻进行阅读。
        boolean clickResult = mGestureUtil.click(childNodeInfo);
        if(!clickResult){
            mToast.error("点击失效，重新选择文章");
            return false;
        }
        //开始阅读新闻
        if (clickResult) {
            //等待反应
            mFunction.sleep(mConfig.clickSleepTime);

            //滑动次数(随机10到20)
            int SwiperCount = mFunction.getRandom_6_12();
            mToast.info("资讯任务:滑动"+SwiperCount+"次");

            //开始滑动文章
            while (true) {
                if (SwiperCount < 1) {
                    break;
                }
                //向上滑动
                mGestureUtil.scroll_up();

                //点开【查看全文，奖励更多】按钮，阅读全文
                AccessibilityNodeInfo info = AccessibilityHelper.findNodeInfosByText("查看全文，奖励更多");
                if(info != null){
                    AccessibilityHelper.performClick(info);
                }

                //判断是否已经下载了某个APP
                AccessibilityNodeInfo node = AccessibilityHelper.findNodeInfosByText("出于安全考虑，已禁止您的手机安装来自此来源的未知应用。");
                if(node != null){
                    break;
                }

                //停止进行阅读
                int sleepTime = mFunction.getRandom_4_8();
                mFunction.sleep(sleepTime * 1000);
                SwiperCount--;
            }
        }
        //阅读完返回
        mToast.success("阅读完毕");
        return true;
    }

    //过来广告
    private boolean filterAdvertisement( AccessibilityNodeInfo nodeInfo ){
        if(nodeInfo.getClassName().equals("android.widget.RelativeLayout")){
            return true;
        }

        //资源ID目前测试每个版本都是一样的，暂且先这样
        AccessibilityNodeInfo node = AccessibilityHelper.findNodeInfosById(nodeInfo,"com.xiangzi.jukandian:id/item_artical_ad_three_bd_flag");
        if(node != null){
            RxToast.warning(mGlobal.mNavigationBarActivity,"过滤广告").show();
            return true;
        }
        return false;
    }

    /**
     * 执行签到任务
     */
    private void SignIn(){
        AccessibilityNodeInfo nodeInfo = AccessibilityHelper.findNodeInfosByText(
                mGlobal.mAccessibilityService.getRootInActiveWindow(),"去签到");
        if(nodeInfo != null){
            AccessibilityHelper.performClick(nodeInfo);
        }
        mFunction.sleep(mConfig.clickSleepTime);
    }

    /**
     * 回归到首页，如果APP未打开，则会自行打开
     * @return
     */
    @Override
    public boolean returnHome(){

        if(!super.returnHome()){
            return false;
        }
        if(!mFunction.loopOpenApp(AppName)){
            return false;
        }
        //获取底部导航栏的图标
        AccessibilityNodeInfo NodeInfo1 = AccessibilityHelper.findNodeInfosByText("任务");
        AccessibilityNodeInfo NodeInfo2 = AccessibilityHelper.findNodeInfosByText("首页");

        if ( NodeInfo1 != null && NodeInfo2 != null ) {
            mToast.success("回应用首页成功");
            return true;
        } else {

            //到此，虽然不是主界面，但却是处于打开状态，目前可能是处于，内页，至于哪个内页，无法确定，
            //采取触发返回键的方式。
            int count = mConfig.loopCount;
            while (true) {
                this.CloseDialog();

                NodeInfo1 = AccessibilityHelper.findNodeInfosByText("任务");
                NodeInfo2 = AccessibilityHelper.findNodeInfosByText("首页");
                if ( NodeInfo1 != null && NodeInfo2 != null ) {
                    break;
                }
                count--;
                if (count < 0) {
                    break;
                }
                AccessibilityHelper.performBack(mGlobal.mAccessibilityService);
                //停一下，等待反应
                mFunction.sleep(mConfig.loopSleepTime);
            }
            if (NodeInfo1 != null || NodeInfo2 != null) {
                mToast.success("回应用首页成功");
                return true;
            } else {
                mToast.error("回应用首页失败");
                //mCommonTask.ClearPhoneCacheTask();
                return false;
            }
        }
    }
}