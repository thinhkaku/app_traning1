package aero.pilotlog.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.PilotListAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.IPilotCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * Created by ngoc.dh on 7/21/2015.
 * Pilot list
 */
public class PilotListFragment extends BaseMCCFragment implements IAsyncTaskCallback {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.indexableListView)
    IndexableListView mLvPilot;
    @Nullable
    @Bind(R.id.edtSearch)
    EditText mEdtSearch;
    @Nullable
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    @Nullable
    @Bind(R.id.tvTitle)
    TextView mHeaderTvTitle;
    @Bind(R.id.tvNumber)
    TextView mHeaderTvNumber;
    @Bind(R.id.btn_bottom1)
    Button mFooterBtnAdd;
    @Bind(R.id.btn_bottom2)
    Button mFooterBtnImportContact;
    @Bind(R.id.btn_bottom3)
    Button mFooterBtnSort;
    @Bind(R.id.btn_bottom4)
    Button mFooterBtnDelete;
    @Bind(R.id.btn_bottom5)
    Button mFooterBtnSelect;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
   /* @Bind(R.id.ln_clear)
    LinearLayout mLnClear;*/

    private static final int PICK_CONTACT = 0;
    private List<Pilot> mPilots;
    private List<Pilot> mPilotsCopy;
    //private List<FlightPilot> mFlightPilots;
    private int mViewType = MCCPilotLogConst.LIST_MODE;
    //For side index scroll
    private PilotListAdapter mPilotListAdapter;
    //private DatabaseManager mDatabaseManager;
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManagerV5;
    //For search
    private boolean mIsSearch;
    private boolean mIsFirstSort;
    //private String mCurrentSelectPilot = "1";
    private byte[] mCurrentSelectPilot = Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_EMPTY);
    private String SELECT_LIST_TYPE = MCCPilotLogConst.SELECT_LIST_TYPE_PILOT_INFO;

    /**
     * Constructor
     */
    public PilotListFragment() {

    }

    /**
     * hidden or visible icon menu
     *
     * @param pFlag boolean
     */
    public void setStateMenu(boolean pFlag) {
        mIbMenu.setVisibility(pFlag ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        //mDatabaseManager = DatabaseManager.getInstance(mActivity);
        mDatabaseManagerV5 = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity);
        initView();
        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    @Override
    protected int getContentResId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mViewType = bundle.getInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE);
        }

//        return R.layout.fragment_pilot_list;
        return R.layout.fragment_delay_tails;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_bottom_bar;
    }

    /**
     * Initialize views
     */
    private void initView() {
        mIsSearch = true;
        mPilotsCopy = new ArrayList<>();
        if (isTablet()) {
            mHeaderRlBack.setClickable(false);
        }
        /*Set footer view*/
        mFooterBtnAdd.setVisibility(View.VISIBLE);
        mFooterBtnImportContact.setVisibility(View.VISIBLE);
        //mFooterBtnSort.setText(R.string.sort_pilot_list_name);
        mFooterBtnSort.setVisibility(View.VISIBLE);
        mFooterBtnDelete.setVisibility(View.VISIBLE);
        mFooterBtnSelect.setVisibility(View.GONE);
        mFooterBtnImportContact.setBackgroundResource(R.drawable.btn_contact_import);

        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            if (mHeaderTvTitle != null) {
                mHeaderTvTitle.setText("Select Pilot");
            }
            mIbMenu.setImageResource(R.drawable.ic_back);
            mFooterBtnSort.setVisibility(View.GONE);
            mFooterBtnDelete.setVisibility(View.GONE);
            mFooterBtnImportContact.setVisibility(View.GONE);
            /*FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
            if (fragment != null) {
                mLnClear.setVisibility(View.VISIBLE);
            }*/
        } else {
            if (mHeaderTvTitle != null) {
                mHeaderTvTitle.setText(R.string.pilot_list_title);
            }
        }

        KeyboardUtils.hideKeyboardWhenClickLoupe(mEdtSearch, getActivity());
        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        //get data from database
        mPilots = mDatabaseManagerV5.getAllPilot();
        //For search
        if (mPilots != null && !mPilots.isEmpty()) {
            mPilotsCopy.addAll(mPilots);
        }
        //mFlightPilots = populateFlightPilot();
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        if (mPilots == null) {
            return;
        }
        //fill data on views
        setTextSizePilots();
        final int sortType = Integer.parseInt(mDatabaseManagerV5.getSettingLocal("SortPilot").getData());
        /*Handle swipe to delete*/
        mPilotListAdapter = new PilotListAdapter(mActivity, mPilots, sortType) {
            @Override
            public void deleteAction(final int position) {
                closeSwipeView(true);
                if (checkIfPilotSelf(position)) {
                    MccDialog.getOkAlertDialog(mActivity, R.string.delete_pilot_dialog_title, R.string.delete_pilot_self_denied).show();
                } else if (!mDatabaseManagerV5.isPilotUsedInFlight(mPilots.get(position).getPilotCode())) {
                    //PL-685
                    String pilotName = mPilots.get(position).getPilotName();
                    MccDialog.getOkDeleteAlertDialog(mActivity, R.string.delete_pilot_dialog_title
                            , String.format(getString(R.string.delete_single_pilot_confirm), pilotName), new MccDialog.MCCDialogCallBack() {
                                @Override
                                public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                    if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                        mDatabaseManagerV5.deletePilot(mPilots.get(position).getPilotCode());
                                        mPilots.remove(position);
                                        refreshPilotListAdapter();
                                        //refresh copy
                                        mPilotsCopy.clear();
                                        mPilotsCopy.addAll(mPilots);
                                        if (isTablet() && mPilots.get(position).getPilotCode() == mCurrentSelectPilot) {
                                            //if pilot was delete, being view info. Then info screen view info's next item in list.
                                            int newPosition = (position <= mPilots.size() - 1) ? position : 0;
                                            mCurrentSelectPilot = mPilots.get(newPosition).getPilotCode();
                                            mPilotListAdapter.removeAllItemSelected();
                                            mPilotListAdapter.setSelectItem(newPosition);
                                            mLvPilot.setSelection(newPosition);
                                            Bundle bundle = new Bundle();
                                            bundle.putByteArray(MCCPilotLogConst.PILOT_CODE_KEY, mCurrentSelectPilot);
                                            replaceFragment(R.id.rightContainerFragment, PilotInfoFragment.class, bundle, FLAG_NOT_ADD_STACK);
                                        }
                                        MccDialog.getOkAlertDialog(mActivity, R.string.delete_pilot_dialog_title, R.string.delete_pilot_done).show();
                                    }
                                }
                            }).show();
                } else {
                    String pilotName = mPilots.get(position).getPilotName();
                    MccDialog.getDeActiveAlertDialogMagenta(mActivity, R.string.delete_pilot_dialog_title,
                            String.format(getString(R.string.delete_pilot_denied), pilotName), new MccDialog.MCCDialogCallBack() {
                                @Override
                                public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                    if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                        mPilots.get(position).setActive(false);
                                        mDatabaseManagerV5.insertPilot(mPilots.get(position));
                                        mPilots.remove(position);
                                        refreshPilotListAdapter();

                                    }
                                }
                            }).show();
                }
            }

            @Override
            public void deActiveAction(final int position) {
                closeSwipeView(true);
                if (checkIfPilotSelf(position)) {
                    MccDialog.getOkAlertDialog(mActivity, R.string.de_active_pilot_dialog_title, R.string.de_active_pilot_self_denied).show();
                } else {
                    MccDialog.getDeActiveAlertDialog(mActivity, R.string.de_active_pilot_dialog_title
                            , String.format(getString(R.string.de_active_single_pilot_confirm),  mPilots.get(position).getPilotName()),
                            new MccDialog.MCCDialogCallBack() {
                                @Override
                                public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                    if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                        mPilots.get(position).setActive(false);
                                        mDatabaseManagerV5.insertPilot(mPilots.get(position));
                                        mPilots.remove(position);
                                        refreshPilotListAdapter();
                                        //refresh copy
                                        mPilotsCopy.clear();
                                        mPilotsCopy.addAll(mPilots);
                                    }
                                }
                            }).show();


                }
            }
        };

        mLvPilot.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // when scroll list view then close swipe layout menu if have
                closeSwipeView(true);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mLvPilot.setDrawRightBar(true);//Draw right bar contain characters of list view

        mPilotListAdapter.setSelectMode(mViewType == MCCPilotLogConst.SELECT_MODE);
        mPilotListAdapter.setListView(mLvPilot);
        /*Handle side index scroll*/
        mLvPilot.setAdapter(mPilotListAdapter);

        mLvPilot.setFastScrollEnabled(true);

        //sort settings
        mIsFirstSort = true;
        //int sortType = StorageUtils.getIntFromSharedPref(mActivity, MCCPilotLogConst.PILOT_SORT_KEY, MCCPilotLogConst.PILOT_SORT_TYPE_NAME);

        if (sortType == MCCPilotLogConst.PILOT_SORT_TYPE_NAME) {
            mFooterBtnSort.setText(R.string.sort_pilot_list_name);
            sortPilotList();
        } else if (sortType == MCCPilotLogConst.PILOT_SORT_TYPE_EMPID) {
            mFooterBtnSort.setText(R.string.sort_pilot_list_empId);
            sortPilotList();
        }
        //with tablet, auto call Fragment to show detail for first item pilot
        if (isTablet()) {
            // Change 02/25/2016
//            if (!mPilots.isEmpty() && mViewType == MCCPilotLogConst.LIST_MODE) {
//                mCurrentSelectPilot = mPilots.get(0).getPilotCode();
//                mPilotListAdapter.setSelectItem(0);
//                Bundle bundle = new Bundle();
//                bundle.putString(MCCPilotLogConst.PILOT_CODE_KEY, mCurrentSelectPilot);
//                callPilotInfo(bundle);
//                mHeaderRlBack.setClickable(false);
//            }

        /*Set footer view*/
            mFooterBtnAdd.setVisibility(View.VISIBLE);
            mFooterBtnImportContact.setVisibility(View.VISIBLE);
            mFooterBtnSort.setText(R.string.sort_pilot_list_name);
            mFooterBtnSort.setVisibility(View.VISIBLE);
            mFooterBtnDelete.setVisibility(View.VISIBLE);

            mFooterBtnSelect.setVisibility(View.GONE);
            mFooterBtnImportContact.setBackgroundResource(R.drawable.btn_contact_import);

//            if (isTablet()) {
//                if (mHeaderRlSearch != null) {
//                    mHeaderRlSearch.setVisibility(View.GONE);
//                }
//            }

            if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                mFooterBtnImportContact.setVisibility(View.GONE);
                mFooterBtnSort.setVisibility(View.GONE);
                mFooterBtnDelete.setVisibility(View.GONE);
            }
            KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
        }
    }

    /**
     * close swipe layout in case list view
     *
     * @param pSmooth smooth
     */
    private void closeSwipeView(boolean pSmooth) {
        if (mPilotListAdapter != null && mPilotListAdapter.getSwipeLayoutShown()) {
            mPilotListAdapter.closeSwipe(pSmooth);
        }
    }

    /**
     * Handle on pilot item click, go to pilot info page
     *
     * @param pAdapterViewParent parent
     * @param pView              view
     * @param position           position
     * @param id                 id
     */
    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterViewParent, View pView, final int position, long id) {
        if (pView instanceof SwipeLayout) {
            final SwipeLayout swipeLayout = (SwipeLayout) pView;
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open || !mPilotListAdapter.isEnableClick()) {
                return;
            }
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            SELECT_LIST_TYPE = bundle.getString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_PILOT_INFO);
        }
      /*  if (isTablet()) {

        } else {*/
            switch (SELECT_LIST_TYPE) {
                case MCCPilotLogConst.SELECT_LIST_TYPE_PILOT_INFO:
                    bundle = new Bundle();
                    bundle.putByteArray(MCCPilotLogConst.PILOT_CODE_KEY, mPilots.get(position).getPilotCode());
                    replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, PilotInfoFragment.class, bundle, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_ADD:
                    FlightAddsFragment flightAddsFragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
                    if (flightAddsFragment != null) {
                        int indicator = bundle.getInt(MCCPilotLogConst.SELECT_LIST_PILOT_INDICATOR, 1);
                        flightAddsFragment.setPilot(mPilots.get(position), indicator);
                        KeyboardUtils.hideKeyboard(mActivity);
                        finishFragment();
                    }
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_LOGBOOK_SEARCH:
                    final LogbooksListFragment logbooksListFragment = (LogbooksListFragment) getFragment(LogbooksListFragment.class);
                    if (logbooksListFragment != null) {
                        KeyboardUtils.hideKeyboard(mActivity);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                logbooksListFragment.setPilotFilter(mPilots.get(position).getPilotName());
                            }
                        });

                        finishFragment();
                    }
                    break;
            }
        //}

    }

    /**
     * process and show pilot info with tablet
     *
     * @param pBundle bundle
     */
    private void callPilotInfo(final Bundle pBundle) {
        replaceFragment(R.id.rightContainerFragment, PilotInfoFragment.class, pBundle, FLAG_NOT_ADD_STACK);
    }

    /**
     * Delete all pilot
     */


    /**
     * Search bar
     *
     * @param pText
     */
    @Nullable
    @OnTextChanged({R.id.edtSearch})
    public void onTextChanged(CharSequence pText) {
        if (mIsSearch && pText.length() != 0) {
            String strSearch = pText.toString().trim();
            mPilots.clear();
            if (strSearch.length() > 0) {
                mPilots = mDatabaseManagerV5.getAllPilots(strSearch);
            } else {
                mPilots.addAll(mPilotsCopy);
            }
        } else if (pText.length() == 0 && mPilots != null) {
            mPilots.clear();
            mPilots.addAll(mPilotsCopy);
        }
        refreshPilotListAdapter();
        mIsSearch = true;
    }

    /**
     * Handle bottom buttons
     *
     * @param pView
     */
    @Nullable
    @OnClick({R.id.btn_bottom1, R.id.btn_bottom2, R.id.btn_bottom3, R.id.btn_bottom4, R.id.btnCancel, R.id.ibMenu})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btn_bottom1: // add pilot
                if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                    Bundle bundleAdd = new Bundle();
                    bundleAdd.putInt(MCCPilotLogConst.PILOT_ADD_EDIT_VIEW_TYPE, MCCPilotLogConst.PILOT_ADD_VIEW);
                    bundleAdd.putBoolean(MCCPilotLogConst.IS_ADD_NEW_PILOT_FOR_FLIGHT, true);
                 /*   if (!isTablet()) {
                        replaceFragment(R.id.fragmentMain, PilotAddEditFragment.class, bundleAdd, FLAG_ADD_STACK);
                    } else {
                        replaceFragment(R.id.rightContainerFragment, PilotAddEditFragment.class, bundleAdd, FLAG_ADD_STACK);
                    }*/
                    replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, PilotAddEditFragment.class, bundleAdd, FLAG_ADD_STACK);
                    return;
                }
                Bundle bundleAdd = new Bundle();
                bundleAdd.putInt(MCCPilotLogConst.PILOT_ADD_EDIT_VIEW_TYPE, MCCPilotLogConst.PILOT_ADD_VIEW);
                PilotAddEditFragment pilotAddEditFragment = new PilotAddEditFragment();
                pilotAddEditFragment.setIPilotCallback(new IPilotCallback() {
                    @Override
                    public void selectPilot(Pilot pPilot) {
                        //add pilot to list
                        mPilots.add(pPilot);
                        refreshPilotListAdapter();
                        sortPilotList();
                        switchSortType();
                        mPilotsCopy.clear();
                        mPilotsCopy.addAll(mPilots);
                    }
                });
                //replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, PilotAddEditFragment.class, bundleAdd, FLAG_ADD_STACK);
                if (isTablet()) {
                    Fragment fragment = getRightFragment();
                    if (fragment == null || !(fragment instanceof PilotAddEditFragment)) {
                        //setStateMenu(FLAG_GONE_ICON_MENU);
                        replaceFragment(R.id.rightContainerFragment, pilotAddEditFragment, bundleAdd, FLAG_ADD_STACK);
                    } else {
                        ((PilotAddEditFragment) fragment).clearValue();
                        ((PilotAddEditFragment) fragment).initView(bundleAdd);
                    }
                } else {
                    replaceFragment(R.id.fragmentMain, pilotAddEditFragment, bundleAdd, FLAG_ADD_STACK);
                }

                break;
            case R.id.btn_bottom2: // Import contacts
                showContacts();
                break;
            case R.id.btn_bottom3: // Sort pilots
                if (mFooterBtnSort.getText().toString().equals(getString(R.string.sort_pilot_list_empId))) {
                    //StorageUtils.writeIntToSharedPref(mActivity, MCCPilotLogConst.PILOT_SORT_KEY, MCCPilotLogConst.PILOT_SORT_TYPE_NAME);
                    mDatabaseManagerV5.updateSettingLocal("SortPilot", String.valueOf(MCCPilotLogConst.PILOT_SORT_TYPE_NAME));
                    mPilotListAdapter.setSoftType(MCCPilotLogConst.PILOT_SORT_TYPE_NAME);
                } else {
                    //StorageUtils.writeIntToSharedPref(mActivity, MCCPilotLogConst.PILOT_SORT_KEY, MCCPilotLogConst.PILOT_SORT_TYPE_EMPID);
                    mDatabaseManagerV5.updateSettingLocal("SortPilot", String.valueOf(MCCPilotLogConst.PILOT_SORT_TYPE_EMPID));
                    mPilotListAdapter.setSoftType(MCCPilotLogConst.PILOT_SORT_TYPE_EMPID);
                }

                sortPilotList();
                break;
            case R.id.btn_bottom4: //Delete all pilots
                deleteAllPilot();
                break;
            case R.id.btnCancel: //Cancel search
                mIsSearch = false;
                if (mEdtSearch != null) {
                    mEdtSearch.setText(MCCPilotLogConst.STRING_EMPTY);
                    mEdtSearch.clearFocus();
                }
                mPilots.clear();
                mPilots.addAll(mPilotsCopy);
                refreshPilotListAdapter();
                break;
            case R.id.ibMenu: //Open/Close sliding menu
                if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                    finishFragment();
                } else {
                    toggleMenu();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Check if pilot is logged on any flight
     *
     * @param flightPilots list flight
     * @param position     position to be deleted
     * @return true if pilot is not logged on any flight, otherwise return false
     */
   /* private boolean isAbleToDeletePilot(List<FlightPilot> flightPilots, int position) {
        String pilotCode = mPilots.get(position).getPilotCode();
        FlightPilot flightPilot;
        for (int i = 0; i < flightPilots.size(); i++) {
            flightPilot = flightPilots.get(i);
            if (pilotCode.equals(flightPilot.getP1Code()) || pilotCode.equals(flightPilot.getP2Code()) || pilotCode.equals(flightPilot.getP3Code())
                    || pilotCode.equals(flightPilot.getP4Code())) {
                return false;
            }
        }
        return true;
    }*/


    /**
     * Check if pilot is self
     *
     * @param pPosition: item position
     * @return: if pilot is self
     */
    private boolean checkIfPilotSelf(int pPosition) {
        return (MCCPilotLogConst.PILOT_CODE_SELF.equals(Utils.getGUIDFromByteArray(mPilots.get(pPosition).getPilotCode())));
    }

    /**
     * Method to sort list when bottom bar button gets clicked
     */
    public void sortPilotList() {
        if (mPilots == null) {
            return;
        }
        //if (StorageUtils.getIntFromSharedPref(mActivity, MCCPilotLogConst.PILOT_SORT_KEY, MCCPilotLogConst.PILOT_SORT_TYPE_NAME) == MCCPilotLogConst.PILOT_SORT_TYPE_NAME) {
        if (Integer.parseInt(mDatabaseManagerV5.getSettingLocal("SortPilot").getData()) == MCCPilotLogConst.PILOT_SORT_TYPE_NAME) {
            Collections.sort(mPilots, new Comparator<Pilot>() {
                @Override
                public int compare(Pilot pilot1, Pilot pilot2) {
                    String strLeft = TextUtils.isEmpty(pilot1.getPilotName()) ? STRING_EMPTY : pilot1.getPilotName();
                    String strRight = TextUtils.isEmpty(pilot2.getPilotName()) ? STRING_EMPTY : pilot2.getPilotName();

                    return strLeft.compareToIgnoreCase(strRight);
                }
            });
            mFooterBtnSort.setText(R.string.sort_pilot_list_name);
            mPilotListAdapter.notifyDataSetChanged();
        } else {
            Collections.sort(mPilots, new Comparator<Pilot>() {
                @Override
                public int compare(Pilot pilot1, Pilot pilot2) {
                    if (TextUtils.isEmpty(pilot1.getPilotRef())) {
                        pilot1.setPilotRef(MCCPilotLogConst.STRING_EMPTY);
                    }
                    if (TextUtils.isEmpty(pilot2.getPilotRef())) {
                        pilot2.setPilotRef(MCCPilotLogConst.STRING_EMPTY);
                    }
                    return pilot1.getPilotRef().compareToIgnoreCase(pilot2.getPilotRef());
                }
            });
            mFooterBtnSort.setText(R.string.sort_pilot_list_empId);
            mPilotListAdapter.notifyDataSetChanged();
        }
        Fragment fragment = getRightFragment();
        if (isTablet() && !mIsFirstSort && fragment != null && !(fragment instanceof PilotInfoEmptyFragment)) {
            mPilotListAdapter.removeAllItemSelected();
            int newPosition = getCurrentIndexAfterSort();
            mPilotListAdapter.setSelectItem(newPosition);
            mLvPilot.setSelection(newPosition);
        }
        mIsFirstSort = false;

        mPilotsCopy.clear();
        mPilotsCopy.addAll(mPilots);
    }

    private int getCurrentIndexAfterSort() {
        if (mPilots != null) {
            for (int size = mPilots.size(), i = 0; i < size; i++) {
                if (mCurrentSelectPilot != null && mPilots.get(i).getPilotCode() != null && mCurrentSelectPilot.equals(mPilots.get(i).getPilotCode())) {
                    return i;
                }
            }
        }

        return 0;
    }

    /**
     * Switch sort type for list view
     * Called after sortPilotList ()
     */
    public void switchSortType() {
        if (mPilots == null) {
            return;
        }
        if (mFooterBtnSort.getText().toString().equals(getString(R.string.sort_pilot_list_empId))) {
            mFooterBtnSort.setText(R.string.sort_pilot_list_name);
        } else {
            mFooterBtnSort.setText(R.string.sort_pilot_list_empId);
        }
    }

    /**
     * Method to delete all pilot list when bottom bar button get clicked
     */
    private void deleteAllPilot() {
        if (mDatabaseManagerV5.getAllFlight().size() == 0) {
            MccDialog.getOkDeleteAllAlertDialog(mActivity, R.string.delete_all_pilot_dialog_title
                    , R.string.delete_all_pilots, new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                mDatabaseManagerV5.deleteAllPilots();
                                mPilots.clear();
                                mPilots = mDatabaseManagerV5.getAllPilot();
                                refreshPilotListAdapter();
                                mPilotsCopy.clear();
                                mPilotsCopy.addAll(mPilots);
                                if (isTablet()) {
                                    if (mPilots != null && !mPilots.isEmpty()) {
                                        //show info first item
                                        mCurrentSelectPilot = mPilots.get(0).getPilotCode();
                                        mPilotListAdapter.removeAllItemSelected();
                                        mPilotListAdapter.setSelectItem(0);
                                        mLvPilot.setSelection(0);
                                        Bundle bundle = new Bundle();
                                        bundle.putByteArray(MCCPilotLogConst.PILOT_CODE_KEY, mCurrentSelectPilot);
                                        //bundle.putString(MCCPilotLogConst.PILOT_NAME_KEY, mPilots.get(0).getPilotName());
                                        replaceFragment(R.id.rightContainerFragment, PilotInfoFragment.class, bundle, FLAG_NOT_ADD_STACK);
                                    } else {
                                        //show info empty
                                        mCurrentSelectPilot = Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_EMPTY);
                                        replaceFragment(R.id.rightContainerFragment, PilotInfoEmptyFragment.class, FLAG_NOT_ADD_STACK);
                                    }
                                }
                            }
                        }
                    }).show();
        } else {
            MccDialog.getOkAlertDialog(mActivity, R.string.delete_pilot_dialog_title
                    , R.string.delete_each_pilot).show();
        }
    }

    /**
     * Show contact view
     */
    private void showContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        this.startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                setContactInfo(resultCode, data);
                break;
        }
    }

    /**
     * Put data from contact list into bundle for Pilot Add view
     *
     * @param resultCode result code from onActivityResult
     * @param data       data from onActivityResult
     */
    public void setContactInfo(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            Uri contactData = data.getData();
            Cursor c = mActivity.managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
             /*   int mTot;
                int tot = mDatabaseManager.getNextPilotCode();
                if (tot != -1) {
                    mTot = tot;
                } else {
                    mTot = 1;
                }*/
                Bundle bundle = new Bundle();
                bundle.putInt(MCCPilotLogConst.PILOT_ADD_EDIT_VIEW_TYPE, MCCPilotLogConst.PILOT_IMPORT_VIEW);
                //String mPilotCode = String.format(MCCPilotLogConst.PILOT_CODE_FORMAT, mTot);
                //bundle.putString(MCCPilotLogConst.PILOT_CODE_KEY, mPilotCode);
                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                Cursor phoneCur = mActivity
                        .getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                                , new String[]{id}, null);
                if (phoneCur != null) {
                    while (phoneCur.moveToNext()) {
                        String mPilotName = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String mPilotPhone = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        /*bundle.putString(MCCPilotLogConst.PILOT_NAME_KEY, mPilotName);
                        bundle.putString(MCCPilotLogConst.PILOT_PHONE_KEY, mPilotPhone);*/
                    }
                }
                Cursor emailCur = mActivity.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                if (emailCur != null) {
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String mPilotEmail = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        bundle.putString(MCCPilotLogConst.PILOT_EMAIL_KEY, mPilotEmail);
                    }
                }
                        /*Set call back from add view*/
                PilotAddEditFragment pilotAddEditFragment = new PilotAddEditFragment();
                pilotAddEditFragment.setIPilotCallback(new IPilotCallback() {
                    @Override
                    public void selectPilot(Pilot pPilot) {
                        //add pilot to list
                        mPilots.add(pPilot);
//                        mPilotListAdapter.refreshPilotListAdapter(mPilots);
                        refreshPilotListAdapter();
                        sortPilotList();
                        switchSortType();
                        mPilotsCopy.clear();
                        mPilotsCopy.addAll(mPilots);
//                        mHeaderTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + mPilots.size());
                    }
                });
                if (isTablet()) {
                    replaceFragment(R.id.rightContainerFragment, PilotAddEditFragment.class, bundle, FLAG_ADD_STACK);
//                    android.support.v4.app.FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
//                    pilotAddEditFragment.setArguments(bundle);
//                    final String backStateName = pilotAddEditFragment.getClass().getName();
//                    ft.addToBackStack(backStateName);
//                    ft.replace(R.id.rightContainerFragment, pilotAddEditFragment, backStateName);
//                    ft.commit();
                } else {
                    replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, pilotAddEditFragment, bundle, FLAG_ADD_STACK);
                }
                assert phoneCur != null;
                phoneCur.close();
                assert emailCur != null;
                emailCur.close();
            }
        }
    }

    /**
     * Refresh pilot list
     */
    private void refreshPilotListAdapter() {
        if (mPilotListAdapter != null) {
            mPilotListAdapter.refreshAdapter(mPilots);
            setTextSizePilots();
        }
    }

    /**
     * set size of pilot
     */
    public void setTextSizePilots() {
        int size = 0;
        if (mPilots != null && !mPilots.isEmpty()) {
            size = mPilots.size();
        }
        mHeaderTvNumber.setText("" + size);
    }

    /**
     * Restore view state of button sort
     * onResume()
     *
     * @param savedInstanceState bundle
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (StorageUtils.getIntFromSharedPref(
                mActivity
                , MCCPilotLogConst.PILOT_SORT_KEY
                , MCCPilotLogConst.PILOT_SORT_TYPE_NAME) == MCCPilotLogConst.PILOT_SORT_TYPE_NAME) {
            mFooterBtnSort.setText(R.string.sort_pilot_list_name);
        } else {
            mFooterBtnSort.setText(R.string.sort_pilot_list_empId);
        }
    }

    @Override
    protected void onKeyBackPress() {
        if (!isTablet()) {
            super.onKeyBackPress();
        }
    }
}