package aero.pilotlog.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.FNPTAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZFNPT;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;

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
public class FNPTFragment extends BaseMCCFragment {


    public FNPTFragment() {
        // Required empty public constructor
    }

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

    private List<ZFNPT> mFNPTList = new ArrayList<>();
    private List<ZFNPT> mFNPTListCopy = new ArrayList<>();
    private FNPTAdapter mFNPTAdapter;
    private boolean mIsSearch = true;
    private StringBuilder mZCodes;
    private List<String> mZShorts;



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
        Bundle bundle = getArguments();
        initView(bundle);
    }

    private void initView(Bundle bundle) {

        mIbMenu.setImageResource(R.drawable.ic_back);
        boolean isDrone = bundle.getBoolean(MCCPilotLogConst.ZNPT_TYPE, false);
        if (isDrone) {
            mTvTitle.setText("Select Drone Type");
        } else {
            mTvTitle.setText("Select Simulator Type");
        }
        mFNPTList = DatabaseManager.getInstance(mActivity).getAllZFNPTByDrone(isDrone);
        if (mFNPTList != null && mFNPTList.size() > 0 && mFNPTList.get(0).getFnptCode() == 0) {
            mFNPTList.remove(0);
        }
        mFNPTListCopy.clear();
        if (mFNPTList != null && !mFNPTList.isEmpty()) {
            mFNPTListCopy.addAll(mFNPTList);
        } else {
            mFNPTList = new ArrayList<>();
        }

        mFNPTAdapter = new FNPTAdapter(mActivity, mFNPTList);
        mIndexableListView.setDrawRightBar(false);//Draw right bar contain characters of list view
        mIndexableListView.setAdapter(mFNPTAdapter);
        mIndexableListView.setFastScrollEnabled(true);


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
                finishFragment();
                break;
        }
    }

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView,final int pPosition, long pLong) {
        AircraftAddFragment fragmentAircraftAdd = (AircraftAddFragment) getFragment(AircraftAddFragment.class);
        if (fragmentAircraftAdd != null) {
            fragmentAircraftAdd.setSelectedFNPT(mFNPTList.get(pPosition));
            KeyboardUtils.hideKeyboard(mActivity);
            finishFragment();
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
        mFNPTList.clear();
        if (strSearch.length() > 0) {
            mFNPTList = DatabaseManager.getInstance(mActivity).searchZFNPT(strSearch);
        } else {
            mFNPTList.addAll(mFNPTListCopy);
        }

        refreshAdapter();
    }

    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
        mFNPTList.clear();
        mFNPTList.addAll(mFNPTListCopy);

        refreshAdapter();
    }

    public void refreshAdapter() {
        if (mFNPTAdapter != null) {
            mFNPTAdapter.refreshAdapter(mFNPTList);
        }
    }

    @Override
    public void onKeyBackPress() {
        finishFragment();
        super.onKeyBackPress();
    }
}
