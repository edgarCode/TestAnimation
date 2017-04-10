package com.letv.testanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

import io.codetail.animation.ArcAnimator;
import io.codetail.animation.Side;

public class MainActivity extends Activity {
    private ImageView image_mascot;//吉祥物
    private ImageView image_stick, image_stick_backup;
    private RelativeLayout layout_dialog_result;
    private ImageView image_bg_dialog;

    private final float scale_start = 0.25f;
    private final float scale_end = 1.0f;
    private final int arc_move = 90;
    private final int arc_rotation = 15;
    private int offsetX_dialog_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        offsetX_dialog_bg = getResources().getDimensionPixelOffset(R.dimen.dp_700);

        setContentView(R.layout.activity_main);
        image_mascot = (ImageView) findViewById(R.id.img_mascot);
        image_stick = (ImageView) findViewById(R.id.img_stick);
        image_stick_backup = (ImageView) findViewById(R.id.img_stick_backup);
        layout_dialog_result = (RelativeLayout) findViewById(R.id.layout_dialog_result);
        image_bg_dialog = (ImageView) findViewById(R.id.img_dialog_bg);

        try {
            File file = getFileStreamPath("anim_puzzle");
            if (file.exists() && file.isDirectory() && file.listFiles()!=null && file.listFiles().length>0) {
                return;
            }
            Utils.UnZipFolder(getFileStreamPath("anim_puzzle.zip").getAbsolutePath(), getFilesDir().getAbsolutePath());
            getFileStreamPath("anim_puzzle.zip").delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rotation() {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(image_stick, "rotation", 0, arc_rotation);
        ObjectAnimator scaleAnimator1 = ObjectAnimator.ofFloat(image_stick, "scaleX", scale_end, scale_start);
        ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(image_stick, "scaleY", scale_end, scale_start);
        AnimatorSet as = new AnimatorSet();
        as.setDuration(1);
        as.playTogether(rotation, scaleAnimator1, scaleAnimator2);
        as.addListener(new MyAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                image_stick.setVisibility(View.VISIBLE);
                move();
            }
        });
        as.start();
    }

    float offsetX, offsetY;

    private void move() {
        int duration_move = 1300;
        int duration_rotationY = 800;
        final int duration_show_bg = 1200;
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) (getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(dm);
        offsetX = image_stick.getX() - (dm.widthPixels - offsetX_dialog_bg) / 2;
        offsetY = image_stick.getY() - dm.heightPixels / 2;
        //弧度移动动画
        ArcAnimator.createArcShift(image_stick, (dm.widthPixels - offsetX_dialog_bg) / 2, dm.heightPixels / 2, arc_move, Side.LEFT).setDuration(duration_move).start();
        //横向缩放
        ObjectAnimator scaleAnimator1 = ObjectAnimator.ofFloat(image_stick, "scaleX", scale_start, scale_end).setDuration(duration_move);
        //纵向缩放
        ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(image_stick, "scaleY", scale_start, scale_end).setDuration(duration_move);
        //角度旋转为0
        ObjectAnimator rotation = ObjectAnimator.ofFloat(image_stick, "rotation", arc_rotation, 0).setDuration(duration_move);
        scaleAnimator2.addListener(new MyAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_dialog_result.setVisibility(View.VISIBLE);
                //背景图动画展现
                image_bg_dialog.animate().translationXBy(offsetX_dialog_bg).setDuration(duration_show_bg).start();
            }
        });
        //翻转动画
        ObjectAnimator rotationY = ObjectAnimator.ofFloat(image_stick, "rotationY", 0, 180, 360).setDuration(duration_rotationY);
        rotationY.addListener(
                new MyAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        image_stick.setImageResource(R.drawable.stick_positive);
                    }
                });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(scaleAnimator1, scaleAnimator2, rotation, rotationY);
        set.start();
    }

    private void reset() {
        layout_dialog_result.setVisibility(View.GONE);
        image_stick.setVisibility(View.INVISIBLE);
        //重置签的背景
        image_stick.setImageResource(R.drawable.stick_back);
        //重置dialog背景图的位置
        image_bg_dialog.animate().translationXBy(-offsetX_dialog_bg).setDuration(1).start();
        //重置签的位置
        ArcAnimator.createArcShift(image_stick, image_stick_backup, arc_move, Side.LEFT).setDuration(1).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            MyAnimationDrawable2.animateRawManuallyFromXML(new File(getFilesDir().getAbsolutePath()+"/anim_puzzle/xiaoren_animation.xml"), image_mascot, null, null);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            rotation();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            reset();
        }
        return super.onKeyDown(keyCode, event);
    }
}
