package aero.pilotlog.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.List;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.api.APIManager;
import aero.pilotlog.api.MccNetWorkingService;
import aero.pilotlog.common.CloudApiConstants;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.RecordDelete;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.MccCallback;
import aero.pilotlog.models.Meta;
import aero.pilotlog.models.TableUpload;
import aero.pilotlog.models.UserProfile;
import aero.pilotlog.models.UserProfileRespond;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.utilities.TimeUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.MccDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;


public class SyncV5Fragment extends BaseMCCFragment {

    @Bind(R.id.containerSync)
    LinearLayout llContainerSync;
    @Bind(R.id.navigation)
    BottomNavigationView navigationView;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_sync_v5;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        if (fragmentSync == null)
            fragmentSync = new SyncFragment();
        fragTransaction.add(llContainerSync.getId(), fragmentSync, SyncFragment.class.getName());
        fragTransaction.commit();


        mTvTitle.setText(R.string.title_sync);
    }

    @Nullable
    @OnClick({R.id.ibMenu})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu: // Open sliding menu
                toggleMenu();
                break;
            default:
                break;
        }
    }


    private Fragment fragmentSync, fragmentMyDevices, fragmentResetDB;

    private void switchFragment(FragmentTransaction fragmentTransaction, Fragment fragmentShow, Fragment fragmentHide1, Fragment fragmentHide2) {
        if (fragmentHide1 != null) {
            fragmentTransaction.hide(fragmentHide1);
        }
        if (fragmentHide2 != null) {
            fragmentTransaction.hide(fragmentHide2);
        }
        if (fragmentShow != null) {
            fragmentTransaction.show(fragmentShow);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_sync:
                    if (fragmentSync == null) {
                        fragmentSync = new SyncFragment();
                        fragTransaction.add(llContainerSync.getId(), fragmentSync, SyncFragment.class.getName());
                    }
                    switchFragment(fragTransaction, fragmentSync, fragmentMyDevices, fragmentResetDB);
                    fragTransaction.commit();
                    return true;
                case R.id.navigation_devices:
                    if (fragmentMyDevices == null) {
                        fragmentMyDevices = new MyDevicesFragment();
                        fragTransaction.add(llContainerSync.getId(), fragmentMyDevices, MyDevicesFragment.class.getName());
                    }
                    switchFragment(fragTransaction, fragmentMyDevices, fragmentSync, fragmentResetDB);
                    fragTransaction.commit();
                    return true;
                case R.id.navigation_reset_database:
                    if (fragmentResetDB == null) {
                        fragmentResetDB = new ResetDatabaseFragment();
                        fragTransaction.add(llContainerSync.getId(), fragmentResetDB, ResetDatabaseFragment.class.getName());
                    }
                    switchFragment(fragTransaction, fragmentResetDB, fragmentSync, fragmentMyDevices);
                    fragTransaction.commit();
                    return true;
            }
            return false;
        }
    };
}
