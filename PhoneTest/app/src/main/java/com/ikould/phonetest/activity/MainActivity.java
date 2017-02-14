package com.ikould.phonetest.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.RadioGroup;

import com.ikould.phonetest.R;
import com.ikould.phonetest.fragment.Fragment1;
import com.ikould.phonetest.fragment.Fragment2;
import com.ikould.phonetest.fragment.Fragment3;
import com.ikould.phonetest.fragment.Fragment4;
import com.ikould.phonetest.utils.PreferenceUtils;
import com.ikould.phonetest.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yokeyword.swipebackfragment.SwipeBackActivity;


public class MainActivity extends SwipeBackActivity {

    private String mKeyTrackingMode = "Mode1";

    private RadioGroup mRgMain;

    public Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;
    private Fragment4 mFragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        new Thread(new Runnable() {
            int i;

            @Override
            public void run() {
                while (true) {
                    Log.d("MainActivity", "run: i = " + ++i + " MainActivity = " + MainActivity.this);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static int add(int s) {
        return 555;
    }

    private void initView() {
        mRgMain = (RadioGroup) findViewById(R.id.rg_main);
    }

    private void initListener() {
        mRgMain.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_fragment1:
                    if (mFragment1 == null)
                        mFragment1 = new Fragment1();
                    replaceFragment(mFragment1, "Fragment1", 1, 1000);
                    break;
                case R.id.rb_fragment2:
                    if (mFragment2 == null)
                        mFragment2 = new Fragment2();
                    replaceFragment(mFragment2, "Fragment2", 2, 1000);
                    break;
                case R.id.rb_fragment3:
                    if (mFragment3 == null)
                        mFragment3 = new Fragment3();
                    replaceFragment(mFragment3, "Fragment3", 3, 1000);
                    break;
                case R.id.rb_fragment4:
                    if (mFragment4 == null)
                        mFragment4 = new Fragment4();
                    replaceFragment(mFragment4, "Fragment4", 4, 1000);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

        }
    }

    private void saveTrackingMode(int flag) {
        PreferenceUtils.setPrefInt(getApplicationContext(), mKeyTrackingMode, flag);
    }

    private List<String> lists = new ArrayList<>();

    private void replaceFragment(Fragment fragment, String tag, int animType, int duration) {
        addFragments(R.id.fl_main, fragment, tag);
        Log.d("MainActivity", "replaceFragment: tag = " + tag);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(animation -> {
            float value = (float) valueAnimator.getAnimatedValue();
            Log.d("MainActivity", "replaceFragment: value = " + value);
            switch (animType) {
                case 1://渐变
                    if (fragment.getView() != null)
                        fragment.getView().setAlpha(value);
                    break;
                case 2://放大
                    if (fragment.getView() != null) {
                        fragment.getView().setScaleX(value);
                        fragment.getView().setScaleY(value);
                    }
                    break;
                case 3://左滑 TODO
                    break;
                case 4://右滑
                    break;
            }
        });
        valueAnimator.start();
        class TestClass {
            private String ss;

            public String getSS() {
                return ss;
            }
        }
        TestClass testClass = new TestClass();
        String ss = testClass.getSS();
    }

    public void removeTag(String tag) {
        Log.d("MainActivity", "removeTag: tag = " + tag);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        new Thread() {
            @Override
            public void run() {
                super.run();
            }
        }.start();
        //  lists.remove(tag);
    }

    public Map<String, Fragment> mFragments = new HashMap<>();
    private FragmentTransaction fragmentTransaction;

    /**
     * 添加Fragments
     *
     * @param id       要替换的View id
     * @param fragment 要添加的Fragment
     * @param tag      标记
     */
    public void addFragments(int id, Fragment fragment, String tag) {
        if (TextUtils.isEmpty(tag) || fragment == null) {
            return;
        }
        if (mFragments.get(tag) == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(id, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            mFragments.put(tag, fragment);
        }
        //同时需要隐藏其他fragment
        showFragment(tag);
    }

    /**
     * 显示当前Fragment
     *
     * @param tag 标记
     */
    public void showFragment(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        for (String fragmentkey : mFragments.keySet()) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (tag.equals(fragmentkey)) {
                fragmentTransaction.show(mFragments.get(fragmentkey));
            } else {
                fragmentTransaction.hide(mFragments.get(fragmentkey));
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

}
