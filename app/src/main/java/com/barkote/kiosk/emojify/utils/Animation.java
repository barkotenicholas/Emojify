package com.barkote.kiosk.emojify.utils;

import android.view.View;
import android.widget.ImageView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;


public class Animation {
    static SpringConfig springConfig = SpringConfig.fromOrigamiTensionAndFriction(40, 7);

    public static void popOut(final ImageView v, int time) {
        v.setVisibility(View.INVISIBLE);
        final Spring s = SpringSystem.create().createSpring();
        s.setSpringConfig(springConfig);
        s.setCurrentValue(0);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setVisibility(View.VISIBLE);
                s.setEndValue(1);
            }
        }, time);
    }
}
