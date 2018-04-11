package aero.pilotlog.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.OperationAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZOperation;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.tagsedittext.TagsEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class OperationFragment extends BaseMCCFragment implements IAsyncTaskCallback {

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

    private List<ZOperation> mOperationList = new ArrayList<>();
    private List<ZOperation> mOperationListCopy = new ArrayList<>();
    private OperationAdapter mOperationAdapter;
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
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        initView();
        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void initTagsView() {
        lnTagBar.setVisibility(View.VISIBLE);
        List<String> listZShort = new ArrayList<>();
        for (int i = 0; i < mOperationList.size(); i++) {
            listZShort.add(mOperationList.get(i).getOpsShort());
        }
        String[] arrayZShort = new String[listZShort.size()];
        arrayZShort = listZShort.toArray(arrayZShort);
        //tagsEditText.setArrayAutoComplete(arrayZShort);
        FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
        if (fragment != null) {
            String zCodes = fragment.getmZOperationCodes();
            if (!TextUtils.isEmpty(zCodes)) {
                String[] zCodeArray;
                zCodeArray = zCodes.split(MCCPilotLogConst.SPLIT_KEY);
                mZShorts = new ArrayList<>();
                for (int i = 0; i < zCodeArray.length; i++) {
                    ZOperation zOperation = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity).getZOperation(zCodeArray[i]);
                    if (zOperation != null) {
                        mZShorts.add(zOperation.getOpsShort());
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
                                ZOperation zOperation = DatabaseManager.getInstance(mActivity).getZOperationByOpsShort(tagsEditText.getTags().get(i));
                                if (zOperation != null && !TextUtils.isEmpty(zOperation.getOpsShort())) {
                                    mZCodes.append(zOperation.getOpsCode());
                                    mZCodes.append(MCCPilotLogConst.SPLIT_KEY_APPEND);
                                }
                            }
                        }
                        if (mZCodes.length() > 2) {
                            fragment.setmZOperationCodes((mZCodes.toString().substring(0, mZCodes.length() - 2)));
                        }
                    } else {
                        fragment.setmZOperationCodes("");
                    }
                }
       /*     }
        });*/
        finishFragment();
    }

    private void initView() {
        mTvTitle.setText("Select Operation");
        //mLnClear.setVisibility(View.VISIBLE);
        mIbMenu.setImageResource(R.drawable.ic_back);
        mOperationList = DatabaseManager.getInstance(mActivity).getAllZOperation();
        //PL-385
        if (mOperationList != null && mOperationList.size() > 0 && mOperationList.get(0).getOpsCode() == 0) {
            mOperationList.remove(0);
        }
        //End PL-385
        //PL-473
        //sortTypeOfApp(mTypeOfAppList);
        //End PL-473

        mOperationListCopy.clear();
        if (mOperationList != null && !mOperationList.isEmpty()) {
            mOperationListCopy.addAll(mOperationList);
        } else {
            mOperationList = new ArrayList<>();
        }

        mOperationAdapter = new OperationAdapter(mActivity, mOperationList);
        mIndexableListView.setDrawRightBar(false);//Draw right bar contain characters of list view
        mIndexableListView.setAdapter(mOperationAdapter);
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
            fragmentAircraftAdd.setSelectedOperation(mOperationList.get(pPosition));
            KeyboardUtils.hideKeyboard(mActivity);
            finishFragment();
        } else {
            ZOperation zOperation = mOperationList.get(pPosition);
            zOperation.getOpsCode();
            mZShorts = new ArrayList<>();
            if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                    mZShorts.add(tagsEditText.getTags().get(i));
                }
            }
            if (mZShorts.size() < MCCPilotLogConst.MAX_LENGTH_TAGS) {
                mZShorts.add(zOperation.getOpsShort());
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
        mOperationList.clear();
        if (strSearch.length() > 0) {
            mOperationList = DatabaseManager.getInstance(mActivity).searchZOperation(strSearch);
        } else {
            mOperationList.addAll(mOperationListCopy);
        }

        refreshAdapter();
    }

    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
        mOperationList.clear();
        mOperationList.addAll(mOperationListCopy);

        refreshAdapter();
    }

    public void refreshAdapter() {
        if (mOperationAdapter != null) {
            mOperationAdapter.refreshAdapter(mOperationList);
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
