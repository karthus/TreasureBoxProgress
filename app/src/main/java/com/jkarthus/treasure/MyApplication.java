package com.jkarthus.treasure;

import android.app.Application;

import com.jkarthus.treasure.utils.KeyStore;
import com.umeng.analytics.MobclickAgent;

/**
 * @author shihuajian
 * @depict
 * @since 2017/6/21 17:28
 */
public class MyApplication extends Application {
    
    protected static MyApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        
        setupUmeng();
    }

    public static MyApplication getApplication() {
        synchronized (MyApplication.class) {
            if (mApplication == null) {
                mApplication = new MyApplication();
            }
        }
        return mApplication;
    }

    public void setupUmeng() {
        // 友盟统计注册
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(
                MyApplication.this,     // 上下文
                KeyStore.getUmengAppKey(),  // AppKey
                "Default",
                MobclickAgent.EScenarioType.E_UM_NORMAL);   // 渠道号
        MobclickAgent.startWithConfigure(config);
    }
}
