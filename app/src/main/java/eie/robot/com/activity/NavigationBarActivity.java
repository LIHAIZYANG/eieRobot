package eie.robot.com.activity;

import android.os.Bundle;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.vondear.rxtool.RxDeviceTool;

import eie.robot.com.R;
import eie.robot.com.common.mCommonFunctionTask;
import eie.robot.com.common.mDeviceUtil;
import eie.robot.com.common.mFloatWindow;
import eie.robot.com.common.mFunction;
import eie.robot.com.common.mGlobal;
import eie.robot.com.fragment.BaseFragment;
import eie.robot.com.fragment.HomeFragment;
import eie.robot.com.receiver.baseReceiver;

public class NavigationBarActivity extends QMUIFragmentActivity {
    @Override
    protected int getContextViewId() { return R.id.eieRobot; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            BaseFragment fragment = new HomeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())

                    .commit();
        }

        //保存导航栏页的Activity到全局类，以备后用
        mGlobal.mNavigationBarActivity = NavigationBarActivity.this;

        //动态获取权限
        mFunction.setPermissions(NavigationBarActivity.this);

        //初始化屏幕的宽高
        mGlobal.mScreenWidth = RxDeviceTool.getScreenWidth(mGlobal.mNavigationBarActivity);
        mGlobal.mScreenHeight = mDeviceUtil.getFullActivityHeight(mGlobal.mNavigationBarActivity);

        mFloatWindow.showFloatWindow();

        mFunction.runInChildThread(new Runnable() {
            @Override
            public void run() {
                mFunction.sleep(5*1000);
                mFunction.openAccessibilityService();
                mFloatWindow.StartFloatWindowRobTask();
                //mCommonTask.StartTask();
            }
        });

        baseReceiver.register(NavigationBarActivity.this);
        mCommonFunctionTask.loopJudgeIsReboot();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
