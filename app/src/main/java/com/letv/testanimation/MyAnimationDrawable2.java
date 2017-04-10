package com.letv.testanimation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Xml;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAnimationDrawable2 {
    public static class MyFrame {  
        byte[] bytes;  
        int duration;  
        Drawable drawable;
        boolean isReady = false;  
    }  
  
    public interface OnDrawableLoadedListener {  
        public void onDrawableLoaded(List<MyFrame> myFrames);
    }  
  
    
    public static void animateRawManuallyFromXML(File xmlFile,
                                                 final ImageView imageView, final Runnable onStart,
                                                 final Runnable onComplete) {
        loadFromXml(xmlFile, imageView.getContext(),
                new OnDrawableLoadedListener() {  
                    @Override
                    public void onDrawableLoaded(List<MyFrame> myFrames) {
                        if (onStart != null) {  
                            onStart.run();  
                        }  
                        animateRawManually(myFrames, imageView, onComplete);  
                    }  
                });  
    }  
  
    //
    private static void loadFromXml(final File xmlFile,
                                    final Context context,
                                    final OnDrawableLoadedListener onDrawableLoadedListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {  
                final ArrayList<MyFrame> myFrames = new ArrayList<MyFrame>();
                XmlPullParser parser = Xml.newPullParser();
                try {
                    FileInputStream fis = new FileInputStream(xmlFile);
                    parser.setInput(fis, "UTF-8");
                    int eventType = parser.getEventType();  
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
  
                        } else if (eventType == XmlPullParser.START_TAG) {
  
                            if (parser.getName().equals("item")) {  
                                byte[] bytes = null;  
                                int duration = 1000;  
  
                                for (int i = 0; i < parser.getAttributeCount(); i++) {  
                                    if (parser.getAttributeName(i).equals(  
                                            "drawable")) {  
                                        String imgName = parser.getAttributeValue(i);
                                        bytes = IOUtils.toByteArray(new FileInputStream(xmlFile.getParent()+"/"+imgName));
                                    } else if (parser.getAttributeName(i)  
                                            .equals("duration")) {  
                                        duration = Integer.parseInt(parser.getAttributeValue(i));
                                    }  
                                }  
  
                                MyFrame myFrame = new MyFrame();  
                                myFrame.bytes = bytes;  
                                myFrame.duration = duration;  
                                myFrames.add(myFrame);  
                            }  
  
                        } else if (eventType == XmlPullParser.END_TAG) {
  
                        } else if (eventType == XmlPullParser.TEXT) {
  
                        }  
  
                        eventType = parser.next();  
                    }  
                } catch (IOException e) {
                    e.printStackTrace();  
                } catch (XmlPullParserException e2) {
                    // TODO: handle exception  
                    e2.printStackTrace();  
                }  
  
                // Run on UI Thread  
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {  
                        if (onDrawableLoadedListener != null) {  
                            onDrawableLoadedListener.onDrawableLoaded(myFrames);  
                        }  
                    }  
                });  
            }  
        }).run();  
    }  
  
    //
    private static void animateRawManually(List<MyFrame> myFrames,
                                           ImageView imageView, Runnable onComplete) {
        animateRawManually(myFrames, imageView, onComplete, 0);  
    }  
  
    //
    private static void animateRawManually(final List<MyFrame> myFrames,
                                           final ImageView imageView, final Runnable onComplete,
                                           final int frameNumber) {
        final MyFrame thisFrame = myFrames.get(frameNumber);  
  
        if (frameNumber == 0) {  
            thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                    .getResources(), BitmapFactory.decodeByteArray(
                    thisFrame.bytes, 0, thisFrame.bytes.length));  
        } else {  
            MyFrame previousFrame = myFrames.get(frameNumber - 1);  
            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
            previousFrame.drawable = null;  
            previousFrame.isReady = false;  
        }  
  
        imageView.setImageDrawable(thisFrame.drawable);  
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {  
                // Make sure ImageView hasn't been changed to a different Image  
                // in this time  
                if (imageView.getDrawable() == thisFrame.drawable) {  
                    if (frameNumber + 1 < myFrames.size()) {  
                        MyFrame nextFrame = myFrames.get(frameNumber + 1);  
  
                        if (nextFrame.isReady) {  
                            // Animate next frame  
                            animateRawManually(myFrames, imageView, onComplete,  
                                    frameNumber + 1);  
                        } else {  
                            nextFrame.isReady = true;  
                        }  
                    } else {  
                        if (onComplete != null) {  
                            onComplete.run();  
                        }  
                    }  
                }  
            }  
        }, thisFrame.duration);  
  
        // Load next frame  
        if (frameNumber + 1 < myFrames.size()) {  
            new Thread(new Runnable() {
                @Override
                public void run() {  
                    MyFrame nextFrame = myFrames.get(frameNumber + 1);  
                    nextFrame.drawable = new BitmapDrawable(imageView
                            .getContext().getResources(),  
                            BitmapFactory.decodeByteArray(nextFrame.bytes, 0,
                                    nextFrame.bytes.length));  
                    if (nextFrame.isReady) {  
                        // Animate next frame  
                        animateRawManually(myFrames, imageView, onComplete,  
                                frameNumber + 1);  
                    } else {  
                        nextFrame.isReady = true;  
                    }  
  
                }  
            }).run();  
        }  
    }  
  
    //带时间的方法 
    public static void animateManuallyFromRawResource(
            int animationDrawableResourceId, ImageView imageView,
            Runnable onStart, Runnable onComplete, int duration) throws IOException,
            XmlPullParserException {
        AnimationDrawable animationDrawable = new AnimationDrawable();
  
        XmlResourceParser parser = imageView.getContext().getResources()
                .getXml(animationDrawableResourceId);  
  
        int eventType = parser.getEventType();  
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
  
            } else if (eventType == XmlPullParser.START_TAG) {
  
                if (parser.getName().equals("item")) {  
                    Drawable drawable = null;
  
                    for (int i = 0; i < parser.getAttributeCount(); i++) {  
                        if (parser.getAttributeName(i).equals("drawable")) {  
                            int resId = Integer.parseInt(parser
                                    .getAttributeValue(i).substring(1));  
                            byte[] bytes = IOUtils.toByteArray(imageView
                                    .getContext().getResources()  
                                    .openRawResource(resId));//IOUtils.readBytes  
                            drawable = new BitmapDrawable(imageView
                                    .getContext().getResources(),  
                                    BitmapFactory.decodeByteArray(bytes, 0,
                                            bytes.length));  
                        } else if (parser.getAttributeName(i)  
                                .equals("duration")) {  
                            duration = parser.getAttributeIntValue(i, 66);  
                        }  
                    }  
  
                    animationDrawable.addFrame(drawable, duration);  
                }  
  
            } else if (eventType == XmlPullParser.END_TAG) {
  
            } else if (eventType == XmlPullParser.TEXT) {
  
            }  
  
            eventType = parser.next();  
        }  
  
        if (onStart != null) {  
            onStart.run();  
        }  
        animateDrawableManually(animationDrawable, imageView, onComplete, 0);  
    }  
  
    private static void animateDrawableManually(
            final AnimationDrawable animationDrawable,
            final ImageView imageView, final Runnable onComplete,
            final int frameNumber) {  
        final Drawable frame = animationDrawable.getFrame(frameNumber);
        imageView.setImageDrawable(frame);  
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {  
                // Make sure ImageView hasn't been changed to a different Image  
                // in this time  
                if (imageView.getDrawable() == frame) {  
                    if (frameNumber + 1 < animationDrawable.getNumberOfFrames()) {  
                        // Animate next frame  
                        animateDrawableManually(animationDrawable, imageView,  
                                onComplete, frameNumber + 1);  
                    } else {  
                        // Animation complete  
                        if (onComplete != null) {  
                            onComplete.run();  
                        }  
                    }  
                }  
            }  
        }, animationDrawable.getDuration(frameNumber));  
    }  
}