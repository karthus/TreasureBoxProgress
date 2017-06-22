package com.jkarthus.treasure.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.List;

/**
 * @author shihuajian
 * @depict 程序信息工具类
 * @since 9/30/16 3:51 PM
 */

public class AppInfoUtils {

    private static final String TAG = AppInfoUtils.class.getSimpleName();
    
    public static String printDeviceInfo(Context ctx) {
        StringBuffer deviceInfo = new StringBuffer();
        deviceInfo.append("包名: " + getPackageName(ctx));
        deviceInfo.append("\nICCID: " + getICCID(ctx));
        deviceInfo.append("\nIMEI: " + getIMEI(ctx));
        deviceInfo.append("\nPhoneNumber: " + getLine1Number(ctx));
        deviceInfo.append("\nModel: " + getModel());
        deviceInfo.append("\nBrand: " + getBrand());
        deviceInfo.append("\nVersion: " + getVersion());
        deviceInfo.append("\nVersionName: " + getVersionName(ctx));
        deviceInfo.append("\nVersionCode: " + getVersionCode(ctx));
        deviceInfo.append("\nScreenWidth: " + getScreenWidth(ctx));
        deviceInfo.append("\nScreenHeight: " + getScreenHeight(ctx));
        deviceInfo.append("\nScreenDensity: " + getScreenDensity(ctx));
        
        return deviceInfo.toString();
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取应用包名
     *
     * @param context 上下文
     * @return 应用包名
     */
    public static String getPackageName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String packageName = info.packageName;
            return packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取渠道号
     *
     * @param context 上下文
     * @return 返回渠道号, 默认返回default_
     */
    public static String getGradleChannel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("QIAN_DEER_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
            ignored.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default_";

    }
    
    /**
     * 获取sim卡序列号iccid 不同于misi
     *
     * @param context
     *
     * @return
     */
    public static String getICCID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }

    /**
     * 获取IMEI
     */
    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return imei;
    }

    /**
     * 获取手机号码
     */
    public static String getLine1Number(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = telephonyManager.getLine1Number();
        return number;
    }

    /**
     * 获取手机型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取android版本号
     */
    public static String getVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取本应用VersionName
     */
    public static String getVersionName(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    /**
     * 获取本应用VersionCode
     */
    public static int getVersionCode(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 是否有活动的网络连接
     *
     * @return (networkInfo != null && networkInfo.isAvailable())
     */
    public static boolean hasNetWorkConnection(Context context) {
        // 获取连接活动管理器
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取链接网络信息
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isAvailable());
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    /**
     * 安装 app
     *
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public static boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }
}
