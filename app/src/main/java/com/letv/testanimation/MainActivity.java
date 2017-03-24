package com.letv.testanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
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

public class MainActivity extends Activity {
  private ImageView img;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    img = (ImageView) findViewById(R.id.img);
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
  TranslateAnimation[] animations = new TranslateAnimation[2];
  private void translateAnimation() {
    TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,40);
    translateAnimation.setDuration(500);
    translateAnimation.setFillAfter(true);
    translateAnimation.setRepeatMode(Animation.REVERSE);
    translateAnimation.setRepeatCount(5);
    translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    //animations[0] = translateAnimation;

    img.startAnimation(translateAnimation);
  }

  private void ObjectAnimator() {
    ObjectAnimator animator1 = ObjectAnimator.ofFloat(img, "translationY",  0, 80, 0,0);
    animator1.setInterpolator(new AccelerateInterpolator());
    animator1.setDuration(600);
    animator1.setRepeatCount(3);


    //ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationY", 20f, -20f);
    //animator2.setInterpolator(new AccelerateDecelerateInterpolator());
    //animator2.setDuration(500);
    //
    AnimatorSet animatorSet = new AnimatorSet();

    animatorSet.play(animator1);
    animatorSet.start();
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
      translateAnimation();
    }
    return super.onKeyDown(keyCode, event);
  }
}
