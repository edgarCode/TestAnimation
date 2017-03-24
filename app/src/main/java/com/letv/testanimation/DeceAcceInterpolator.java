package com.letv.testanimation;

import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by zhangjifeng on 2017/3/23.
 */

public class DeceAcceInterpolator extends AccelerateDecelerateInterpolator {
  @Override public float getInterpolation(float input) {
    float ret =  super.getInterpolation(input);
    Log.e("DeceAcceInterpolator","getInterpolation [" +input +", " + ret+"]");
    return ret;
  }
}
