package aero.pilotlog.fragments;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.LaunchesAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZLaunch;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.tagsedittext.TagsEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * Created by tuan.na on 9/16/2015.
 * GliderLaunches
 */
public class GliderLaunchFragment extends BaseMCCFragment implements IAsyncTaskCallback {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Nullable
    @Bind(R.id.edtSearch)
    EditText mEdtSearch;
    @Bind(R.id.indexableListView)
    IndexableListView mIndexableListView;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
   /* @Bind(R.id.ln_clear)
    LinearLayout mLnClear;*/
    @Bind(R.id.ln_tag_bar)
    LinearLayout lnTagBar;
    @Bind(R.id.edtTags)
    TagsEditText tagsEditText;

    private List<ZLaunch> mLaunches = new ArrayList<>();
    private List<ZLaunch> mLaunchesCopy = new ArrayList<>();
    private LaunchesAdapter mAdapter;
    private boolean mIsSearch = true;
    private StringBuilder mZLaunchCodes;
    private List<String> mZLaunchShorts;
    AircraftAddFragment fragmentAircraftAdd;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_delay_tails;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        initView();
        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void initView() {
        mTvTitle.setText("Select Glider Launch");
        //mLnClear.setVisibility(View.VISIBLE);
        mIbMenu.setImageResource(R.drawable.ic_back);
        mLaunches = DatabaseManager.getInstance(mActivity).getAllGliderLaunch();
        //PL-385
        if (mLaunches != null && mLaunches.size() > 0 && mLaunches.get(0).getLaunchCode() == 0) {
            mLaunches.remove(0);
        }
        //End PL-385
        mLaunchesCopy.clear();
        if (mLaunches != null && !mLaunches.isEmpty()) {
            mLaunchesCopy.addAll(mLaunches);
        }

        mAdapter = new LaunchesAdapter(mActivity, mLaunches);
        mIndexableListView.setDrawRightBar(true);//Draw right bar contain characters of list view
        mIndexableListView.setAdapter(mAdapter);
        mIndexableListView.setFastScrollEnabled(true);

        fragmentAircraftAdd = (AircraftAddFragment) getFragment(AircraftAddFragment.class);

        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
    }

    private void initTagsView() {
        lnTagBar.setVisibility(View.VISIBLE);
        List<String> listZLaunchShort = new ArrayList<>();
        for (int i = 0; i < mLaunches.size(); i++) {
            listZLaunchShort.add(mLaunches.get(i).getLaunchShort());
        }
        String[] arrayZLaunchShort = new String[listZLaunchShort.size()];
        arrayZLaunchShort = listZLaunchShort.toArray(arrayZLaunchShort);
        //tagsEditText.setArrayAutoComplete(arrayZLaunchShort);
        FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
        if (fragment != null) {
            String zLaunchCodes = fragment.getmZLaunchCodes();
            if (!TextUtils.isEmpty(zLaunchCodes)) {
                String[] zLaunchCodeArray;
                zLaunchCodeArray = zLaunchCodes.split(MCCPilotLogConst.SPLIT_KEY);
                mZLaunchShorts = new ArrayList<>();
                for (int i = 0; i < zLaunchCodeArray.length; i++) {
                    ZLaunch zLaunch = DatabaseManager.getInstance(mActivity).getGliderLaunch(zLaunchCodeArray[i]);
                    if (zLaunch != null) {
                        mZLaunchShorts.add(zLaunch.getLaunchShort());
                    }
                }
                String[] mZLaunchShortArr = new String[mZLaunchShorts.size()];
                mZLaunchShorts.toArray(mZLaunchShortArr);
                tagsEditText.setTags(mZLaunchShortArr);
            }
        }
    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnCancel})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btnCancel:
                mIsSearch = false;
                cancelSearch(mEdtSearch);
                break;
            case R.id.ibMenu:
                closeFragment();
                break;
        }
    }

    private void closeFragment() {
      /*  AsyncTask.execute(new Runnable() {
            @Override
            public void run() {*/
                FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
                if (fragment != null) {

                    if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                        mZLaunchCodes = new StringBuilder();
                        for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                            if (!TextUtils.isEmpty(tagsEditText.getTags().get(i))) {
                                ZLaunch zLaunch = DatabaseManager.getInstance(mActivity).getGliderLaunchByLaunchShort(tagsEditText.getTags().get(i));
                                if (zLaunch != null && !TextUtils.isEmpty(zLaunch.getLaunchShort())) {
                                    mZLaunchCodes.append(zLaunch.getLaunchCode());
                                    mZLaunchCodes.append(MCCPilotLogConst.SPLIT_KEY_APPEND);
                                }
                            }
                        }
                        if (mZLaunchCodes.length() > 2) {
                            fragment.setMZLaunchCodes(mZLaunchCodes.toString().substring(0, mZLaunchCodes.length() - 2));
                        }
                    } else {
                        fragment.setMZLaunchCodes("");
                    }
                }
           /* }
        });*/
        finishFragment();
    }

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition, long pLong) {
        if (fragmentAircraftAdd != null) {
            fragmentAircraftAdd.setSelectedLaunch(mLaunches.get(pPosition));
            KeyboardUtils.hideKeyboard(mActivity);
            finishFragment();
        } else {
            ZLaunch zLaunch = mLaunches.get(pPosition);
            zLaunch.getLaunchCode();
            mZLaunchShorts = new ArrayList<>();
            if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                    mZLaunchShorts.add(tagsEditText.getTags().get(i));
                }
            }
            if (mZLaunchShorts.size() < MCCPilotLogConst.MAX_LENGTH_TAGS) {
                mZLaunchShorts.add(zLaunch.getLaunchShort());
            }
            String[] mZLaunchShortArr = new String[mZLaunchShorts.size()];
            mZLaunchShorts.toArray(mZLaunchShortArr);
            tagsEditText.setTags(mZLaunchShortArr);
        }
    }

    @Nullable
    @OnTextChanged(R.id.edtSearch)
    public void onTextChanged(CharSequence pText) {
        if (mIsSearch) {
            doSearch(pText.toString());
        }
        mIsSearch = true;
    }

    public void doSearch(String pText) {
        String strSearch = pText.trim();
        mLaunches.clear();
        if (strSearch.length() > 0) {
            mLaunches = DatabaseManager.getInstance(mActivity).searchLaunches(strSearch);
        } else {
            mLaunches.addAll(mLaunchesCopy);
        }

        refreshAdapter();
    }

    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
        mLaunches.clear();
        mLaunches.addAll(mLaunchesCopy);

        refreshAdapter();
    }

    public void refreshAdapter() {
        if (mAdapter != null) {
            mAdapter.refreshAdapter(mLaunches);
        }
    }

    @Override
    public void onKeyBackPress() {
        closeFragment();
        super.onKeyBackPress();
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {

    }

    @Override
    public void updateUI() {
        if (fragmentAircraftAdd == null) {
            initTagsView();
        }
    }

    @Override
    public void end() {

    }
}
