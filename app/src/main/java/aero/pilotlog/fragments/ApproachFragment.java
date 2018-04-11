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
import aero.pilotlog.adapters.TypeOfAppAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZApproach;
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
 * Created by tuan.na on 9/15/2015.
 * Approach
 */
public class ApproachFragment extends BaseMCCFragment implements IAsyncTaskCallback {

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
    @Bind(R.id.edtTags)
    TagsEditText tagsEditText;
    @Bind(R.id.ln_tag_bar)
    LinearLayout lnTagBar;

    private List<ZApproach> mTypeOfAppList = new ArrayList<>();
    private List<ZApproach> mTypeOfAppListCopy = new ArrayList<>();
    private TypeOfAppAdapter mTypeOfAppAdapter;
    private boolean mIsSearch = true;
    private StringBuilder mZCodes;
    private List<String> mZShorts;
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
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        initView();
        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void initTagsView() {
        lnTagBar.setVisibility(View.VISIBLE);
        List<String> listZShort = new ArrayList<>();
        for (int i = 0; i < mTypeOfAppList.size(); i++) {
            listZShort.add(mTypeOfAppList.get(i).getAPShort());
        }
        String[] arrayZShort = new String[listZShort.size()];
        arrayZShort = listZShort.toArray(arrayZShort);
        //tagsEditText.setArrayAutoComplete(arrayZShort);
        FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
        if (fragment != null) {
            String zCodes = fragment.getmZApproachCodes();
            if (!TextUtils.isEmpty(zCodes)) {
                String[] zCodeArray;
                zCodeArray = zCodes.split(MCCPilotLogConst.SPLIT_KEY);
                mZShorts = new ArrayList<>();
                for (int i = 0; i < zCodeArray.length; i++) {
                    ZApproach zApproach = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity).getZApproach(zCodeArray[i]);
                    if (zApproach != null) {
                        mZShorts.add(zApproach.getAPShort());
                    }
                }
                String[] mZShortArr = new String[mZShorts.size()];
                mZShorts.toArray(mZShortArr);
                tagsEditText.setTags(mZShortArr);
            }
        }
    }

    private void closeFragment() {
        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {*/
                FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
                if (fragment != null) {

                    if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                        mZCodes = new StringBuilder();
                        for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                            if (!TextUtils.isEmpty(tagsEditText.getTags().get(i))) {
                                ZApproach zApproach = DatabaseManager.getInstance(mActivity).getZApproachByAPShort(tagsEditText.getTags().get(i));
                                if (zApproach != null && !TextUtils.isEmpty(zApproach.getAPShort())) {
                                    mZCodes.append(zApproach.getAPCode());
                                    mZCodes.append(MCCPilotLogConst.SPLIT_KEY_APPEND);
                                }
                            }
                        }
                        if (mZCodes.length() > 2) {
                            fragment.setmZApproachCodes(mZCodes.toString().substring(0, mZCodes.length() - 2));
                        }
                    } else {
                        fragment.setmZApproachCodes("");
                    }
                }
       /*     }
        });*/
        finishFragment();
    }

    private void initView() {
        mTvTitle.setText("Select Approach");
        mIbMenu.setImageResource(R.drawable.ic_back);
        mTypeOfAppList = DatabaseManager.getInstance(mActivity).getAllZApproach();
        if (mTypeOfAppList != null && mTypeOfAppList.size() > 0 && mTypeOfAppList.get(0).getAPCode() == 0) {
            mTypeOfAppList.remove(0);
        }
        mTypeOfAppListCopy.clear();
        if (mTypeOfAppList != null && !mTypeOfAppList.isEmpty()) {
            mTypeOfAppListCopy.addAll(mTypeOfAppList);
        } else {
            mTypeOfAppList = new ArrayList<>();
        }

        mTypeOfAppAdapter = new TypeOfAppAdapter(mActivity, mTypeOfAppList);
        mIndexableListView.setDrawRightBar(false);//Draw right bar contain characters of list view
        mIndexableListView.setAdapter(mTypeOfAppAdapter);
        mIndexableListView.setFastScrollEnabled(true);

        fragmentAircraftAdd = (AircraftAddFragment) getFragment(AircraftAddFragment.class);

        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
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

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition, long pLong) {
        if (fragmentAircraftAdd != null) {
            fragmentAircraftAdd.setSelectedApproach(mTypeOfAppList.get(pPosition));
            KeyboardUtils.hideKeyboard(mActivity);
            finishFragment();
        } else {
            ZApproach zApproach = mTypeOfAppList.get(pPosition);
            //zApproach.getAPCode();
            mZShorts = new ArrayList<>();
            if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                    mZShorts.add(tagsEditText.getTags().get(i));
                }
            }
            if (mZShorts.size() < MCCPilotLogConst.MAX_LENGTH_TAGS) {
                mZShorts.add(zApproach.getAPShort());
            }
            String[] mZShortArr = new String[mZShorts.size()];
            mZShorts.toArray(mZShortArr);
            tagsEditText.setTags(mZShortArr);
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
        mTypeOfAppList.clear();
        if (strSearch.length() > 0) {
            mTypeOfAppList = DatabaseManager.getInstance(mActivity).searchZApproach(strSearch);
        } else {
            mTypeOfAppList.addAll(mTypeOfAppListCopy);
        }

        refreshAdapter();
    }

    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
        mTypeOfAppList.clear();
        mTypeOfAppList.addAll(mTypeOfAppListCopy);

        refreshAdapter();
    }

    public void refreshAdapter() {
        if (mTypeOfAppAdapter != null) {
            mTypeOfAppAdapter.refreshAdapter(mTypeOfAppList);
        }
    }
    //PL-473
 /*   private void sortTypeOfApp(List<ZApproach> typeOfApps) {
        if (typeOfApps != null) {
            Collections.sort(typeOfApps, new Comparator<ZApproach>() {
                @Override
                public int compare(ZApproach o1, ZApproach o2) {
                    int value = o1.getCounter().compareTo(o2.getCounter());
                    return -value;
                }
            });
        }
    }*/
    //End PL-473

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
