package com.letv.testanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import io.codetail.animation.ArcAnimator;
import io.codetail.animation.Side;

public class MainActivity extends Activity {
  private ImageView img;
  private ImageView img_qian;
  private RelativeLayout layout_qiangpin;
  private ImageView img_dialog_bg;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    img = (ImageView) findViewById(R.id.img);
    img_qian = (ImageView) findViewById(R.id.img_qian);
    layout_qiangpin = (RelativeLayout) findViewById(R.id.layout_qiangpin);
    img_dialog_bg = (ImageView) findViewById(R.id.img_bg);
  }

  private void rotateAnimation() {
    RotateAnimation rotateAnimation = new RotateAnimation(0, 30, Animation.RELATIVE_TO_SELF, 0.4f, Animation.RELATIVE_TO_SELF, 1.0f);
    rotateAnimation.setDuration(1500);
    rotateAnimation.setRepeatCount(1);
    rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    rotateAnimation.setRepeatMode(Animation.REVERSE);
    RotateAnimation rotateAnimation2 = new RotateAnimation(0, -30, Animation.RELATIVE_TO_SELF, 0.4f, Animation.RELATIVE_TO_SELF, 1.0f);
    rotateAnimation2.setDuration(1500);
    rotateAnimation2.setRepeatCount(1);
    rotateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator ());
    rotateAnimation2.setRepeatMode(Animation.REVERSE);

    AnimationSet animationSet = new AnimationSet(true);
    animationSet.addAnimation(rotateAnimation);
    animationSet.addAnimation(rotateAnimation2);
    img.startAnimation(animationSet);

  }

  private void rotation() {
    ObjectAnimator rotation = ObjectAnimator.ofFloat(img_qian, "rotation",  0, 15);
    ObjectAnimator scaleAnimator1 = ObjectAnimator.ofFloat(img_qian, "scaleX", 1f, 0.25f);
    ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(img_qian, "scaleY", 1f, 0.25f);
    AnimatorSet as = new AnimatorSet();
    as.setDuration(1);
    as.playTogether(rotation, scaleAnimator1, scaleAnimator2);
    as.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        img_qian.setVisibility(View.VISIBLE);
        move();
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });
    as.start();
  }
  private void move() {
    int duration = 1300;
    ArcAnimator.createArcShift(img_qian, 470, 530, 90, Side.LEFT).setDuration(duration).start();
    ObjectAnimator scaleAnimator1 = ObjectAnimator.ofFloat(img_qian, "scaleX", 0.25f, 1f).setDuration(duration);
    ObjectAnimator scaleAnimator2 = ObjectAnimator.ofFloat(img_qian, "scaleY", 0.25f, 1f).setDuration(duration);
    ObjectAnimator rotation = ObjectAnimator.ofFloat(img_qian, "rotation",  15, 0).setDuration(duration);
    scaleAnimator2.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        layout_qiangpin.setVisibility(View.VISIBLE);
        img_dialog_bg.animate().translationXBy(MainActivity.this.getResources().getDimensionPixelOffset(R.dimen.dp_700)).setDuration(1200).start();
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });
    ObjectAnimator rotationY = ObjectAnimator.ofFloat(img_qian,"rotationY",0,180, 360).setDuration(800);
    rotationY.addListener(
        new Animator.AnimatorListener() {
          @Override public void onAnimationStart(Animator animation) {}

          @Override public void onAnimationEnd(Animator animation) {
            img_qian.setImageResource(R.drawable.qian);
          }

          @Override public void onAnimationCancel(Animator animation) {}

          @Override public void onAnimationRepeat(Animator animation) {}
        });

    AnimatorSet set = new AnimatorSet();
    set.setInterpolator(new AccelerateDecelerateInterpolator());
    set.playTogether(scaleAnimator1, scaleAnimator2, rotation, rotationY);
    set.start();

  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
      //translateAnimation();
      MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.xiaoren_animation, img, null, null);
    } else if (keyCode==KeyEvent.KEYCODE_DPAD_DOWN) {
      rotation();
    }
    return super.onKeyDown(keyCode, event);
  }
}
