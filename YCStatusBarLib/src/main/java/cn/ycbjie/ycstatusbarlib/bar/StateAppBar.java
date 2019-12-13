/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package cn.ycbjie.ycstatusbarlib.bar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.ycbjie.ycstatusbarlib.StatusBarUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCStatusBar
 *     time  : 2017/06/4
 *     desc  : 状态栏工具类
 *     revise: 使用方法请看GitHub说明文档
 * </pre>
 */
public final class StateAppBar {

    /**
     * 设置状态栏颜色
     * @param activity                      activity
     * @param statusColor                   颜色
     */
    public static void setStatusBarColor(Activity activity,@ColorInt int statusColor) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //21，大于21设置状态栏颜色
            BarStatusLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //19
            BarStatusKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    /**
     * 设置透明状态栏
     * @param activity                      activity
     */
    public static void translucentStatusBar(Activity activity) {
        StatusBarUtils.checkNull(activity);
        translucentStatusBar(activity, false);
    }

    /**
     * 设置透明状态栏
     * @param activity                      activity
     * @param hideStatusBarBackground       是否隐藏状态栏
     */
    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.translucentStatusBar(activity, hideStatusBarBackground);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.translucentStatusBar(activity);
        }
    }

    /**
     * 设置AppBarLayout折叠布局的状态栏颜色
     * @param activity                      activity
     * @param appBarLayout                  appBar
     * @param collapsingToolbarLayout       collapsingToolbarLayout
     * @param toolbar                       toolbar
     * @param statusColor                   颜色
     */
    public static void setStatusBarColorForCollapsingToolbar(
            @NonNull Activity activity, AppBarLayout appBarLayout,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar, @ColorInt int statusColor) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.setStatusBarColorForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.setStatusBarColorForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        }
    }

    /**
     * 设置状态栏颜色
     * @param activity                      activity
     * @param color                         颜色
     */
    @SuppressLint("NewApi")
    public static void setStatusBarLightMode(Activity activity, @ColorInt int color) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (setStatusBarLightMode(activity, true) ||
                    FlymeSetStatusBarLightMode(activity, true)) {
                //设置状态栏为指定颜色
                //5.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setStatusBarColor(color);
                    //4.4
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //调用修改状态栏颜色的方法
                    setStatusBarColor(activity, color);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                activity.getWindow().getDecorView().
                        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                activity.getWindow().setStatusBarColor(color);

                //fitsSystemWindow 为 false, 不预留系统栏位置.
                ViewGroup mContentView = (ViewGroup) activity.getWindow()
                        .findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    mChildView.setFitsSystemWindows(true);
                    ViewCompat.requestApplyInsets(mChildView);
                }
            }
        }
    }


    public static void setStatusBarLightForCollapsingToolbar(
            Activity activity, AppBarLayout appBarLayout,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar,@ColorInt int statusBarColor) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.setStatusBarWhiteForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.setStatusBarWhiteForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        }
    }


    /**
     * MIUI的沉浸支持透明白色字体和透明黑色字体
     * https://dev.mi.com/console/doc/detail?pId=1159
     */
    public static boolean setStatusBarLightMode(Activity activity, boolean darkmode) {
        StatusBarUtils.checkNull(activity);
        try {
            @SuppressLint("PrivateApi")
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            Class<? extends Window> clazz = activity.getWindow().getClass();
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     */
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        StatusBarUtils.checkNull(activity);
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static void setContentTopPadding(Activity activity, int padding) {
        StatusBarUtils.checkNull(activity);
        ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    static int getPxFromDp(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }
}
