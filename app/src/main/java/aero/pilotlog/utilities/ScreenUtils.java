package aero.pilotlog.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by hung.nn on 3/6/14.
 */
public class ScreenUtils {



    public static int NAVIGATION_BAR_HEIGHT = 0;
    public static int STATUS_BAR_HEIGHT = 0;
    public static int ACTION_BAR_HEIGHT = 0;
    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;

    public static Point getScreenDimension(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        SCREEN_WIDTH = metrics.widthPixels;
        SCREEN_HEIGHT = metrics.heightPixels;
        if (!(SCREEN_HEIGHT == 854 || SCREEN_HEIGHT == 960 || SCREEN_HEIGHT == 1024 || SCREEN_HEIGHT == 1280 ||
                SCREEN_HEIGHT == 1920 || SCREEN_HEIGHT == 2560 || SCREEN_HEIGHT == 600 || SCREEN_HEIGHT == 768 ||
                SCREEN_HEIGHT == 720 || SCREEN_HEIGHT == 800 || SCREEN_HEIGHT == 1600)) {
            SCREEN_HEIGHT = SCREEN_HEIGHT + NAVIGATION_BAR_HEIGHT;
        }
        return new Point(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            NAVIGATION_BAR_HEIGHT = resources.getDimensionPixelSize(resourceId);
        }
        return NAVIGATION_BAR_HEIGHT;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            STATUS_BAR_HEIGHT = resources.getDimensionPixelSize(resourceId);
        }
        return STATUS_BAR_HEIGHT;
    }

    public static int getActionBarHeight(Context context) {
        return getActionBarHeight(context, false);
    }

    public static int getActionBarHeight(Context context, boolean isHavingTabBar) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            ACTION_BAR_HEIGHT = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
        }
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ACTION_BAR_HEIGHT += dipToPixels(context, 48);
        }
        return ACTION_BAR_HEIGHT;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static int dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dipValue, metrics));
    }

    /**
     * Check is tablet
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
