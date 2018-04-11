package aero.pilotlog.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.MainActivity;
import aero.pilotlog.activities.MainTabletActivity;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.utilities.LogUtils;
import aero.pilotlog.utilities.NetworkUtils;

/**
 * Created by tuan.pv on 2015/07/31.
 * Base MCC fragment
 */
public abstract class BaseMCCFragment extends BaseFragment {

    private static final String TAG = "BaseMCCFragment";

    protected abstract int getHeaderResId();

    protected abstract int getContentResId();

    protected abstract int getFooterResId();

    @Override
    protected View onCreateHeaderView(LayoutInflater pInflater, ViewGroup pContainer) {
        if (getHeaderResId() == FLAG_NO_RESOURCE) {
            return super.onCreateHeaderView(pInflater, pContainer);
        }
        return safeInflater(pInflater, pContainer, getHeaderResId());
    }

    @Override
    protected View onCreateContentView(LayoutInflater pInflater, ViewGroup pContainer) {
        return safeInflater(pInflater, pContainer, getContentResId());
    }

    @Override
    protected View onCreateFooterView(LayoutInflater pInflater, ViewGroup pContainer) {
        if (getFooterResId() == FLAG_NO_RESOURCE) {
            return super.onCreateFooterView(pInflater, pContainer);
        }
        return safeInflater(pInflater, pContainer, getFooterResId());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isTablet()) {
            mActivity = (MainTabletActivity) getActivity();
        } else {
            mActivity = (MainActivity) getActivity();
        }
    }

    @Override
    protected void onKeyBackPress() {
        //if (isFragmentValid()) {
            LogUtils.d("onKeyBackPress ok");
            super.onKeyBackPress();
       // } else {
       //     LogUtils.e("onKeyBackPress ignored");
       // }
    }

    protected boolean isHavingNetwork() {
        if (NetworkUtils.isHavingNetwork(getActivity())) {
            return true;
        } else {
//            DialogUtils.getOkAlertDialog(getActivity(), R.string.alert_cpo_connect_error).show();
            return false;
        }
    }

    public boolean isTablet() {
        return MCCPilotLogConst.sIsTablet;
    }

    protected Fragment getFragment(Class<? extends BaseFragment> pFragment) {
        Fragment fragment = null;
        try {
            fragment = getFragmentManager().findFragmentByTag(pFragment.getName());
        } catch (NullPointerException e) {
            LogUtils.e("[NG- NullPointerException]- " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException eOutOfIndex) {
            LogUtils.e("[NG- ArrayIndexOutOfBoundsException]- " + eOutOfIndex.getMessage());
        } catch (IllegalStateException ilse) {
            ilse.printStackTrace();
        }

        return fragment;
    }

    protected Fragment getFragment(String pTag) {
        Fragment fragment = null;
        try {
            fragment = getFragmentManager().findFragmentByTag(pTag);
        } catch (NullPointerException e) {
            LogUtils.e("[NG- NullPointerException]- " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException eOutOfIndex) {
            LogUtils.e("[NG- ArrayIndexOutOfBoundsException]- " + eOutOfIndex.getMessage());
        } catch (IllegalStateException ilse) {
            ilse.printStackTrace();
        }

        return fragment;
    }

    /**
     * Returns the parent Fragment containing this current Fragment.
     *
     * @return
     */
    public Fragment getParentMCCFragment() {
        return getTargetFragment();
    }

    public Fragment getLeftFragment() {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        if (fm != null) {
            return fm.findFragmentById(R.id.leftContainerFragment);
        }
        return null;
    }

    public Fragment getRightFragment() {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        if (fm != null) {
            return fm.findFragmentById(R.id.rightContainerFragment);
        }
        return null;
    }
}