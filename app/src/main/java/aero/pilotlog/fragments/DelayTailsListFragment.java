package aero.pilotlog.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.DelayTailsAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZDelay;
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
 * Created by tuan.pv on 2015/07/13.
 * Delay tail
 */
public class DelayTailsListFragment extends BaseMCCFragment implements IAsyncTaskCallback {

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

    private StringBuilder mZCodes;
    private List<String> mZShorts;

    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManagerV5;
    private List<ZDelay> mDelays = new ArrayList<>();
    private List<ZDelay> mDelaysCopy = new ArrayList<>();
    //private List<TailsCountryModel> mTailsCountryModels = new ArrayList<>();
    //private List<TailsCountryModel> mTailsCountryModelsCopy = new ArrayList<>();
    private DelayTailsAdapter mDelayTailsAdapter;
    private int mViewType = MCCPilotLogConst.LIST_MODE; //mTypeAdapter = MCCPilotLogConst.DELAY_ADAPTER;
    private boolean mIsSearch = true;

    @Override
    protected int getContentResId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mViewType = bundle.getInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE);
           // mTypeAdapter = bundle.getInt(MCCPilotLogConst.DELAY_OR_TAILS);
        }

       /* if (mTypeAdapter == MCCPilotLogConst.DELAY_ADAPTER && mViewType == MCCPilotLogConst.SELECT_MODE) {
            //With screen delay in select mode, then a both phone and tablet return same content resource id.
            return R.layout.fragment_delay_tails;
        }*/
        return  R.layout.fragment_delay_tails;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        //int data
        initView();
        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Init view
     */
    public void initView() {

        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            mTvTitle.setText("Select Delay Codes");
            mIbMenu.setImageResource(R.drawable.ic_back);
        }else {
            mTvTitle.setText( R.string.text_delay_codes );
        }
        if (mEdtSearch != null) {
            KeyboardUtils.hideKeyboardWhenClickLoupe(mEdtSearch, getActivity());
        }


        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        //get data from logbook database

        /*if (mTypeAdapter == MCCPilotLogConst.DELAY_ADAPTER) {
            mDelays = mDatabaseManagerV5.getAllZDelay();
            mDelaysCopy.clear();
            if (mDelays != null && !mDelays.isEmpty()) {
                mDelaysCopy.addAll(mDelays);
            }
            if(mViewType == MCCPilotLogConst.SELECT_MODE){
                initTagsView();
            }
        } else {//MCCPilotLogConst.TAILS_ADAPTER
            mTailsCountryModels = mDatabaseManager.getAllCountryFromTails(MCCPilotLogConst.NON_SEARCH, "");
            mTailsCountryModelsCopy.clear();
            if (mTailsCountryModels != null && !mTailsCountryModels.isEmpty()) {
                mTailsCountryModelsCopy.addAll(mTailsCountryModels);
            }
        }*/
        mDatabaseManagerV5 = DatabaseManager.getInstance(mActivity);

    }

    @Override
    public void updateUI() {
        mDelays = mDatabaseManagerV5.getAllZDelay("");
        mDelaysCopy.clear();
        if (mDelays != null && !mDelays.isEmpty()) {
            mDelaysCopy.addAll(mDelays);
        }
        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            initTagsView();
        }

    }

    @Override
    public void end() {
        //fill data on view
        setTextSizeDelayTail();
        mDelayTailsAdapter = new DelayTailsAdapter(mActivity, mDelays);
        mDelayTailsAdapter.setViewType(mViewType);
        mIndexableListView.setDrawRightBar(true);//Draw right bar contain characters of list view
        mIndexableListView.setAdapter(mDelayTailsAdapter);
        mIndexableListView.setFastScrollEnabled(true);
    }

    /**
     * Handle onclick on back icon
     *
     * @param pView view
     */
    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnCancel})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btnCancel:
                mIsSearch = false;
                cancelSearch(mEdtSearch);
                break;
            case R.id.ibMenu:
                if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                    closeFragment();
                    return;
                }
                toggleMenu();
                break;

        }
    }

    /**
     * Handle click on item in delay listview
     *
     * @param pAdapterView
     * @param pView
     * @param pPosition
     * @param pLong
     */
    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition, long pLong) {
        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            ZDelay zDelay = mDelays.get(pPosition);
            zDelay.getDelayCode();
            mZShorts = new ArrayList<>();
            if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                    mZShorts.add(tagsEditText.getTags().get(i));
                }
            }
            if(mZShorts.size()<MCCPilotLogConst.MAX_LENGTH_TAGS){
                mZShorts.add(zDelay.getDelayCode() + " (" + zDelay.getDelayDD() + ")");
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

    /**
     * process search
     *
     * @param pText
     */
    public void doSearch(String pText) {
        String strSearch = pText.trim();
            mDelays.clear();
            if (strSearch.length() > 0) {
                mDelays = mDatabaseManagerV5.searchZDelay(strSearch);
            } else {
                mDelays.addAll(mDelaysCopy);
            }

        refreshAdapter();
    }

    /**
     * Process cancel search
     *
     * @param pEditTextSearch
     */
    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
            mDelays.clear();
            mDelays.addAll(mDelaysCopy);
        refreshAdapter();
    }

    /**
     * Refresh delay list or tails list
     */
    public void refreshAdapter() {
        if (mDelayTailsAdapter != null) {
                mDelayTailsAdapter.refreshDelayAdapter(mDelays);
            setTextSizeDelayTail();
        }
    }

    public void setTextSizeDelayTail() {
        int size = 0;
            if (mDelays != null && !mDelays.isEmpty()) {
                size = mDelays.size();
            }
        mTvNumber.setText("" + size);
    }

    private void initTagsView() {
        lnTagBar.setVisibility(View.VISIBLE);
        List<String> listZShort = new ArrayList<>();
        for (int i = 0; i < mDelays.size(); i++) {
            listZShort.add(mDelays.get(i).getDelayCode() + " (" + mDelays.get(i).getDelayDD() + ")");
        }
        String[] arrayZShort = new String[listZShort.size()];
        arrayZShort = listZShort.toArray(arrayZShort);
        //tagsEditText.setArrayAutoComplete(arrayZShort);
        FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
        if (fragment != null) {
            String zCodes = fragment.getmZDelayCodes();
            if (!TextUtils.isEmpty(zCodes)) {
                String[] zCodeArray;
                zCodeArray = zCodes.split(MCCPilotLogConst.SPLIT_KEY);
                mZShorts = new ArrayList<>();
                for (int i = 0; i < zCodeArray.length; i++) {
                    ZDelay zDelay = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity).getZDelay(zCodeArray[i]);
                    if (zDelay != null) {
                        mZShorts.add(zDelay.getDelayCode() + " (" + zDelay.getDelayDD() + ")");
                    }
                }
                String[] mZShortArr = new String[mZShorts.size()];
                mZShorts.toArray(mZShortArr);
                tagsEditText.setTags(mZShortArr);
            }
        }
    }

    private void closeFragment() {
       /* AsyncTask.execute(new Runnable() {
            @Override
            public void run() {*/
                FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
                if (fragment != null) {

                    if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                        mZCodes = new StringBuilder();
                        for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                            if (!TextUtils.isEmpty(tagsEditText.getTags().get(i))) {
                                ZDelay zDelay = mDatabaseManagerV5.getInstance(mActivity)
                                        .getZDelay(tagsEditText.getTags().get(i).split(" \\(")[0]);
                                if (zDelay != null) {
                                    mZCodes.append(zDelay.getDelayCode());
                                    mZCodes.append(MCCPilotLogConst.SPLIT_KEY_APPEND);
                                }
                            }
                        }
                        if (mZCodes.length() > 2) {
                            fragment.setmZDelayCodes(mZCodes.toString().substring(0, mZCodes.length() - 2));
                        }
                    } else {
                        fragment.setmZDelayCodes("");
                    }
                }
       /*     }
        });*/

        finishFragment();
    }

    @Override
    public void onKeyBackPress() {
        closeFragment();
        super.onKeyBackPress();
    }
}