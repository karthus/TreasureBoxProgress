package com.jkarthus.treasure.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jkarthus.treasure.BuildConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * @author shihuajian
 * @depict
 * @since 2017/6/21 17:10
 */
public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    
    protected Context mCtx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCtx = this;

        setupUmeng();
    }

    /** Umeng统计配置 */
    private void setupUmeng() {
        //设置是否对日志信息进行加密, 默认false(不加密)
        MobclickAgent.enableEncrypt(BuildConfig.DEBUG);//6.0.0版本及以后
        // 启动友盟统计
        MobclickAgent.setCatchUncaughtExceptions(true);
        // Umeng统计配置
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        // 为了统计Fragment页面，这里需要设置为fasle
        // 如果不需要统计Fragment页面，则设置为true;
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 友盟统计
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
