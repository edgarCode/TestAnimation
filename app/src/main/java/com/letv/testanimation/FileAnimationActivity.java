package com.letv.testanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.jasonzhang.fileanimation.FileAnimation;

/**
 * Created by JifengZhang on 2017/4/21.
 */

public class FileAnimationActivity extends Activity {
    ImageView mImageMascot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mImageMascot = (ImageView) findViewById(R.id.img_mascot);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            FileAnimation.showAnimation("anim_puzzle.zip", mImageMascot, null, null);
        }
        return super.onKeyDown(keyCode, event);
    }
}
