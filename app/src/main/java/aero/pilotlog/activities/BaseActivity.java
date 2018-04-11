package aero.pilotlog.activities;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.fragments.AircraftAddFragment;
import aero.pilotlog.fragments.AirfieldAddsFragment;
import aero.pilotlog.fragments.ApproachFragment;
import aero.pilotlog.fragments.DelayTailsListFragment;
import aero.pilotlog.fragments.DutyAddFragment;
import aero.pilotlog.fragments.ExpenseAddFragment;
import aero.pilotlog.fragments.FlightAddsFragment;
import aero.pilotlog.fragments.GliderLaunchFragment;
import aero.pilotlog.fragments.MainMenuFragment;
import aero.pilotlog.fragments.PilotAddEditFragment;
import aero.pilotlog.fragments.PilotInfoFragment;
import aero.pilotlog.utilities.LogUtils;
import aero.pilotlog.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import aero.crewlounge.pilotlog.R;


/**
 * Created by tuan.pv on 3/24/2015.
 * Base Activity
 */
public class BaseActivity extends FragmentActivity {


    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Fragment mFrag;
    private SlidingMenu mMenu;
    private final List<SlidingMenuCallBack> mMenuCallBackList = new ArrayList<>();
    private final List<KeyBackPressCallBack> mKeyBackPressList = new ArrayList<>();
    private long firstClick = 0;
    private Handler mDoubleClickHandler = new Handler();
    private static final int TIME_OUT = 3000;
    private int mWidth, mWidthMenu;
    private int mFlightLogbookSortType = MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31;
    private boolean mIsSyncing = false;
    private String mTitleShow = "";

    public void setFlightLogbookSortType(int pSortType) {
        this.mFlightLogbookSortType = pSortType;
    }

    public void setSyncing(boolean pIsSyncing) {
        this.mIsSyncing = pIsSyncing;
    }

    public void addMenuCallBack(SlidingMenuCallBack pMenuCallBack) {
        this.mMenuCallBackList.add(pMenuCallBack);
    }

    public void removeMenuCallBack(SlidingMenuCallBack pMenuCallBack) {
        this.mMenuCallBackList.remove(pMenuCallBack);
    }

    public void addKeyBackCallBack(KeyBackPressCallBack pKeyBackPressCallBack) {
        this.mKeyBackPressList.add(pKeyBackPressCallBack);
    }

    public void removeKeyBackCallBack(KeyBackPressCallBack pKeyBackPressCallBack) {
        this.mKeyBackPressList.remove(pKeyBackPressCallBack);
    }

    public void callStartPopup() {
        /*String mSyncId = "";
        Setting setting = new DatabaseManager(this).getSetting(SettingsConst.SYNC_ID);
        if (setting != null) {
            mSyncId = setting.getData();
        }
        if (TextUtils.isEmpty(mSyncId) || MCCPilotLogConst.NULL.equalsIgnoreCase(mSyncId)) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_get_start);
            WindowManager.LayoutParams param = dialog.getWindow().getAttributes();
            param.dimAmount = 0.75f;
            dialog.getWindow().setAttributes(param);
            dialog.setCancelable(false);
            dialog.show();
            TextView txtGetStart = (TextView) dialog.findViewById(R.id.txtGetStart);
            txtGetStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent browserStartIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MCCPilotLogConst.URL_START));
                    startActivity(browserStartIntent);
                }
            });
            TextView txtSkip = (TextView) dialog.findViewById(R.id.txtSkip);
            txtSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MCCPilotLogConst.sIsTablet) {
            mWidth = getResources().getDisplayMetrics().widthPixels;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mWidthMenu = mWidth / 3;
            } else {
                mWidthMenu = mWidth / 2;
            }
        }

        initSlideMenu(savedInstanceState);
    }

    private void initSlideMenu(Bundle savedInstanceState) {
        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//        mMenu.setSlidingEnabled(false);
        mMenu.setSlidingEnabled(true);
        mMenu.setShadowWidthRes(R.dimen.shadow_width);
        mMenu.setShadowDrawable(R.drawable.shadowright);
        if (MCCPilotLogConst.sIsTablet) {
//            mMenu.setBehindWidth(MCCPilotLogConst.sScreenWidth / 3);
            mMenu.setBehindWidth(mWidthMenu);
        } else {
            mMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        }
        mMenu.setFadeDegree(0.35f);
        mMenu.attachToActivity(BaseActivity.this, SlidingMenu.SLIDING_WINDOW);
        mMenu.setMenu(R.layout.layout_menu);
        if (savedInstanceState == null) {
            android.support.v4.app.FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mFrag = new MainMenuFragment();
            String pTag = MainMenuFragment.class.getName(); //TuanPV add new
//            t.replace(R.id.menuFrame, mFrag);
            t.replace(R.id.menuFrame, mFrag, pTag);
            t.commit();
        } else {
            mFrag = this.getSupportFragmentManager().findFragmentById(R.id.menuFrame);
        }

        mMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                for (int i = 0, size = mMenuCallBackList.size(); i < size; i++) {
                    try {
                        mMenuCallBackList.get(i).onMenuClosed();
                    } catch (Throwable ignored) {
                    }
                }
            }
        });
        mMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                for (int i = 0, size = mMenuCallBackList.size(); i < size; i++) {
                    try {
                        mMenuCallBackList.get(i).onMenuOpened();
                    } catch (Throwable ignored) {
                    }
                }
            }
        });
    }

    public void toggleMenu() {
        this.mMenu.toggle();
    }

    protected void setContentAboveNavigationBar() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if (mMenu.isMenuShowing()) {
            toggleMenu();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            int backStackCount = fm.getBackStackEntryCount();
            Fragment fragmentFlightAdd = fm.findFragmentByTag(FlightAddsFragment.class.getName());
            Fragment fragmentPilotAdd = fm.findFragmentByTag(PilotAddEditFragment.class.getName());
            Fragment pilotInfoFragment = fm.findFragmentByTag(PilotInfoFragment.class.getName());
            Fragment zLaunchFragment = fm.findFragmentByTag(GliderLaunchFragment.class.getName());
            Fragment zApproachFragment = fm.findFragmentByTag(ApproachFragment.class.getName());
            Fragment zDelayFragment = fm.findFragmentByTag(DelayTailsListFragment.class.getName());
            Fragment aircraftAddFragment = fm.findFragmentByTag(AircraftAddFragment.class.getName());
            Fragment airfieldAddFragment = fm.findFragmentByTag(AirfieldAddsFragment.class.getName());
            Fragment dutyAddFragment = fm.findFragmentByTag(DutyAddFragment.class.getName());
            Fragment expenseAddFragment = fm.findFragmentByTag(ExpenseAddFragment.class.getName());
            if (MCCPilotLogConst.sIsTablet)
                if (fragmentFlightAdd != null)
                    backStackCount += 1;
            if (backStackCount > 0) {
                if (fragmentFlightAdd != null && backStackCount == 1) {
                    ((FlightAddsFragment) fragmentFlightAdd).onKeyBackPress();
                } else if (fragmentPilotAdd != null) {
                    ((PilotAddEditFragment) fragmentPilotAdd).navigationFrom();
                } else if (pilotInfoFragment != null && !((PilotInfoFragment) pilotInfoFragment).checkValidBackPress()) {
                    ((PilotInfoFragment) pilotInfoFragment).outImgFullScreen();
                } else if (zLaunchFragment != null) {
                    ((GliderLaunchFragment) zLaunchFragment).onKeyBackPress();
                } else if (zApproachFragment != null) {
                    ((ApproachFragment) zApproachFragment).onKeyBackPress();
                } else if (zDelayFragment != null) {
                    ((DelayTailsListFragment) zDelayFragment).onKeyBackPress();
                } else if (aircraftAddFragment != null) {
                    ((AircraftAddFragment) aircraftAddFragment).onKeyBackPress();
                } else if (airfieldAddFragment != null) {
                    ((AirfieldAddsFragment) airfieldAddFragment).onKeyBackPress();
                } else if (dutyAddFragment != null) {
                    ((DutyAddFragment) dutyAddFragment).onKeyBackPress();
                } else if (expenseAddFragment != null) {
                    ((ExpenseAddFragment) expenseAddFragment).onKeyBackPress();
                } else {
                    LogUtils.d(TAG, "popping back stack");
                    fm.popBackStackImmediate();
                }
            } else if (pilotInfoFragment != null && !((PilotInfoFragment) pilotInfoFragment).checkValidBackPress()) {
                ((PilotInfoFragment) pilotInfoFragment).outImgFullScreen();
            } else ConfirmBack();
        }
    }

    public void ConfirmBack() {
        if (mIsSyncing && mTitleShow.equalsIgnoreCase(getString(R.string.message_close_app))) {
            firstClick = -TIME_OUT;
        }
        if (System.currentTimeMillis() - firstClick < TIME_OUT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.finishAndRemoveTask();
            } else {
                this.finish();
            }
        } else {
            firstClick = System.currentTimeMillis();
            mTitleShow = mIsSyncing ? getString(R.string.message_interrupt_click_back) : getString(R.string.message_close_app);
            Utils.showToast(this, mTitleShow);
            mDoubleClickHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    firstClick = 0;
                }
            }, TIME_OUT);
        }
    }

    /**
     * Call back when user pressed Back Button Device
     */
    public interface KeyBackPressCallBack {
        void handleBackPress();
    }

    /**
     * Call back when SlidingMenu Open or Closed
     */
    public interface SlidingMenuCallBack {
        void onMenuClosed();

        void onMenuOpened();
    }

    /**
     * Called after your activity has been stopped, prior to it being started again.
     * Always followed by onStart()
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e("onRestart Fully exit application");
        //StorageUtils.writeIntToSharedPref(this, MCCPilotLogConst.PREF_SORT_TYPE_FLIGHT, mFlightLogbookSortType);
    }

    /**
     * Called when you are no longer visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("onStop Fully exit application");
        //StorageUtils.writeIntToSharedPref(this, MCCPilotLogConst.PREF_SORT_TYPE_FLIGHT, MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy Fully exit application");
        //StorageUtils.writeIntToSharedPref(this, MCCPilotLogConst.PREF_SORT_TYPE_FLIGHT, MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (MCCPilotLogConst.sIsTablet) {
            mWidth = getResources().getDisplayMetrics().widthPixels;
            mWidthMenu = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? mWidth / 3 : mWidth / 2;
            if (mMenu != null) {
                mMenu.setBehindWidth(mWidthMenu);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                toggleMenu();
                return true;
        }
        return super.onKeyDown(keycode, e);
    }
}