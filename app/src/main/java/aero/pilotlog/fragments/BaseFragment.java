package aero.pilotlog.fragments;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.BaseActivity;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.LogUtils;

import java.text.ParseException;

/**
 * Created by tuan.pv on 2015/07/31.
 * Base fragment
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    public static final String STRING_EMPTY = "";

    public static final boolean FLAG_VISIBLE_ICON_MENU = true;

    public static final boolean FLAG_GONE_ICON_MENU = false;

    /**
     * Flag to not add fragment to back stack
     */
    public static final boolean FLAG_NOT_ADD_STACK = false;

    /**
     * Flag to add fragment to back stack
     */
    public static final boolean FLAG_ADD_STACK = true;

    /**
     * Flag to set no header view to add
     */
    public static final int FLAG_NO_RESOURCE = 0;

    protected BaseActivity mActivity;

    // root view attach other view to it
    protected View mViewContainer;

    // //view content root init attach other content view to it
    protected ViewGroup mViewContentContainer;

    protected ViewGroup mViewHeaderContainer;

    protected ViewGroup mFooterContainer;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            mActivity = (BaseActivity) activity;
            mActivity.addKeyBackCallBack(mKeyBackPressCallBack);
            mActivity.addMenuCallBack(mSlidingMenuCallBack);
        } else {
            LogUtils.e(TAG, "Activity not instanceof MainActivity");
        }
    }

    private final BaseActivity.SlidingMenuCallBack mSlidingMenuCallBack = new BaseActivity.SlidingMenuCallBack() {
        @Override
        public void onMenuClosed() {
            onSlidingMenuClosed();
        }

        @Override
        public void onMenuOpened() {
            onSlidingMenuOpened();
        }
    };

    protected void onSlidingMenuClosed() {
        //Code here
    }

    protected void onSlidingMenuOpened() {
        //Code here
    }

    private final BaseActivity.KeyBackPressCallBack mKeyBackPressCallBack = new BaseActivity.KeyBackPressCallBack() {
        @Override
        public void handleBackPress() {
            if (isFragmentValid()) {
                onKeyBackPress();
            }
        }
    };

    protected final boolean isFragmentValid() {
        return isVisible() && isAdded() && isResumed() && !isRemoving() && !isDetached();
    }

    @Override
    public void onDetach() {
        if (mActivity != null) {
            mActivity.removeKeyBackCallBack(mKeyBackPressCallBack);
            mActivity.removeMenuCallBack(mSlidingMenuCallBack);
        }
        super.onDetach();
    }

    private boolean isFragmentAlreadyLoaded;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null && !this.isFragmentAlreadyLoaded) {
            this.isFragmentAlreadyLoaded = true;
            // Code placed here will be executed once
        }
        //Code placed here will be executed even when the fragment comes from backstack
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected boolean ignoreBaseContainerLayout() {
        return false;
    }

    /**
     * Merge with Header root layout
     */
    protected boolean ignoreHeaderContainer() {
        return false;
    }

    /**
     * Merge with Footer root layout
     */
    protected boolean ignoreFooterContainer() {
        return false;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.mViewContainer != null && this.mViewContainer.getParent() != null) {
            ((ViewGroup) this.mViewContainer.getParent()).removeView(this.mViewContainer);
        } else {
            // ----init container view
//            final LayoutInflater inflater = this.getLayoutInflater();
            if (!isFragmentAlreadyLoaded) if (ignoreBaseContainerLayout()) {
                this.mViewContainer = this.onCreateContentView(inflater, container);
                try {
                    this.onCreateFragment(this.mViewContainer);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {

                this.mViewContainer = safeInflater(inflater, container, R.layout.base_screen_container, false);

                this.mViewHeaderContainer = (ViewGroup) this.mViewContainer.findViewById(R.id.layout_header);
                this.mViewContentContainer = (ViewGroup) this.mViewContainer.findViewById(R.id.layout_content);
                this.mFooterContainer = (ViewGroup) this.mViewContainer.findViewById(R.id.layout_footer);

                // ----add header view
                final View headerView = this.onCreateHeaderView(inflater, this.mViewHeaderContainer);
                if (headerView != null && headerView.getParent() == null) {
                    if (ignoreHeaderContainer()) {
                        replaceView(this.mViewHeaderContainer, headerView, null);
                    } else {
                        this.mViewHeaderContainer.addView(headerView);
                    }
                }

                // -----add footer view
                final View footerView = this.onCreateFooterView(inflater, this.mFooterContainer);
                if (footerView != null && footerView.getParent() == null) {
                    if (ignoreHeaderContainer()) {
                        replaceView(this.mFooterContainer, footerView, null);
                    } else {
                        this.mFooterContainer.addView(footerView);
                    }
                }

                // ----add content view
                final View viewContent = this.onCreateContentView(inflater, this.mViewContentContainer);
                //background sync
                //final View backgroundSyncView = safeInflater(inflater, this.mViewContentContainer, R.layout.background_sync);
                //end background sync
                if (viewContent != null && viewContent.getParent() == null) {
                    if (ignoreHeaderContainer()) {
                        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mViewContentContainer.getLayoutParams();
                        int idHeader = getNewId(ignoreHeaderContainer(), this.mViewHeaderContainer, headerView);
                        if (idHeader != 0) {
                            params.addRule(RelativeLayout.BELOW, idHeader);
                        }
                        int idFooter = getNewId(ignoreFooterContainer(), this.mFooterContainer, footerView);
                        if (idFooter != 0) {
                            params.addRule(RelativeLayout.ABOVE, idFooter);
                        }
                        replaceView(this.mViewContentContainer, viewContent, params);
                    } else {

                        this.mViewContentContainer.addView(viewContent);
                        //this.mViewContentContainer.addView(backgroundSyncView);
                    }
                    try {
                        this.onCreateFragment(viewContent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this.mViewContainer;
    }

    private int getNewId(boolean ignore, View currentView, View newView) {
        int idView;
        if (ignore) {
            if (newView != null) {
                idView = newView.getId();
            } else {
                idView = currentView.getId();
            }
        } else {
            idView = currentView.getId();
        }
        return idView;
    }

    protected void replaceContentView(int pNewResId) {
        mViewContentContainer.removeAllViews();
        mViewContentContainer.addView(getLayoutInflater().inflate(pNewResId, null));
    }

    public static ViewGroup getParent(View view) {
        return (ViewGroup) view.getParent();
    }

    public static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if (parent != null) {
            parent.removeView(view);
        }
    }

    public static void replaceView(View currentView, View newView, ViewGroup.LayoutParams layoutParams) {
        ViewGroup parent = getParent(currentView);
        if (parent == null) {
            return;
        }
        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        removeView(newView);
        if (layoutParams != null) {
            parent.addView(newView, index, layoutParams);
        } else {
            parent.addView(newView, index);
        }
    }


    protected abstract View onCreateContentView(LayoutInflater pInflater, ViewGroup pContainer);

    protected View onCreateFooterView(LayoutInflater pInflater, ViewGroup pContainer) {
        return null;
    }

    protected abstract void onCreateFragment(View pContentView) throws ParseException;

    protected View onCreateHeaderView(LayoutInflater pInflater, ViewGroup pContainer) {
        return null;
    }

    public final View safeInflater(LayoutInflater pInflater, ViewGroup pContainer, int pLayoutResId) {
        return this.safeInflater(pInflater, pContainer, pLayoutResId, false);
    }

    public final View safeInflater(LayoutInflater pInflater, ViewGroup pContainer, int pLayoutResId, boolean pAttachToRoot) {
        View view = null;
        try {
            view = pInflater.inflate(pLayoutResId, pContainer, pAttachToRoot);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return view;
    }

    protected void onKeyBackPress() {
        finishFragment();
    }

    public final LayoutInflater getLayoutInflater() {
        return (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Replace fragment
     */
    public void replaceFragment(int pLayoutResId, Class<? extends BaseFragment> pFragment, boolean pAddToBackStack) {
        this.replaceFragment(pLayoutResId, pFragment, null, pAddToBackStack);
    }

    /**
     * Replace fragment
     */
    public void replaceFragment(int pLayoutResId, BaseFragment pFragment, boolean pAddToBackStack) {
        this.replaceFragment(pLayoutResId, pFragment, null, pAddToBackStack);
    }

    /**
     * Replace fragment
     */
    public void replaceFragment(int pLayoutResId, Class<? extends BaseFragment> pFragment, Bundle pBundle, boolean pAddToBackStack) {
        this.replaceFragment(pLayoutResId, pFragment, pFragment.getName(), pBundle, mActivity.getSupportFragmentManager(), pAddToBackStack);
    }



    /**
     * Replace fragment
     */
//    public void replaceFragmentNoAnimation(int pLayoutResId, Class<? extends BaseFragment> pFragment, Bundle pBundle) {
//        this.replaceFragment(pLayoutResId, pFragment, pBundle/*, 0*/);
//    }

//    /**
//     * Replace fragment with custom animation
//     */
//    public void replaceFragment(int pLayoutResId, Class<? extends BaseFragment> pFragment, Bundle pBundle/*, int pAnimation*/) {
//        this.replaceFragment(pLayoutResId, pFragment, pFragment.getName(), pBundle, getFragmentManager()/*, pAnimation*/);
//    }
    public void clearBackStack(FragmentManager pFragmentManager) {
        pFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void clearBackStackAndKeepFirstFragment(FragmentManager pFragmentManager) {
        //http://stackoverflow.com/questions/5802141/is-this-the-right-way-to-clean-up-fragment-back-stack-when-leaving-a-deeply-nest
        if (pFragmentManager.getBackStackEntryCount() > 0) {
            int firstFragmentId = pFragmentManager.getBackStackEntryAt(0).getId();
            pFragmentManager.popBackStackImmediate(firstFragmentId, 0);
        }
    }

    /**
     * Replace fragment with custom animation
     */
    private void replaceFragment(int pLayoutResId, Class<? extends BaseFragment> pFragment, String pTag, Bundle pBundle, FragmentManager pFragmentManager, boolean pAddToBackStack) {
        try {
            final BaseFragment frag = pFragment.newInstance();
            this.replaceFragment(pLayoutResId, frag, pTag, pBundle, pFragmentManager, pAddToBackStack);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

  /*  public void replaceFragment(int pLayoutResId, BaseFragment pFragment, Bundle pBundle, FragmentManager pFragmentManager, boolean pAddToBackStack) {
        try {
//            final BaseFragment frag = pFragment.newInstance();
            this.replaceFragment(pLayoutResId, pFragment, pFragment.getTag(), pBundle, pFragmentManager, pAddToBackStack);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }*/

    public void replaceFragment(int pLayoutResId, BaseFragment pFragment, Bundle pBundle, boolean pAddToBackStack) {
        this.replaceFragment(pLayoutResId, pFragment, null, pBundle, mActivity.getSupportFragmentManager(), pAddToBackStack);
    }

    private void replaceFragment(int pLayoutResId, BaseFragment pFragment, String pTag, Bundle pBundle, FragmentManager pFragmentManager, boolean pAddToBackStack) {
        if (mActivity != null) {
            KeyboardUtils.hideKeyboard(mActivity);
        }
        final FragmentTransaction fragmentTransaction = pFragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        if (pBundle != null) {
            pFragment.setArguments(pBundle);
        }
         //fragmentTransaction.hide(this);
        final String backStateName = pTag != null ? pTag : pFragment.getClass().getName();
        if (pAddToBackStack) {
            fragmentTransaction.addToBackStack(backStateName);
        }
        fragmentTransaction.replace(pLayoutResId, pFragment, backStateName);
        fragmentTransaction.commit();
    }

    public void replaceFragmentForMenu(int pLayoutResId, Class<? extends BaseFragment> pFragment, Bundle pBundle, boolean pAddToBackStack) {
        if (mActivity != null) {
            KeyboardUtils.hideKeyboard(mActivity);
        }
        final FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);

            try {
                BaseFragment baseFragment = pFragment.newInstance();
                if (pBundle != null) {
                    baseFragment.setArguments(pBundle);
                }
                final String backStateName = pFragment.getName();
                if (pAddToBackStack) {
                    fragmentTransaction.addToBackStack(backStateName);
                }
                fragmentTransaction.replace(pLayoutResId,  baseFragment, backStateName);
                fragmentTransaction.commit();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        //fragmentTransaction.hide(this);

    }

    /**
     * Replace fragment for tablet
     */
    public void replaceFragmentTablet(int pLayoutResId, Class<? extends BaseFragment> pFragment, boolean pFlag) {
        try {
            final BaseFragment frag = pFragment.newInstance();
            this.replaceFragmentTablet(pLayoutResId, frag, pFragment.getName(), null, mActivity.getSupportFragmentManager(), pFlag/*, pAnimation*/);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void replaceFragmentTablet(int pLayoutResId, BaseFragment pFragment, boolean pFlag) {
        try {
            this.replaceFragmentTablet(pLayoutResId, pFragment, null, null, mActivity.getSupportFragmentManager(), pFlag/*, pAnimation*/);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void replaceFragmentTablet(int pLayoutResId, Class<? extends BaseFragment> pFragment, Bundle pBundle, boolean pFlag) {
        try {
            final BaseFragment frag = pFragment.newInstance();
            this.replaceFragmentTablet(pLayoutResId, frag, pFragment.getName(), pBundle, mActivity.getSupportFragmentManager(), pFlag);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     */
    public void replaceFragmentTablet(int pLayoutResId, Class<? extends BaseFragment> pFragment, Bundle pBundle, FragmentManager pFragmentManager, boolean pFlag) {
        try {
            final BaseFragment frag = pFragment.newInstance();
            this.replaceFragmentTablet(pLayoutResId, frag, pFragment.getName(), pBundle, pFragmentManager, pFlag);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void replaceFragmentTablet(int pLayoutResId, BaseFragment pFragment, Bundle pBundle, FragmentManager pFragmentManager, boolean pFlag) {
        this.replaceFragmentTablet(pLayoutResId, pFragment, null, pBundle, pFragmentManager, pFlag);
    }

    /**
     * Replace fragment
     */
    public void replaceFragmentTablet(int pLayoutResId, BaseFragment pFragment) {
        this.replaceFragmentTablet(pLayoutResId, pFragment, null, null, mActivity.getSupportFragmentManager(), FLAG_NOT_ADD_STACK);
    }

    public void replaceFragmentTablet(int pLayoutResId, BaseFragment pFragment, Bundle pBundle, boolean pFlag) {
        this.replaceFragmentTablet(pLayoutResId, pFragment, null, pBundle, mActivity.getSupportFragmentManager(), pFlag);
    }

    public void replaceFragmentTablet(int pLayoutResId, BaseFragment pFragment, String pTag, boolean pFlag) {
        this.replaceFragmentTablet(pLayoutResId, pFragment, pTag, null, mActivity.getSupportFragmentManager(), pFlag);
    }

    /**
     * Replace fragment for tablet
     */
    public void replaceFragmentTablet(int pLayoutResId, BaseFragment pFragment, String pTag, Bundle pBundle, FragmentManager pFragmentManager, boolean pFlag /*, int pAnimation*/) {
        final FragmentTransaction fragmentTransaction = pFragmentManager.beginTransaction();
        if (pBundle != null) {
            pFragment.setArguments(pBundle);
        }
        final String backStateName = pTag != null ? pTag : pFragment.getClass().getName();
        if (pFlag) {//if pFlag == true then add to back stack
            fragmentTransaction.addToBackStack(backStateName);
        }
        fragmentTransaction.replace(pLayoutResId, pFragment, backStateName);
        fragmentTransaction.commit();
    }

//    public void removeFragmentTablet(int pFragmentId){
//        final FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.remove(mActivity.getSupportFragmentManager().findFragmentById(pFragmentId)).commit();
//    }

    public void removeFragmentTablet(Class<? extends BaseFragment> pFragment) {
        mActivity.getSupportFragmentManager().popBackStackImmediate(pFragment.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void clearBackStack() {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 0) {
//            int i = fm.getBackStackEntryCount();
            fm.popBackStackImmediate();
        }
    }

    public void finishFragment() {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        int backStackCount = fm.getBackStackEntryCount();
        LogUtils.d(TAG, "" + backStackCount);
        if (backStackCount > 0) {
            LogUtils.d(TAG, "popping back stack");
            fm.popBackStackImmediate();
        }
    }

    public Application getApplication() {
        return getActivity().getApplication();
    }

    public void toggleMenu() {
        mActivity.toggleMenu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideKeyboard(mActivity);
    }
}