package com.example.tester3.pizza;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;

public class Utils {

    public static int getIntRes(Context context, @IntegerRes int resId) {

        return context.getResources().getInteger(resId);
    }
}
