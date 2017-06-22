package com.jkarthus.treasure.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jkarthus.treasure.R;
import com.jkarthus.treasure.utils.AppInfoUtils;
import com.jkarthus.treasure.utils.UmUtils;
import com.jkarthus.treasure.view.TreasureBoxProgressView;

/**
 * 首页
 *
 * @author shihuajian
 * @since 2017/6/21 16:26
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TreasureBoxProgressView semicircleProgressView;

    private TextView mTextMessage;
    
    private TextView mTextDeviceInfo;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(UmUtils.getDeviceInfo(mCtx));
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mTextMessage = (TextView) findViewById(R.id.message);
        semicircleProgressView = (TreasureBoxProgressView) findViewById(R.id.semicircleProgressView);
        mTextDeviceInfo = (TextView) findViewById(R.id.deviceInfo);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        
        semicircleProgressView.setOnClickListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int level = 8;
        int value = 99;
        int total = 100;
        semicircleProgressView.setTreasureProgressCenterBitmapResId(setupTreasureBoxBitmap(level, value, total));
        semicircleProgressView.setSesameValues(value, total);
        if (value == total) {
            semicircleProgressView.setSesameLevel(getResources().getString(R.string.treasure_get));
        } else if (value < total && value > 0) {
            semicircleProgressView.setSesameLevel(value + "/" + total);
        } else {
            semicircleProgressView.setSesameLevel(getResources().getString(R.string.treasure_tips));
        }
        
        String deviceInfo = AppInfoUtils.printDeviceInfo(mCtx);
        Log.e(TAG, deviceInfo);
        mTextDeviceInfo.setText(deviceInfo);
    }

    /**
     * 根据参数设置宝箱等级
     * <p/>分为5个等级 %25 %50 %75 %99 %100
     *
     * @param level 宝箱等级
     * @param value 宝箱当前值
     * @param total 宝箱总值
     * @return 返回图片ResId
     */
    public int setupTreasureBoxBitmap(int level, int value, int total) {
        int resId = R.mipmap.ic_treasure_box_default;
        int totalFifth = total / 4; // 总数的四分之一, 用于计算等级份额
        if (value == 0) {
            resId = R.mipmap.ic_treasure_box_default;
        } else if (value <= totalFifth) {
            if (2 == level) {
                resId = R.mipmap.ic_treasure_box_brass_01;
            } else if (3 == level) {
                resId = R.mipmap.ic_treasure_box_silver_01;
            } else if (4 == level) {
                resId = R.mipmap.ic_treasure_box_gold_01;
            } else if (5 == level) {
                resId = R.mipmap.ic_treasure_box_platinum_01;
            } else if (6 == level) {
                resId = R.mipmap.ic_treasure_box_diamond_01;
            } else if (7 == level) {
                resId = R.mipmap.ic_treasure_box_king_01;
            } else if (8 == level) {
                resId = R.mipmap.ic_treasure_box_overweening_01;
            } else {
                resId = R.mipmap.ic_treasure_box_default;
            }
        } else if (value <= totalFifth * 2) {
            if (2 == level) {
                resId = R.mipmap.ic_treasure_box_brass_02;
            } else if (3 == level) {
                resId = R.mipmap.ic_treasure_box_silver_02;
            } else if (4 == level) {
                resId = R.mipmap.ic_treasure_box_gold_02;
            } else if (5 == level) {
                resId = R.mipmap.ic_treasure_box_platinum_02;
            } else if (6 == level) {
                resId = R.mipmap.ic_treasure_box_diamond_02;
            } else if (7 == level) {
                resId = R.mipmap.ic_treasure_box_king_02;
            } else if (8 == level) {
                resId = R.mipmap.ic_treasure_box_overweening_02;
            } else {
                resId = R.mipmap.ic_treasure_box_default;
            }
        } else if (value <= totalFifth * 3) {
            if (2 == level) {
                resId = R.mipmap.ic_treasure_box_brass_03;
            } else if (3 == level) {
                resId = R.mipmap.ic_treasure_box_silver_03;
            } else if (4 == level) {
                resId = R.mipmap.ic_treasure_box_gold_03;
            } else if (5 == level) {
                resId = R.mipmap.ic_treasure_box_platinum_03;
            } else if (6 == level) {
                resId = R.mipmap.ic_treasure_box_diamond_03;
            } else if (7 == level) {
                resId = R.mipmap.ic_treasure_box_king_03;
            } else if (8 == level) {
                resId = R.mipmap.ic_treasure_box_overweening_03;
            } else {
                resId = R.mipmap.ic_treasure_box_default;
            }
        } else if (value < totalFifth * 4) {
            if (2 == level) {
                resId = R.mipmap.ic_treasure_box_brass_04;
            } else if (3 == level) {
                resId = R.mipmap.ic_treasure_box_silver_04;
            } else if (4 == level) {
                resId = R.mipmap.ic_treasure_box_gold_04;
            } else if (5 == level) {
                resId = R.mipmap.ic_treasure_box_platinum_04;
            } else if (6 == level) {
                resId = R.mipmap.ic_treasure_box_diamond_04;
            } else if (7 == level) {
                resId = R.mipmap.ic_treasure_box_king_04;
            } else if (8 == level) {
                resId = R.mipmap.ic_treasure_box_overweening_04;
            } else {
                resId = R.mipmap.ic_treasure_box_default;
            }
        } else if (value >= totalFifth * 4) {
            if (2 == level) {
                resId = R.mipmap.ic_treasure_box_brass_05;
            } else if (3 == level) {
                resId = R.mipmap.ic_treasure_box_silver_05;
            } else if (4 == level) {
                resId = R.mipmap.ic_treasure_box_gold_05;
            } else if (5 == level) {
                resId = R.mipmap.ic_treasure_box_platinum_05;
            } else if (6 == level) {
                resId = R.mipmap.ic_treasure_box_diamond_05;
            } else if (7 == level) {
                resId = R.mipmap.ic_treasure_box_king_05;
            } else if (8 == level) {
                resId = R.mipmap.ic_treasure_box_overweening_05;
            } else {
                resId = R.mipmap.ic_treasure_box_default;
            }
        }

        return resId;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.semicircleProgressView:
                intent = new Intent(this, TabbedActivity.class);
                startActivity(intent);
                break;
            
            default:
                break;
        }
    }
}
