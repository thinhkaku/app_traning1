package aero.pilotlog.fragments;

import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.CountryAdapter;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;


public class CountryFragment extends BaseMCCFragment implements IAsyncTaskCallback {

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
    /*@Bind(R.id.ln_clear)
    LinearLayout mLnClear;*/
    @Bind(R.id.ln_tag_bar)
    LinearLayout lnTagBar;

    private List<ZCountry> mCountryList  = new ArrayList<>();
    private List<ZCountry> mCountryListCopy  = new ArrayList<>();
    private CountryAdapter mCountryAdapter;
    private boolean mIsSearch = true;
    private LoadDataTask mLoadDataTask;

    public CountryFragment(){

    }

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
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void closeFragment(){

        finishFragment();
    }


    private void initView() {
        mTvTitle.setText("Select Country");
        mIbMenu.setImageResource(R.drawable.ic_back);
        mEdtSearch.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        //KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
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
            default:
                break;
        }
    }

    @Nullable
    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition, long pLong) {
        AirfieldAddsFragment airfieldAddsFragment = (AirfieldAddsFragment) getFragment(AirfieldAddsFragment.class);
        if (airfieldAddsFragment != null) {
            airfieldAddsFragment.setCountry(mCountryList.get(pPosition));
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

    private void doSearch(String s) {
        String strSearch = s.trim();
        mCountryList.clear();
        if(strSearch.length()>0){
            mCountryList = DatabaseManager.getInstance(mActivity).searchCountry(strSearch);
        } else {
            mCountryList.addAll(mCountryListCopy);
        }
        refreshAdapter();
    }


    private void cancelSearch(EditText mEdtSearch) {
        mEdtSearch.setText("");
        mEdtSearch.clearFocus();
        mCountryList.clear();
        mCountryList.addAll(mCountryListCopy);
        refreshAdapter();
    }

    private void refreshAdapter() {
        if(mCountryAdapter != null){
            mCountryAdapter.refreshAdapter(mCountryList);
        }
    }

    @Override
    protected void onKeyBackPress() {
        closeFragment();
        super.onKeyBackPress();
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mCountryList = DatabaseManager.getInstance(mActivity).getAllZCountry();
    }

    @Override
    public void updateUI() {
        if (mCountryList != null && mCountryList.size() > 0 && mCountryList.get(0).getCountryCode() == 0) {
            mCountryList.remove(0);
        }

        mCountryListCopy.clear();
        if (mCountryList != null && !mCountryList.isEmpty()) {
            mCountryListCopy.addAll(mCountryList);
        } else {
            mCountryList = new ArrayList<>();
        }

        mCountryAdapter = new CountryAdapter(mActivity,mCountryList);
        mIndexableListView.setDrawRightBar(true);
        mIndexableListView.setAdapter(mCountryAdapter);
        mIndexableListView.setFastScrollEnabled(true);
    }

    @Override
    public void end() {

    }
}
