package aero.pilotlog.fragments;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IPilotCallback;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Created by ngoc.dh on 7/28/2015.
 * Add / edit pilot
 */
public class PilotAddEditFragment extends BaseMCCFragment implements MccEditText.EditTextImeBackListener, View.OnClickListener {

    @Bind(R.id.tv_action_bar_center)
    TextView tvActionbarCenter;
    @Bind(R.id.lineDraft)
    View lineDraft;
    @Bind(R.id.top_key)
    LinearLayout mTopKey;
    @Bind(R.id.edtCompany)
    MccEditText mEditCompany;
    @Bind(R.id.edtEmplId)
    MccEditText mEditEmplId;
    @Bind(R.id.edtPilotName)
    MccEditText mEditPilotName;
    @Bind(R.id.edtCellPhone)
    MccEditText mEditCellPhone;
    @Bind(R.id.edtEmail)
    MccEditText mEditEmail;
    @Bind(R.id.edtNotes)
    MccEditText mEditNotes;
    @Bind(R.id.edtCertificate)
    MccEditText mEditCertificate;
    @Bind(R.id.edtRoster)
    MccEditText mEditRoster;
    @Bind(R.id.edtFacebook)
    MccEditText mEditFacebook;
    @Bind(R.id.edtLinkedIn)
    MccEditText mEditLinkedIn;

    @Bind(R.id.ibMenu)
    ImageButton mHeaderIbMenu;

    @Bind(R.id.tv_title)
    TextView mHeaderTvTitle;

    private static final int INVALID_EMP_ID = 1;
    private static final int INVALID_PILOT_NAME = 2;
    private static final int INVALID_EMAIL = 3;
    private static final int INVALID_EMP_ID_LENGTH = 4;
    private static final int EMP_ID_MAX_LENGTH = 10;
    private DatabaseManager mDbManager;
    private byte[] mPilotCode;
    private Pilot mPilot;
    private int mViewType;
    private IPilotCallback mIPilotCallback;
    private boolean mIsFromFlight;

    boolean isShowTopKey = false;
    private HashMap<MccEditText, Integer> editTextTagMap;

    /**
     * Set call back
     *
     * @param pIPilotCallback callback
     */
    public void setIPilotCallback(IPilotCallback pIPilotCallback) {
        this.mIPilotCallback = pIPilotCallback;
    }


    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        //Get pilot from bundle
        Bundle bundleEdit = getArguments();
        initView(bundleEdit);
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_pilot_add_edit;
    }

    @Override
    protected int getHeaderResId() {
//        return R.layout.layout_add_edit_action_bar;
        return R.layout.layout_add_edit_flight_new;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_add_pilot_footer;
        //return FLAG_NO_RESOURCE;
    }

    /**
     * Initialize view
     *
     * @param pBundle bundle
     */
    public void initView(Bundle pBundle) {
        editTextTagMap = initEditTextTag();
        mDbManager = DatabaseManager.getInstance(mActivity);
        mEditPilotName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        if (pBundle != null) {
            mPilotCode = pBundle.getByteArray(MCCPilotLogConst.PILOT_CODE_KEY);
            mViewType = pBundle.getInt(MCCPilotLogConst.PILOT_ADD_EDIT_VIEW_TYPE);
            mIsFromFlight = pBundle.getBoolean(MCCPilotLogConst.IS_ADD_NEW_PILOT_FOR_FLIGHT, false);
        }
        if (mPilotCode != null) {
            mPilot = mDbManager.getPilotByCode(mPilotCode);
        }
        //Set filters for edit text
        InputFilter filterEmpId = createInputFilter();
        InputFilter filterNotes = createQuotationMarkFilter();
        mEditNotes.setFilters(new InputFilter[]{filterNotes});

        switch (mViewType) {
            /*Set add view*/
            case MCCPilotLogConst.PILOT_ADD_VIEW:
                mHeaderTvTitle.setText(R.string.pilot_add_title);
                //mEditCompany.setText(mDbManager.getSetting(SettingsConst.COMPANY_PREFIX).getData());
                mEditEmplId.setFilters(new InputFilter[]{filterEmpId, new InputFilter.LengthFilter(EMP_ID_MAX_LENGTH)});

                SettingConfig settingConfig = mDbManager.getSetting("AirlineOperator");
                if (settingConfig != null) {
                    String company = settingConfig.getData();
                    if (company != null)
                        mEditCompany.setText(company);
                }
                break;

            /*Set edit view*/
            case MCCPilotLogConst.PILOT_EDIT_VIEW:
                mHeaderTvTitle.setText(R.string.pilot_edit_title);
                if (Utils.getGUIDFromByteArray(mPilot.getPilotCode()).equalsIgnoreCase(MCCPilotLogConst.PILOT_CODE_SELF)) {
                    mEditPilotName.setEnabled(false);
                } else {
                    mEditEmplId.setFilters(new InputFilter[]{filterEmpId, new InputFilter.LengthFilter(EMP_ID_MAX_LENGTH)});
                }
                if (!TextUtils.isEmpty(mPilot.getCompany())) {
                    mEditCompany.setText(mPilot.getCompany());
                }
                if (!TextUtils.isEmpty(mPilot.getPilotRef())) {
                    mEditEmplId.setText(mPilot.getPilotRef());
                }
                if (!TextUtils.isEmpty(mPilot.getPilotName())) {
                    mEditPilotName.setText(mPilot.getPilotName());
                }
                if (!TextUtils.isEmpty(mPilot.getPilotPhone())) {
                    mEditCellPhone.setText(mPilot.getPilotPhone());
                }
                if (!TextUtils.isEmpty(mPilot.getPilotEMail())) {
                    mEditEmail.setText(mPilot.getPilotEMail());
                }
                if (!TextUtils.isEmpty(mPilot.getNotes())) {
                    mEditNotes.setText(mPilot.getNotes());
                }
                if (!TextUtils.isEmpty(mPilot.getCertificate())) {
                    mEditCertificate.setText(mPilot.getCertificate());
                }
                if (!TextUtils.isEmpty(mPilot.getRosterAlias())) {
                    mEditRoster.setText(mPilot.getRosterAlias());
                }
                if (!TextUtils.isEmpty(mPilot.getFacebook())) {
                    mEditFacebook.setText(mPilot.getFacebook());
                }
                if (!TextUtils.isEmpty(mPilot.getLinkedIn())) {
                    mEditLinkedIn.setText(mPilot.getLinkedIn());
                }
                break;

            /*Set import from contact view*/
            case MCCPilotLogConst.PILOT_IMPORT_VIEW:
                mHeaderTvTitle.setText(R.string.pilot_add_title);
                //mEditCompany.setText(mDbManager.getSetting(SettingsConst.COMPANY_PREFIX).getData());
                mEditEmplId.setFilters(new InputFilter[]{filterEmpId, new InputFilter.LengthFilter(EMP_ID_MAX_LENGTH)});
                if (mPilot != null) {
                    if (!TextUtils.isEmpty(mPilot.getPilotName())) {
                        mEditPilotName.setText(mPilot.getPilotName());
                    }
                    if (!TextUtils.isEmpty(mPilot.getPilotPhone())) {
                        mEditCellPhone.setText(mPilot.getPilotPhone());
                    }
                    if (!TextUtils.isEmpty(mPilot.getPilotEMail())) {
                        mEditEmail.setText(mPilot.getPilotEMail());
                    }
                }

                break;
            default:
                break;
        }
        mHeaderIbMenu.setVisibility(View.GONE);
//        mTvCancel.setVisibility(View.VISIBLE);
        mEditCellPhone.setOnEditTextImeBackListener(this);
        mEditCompany.setOnEditTextImeBackListener(this);
        mEditEmail.setOnEditTextImeBackListener(this);
        mEditEmplId.setOnEditTextImeBackListener(this);
        mEditNotes.setOnEditTextImeBackListener(this);
        mEditPilotName.setOnEditTextImeBackListener(this);

        mEditCellPhone.setSelectAllOnFocus(true);
        mEditCompany.setSelectAllOnFocus(true);
        mEditEmail.setSelectAllOnFocus(true);
        mEditEmplId.setSelectAllOnFocus(true);
        mEditNotes.setSelectAllOnFocus(true);
        mEditPilotName.setSelectAllOnFocus(true);
        tvActionbarCenter.setVisibility(View.GONE);
        lineDraft.setVisibility(View.GONE);
    }

    public void clearValue() {
        mEditCompany.setText(STRING_EMPTY);
        mEditEmplId.setText(STRING_EMPTY);
        mEditPilotName.setText(STRING_EMPTY);
        mEditCellPhone.setText(STRING_EMPTY);
        mEditEmail.setText(STRING_EMPTY);
        mEditNotes.setText(STRING_EMPTY);
        mEditCertificate.setText(STRING_EMPTY);
        mEditRoster.setText(STRING_EMPTY);
        mEditPilotName.setEnabled(true);
    }

    @OnTextChanged(value = R.id.edtNotes, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onAfterTextChanged(Editable pCharSequence) {
        String text = pCharSequence.toString();
        int length = text.length();
        if (text.contains("\'") || text.contains("\"")) {
            pCharSequence.delete(length - 1, length);
        }
    }

    /**
     * Handle button Back and button Save
     *
     * @param pView view
     */
    @OnClick({R.id.btn_cancel, R.id.btn_next, R.id.btn_done, R.id.ivCellPhone, R.id.ivEmail, R.id.ivNotes,
            R.id.ivFacebook, R.id.ivLinkedIn, R.id.ivRoster, R.id.ivCertificate,
            R.id.tv_action_bar_left, R.id.tv_action_bar_right, R.id.ibMenu, R.id.ivName})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btn_cancel:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.CANCEL);
                break;
            case R.id.btn_next:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.NEXT);
                break;
            case R.id.btn_done:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.DONE);
                break;
            case R.id.tv_action_bar_left:
                navigationFrom();
                //PL-112 keyboard logic add new
                ClearAllFocusable();
                hideTopKey();
                //KeyboardUtils.hideSoftKeyboard(mActivity);
                //End PL-112
                break;

            case R.id.tv_action_bar_right:
                //PL-112 keyboard logic add new
                View v = mActivity.getCurrentFocus();
                if (v != null && v instanceof MccEditText) {
                    MccEditText mccEditText = (MccEditText) v;
                    if ("".equals(mccEditText.getText().toString())) {
                        if (!"".equals(keyboardMemoryTemp1)) {
                            mccEditText.setText(keyboardMemoryTemp1);
                        }
                    }
                    mccEditText.clearFocus();
                }

                handleSaveButton();
                //ClearAllFocusable();
                //KeyboardUtils.hideSoftKeyboard(mActivity);
                hideTopKey();
                //End PL-112
                break;
            case R.id.ivCellPhone:
                mEditCellPhone.getText().clear();
                break;
            case R.id.ivEmail:
                mEditEmail.getText().clear();
                break;
            case R.id.ivNotes:
                mEditNotes.getText().clear();
                break;
            case R.id.ivFacebook:
                mEditFacebook.getText().clear();
                ;
                break;
            case R.id.ivLinkedIn:
                mEditLinkedIn.getText().clear();
                break;
            case R.id.ivRoster:
                mEditRoster.getText().clear();
                break;
            case R.id.ivCertificate:
                mEditCertificate.getText().clear();
                break;
            case R.id.ivName:
                if (!TextUtils.isEmpty(mEditPilotName.getText().toString()))
                    changePilotNameCase(mEditPilotName.getText().toString());
                //mEditNotes.getText().clear();
                break;
            case R.id.ibMenu:
                toggleMenu();
                break;
            default:
                break;
        }
    }

    int currentPilotNameCount = 0;

    private void changePilotNameCase(String name) {
        if (currentPilotNameCount >= 5) currentPilotNameCount = 0;
        currentPilotNameCount++;
        name = name.toLowerCase();
        String[] names = name.split(" ");
        switch (currentPilotNameCount) {
            case 1:
                mEditPilotName.setText(toTitleCase(name));
                break;
            case 2:
                mEditPilotName.setText(name.toUpperCase());
                break;
            case 3:
                mEditPilotName.setText(name.toLowerCase());
                break;
            case 4:
                String result = "";
                result += names[0].toUpperCase();
                if (names.length > 1) {
                    for (int i = 1; i < names.length; i++) {
                        result += " " + toTitleCase(names[i]);
                    }
                }
                mEditPilotName.setText(result);
                break;
            case 5:
                String result2 = "";
                result2 += toTitleCase(names[0]);
                if (names.length > 1) {
                    for (int i = 1; i < names.length; i++) {
                        result2 += " " + names[i].toUpperCase();
                    }
                }
                mEditPilotName.setText(result2);
                break;
            default:
                mEditPilotName.setText(toTitleCase(name));
                break;
        }
    }

    private String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public void navigationFrom() {
    /*Back from edit view*/
        // PL-226 add title in message box
        if (mViewType == MCCPilotLogConst.PILOT_EDIT_VIEW) {
            if (checkEdtChanges()) {
                MccDialog.getOkCancelAlertDialog(mActivity, R.string.pilot_edit_title
                        , R.string.cancel_message_content
                        , new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    callFinishFragment();
                                }
                            }
                        }).show();
            } else {
                callFinishFragment();
            }
        } else { /*Back from add view*/
            if (checkAddChanges()) {
                MccDialog.getOkCancelAlertDialog(mActivity, R.string.pilot_add_title
                        , R.string.cancel_message_content
                        , new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    callFinishFragment();
                                }
                            }
                        }).show();
            } else {
                callFinishFragment();
            }
        }
        return;
        //end PL-226
    }

    private void ClearAllFocusable() {
        MccEditText[] listMccEdt1 = new MccEditText[]{mEditCompany, mEditEmplId, mEditPilotName, mEditCellPhone, mEditEmail, mEditNotes};
        for (int i = 0; i < listMccEdt1.length; i++) {
            listMccEdt1[i].clearFocus();
        }
    }

    /**
     * finish fragment
     */
    private void callFinishFragment() {
        finishFragment();
        if (isTablet()) {
            setVisibleIconMenu();
        }
    }

    /**
     * from screen add or edit pilot, tap on cancel or save then visible icon menu at screen list pilot
     */
    private void setVisibleIconMenu() {
        Fragment fragment = getLeftFragment();
        if (fragment != null && fragment instanceof PilotListFragment) {
            ((PilotListFragment) fragment).setStateMenu(FLAG_VISIBLE_ICON_MENU);
        }
    }

    /**
     * Create filter: Emp.ID accepts only 0-9, A-Z
     *
     * @return input filter
     */
    private InputFilter createInputFilter() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return MCCPilotLogConst.STRING_EMPTY;
                    }
                }
                return null;
            }
        };
    }

    /**
     * Create filter: Notes does not accept quotation marks
     *
     * @return input filter
     */
    private InputFilter createQuotationMarkFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                char[] unacceptedChar = {'\'', '\"'};
                for (int i = start; i < end; i++) {
                    if (new String(unacceptedChar).contains(String.valueOf(source.charAt(i)))) {
                        return MCCPilotLogConst.STRING_EMPTY;
                    }
                }
                return null;
            }
        };
    }

    /**
     * Validate edit text content
     *
     * @return int
     */
    private int setValidation() {
        /*Check emp.ID*/
        String strPilotRef = mEditEmplId.getText().toString().trim();
        if (!TextUtils.isEmpty(strPilotRef) && mViewType == MCCPilotLogConst.PILOT_ADD_VIEW) {
            if (mDbManager.isPilotExist(strPilotRef)) {
                return INVALID_EMP_ID;
            }
        }
        //JIRA PL-760 step 1/2
         /*Check emp.ID lenght*/
        if (!TextUtils.isEmpty(strPilotRef) && strPilotRef.length() == 1) { //JIRA PL-836
            return INVALID_EMP_ID_LENGTH;
        }
        //End JIRA PL-760 step 1/2
        /*Check pilot name*/
        if (mEditPilotName.getText().toString().trim().length() < 3) {
            return INVALID_PILOT_NAME;
        }
        /*Check email*/
        if (!isValidEmail(mEditEmail.getText().toString().trim())) {
            return INVALID_EMAIL;
        }

        return 0;
    }

    /**
     * Check if email is valid
     *
     * @param target target
     * @return boolean
     */
    private boolean isValidEmail(CharSequence target) {
        return target.length() == 0 || android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Handle the Save button
     */
    private void handleSaveButton() {
        int mIsValidated = setValidation();
        switch (mIsValidated) {
            case INVALID_EMP_ID: //Invalid emp.ID
                MccDialog.getOkAlertDialog(mActivity, getResources().getString(R.string.add_pilot_title)
                        , getResources().getString(R.string.error_used_empId)).show();
                break;
            //JIRA PL-760 step 2/2
            case INVALID_EMP_ID_LENGTH:
                MccDialog.getOkAlertDialog(mActivity, getResources().getString(R.string.add_pilot_title)
                        , getResources().getString(R.string.error_short_empId)).show();
                break;
            //End JIRA PL-760 step 2/2
            case INVALID_PILOT_NAME: //Invalid pilot name
                MccDialog.getOkAlertDialog(mActivity, getResources().getString(R.string.add_pilot_title)
                        , getResources().getString(R.string.error_short_pilot_name)).show();
                break;
            case INVALID_EMAIL: //Invalid email
                MccDialog.getOkAlertDialog(mActivity, getResources().getString(R.string.add_pilot_title)
                        , getResources().getString(R.string.error_invalid_email)).show();
                break;
            default:
                boolean isSaved = savePilot();
                if (isSaved) {
                    if (mIPilotCallback != null) {
                        mIPilotCallback.selectPilot(mPilot);
                    }
                    //if (!isTablet()) {
                        FlightAddsFragment flightAddsFragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
                        if (flightAddsFragment != null && mIsFromFlight) {
                            int indicator = getArguments().getInt(MCCPilotLogConst.SELECT_LIST_PILOT_INDICATOR, 1);
                            flightAddsFragment.setPilot(mPilot, indicator);
                            finishFragment();
                            finishFragment();
                        } else {
                            finishFragment();
                        }

                   // }
                    /*else {
                        Bundle bundle = new Bundle();
                        bundle.putByteArray(MCCPilotLogConst.PILOT_CODE_KEY, mPilot.getPilotCode());
                        replaceFragment(R.id.fragmentMain, PilotListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                        finishFragment();
                        break;
                    }*/
                }
        }
        View v = mActivity.getCurrentFocus();
        if (v != null) {
            v.clearFocus();
        }
    }

    private boolean savePilot() {
        byte[] pilotCode;
        if (mPilot != null)
            pilotCode = mPilot.getPilotCode();
        else
            pilotCode = Utils.getByteArrayFromGUID(Utils.generateStringGUID());
        String pilotCompany = mEditCompany.getText().toString();
        String pilotRef = mEditEmplId.getText().toString();
        String pilotName = mEditPilotName.getText().toString();
        String pilotPhone = mEditCellPhone.getText().toString();
        String pilotEmail = mEditEmail.getText().toString();
        String pilotNotes = mEditNotes.getText().toString();
        String pilotCertificate = mEditCertificate.getText().toString();
        String pilotRoster = mEditRoster.getText().toString();
        String facebook = mEditFacebook.getText().toString();
        String linkedIn = mEditLinkedIn.getText().toString();
        mPilot = new Pilot();
        mPilot.setPilotCode(pilotCode);
        mPilot.setActive(true);
        mPilot.setCompany(pilotCompany);
        mPilot.setPilotRef(pilotRef);
        mPilot.setPilotName(pilotName);
        mPilot.setPilotPhone(pilotPhone);
        mPilot.setPilotEMail(pilotEmail);
        mPilot.setNotes(pilotNotes);
        mPilot.setCertificate(pilotCertificate);
        mPilot.setRosterAlias(pilotRoster);
        mPilot.setFacebook(facebook);
        mPilot.setLinkedIn(linkedIn);
        mPilot.setRecord_Modified(DateTimeUtils.getCurrentUTCUnixTimeStamp());
        mPilot.setRecord_Upload(true);
        return mDbManager.insertPilot(mPilot);
    }

    /**
     * Check changes in Pilot Edit
     *
     * @return boolean
     */
    private boolean checkEdtChanges() {
        return !((mEditCompany.getText().toString().equals(TextUtils.isEmpty(mPilot.getCompany())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getCompany()))
                && mEditEmplId.getText().toString().equals(TextUtils.isEmpty(mPilot.getPilotRef())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getPilotRef())
                && mEditPilotName.getText().toString().equals(TextUtils.isEmpty(mPilot.getPilotName())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getPilotName())
                && mEditEmail.getText().toString().equals(TextUtils.isEmpty(mPilot.getPilotEMail())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getPilotEMail())
                && mEditCellPhone.getText().toString().equals(TextUtils.isEmpty(mPilot.getPilotPhone())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getPilotPhone())
                && mEditCertificate.getText().toString().equals(TextUtils.isEmpty(mPilot.getCertificate())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getCertificate())
                && mEditRoster.getText().toString().equals(TextUtils.isEmpty(mPilot.getRosterAlias())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getRosterAlias())
                && mEditNotes.getText().toString().equals(TextUtils.isEmpty(mPilot.getNotes())
                ? MCCPilotLogConst.STRING_EMPTY
                : mPilot.getNotes()));
    }

    /**
     * Check changes in Pilot Add
     *
     * @return boolean
     */
    private boolean checkAddChanges() { //Check changes for add view
        SettingConfig settingConfig = mDbManager.getSetting("AirlineOperator");
        String company = "";
        if (settingConfig != null) {
            company = settingConfig.getData();
        }
        return !(mEditCompany.getText().toString().equals(company)
                && mEditEmplId.getText().toString().equals(MCCPilotLogConst.STRING_EMPTY)
                && mEditPilotName.getText().toString().equals(MCCPilotLogConst.STRING_EMPTY)
                && mEditEmail.getText().toString().equals(MCCPilotLogConst.STRING_EMPTY)
                && mEditCellPhone.getText().toString().equals(MCCPilotLogConst.STRING_EMPTY)
                && mEditCertificate.getText().toString().equals(MCCPilotLogConst.STRING_EMPTY)
                && mEditRoster.getText().toString().equals(MCCPilotLogConst.STRING_EMPTY)
                && mEditNotes.getText().toString().equals(MCCPilotLogConst.STRING_EMPTY));
    }

    /**
     * Export new pilot to contact
     *
     * @return true if export succeed, otherwise return false
     */
    private boolean exportAddToContact() {
        if (mPilot == null) {
            return false;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (!TextUtils.isEmpty(mPilot.getPilotName())) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            mPilot.getPilotName()).build());
        }

        //------------------------------------------------------ Work Numbers
        if (!TextUtils.isEmpty(mPilot.getPilotPhone())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mPilot.getPilotPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (!TextUtils.isEmpty(mPilot.getPilotEMail())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, mPilot.getPilotEMail())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!TextUtils.isEmpty(mPilot.getCompany())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, mPilot.getCompany())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, MCCPilotLogConst.STRING_EMPTY)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            mActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onHideKeyboard(MccEditText pEditText) {
        View v = mActivity.getCurrentFocus();
        if (v != null) {
            v.clearFocus();
        }
    }

    private HashMap<MccEditText, Integer> initEditTextTag() {
        HashMap<MccEditText, Integer> mapEditText = new HashMap<>();
        mapEditText.put(mEditCompany, 1);
        mapEditText.put(mEditEmplId, 2);
        mapEditText.put(mEditPilotName, 3);
        mapEditText.put(mEditCellPhone, 4);
        mapEditText.put(mEditEmail, 5);
        mapEditText.put(mEditFacebook, 6);
        mapEditText.put(mEditLinkedIn, 7);
        mapEditText.put(mEditCertificate, 8);
        mapEditText.put(mEditNotes, 9);
        mapEditText.put(mEditRoster, 10);
        setActionDone(mEditCompany);
        setActionDone(mEditEmplId);
        setActionDone(mEditPilotName);
        setActionDone(mEditCellPhone);
        setActionDone(mEditEmail);
        setActionDone(mEditFacebook);
        setActionDone(mEditLinkedIn);
        setActionDone(mEditCertificate);
        setActionDone(mEditNotes);
        setActionDone(mEditRoster);
        mEditCompany.setSelectAllOnFocus(true);
        mEditEmplId.setSelectAllOnFocus(true);
        mEditPilotName.setSelectAllOnFocus(true);
        mEditCellPhone.setSelectAllOnFocus(true);
        mEditEmail.setSelectAllOnFocus(true);
        mEditFacebook.setSelectAllOnFocus(true);
        mEditLinkedIn.setSelectAllOnFocus(true);
        mEditCertificate.setSelectAllOnFocus(true);
        mEditNotes.setSelectAllOnFocus(true);
        mEditRoster.setSelectAllOnFocus(true);
        return mapEditText;
    }

    private void setActionDone(final MccEditText edt) {
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    nextPrevFocus(MccEnum.topKeyboardCustomInput.DONE);
                }
                return false;
            }
        });

    }


    private void showTopKey() {
        if (!isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (!isTablet()) {
                mTopKey.setVisibility(View.VISIBLE);
            } else {
                mTopKey.setVisibility(View.VISIBLE);
                Fragment fragment = getLeftFragment();
                if (fragment instanceof PilotListFragment) {
                    ((PilotListFragment) fragment).mFooterContainer.setVisibility(View.GONE);
                }
            }
            isShowTopKey = true;
        }

    }

    private void hideTopKey() {
        if (isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            if (!isTablet()) {
                mTopKey.setVisibility(View.GONE);
            } else {
                mTopKey.setVisibility(View.GONE);
                Fragment fragment = getLeftFragment();
                if (fragment instanceof PilotListFragment) {
                    ((PilotListFragment) fragment).mFooterContainer.setVisibility(View.VISIBLE);
                }
            }
            isShowTopKey = false;
        }
    }

    String keyboardMemoryTemp1 = "";

    private void nextPrevFocus(MccEnum.topKeyboardCustomInput input) {
        View v = mActivity.getCurrentFocus();
        MccEditText edt;
        int entryPoint = 0;
        if (v instanceof MccEditText) {
            entryPoint = editTextTagMap.get(v);
            edt = (MccEditText) v;
        } else {
            return;
        }
        switch (input) {
            case CANCEL:
                final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                edt.setText(keyboardMemoryTemp1);
                edt.clearFocus();
                hideTopKey();
                break;
            case DONE:
                final InputMethodManager imm1 = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                edt.clearFocus();
                hideTopKey();
                break;
            case NEXT:
                entryPoint++;

                MccEditText edtNext = getKeyByValue(editTextTagMap, entryPoint);

                if (edtNext != null && edtNext.getVisibility() != View.GONE) {
                    edtNext.requestFocus();
                } else {
                    edt.clearFocus();
                    hideTopKey();
                    KeyboardUtils.hideSoftKeyboard(mActivity);
                }
                break;
        }
    }

    private MccEditText getKeyByValue(HashMap<MccEditText, Integer> map, int value) {
        for (Map.Entry<MccEditText, Integer> entry : map.entrySet()) {
            if (value == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }


    @OnFocusChange({
            R.id.edtCompany, R.id.edtEmplId, R.id.edtPilotName,
            R.id.edtCellPhone, R.id.edtEmail, R.id.edtNotes,
            R.id.edtCertificate, R.id.edtRoster, R.id.edtFacebook, R.id.edtLinkedIn
    })
    void onFocusChange(View v) {
        switch (v.getId()) {
            case R.id.edtCompany:
            case R.id.edtEmplId:
            case R.id.edtPilotName:
            case R.id.edtCellPhone:
            case R.id.edtEmail:
            case R.id.edtNotes:
            case R.id.edtCertificate:
            case R.id.edtRoster:
            case R.id.edtFacebook:
            case R.id.edtLinkedIn:
                keyboardMemoryTemp1 = ((MccEditText) v).getText().toString();
                showTopKey();
                break;
        }
        View v2 = mActivity.getCurrentFocus();
        if (v2 == null || !(v2 instanceof MccEditText)) {
            hideTopKey();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        KeyboardUtils.hideSoftKeyboard(mActivity);
        hideTopKey();
    }

    private void clearFocus() {
        View v = mActivity.getCurrentFocus();
        if (v != null && v instanceof MccEditText) {
            v.clearFocus();
        }
    }
}