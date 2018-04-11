package aero.pilotlog.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Expense;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ZCurrency;
import aero.pilotlog.databases.entities.ZExpense;
import aero.pilotlog.databases.entities.ZExpenseGroup;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.tasks.ImageProcessTask;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.PhotoUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseAddFragment extends BaseMCCFragment {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ibMenu)
    ImageView ivMenu;
    @Bind(R.id.tv_action_bar_center)
    TextView tvActionbarCenter;
    @Bind(R.id.item_expense_group)
    ItemInputTextWithIcon itemExpenseGroup;
    @Bind(R.id.item_expense_type)
    ItemInputTextWithIcon itemExpenseType;
    @Bind(R.id.item_expense_document)
    ItemInputTextWithIcon itemExpenseDocument;
    @Bind(R.id.tv_currency_type)
    TextView tvCurrencyType;
    @Bind(R.id.tv_original_amount_type)
    TextView tvOriginCurrency;
    @Bind(R.id.edt_original_amount)
    MccEditText edtOriginalAmount;
    @Bind(R.id.edt_my_amount)
    MccEditText edtMyAmount;
    @Bind(R.id.edtNotes)
    MccEditText edtNotes;
    @Bind(R.id.item_expense_date)
    ItemInputTextWithIcon mItemExpenseDate;
    @Bind(R.id.top_key)
    LinearLayout mTopKey;
    private boolean isChanged;
    private Calendar mDate = Calendar.getInstance();
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    private static final SimpleDateFormat DAY_OF_WEEK_FORMAT = new SimpleDateFormat("EEEE", Locale.US);

    public ZCurrency getOriginCurrency() {
        return mOriginCurrency;
    }

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public void setOriginCurrency(final ZCurrency mOriginCurrency) {

        this.mOriginCurrency = mOriginCurrency;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(tvOriginCurrency.getText().toString()) && !TextUtils.isEmpty(edtOriginalAmount.getText().toString())) {
                    if (mOriginCurrency != null && defaultCurrency != null) {
                        double myAmount = calculateCurrency(Utils.parseDouble(edtOriginalAmount.getText().toString()));
                        edtMyAmount.setText(decimalFormat.format(myAmount));
                    }
                }
            }
        });
    }

    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private ZCurrency mOriginCurrency;
    private ZCurrency defaultCurrency;
    private Expense mExpense;
    private ZExpenseGroup group;
    private String keyboardMemoryTemp1 = "";

    public ZExpense getzExpense() {
        return zExpense;
    }

    public void setTypeExpense(ZExpense zExpense) {
        this.zExpense = zExpense;
    }

    private ZExpense zExpense;

    public ZExpenseGroup getGroup() {
        return group;
    }

    public void setGroup(ZExpenseGroup group) {
        this.group = group;
    }


    private DatabaseManager mDatabaseManager;
    private Bundle mBundle;

    public ExpenseAddFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        initView();
        mBundle = getArguments();
        if (mBundle != null) {
            byte[] expCode = mBundle.getByteArray(MCCPilotLogConst.EXPENSE_CODE);
            if (expCode != null) {
                mExpense = mDatabaseManager.getExpenseByCode(expCode);
                setDataEdit();
            }
        }else {
            mItemExpenseDate.setDescription(DISPLAY_DATE_FORMAT.format(mDate.getTime()));
            mItemExpenseDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mDate.getTime()));
        }

    }

    private void setDataEdit() {
        if (mExpense != null) {
            try {
                if (mExpense.getExpDate() != null) {
                    mDate.setTime(DB_DATE_FORMAT.parse(mExpense.getExpDate()));
                }
            } catch (ParseException pex) {

            }
            mItemExpenseDate.setDescription(DISPLAY_DATE_FORMAT.format(mDate.getTime()));
            Calendar calToday = Calendar.getInstance();
            if (calToday.get(Calendar.YEAR) == mDate.get(Calendar.YEAR) &&
                    calToday.get(Calendar.MONTH) == mDate.get(Calendar.MONTH) &&
                    calToday.get(Calendar.DAY_OF_MONTH) == mDate.get(Calendar.DAY_OF_MONTH)) {
                mItemExpenseDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mDate.getTime()), R.color.grey_footer_text);
            } else {
                mItemExpenseDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mDate.getTime()), R.color.holo_red_light);
            }

            zExpense = mDatabaseManager.getZExpenseByCode(mExpense.getETCode());
            if (zExpense != null) {
                group = mDatabaseManager.getZExpenseGroupByCode(zExpense.getExpGroupCode());
            }

            if (mExpense.getCurrCode() != null) {
                defaultCurrency = mDatabaseManager.getZCurrencyByCode(mExpense.getCurrCode());
            }
            if (mExpense.getCurrCodeForeign() != null) {
                mOriginCurrency = mDatabaseManager.getZCurrencyByCode(mExpense.getCurrCodeForeign());
            }

            if (mExpense.getAmountForeign() != null && mExpense.getAmountForeign() > 0) {
                edtOriginalAmount.setText(decimalFormat.format((double) mExpense.getAmountForeign() / 100));
            }
            if (mExpense.getAmount() != null && mExpense.getAmount() > 0) {
                edtMyAmount.setText(decimalFormat.format((double) mExpense.getAmount() / 100));
            }
            if (!TextUtils.isEmpty(mExpense.getDescription())) {
                edtNotes.setText(mExpense.getDescription());
            }
        }
    }

    private void initView() {
        ivMenu.setVisibility(View.GONE);
        tvActionbarCenter.setVisibility(View.GONE);
        mTvTitle.setText("Add Expense");
        createListItems();
        initSetting();
        mItemExpenseDate.setIcon(R.drawable.ic_arrow);
        mItemExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog(mItemExpenseDate.getDescription(), mItemExpenseDate, mDate);
            }
        });
        createGroup();
        createType();
        createAmountType();
        createDocument();
        edtMyAmount.addTextChangedListener(textWatcher);
        edtOriginalAmount.addTextChangedListener(textWatcher);
        edtNotes.addTextChangedListener(textWatcher);
        edtOriginalAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!TextUtils.isEmpty(tvOriginCurrency.getText().toString()) && !TextUtils.isEmpty(edtOriginalAmount.getText().toString())) {
                         edtOriginalAmount.setText(decimalFormat.format(Utils.parseDouble(edtOriginalAmount.getText().toString())));
                        if (mOriginCurrency != null && defaultCurrency != null) {
                            double myAmount = calculateCurrency(Utils.parseDouble(edtOriginalAmount.getText().toString()));
                            edtMyAmount.setText(decimalFormat.format(myAmount));
                        }
                    }
                    hideTopKey();
                } else {
                    currentFocus = 0;
                    showTopKey();
                }
            }
        });
        edtMyAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    currentFocus = 1;
                    showTopKey();
                } else {
                    hideTopKey();
                }
            }
        });
        edtNotes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    currentFocus = 2;
                    showTopKey();
                } else {
                    hideTopKey();
                }
            }
        });
    }

    private double calculateCurrency(double originalAmount) {
        double originalCurrencyRate = mOriginCurrency.getConversionRate() / 100.0;
        double mccAmount = originalAmount / originalCurrencyRate;
        double defaultCurrencyRate = defaultCurrency.getConversionRate() / 100.0;
        double myAmount = mccAmount * defaultCurrencyRate;
        return /*Math.floor*/((double) Math.round(myAmount * 100) / 100);
    }

    private void initSetting() {
        SettingConfig settingDefaultCurrency = mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_DEFAULT_CURRENCY);
        if (settingDefaultCurrency != null) {
            String defaultCurrencyCode = settingDefaultCurrency.getData();
            if (!TextUtils.isEmpty(defaultCurrencyCode)) {
                mOriginCurrency = defaultCurrency = mDatabaseManager.getZCurrencyByCode(defaultCurrencyCode);
                if (defaultCurrency != null) {
                    tvCurrencyType.setText(defaultCurrency.getCurrShort());
                }

            }
        }
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_add_edit_flight_new;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_expense_add;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_add_pilot_footer;
    }


    private void createGroup() {
        itemExpenseGroup.setIcon(R.drawable.ic_arrow);
        itemExpenseGroup.setLayoutResId(R.id.fragmentMain);
        itemExpenseGroup.setParentFragment(this);
        itemExpenseGroup.setNavigationToFragment(ZExpenseGroupFragment.class);
    }

    private void createType() {
        itemExpenseType.setIcon(R.drawable.ic_arrow);
        itemExpenseType.setLayoutResId(R.id.fragmentMain);
        itemExpenseType.setParentFragment(this);

    }

    private void createAmountType() {
       /* itemExpenseAmountType.setVisibilityLineBottom(View.GONE);
        itemExpenseAmountType.setIcon(R.drawable.ic_arrow);
        itemExpenseAmountType.setLayoutResId(R.id.fragmentMain);
        itemExpenseAmountType.setParentFragment(this);
        itemExpenseAmountType.setNavigationToFragment(ZExpenseGroupFragment.class);*/
    }

    private void createDocument() {
        itemExpenseDocument.setVisibilityLineBottom(View.GONE);
        itemExpenseDocument.setIcon(R.drawable.icon_delete_cyan);
        //itemExpenseDocument.setDescription("bank_receipt.pdf");
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (group != null) {
            if (!group.getExpGroupShort().equals(itemExpenseGroup.getDescription()) && !TextUtils.isEmpty(itemExpenseGroup.getDescription())) {
                zExpense = null;
            }
            itemExpenseGroup.setDescription(group.getExpGroupShort());
            itemExpenseGroup.setFootNote(group.getExpGroupLong());
            itemExpenseType.setNavigationToFragment(ZExpenseFragment.class);
            itemExpenseType.setItemId(group.getExpGroupCode());
        }
        if (zExpense != null) {
            itemExpenseType.setDescription(zExpense.getExpTypeShort());
            itemExpenseType.setFootNote(zExpense.getExpTypeLong());
            if (zExpense.getDebit() != null && zExpense.getDebit()) {
                itemExpenseType.setTvFootNote2("DEBIT ( - )", R.color.color_red);
            } else {
                itemExpenseType.setTvFootNote2("CREDIT ( + )", R.color.mcc_green_credit);
            }
        } else {
            itemExpenseType.clearText();
        }
        if (mOriginCurrency != null) {
            tvOriginCurrency.setText(mOriginCurrency.getCurrShort());
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private String imageFileName() {
//        return String.format(isSyncV4 ? MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4 : MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V3, mPilotCode);
        Date selectedDate = new Date(mItemExpenseDate.getDescription());
        if (selectedDate == null) {
            selectedDate = new Date();
        }
        long longDate = selectedDate.getTime();
        return String.format(MCCPilotLogConst.EXPENSE_IMAGE_FILE_NAME, String.valueOf(longDate));
    }

    @Nullable
    @OnClick({R.id.tv_action_bar_left, R.id.tv_action_bar_right, R.id.btn_cancel, R.id.btn_next, R.id.btn_done, R.id.ivNotes,
            R.id.ln_original_amount_type, R.id.ln_original_amount_type_button, R.id.iv_find_file, R.id.iv_camera})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.tv_action_bar_left:
                onKeyBackPress();
                break;
            case R.id.iv_find_file:
                openFolder();
                break;
            case R.id.iv_camera:
                try {
                    new PhotoUtils(ExpenseAddFragment.this).takePhotoFromCameraForFragment(mActivity, imageFileName());
                } catch (ActivityNotFoundException anfe) {
                    anfe.printStackTrace();
                    Utils.showToast(mActivity, R.string.toast_camera_not_found);
                }
                break;
            case R.id.ln_original_amount_type:
            case R.id.ln_original_amount_type_button:
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, ZCurrencyFragment.class, null, FLAG_ADD_STACK);
                break;
            case R.id.tv_action_bar_right:
                saveExpense();
                break;
            case R.id.btn_cancel:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.CANCEL);
                break;
            case R.id.btn_next:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.NEXT);
                break;
            case R.id.btn_done:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.DONE);
                break;
            case R.id.ivNotes:
                edtNotes.setText("");
                break;
        }
    }

    private List<MccEditText> listMccEditText;

    private void createListItems() {
        listMccEditText = new ArrayList<>();
        listMccEditText.add(edtOriginalAmount);
        listMccEditText.add(edtMyAmount);
        listMccEditText.add(edtNotes);
    }

    boolean isShowTopKey;

    private void showTopKey() {
        if (!isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (!isTablet()) {
                mTopKey.setVisibility(View.VISIBLE);
                //mBottomBar.setVisibility(View.GONE);

            } else {
                mTopKey.setVisibility(View.VISIBLE);
                //mBottomBar.setVisibility(View.GONE);
                /*Fragment fragment = getLeftFragment();
                if (fragment instanceof LogbookListFragment) {
                    ((LogbookListFragment) fragment).hideBottomBar();
                }*/
            }
            isShowTopKey = true;
        }
    }

    private void hideTopKey() {
        if (isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            if (!isTablet()) {
                mTopKey.setVisibility(View.GONE);
                // mBottomBar.setVisibility(View.VISIBLE);
            } else {
                mTopKey.setVisibility(View.GONE);
                //  mBottomBar.setVisibility(View.VISIBLE);
                /*Fragment fragment = getLeftFragment();
                if (fragment instanceof LogbookListFragment) {
                    ((LogbookListFragment) fragment).showBottomBar();
                }*/
            }
            isShowTopKey = false;
        }
    }

    int currentFocus;

    private void nextPrevFocus(MccEnum.topKeyboardCustomInput input) {
        View v = mActivity.getCurrentFocus();
        final MccEditText edt;
        if (v instanceof MccEditText) {
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
                currentFocus++;
                if (listMccEditText.size() > currentFocus) {
                    MccEditText edtNext = listMccEditText.get(currentFocus);
                    if (edtNext.getVisibility() == View.VISIBLE && edtNext.isEnabled()) {
                        edtNext.requestFocus();
                    } else {
                        nextPrevFocus(MccEnum.topKeyboardCustomInput.NEXT);
                    }
                } else {
                    clearFocus();
                    hideTopKey();
                    KeyboardUtils.hideSoftKeyboard(mActivity);
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        clearFocus();
        hideTopKey();
    }

    private void clearFocus() {
        View v = mActivity.getCurrentFocus();
        if (v != null && v instanceof MccEditText) {
            v.clearFocus();
        }
    }

    int PICK_REQUEST_CODE = 0;

    public void openFolder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/myFolder/");
        //intent.setDataAndType(uri, "text/csv");
        //String[] mimetypes = {"image/*", "application/pdf|application/x-excel"};
        //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        //intent.setType("image/*|application/pdf|application/x-excel");
        intent.setType("image/*,application/x-excel|application/pdf");
        //intent.setTypeAndNormalize("image/*|application/x-excel|application/pdf");
        //intent.setType(Intent.normalizeMimeType("image/*|application/x-excel|application/pdf"));
        startActivityForResult(Intent.createChooser(intent, "Open folder"), PICK_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String type = data.getType();
                //LogHelper.i(TAG,"Pick completed: "+ uri + " "+type);
                if (uri != null) {
                    String path = null;
                    try {
                        path = URLDecoder.decode(uri.toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (path != null && path.toLowerCase().startsWith("file://")) {
                        // Selected file/directory path is below
                        //path = (new File(URI.create(path))).getAbsolutePath();
                        itemExpenseDocument.setDescription(path.substring(path.lastIndexOf("/") + 1));
                    }

                }
            }
            //else LogHelper.i(TAG,"Back from pick with cancel status");
        } else if (requestCode == PhotoUtils.TAKE_IMAGE && resultCode == Activity.RESULT_OK) {
            final File f = new File(StorageUtils.getStorageRootFolder(mActivity), imageFileName());
            if (!f.exists()) {
                return;
            }
            ImageProcessTask mTask = new ImageProcessTask(mActivity, imageFileName(), false) {
                @Override
                public void handleBitmap(Bitmap bitmap) {
                    //changeToPortraitLayout(f);
                    //mIvPilotImage.setImageBitmap(bitmap);
                    //mHasImage = true;
                    itemExpenseDocument.setDescription(imageFileName());
                }
            };
            mTask.execute(f);
            PhotoUtils.saveTakenImageToGallery(mActivity, Uri.fromFile(f));

            //mPilot.setSyncPict(SyncConst.SYNC_PICT_YES);
            /*mPilot.setPC(SyncConst.PHONE);
            DatabaseManager.getInstance(mActivity).updatePilot(mPilot);*/
        }
        System.gc();
    }

    private void saveExpense() {
        if (TextUtils.isEmpty(itemExpenseType.getDescription())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.invalid_expense_type_title, R.string.invalid_expense_type_content).show();
            return;
        }
        if (TextUtils.isEmpty(edtMyAmount.getText().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.invalid_my_currency, R.string.invalid_my_currency_content).show();
            return;
        }
        Expense expense = new Expense();
        byte[] expenseCode;
        if (mExpense != null && mExpense.getExpCode() != null) {
            expenseCode = mExpense.getExpCode();
        } else {
            expenseCode = Utils.getByteArrayFromGUID(Utils.generateStringGUID());
        }
        expense.setExpCode(expenseCode);
        expense.setETCode(zExpense.getETCode());
        Date selectedDate = new Date(mItemExpenseDate.getDescription());
        if (selectedDate != null) {
            expense.setExpDate(DB_DATE_FORMAT.format(selectedDate));
        }else {
            Date todayDate = new Date();
            expense.setExpDate(DB_DATE_FORMAT.format(todayDate));
        }
        expense.setDescription(edtNotes.getText().toString());
        expense.setAmount((long) (Utils.parseDouble(edtMyAmount.getText().toString()) * 100));
        expense.setCurrCode(defaultCurrency.getCurrCode());
        if (!TextUtils.isEmpty(edtOriginalAmount.getText().toString()) && !TextUtils.isEmpty(tvOriginCurrency.getText())) {
            expense.setAmountForeign((long) (Utils.parseDouble(edtOriginalAmount.getText().toString()) * 100));
            expense.setCurrCodeForeign(getOriginCurrency().getCurrCode());
        }
        expense.setRecord_Upload(true);
        expense.setRecord_Modified(DateTimeUtils.getCurrentUTCUnixTimeStamp());
        mDatabaseManager.insertExpense(expense);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ExpenseListFragment expenseListFragment = (ExpenseListFragment) getFragment(ExpenseListFragment.class);
                if (expenseListFragment != null) {
                    expenseListFragment.refreshList();
                }
                finishFragment();

            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            if (!isChanged)
                isChanged = true;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

    public void onKeyBackPress() {
        if ((isChanged || isSelectValueChanged())&&isVisible()) {
            MccDialog.getOkCancelAlertDialog(mActivity, mExpense == null ? R.string.expense_add_title : R.string.expense_edit_title
                    , R.string.cancel_message_content
                    , new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                KeyboardUtils.hideSoftKeyboard(mActivity);
                                finishFragment();
                            }
                        }
                    }).show();
        } else {
            KeyboardUtils.hideSoftKeyboard(mActivity);
            finishFragment();
        }
    }

    private boolean isSelectValueChanged() {
        return itemExpenseGroup.isValueChanged ||
                itemExpenseType.isValueChanged;
    }

    public void displayDateDialog(String currentDateString, final ItemInputTextWithIcon itemDate, final Calendar flightDate) {
        Date currentDate = null;
        try {
            currentDate = DISPLAY_DATE_FORMAT.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar cal = dateToCalendar(currentDate);
        cal.set(Calendar.HOUR, 0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(mActivity, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                //Calendar mFlightDate = Calendar.getInstance();
                flightDate.set(Calendar.MONTH, monthOfYear);
                flightDate.set(Calendar.YEAR, year);
                flightDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                itemDate.setDescription(DISPLAY_DATE_FORMAT.format(flightDate.getTime()));
                Calendar calToday = Calendar.getInstance();
                if (calToday.get(Calendar.YEAR) == flightDate.get(Calendar.YEAR) &&
                        calToday.get(Calendar.MONTH) == flightDate.get(Calendar.MONTH) &&
                        calToday.get(Calendar.DAY_OF_MONTH) == flightDate.get(Calendar.DAY_OF_MONTH)) {
                    itemDate.setFootNote(DAY_OF_WEEK_FORMAT.format(flightDate.getTime()), R.color.grey_footer_text);
                } else {
                    itemDate.setFootNote(DAY_OF_WEEK_FORMAT.format(flightDate.getTime()), R.color.holo_red_light);
                }
            }
        }, year, month, day);
        datePicker.setCancelable(false);
        datePicker.show();
    }
    private Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

}
