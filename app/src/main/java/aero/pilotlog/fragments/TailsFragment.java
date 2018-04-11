package aero.pilotlog.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.TailsAdapter;
import aero.pilotlog.databases.entities.ZCountry;
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
public class TailsFragment extends BaseMCCFragment {


    public TailsFragment() {
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
  /*  @Bind(R.id.ln_clear)
    LinearLayout mLnClear;*/

    private List<ZCountry> mCountries = new ArrayList<>();
    private List<ZCountry> mCountriesCopy = new ArrayList<>();
    private TailsAdapter mTailsAdapter;
    private boolean mIsSearch = true;
    //private StringBuilder mZCodes;
    //private List<String> mZShorts;
    //AircraftAddFragment fragmentAircraftAdd;


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
    }

    private void initView() {
        mTvTitle.setText("Tails");
        mCountries = DatabaseManager.getInstance(mActivity).getAllCountry();
        if (mCountries != null && mCountries.size() > 0 && mCountries.get(0).getCountryCode() == 0) {
            mCountries.remove(0);
        }
        mCountriesCopy.clear();
        if (mCountries != null && !mCountries.isEmpty()) {
            mCountriesCopy.addAll(mCountries);
        } else {
            mCountries = new ArrayList<>();
        }
        mTailsAdapter = new TailsAdapter(mActivity, mCountries);
        mIndexableListView.setAdapter(mTailsAdapter);
        mIndexableListView.setFastScrollEnabled(true);
        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
        mIndexableListView.setDrawRightBar(true);
    }

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition, long pLong) {
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
                toggleMenu();
                break;
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
        if (strSearch.length() > 0) {
            List<ZCountry> listClone = new ArrayList<>();
            for (ZCountry zCountry : mCountries) {
                if (zCountry.getRegAC().replace("-", "").contains(pText)
                        || zCountry.getCountryName().substring(0, pText.length()).equalsIgnoreCase(pText)) {
                    listClone.add(zCountry);
                }
            }
            mCountries.clear();
            mCountries = listClone;
        } else {
            mCountries.clear();
            mCountries.addAll(mCountriesCopy);
        }

        refreshAdapter();
    }

    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
        mCountries.clear();
        mCountries.addAll(mCountriesCopy);
        refreshAdapter();
    }

    public void refreshAdapter() {
        if (mTailsAdapter != null) {
            mTailsAdapter.refreshAdapter(mCountries);
        }
    }

    @Override
    public void onKeyBackPress() {
        finishFragment();
        super.onKeyBackPress();
    }

}
