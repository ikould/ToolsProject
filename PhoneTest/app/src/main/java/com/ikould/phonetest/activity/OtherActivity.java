package com.ikould.phonetest.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.ikould.phonetest.AppConfig;
import com.ikould.phonetest.R;
import com.ikould.phonetest.utils.ScreenUtil;
import com.ikould.phonetest.utils.SuUtil;
import com.ikould.phonetest.view.PathTestView;

import java.io.File;
import java.util.ArrayList;


public class OtherActivity extends AppCompatActivity {
    public static final String AIQIYI = "com.qiyi.video.pad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initView();
    }

    private void initView() {
        PathTestView pathTestView = (PathTestView) findViewById(R.id.path_test_view);
        Button mStart = (Button) findViewById(R.id.bt_start);
        mStart.setOnClickListener(v -> {
            shareSingleImage();
            pathTestView.startAnim();
        });
        int width = ScreenUtil.getScreenWidth(this);
        int height = ScreenUtil.getScreenHeight(this);
        int statusHeight = ScreenUtil.getStatusHeight(this);
        int keyboardHeight = ScreenUtil.getKeyboardHeight(this);
        Log.d("MainActivity", "initView: width = " + width + " height = " + height + " statusHeight = " + statusHeight);
       /* addOnSoftKeyBoardVisibleListener(this, (visible, windowBottom) -> {

        });*/

        boolean isOpen = AppConfig.getInstance().isTestOpen();
        Log.d("OtherActivity", "initView: isOpen = " + isOpen);
    }

    private void initTest() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    //分享文字
    public void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    //分享单张图片
    public void shareSingleImage() {
        String imagePath = "assets/test.jpg";
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    //分享多张图片
    public void shareMultipleImage() {
        ArrayList<Uri> uriList = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory() + File.separator;
        uriList.add(Uri.fromFile(new File(path + "australia_1.jpg")));
        uriList.add(Uri.fromFile(new File(path + "australia_2.jpg")));
        uriList.add(Uri.fromFile(new File(path + "australia_3.jpg")));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
