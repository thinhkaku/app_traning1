package aero.pilotlog.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;

import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.KeyboardUtils;


import aero.pilotlog.utilities.LocationUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.ItemSwitch;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by phuc.dd on 27-03-17.
 */

public class AirfieldAddsFragment extends BaseMCCFragment {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ibMenu)
    ImageView ivMenu;
    @Bind(R.id.tv_action_bar_center)
    TextView tvActionbarCenter;
    @Bind(R.id.lineDraft)
    View lineDraft;

    @Bind(R.id.edt_item_airfield_icao)
    MccEditText edtItemAirfieldIcao;
    @Bind(R.id.edt_item_airfield_iata)
    MccEditText edtItemAirfieldIata;
    @Bind(R.id.edt_item_airfield_airfield_name)
    MccEditText edtItemAirfieldAirfieldName;
    @Bind(R.id.edt_item_airfield_elevation)
    MccEditText edtItemAirfieldElevation;

    @Bind(R.id.item_airfield_country)
    ItemInputTextWithIcon itemAirfieldCountry;
    @Bind(R.id.item_airfield_timezone)
    ItemInputTextWithIcon itemAirfieldTimeZone;
    @Bind(R.id.item_airfield_category)
    ItemInputTextWithIcon itemAirfieldCategory;

    @Bind(R.id.edt_item_airfield_latitude)
    MccEditText edtItemAirfieldLatitude;
    @Bind(R.id.edt_item_airfield_longitude)
    MccEditText edtItemAirfieldLongitude;
    @Bind(R.id.tv_item_airfield_latitude)
    TextView tvItemAirfieldLatitude;
    @Bind(R.id.tv_item_airfield_longitude)
    TextView tvItemAirfieldLongitude;

    @Bind(R.id.edt_item_airfield_nearbycity)
    MccEditText edtItemAirfieldNearbyCity;
    @Bind(R.id.edt_item_airfield_notes)
    MccEditText edtItemAirfieldNotes;
    @Bind(R.id.edt_item_airfield_mynotes)
    MccEditText edtItemAirfieldMyNotes;

    @Bind(R.id.item_airfield_favorite)
    ItemSwitch itemAirfieldFavorite;
    @Bind(R.id.iv_global)
    ImageView ivGlobal;

    @Bind(R.id.top_key)
    LinearLayout mTopKey;
    boolean isShowTopKey = false;
    private HashMap<MccEditText, Integer> editTextTagMap;
    private boolean isChanged;


    private Airfield mAirfield = null;
    private DatabaseManager databaseManager;

    private ZTimeZone mTimeZone;
    private ZCountry mCountry;

    public void setTimeZone(ZTimeZone timeZone) {
        this.mTimeZone = timeZone;
    }

    public void setCountry(ZCountry country) {
        this.mCountry = country;
    }

    public AirfieldAddsFragment() {
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        databaseManager = DatabaseManager.getInstance(mActivity);
        initView();
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_add_edit_flight_new;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfields_add;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_add_pilot_footer;
    }

    private void initView() {
        checkLocation();
        editTextTagMap = initEditTextTag();
        Bundle bundle = getArguments();
        byte[] mAirfieldCode = null;
        if (bundle != null) {
            mAirfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
        }
        if (mAirfieldCode != null) {
            mAirfield = databaseManager.getAirfieldByCode(mAirfieldCode);
        }
        if (mAirfield == null)
            mTvTitle.setText(R.string.add_airfield);
        else
            mTvTitle.setText(R.string.edit_airfield);

        ivMenu.setVisibility(View.GONE);
        tvActionbarCenter.setVisibility(View.GONE);
        lineDraft.setVisibility(View.GONE);

        createTextFields();
        createSwitchFields();
        createCountry();
        createTimezone();
        createCategory();

        edtItemAirfieldIcao.addTextChangedListener(textWatcher);
        edtItemAirfieldIata.addTextChangedListener(textWatcher);
        edtItemAirfieldAirfieldName.addTextChangedListener(textWatcher);
        edtItemAirfieldElevation.addTextChangedListener(textWatcher);
        edtItemAirfieldNearbyCity.addTextChangedListener(textWatcher);
        edtItemAirfieldMyNotes.addTextChangedListener(textWatcher);
    }

    private void createSwitchFields() {
        itemAirfieldFavorite.getSwitch().setTextAppearance(mActivity, R.style.ItemSettingFormatFootNode);
        if (mAirfield != null) {
            itemAirfieldFavorite.getSwitch().setChecked(mAirfield.getShowList());
            itemAirfieldFavorite.getSwitch().setText(mAirfield.getShowList() ? R.string.add_airfield_message_favorite_show
                    : R.string.add_airfield_message_favorite_notshow);
        } else {
            itemAirfieldFavorite.getSwitch().setChecked(true);
            itemAirfieldFavorite.getSwitch().setText(R.string.add_airfield_message_favorite_show);
        }
        itemAirfieldFavorite.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChanged) isChanged = true;
                if (isChecked) {
                    itemAirfieldFavorite.getSwitch().setText(R.string.add_airfield_message_favorite_show);
                } else {
                    itemAirfieldFavorite.getSwitch().setText(R.string.add_airfield_message_favorite_notshow);
                }
            }
        });
    }


    private void createTextFields() {
        edtItemAirfieldLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isChanged)
                    isChanged = true;
                if (!TextUtils.isEmpty(edtItemAirfieldLatitude.getText().toString()) && edtItemAirfieldLatitude.getText().toString().length() > 2)
                    tvItemAirfieldLatitude.setText(LocationUtils.getLatitudeString(edtItemAirfieldLatitude.getText().toString()));
                else
                    tvItemAirfieldLatitude.setText("");
            }
        });
        edtItemAirfieldLongitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isChanged)
                    isChanged = true;
                if (!TextUtils.isEmpty(edtItemAirfieldLongitude.getText().toString()) && edtItemAirfieldLongitude.getText().toString().length() > 2)
                    tvItemAirfieldLongitude.setText(LocationUtils.getLongitudeString(edtItemAirfieldLongitude.getText().toString()));
                else
                    tvItemAirfieldLongitude.setText("");
            }
        });
        if (mAirfield != null) {
            if (!TextUtils.isEmpty(mAirfield.getAFICAO())) {
                edtItemAirfieldIcao.setText(mAirfield.getAFICAO());
            }
            if (!TextUtils.isEmpty(mAirfield.getAFIATA())) {
                edtItemAirfieldIata.setText(mAirfield.getAFIATA());
            }
            if (!TextUtils.isEmpty(mAirfield.getAFName())) {
                edtItemAirfieldAirfieldName.setText(mAirfield.getAFName());
            }
            edtItemAirfieldLatitude.setText(mAirfield.getLatitude() == -200000 ? "" : String.valueOf(mAirfield.getLatitude()));
            edtItemAirfieldLongitude.setText(mAirfield.getLongitude() == -200000 ? "" : String.valueOf(mAirfield.getLongitude()));
            edtItemAirfieldElevation.setText(mAirfield.getElevationFT() == -99 ? "" : mAirfield.getElevationFT().toString());
            if (!TextUtils.isEmpty(mAirfield.getCity())) {
                edtItemAirfieldNearbyCity.setText(mAirfield.getCity());
            }
            if (!TextUtils.isEmpty(mAirfield.getNotes())) {
                edtItemAirfieldNotes.setText(mAirfield.getNotes());
            }
            if (!TextUtils.isEmpty((mAirfield.getNotesUser()))) {
                edtItemAirfieldMyNotes.setText(mAirfield.getNotesUser());
            }
        }


    }

    private void createCategory() {
        itemAirfieldCategory.setIcon(R.drawable.ic_arrow);
        ArrayList<CharSequence> categories = new ArrayList<>();
        categories.add("Drone Site");
        categories.add("Air Force Base, Navy Coast Guard");
        categories.add("Ballooning site, Glider - STOL - ULM strip");
        categories.add("Water Landing Strip , Hidroport");
        categories.add("Heliport, Medical Center");
        categories.add("Small Landing Strip");
        categories.add("Small Airfield");
        categories.add("Medium Aerodrome");
        categories.add("Major International Airport");
        categories.add("Train Station");
        categories.add("Metropolitan Area");

        if (mAirfield != null) {
            itemAirfieldCategory.setListStringDialog(categories, mAirfield.getAFCat() - 1);
            itemAirfieldCategory.setItemId(mAirfield.getAFCat() - 1);
        } else
            itemAirfieldCategory.setListStringDialog(categories, -1);
    }

    private void createTimezone() {
        itemAirfieldTimeZone.setIcon(R.drawable.ic_arrow);
        if (mAirfield != null) {
            ZTimeZone zTimeZone = databaseManager.getZTimezone(mAirfield.getTZCode());
            if (zTimeZone != null) {
                itemAirfieldTimeZone.setFootNote(zTimeZone.getZoneShort());
                itemAirfieldTimeZone.setDescription(zTimeZone.getTimeZone());
                mTimeZone = zTimeZone;
            }
        }
        itemAirfieldTimeZone.setLayoutResId(R.id.fragmentMain);
        itemAirfieldTimeZone.setParentFragment(this);
        itemAirfieldTimeZone.setNavigationToFragment(TimezoneFragment.class);
    }

    private void createCountry() {
        itemAirfieldCountry.setIcon(R.drawable.ic_arrow);
        if (mAirfield != null) {
            ZCountry zCountry = databaseManager.getZCountry(mAirfield.getAFCountry());
            if (zCountry != null) {
                itemAirfieldCountry.setDescription(zCountry.getCountryName());
                //itemAirfieldCountry.setFootNote(zCountry.getCapital());
                mCountry = zCountry;
            }
        }
        itemAirfieldCountry.setVisibleFootNote(View.GONE);
        itemAirfieldCountry.setLayoutResId(R.id.fragmentMain);
        itemAirfieldCountry.setParentFragment(this);
        itemAirfieldCountry.setNavigationToFragment(CountryFragment.class);
    }

    @Nullable
    @OnClick({R.id.tv_action_bar_left, R.id.tv_action_bar_right, R.id.ivLat, R.id.ivLong, R.id.ivMynotes, R.id.ivNearbycity,
            R.id.tv_item_airfield_longitude, R.id.tv_item_airfield_latitude, R.id.btn_cancel, R.id.btn_next, R.id.btn_done, R.id.iv_global, R.id.ln_help})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.tv_action_bar_left:
                KeyboardUtils.hideSoftKeyboard(mActivity);
                onKeyBackPress();
                break;
            case R.id.tv_action_bar_right:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSaved = saveAirfield();
                        if (isSaved) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    KeyboardUtils.hideSoftKeyboard(mActivity);
                                    AirfieldsListFragment airfieldsListFragment = (AirfieldsListFragment) getFragment(AirfieldsListFragment.class);
                                    if (airfieldsListFragment != null) {
                                        airfieldsListFragment.refreshAdapter();
                                    }
                                    AirfieldsInfoFragment airfieldsInfoFragment = (AirfieldsInfoFragment) getFragment(AirfieldsInfoFragment.class);
                                    if(airfieldsInfoFragment!=null){
                                        airfieldsInfoFragment.initDataAirfield();
                                    }
                                    finishFragment();
                                }
                            });
                        }
                    }
                });

                break;
            case R.id.ivLat:
                edtItemAirfieldLatitude.setText("");
                break;
            case R.id.ivLong:
                edtItemAirfieldLongitude.setText("");
                break;
            case R.id.ivMynotes:
                edtItemAirfieldMyNotes.setText("");
                break;
            case R.id.ivNearbycity:
                edtItemAirfieldNearbyCity.setText("");
                break;
            case R.id.tv_item_airfield_latitude:
                edtItemAirfieldLatitude.requestFocus();
                KeyboardUtils.showKeyboard(edtItemAirfieldLatitude);
                break;
            case R.id.tv_item_airfield_longitude:
                edtItemAirfieldLongitude.requestFocus();
                KeyboardUtils.showKeyboard(edtItemAirfieldLongitude);
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
            case R.id.ln_help:
                MccDialog.getInformationAlertDialog(mActivity, getString(R.string.airfield_coordinates), getString(R.string.airfield_coordinates_message)).show();
                break;
        }
    }


    private boolean saveAirfield() {
        if (!validateInput())
            return false;
        byte[] afCode = null;
        if (mAirfield != null) {
            afCode = mAirfield.getAFCode();
        } else {
            afCode = Utils.getByteArrayFromGUID(Utils.generateStringGUID());
        }
        Airfield airfield = new Airfield();
        airfield.setAFCode(afCode);
        airfield.setAFName(edtItemAirfieldAirfieldName.getText().toString()
                .replace(Character.toString((char) 34), Character.toString((char) 180))
                .replace(Character.toString((char) 39), Character.toString((char) 180)));
        airfield.setAFICAO(edtItemAirfieldIcao.getText().toString());
        airfield.setAFIATA(edtItemAirfieldIata.getText().toString());
        airfield.setLatitude(!TextUtils.isEmpty(edtItemAirfieldLatitude.getText().toString()) ?
                Integer.parseInt(edtItemAirfieldLatitude.getText().toString()) : -200000);
        airfield.setLongitude(!TextUtils.isEmpty(edtItemAirfieldLongitude.getText().toString()) ?
                Integer.parseInt(edtItemAirfieldLongitude.getText().toString()) : -200000);
        airfield.setAFCountry(mCountry.getCountryCode());
        airfield.setTZCode(mTimeZone.getTZCode());
        airfield.setAFCat(itemAirfieldCategory.getItemId() + 1);
        airfield.setElevationFT(!TextUtils.isEmpty(edtItemAirfieldElevation.getText().toString()) ?
                Integer.parseInt(edtItemAirfieldElevation.getText().toString()) : -99);
        airfield.setCity(edtItemAirfieldNearbyCity.getText().toString());
        airfield.setNotesUser(edtItemAirfieldMyNotes.getText().toString()
                .replace(Character.toString((char) 34), Character.toString((char) 180))
                .replace(Character.toString((char) 39), Character.toString((char) 180)));
        airfield.setNotes(edtItemAirfieldNotes.getText().toString()
                .replace(Character.toString((char) 34), Character.toString((char) 180))
                .replace(Character.toString((char) 39), Character.toString((char) 180)));
        airfield.setUserEdit(true);
        airfield.setShowList(itemAirfieldFavorite.getChecked());
        long dateModify = DateTimeUtils.getCurrentUTCUnixTimeStamp();
        airfield.setRecord_Modified(dateModify);
        airfield.setRecord_Upload(true);
        return databaseManager.insertOrUpdateAirfield(airfield);
    }

    private boolean showOkAlertDialog(final int title, final int message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getOkAlertDialog(mActivity, title, message).show();
            }
        });
        return false;
    }

    private boolean showOkAlertDialog(final int title, final String message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getOkAlertDialog(mActivity, title, message).show();
            }
        });
        return false;
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(itemAirfieldCountry.getDescription())) {
            return showOkAlertDialog( R.string.add_airfield_name_country, R.string.add_airfield_message_country);
        }
        if (TextUtils.isEmpty(itemAirfieldCategory.getDescription())) {
            return showOkAlertDialog( R.string.add_airfield_name_category, R.string.add_airfield_message_category);
        }
        if (TextUtils.isEmpty(edtItemAirfieldAirfieldName.getText().toString())
                || edtItemAirfieldAirfieldName.getText().toString().trim().length() < 5) {
            return showOkAlertDialog( R.string.add_airfield_name, R.string.add_airfield_name_message);

        }
        if (TextUtils.isEmpty(edtItemAirfieldIcao.getText().toString())
                || edtItemAirfieldIcao.getText().toString().trim().length() != 4) {
            String iCaoTemple = "";
            if (!TextUtils.isEmpty(edtItemAirfieldAirfieldName.getText().toString()) && edtItemAirfieldAirfieldName.getText().toString().length() >= 5) {
                iCaoTemple = "Q" + edtItemAirfieldAirfieldName.getText().toString().replace(" ", "").substring(0, 3).toUpperCase();
            } else {
                iCaoTemple = "QABC";
            }
            return showOkAlertDialog( R.string.add_airfield_icao, String.format(getString(R.string.add_airfield_icao_message), iCaoTemple));
        }
        if (TextUtils.isEmpty(edtItemAirfieldIata.getText().toString()) ||
                edtItemAirfieldIata.getText().toString().length() != 3) {
            if (TextUtils.isEmpty(edtItemAirfieldIata.getText().toString()) &&
                    !"ZZZZ".equals(edtItemAirfieldIcao.getText().toString())) {
                //search db
                Airfield existAirfield = getExistAirfieldByICaoIAta(edtItemAirfieldIcao.getText().toString(), edtItemAirfieldIata.getText().toString());
                if (existAirfield != null) {
                    return  showOkAlertDialog(mAirfield != null ? R.string.edit_airfield : R.string.add_airfield,
                            String.format(getResources().getString(R.string.add_airfield_already_used_message), existAirfield.getAFName(), existAirfield.getAFICAO(), ""));
                }
            } else {
                if ("ZZZZ".equals(edtItemAirfieldIcao.getText().toString()))
                    return showOkAlertDialog(R.string.add_airfield_iata, R.string.add_airfield_iata_message);
                else
                    return showOkAlertDialog(R.string.add_airfield_iata, R.string.add_airfield_iata_message_2);
            }
        } else {
            //search db
            Airfield existAirfield = getExistAirfieldByICaoIAta(edtItemAirfieldIcao.getText().toString(), edtItemAirfieldIata.getText().toString());
            if (existAirfield != null) {
                return showOkAlertDialog( mAirfield != null ? R.string.add_airfield : R.string.edit_airfield,
                        String.format(getResources().getString(R.string.add_airfield_already_used_message), existAirfield.getAFName(), existAirfield.getAFICAO(), " / " + existAirfield.getAFIATA()));
            }
        }

        if (mTimeZone == null) {
            return showOkAlertDialog( R.string.invalid_time_zone, R.string.invalid_time_zone_message);
        }

        if (TextUtils.isEmpty(edtItemAirfieldLatitude.getText().toString()) &&
                TextUtils.isEmpty(edtItemAirfieldLongitude.getText().toString())) {
            if (!showOkCancelDialog(R.string.coordinates_both_empty, R.string.coordinates_both_empty_message))
                return false;

        } else if (TextUtils.isEmpty(edtItemAirfieldLatitude.getText().toString())) {
            return showOkAlertDialog( R.string.add_airfield_coordinates, R.string.add_airfield_coordinates_incomplete);
        } else if (TextUtils.isEmpty(edtItemAirfieldLongitude.getText().toString())) {
            return showOkAlertDialog( R.string.add_airfield_coordinates, R.string.add_airfield_coordinates_incomplete);
        } else {
            if (!checkLatitude(edtItemAirfieldLatitude.getText().toString())) {
                return showOkAlertDialog( R.string.add_airfield_coordinates, R.string.add_airfield_latitude_invalid_message);
            }
            if (!checkLongitude(edtItemAirfieldLongitude.getText().toString())) {
                return showOkAlertDialog( R.string.add_airfield_coordinates, R.string.add_airfield_longitude_invalid_message);
            }
        }

        return true;
    }

    boolean resultSaveDialog;

    private boolean showOkCancelDialog(final int title, final int message) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getOkCancelAlertDialog(mActivity, title, message, new MccDialog.MCCDialogCallBack() {
                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        countDownLatch.countDown();
                        if (pDialogType == DialogInterface.BUTTON_NEGATIVE) {
                            resultSaveDialog = false;
                        } else {
                            resultSaveDialog = true;
                        }
                    }
                }).show();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }
        return resultSaveDialog;
    }

    private boolean checkLatitude(String inputLatitude) {
        if (inputLatitude.length() < 3) return false;
        int lat = Integer.parseInt(inputLatitude);
        if (lat > 90000) {
            return false;
        }
        String minutes = inputLatitude.substring(inputLatitude.length() - 3, inputLatitude.length() - 1);
        int intMinutes = Integer.parseInt(minutes);
        if (intMinutes > 59) {
            return false;
        }
        return true;
    }

    private boolean checkLongitude(String inputLongitude) {
        if (inputLongitude.length() < 3) return false;
        int lat = Integer.parseInt(inputLongitude);
        if (lat > 180000) {
            return false;
        }
        String minutes = inputLongitude.substring(inputLongitude.length() - 3, inputLongitude.length() - 1);
        int intMinutes = Integer.parseInt(minutes);
        if (intMinutes > 59) {
            return false;
        }
        return true;
    }

    private Airfield getExistAirfieldByICaoIAta(String iCao, String iAta) {
        if (!TextUtils.isEmpty(iAta)) {
            Airfield airfield = databaseManager.getAirfieldByICAOIATAWhereNotEdit(iAta, mAirfield != null ?
                    mAirfield.getAFCode() : null);
            if (airfield != null)
                return airfield;
        }
        if (!"ZZZZ".equals(iCao)) {
            Airfield airfield = databaseManager.getAirfieldByICAOIATAWhereNotEdit(iCao, mAirfield != null ?
                    mAirfield.getAFCode() : null);
            if (airfield != null)
                return airfield;
        }
        return null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setTextTimeZone();
        if (mCountry != null) {
            itemAirfieldCountry.setDescription(mCountry.getCountryName());
            //itemAirfieldCountry.setFootNote(mCountry.getCapital());
        }
    }


    /*mccKeyboard*/

    private HashMap<MccEditText, Integer> initEditTextTag() {
        HashMap<MccEditText, Integer> mapEditText = new HashMap<>();
        mapEditText.put(edtItemAirfieldIcao, 1);
        mapEditText.put(edtItemAirfieldIata, 2);
        mapEditText.put(edtItemAirfieldAirfieldName, 3);
        mapEditText.put(edtItemAirfieldLatitude, 4);
        mapEditText.put(edtItemAirfieldLongitude, 5);
        mapEditText.put(edtItemAirfieldElevation, 6);
        mapEditText.put(edtItemAirfieldNearbyCity, 7);
        mapEditText.put(edtItemAirfieldMyNotes, 8);
        setActionDone(edtItemAirfieldIcao);
        setActionDone(edtItemAirfieldIata);
        setActionDone(edtItemAirfieldAirfieldName);
        setActionDone(edtItemAirfieldLatitude);
        setActionDone(edtItemAirfieldLongitude);
        setActionDone(edtItemAirfieldElevation);
        setActionDone(edtItemAirfieldNearbyCity);
        setActionDone(edtItemAirfieldMyNotes);
        edtItemAirfieldIcao.setSelectAllOnFocus(true);
        edtItemAirfieldIata.setSelectAllOnFocus(true);
        edtItemAirfieldAirfieldName.setSelectAllOnFocus(true);
        edtItemAirfieldLatitude.setSelectAllOnFocus(true);
        edtItemAirfieldLongitude.setSelectAllOnFocus(true);
        edtItemAirfieldElevation.setSelectAllOnFocus(true);
        edtItemAirfieldNearbyCity.setSelectAllOnFocus(true);
        edtItemAirfieldMyNotes.setSelectAllOnFocus(true);
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
        int entryPoint;
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
            R.id.edt_item_airfield_icao,
            R.id.edt_item_airfield_iata,
            R.id.edt_item_airfield_airfield_name,
            R.id.edt_item_airfield_latitude,
            R.id.edt_item_airfield_longitude,
            R.id.edt_item_airfield_elevation,
            R.id.edt_item_airfield_nearbycity,
            R.id.edt_item_airfield_mynotes
    })
    void onFocusChange(View v) {
        switch (v.getId()) {
            case R.id.edt_item_airfield_icao:
            case R.id.edt_item_airfield_iata:
            case R.id.edt_item_airfield_airfield_name:
            case R.id.edt_item_airfield_latitude:
            case R.id.edt_item_airfield_longitude:
            case R.id.edt_item_airfield_elevation:
            case R.id.edt_item_airfield_nearbycity:
            case R.id.edt_item_airfield_mynotes:
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
        clearFocus();
        hideTopKey();
    }

    private void clearFocus() {
        View v = mActivity.getCurrentFocus();
        if (v != null && v instanceof MccEditText) {
            v.clearFocus();
        }
    }

    private void setTextTimeZone() {
        if (mTimeZone != null) {
            itemAirfieldTimeZone.setDescription(mTimeZone.getTimeZone());
            TimeZone timeZone = getTimeZone(mTimeZone);
            Calendar c = Calendar.getInstance(timeZone);
            String offset;
            double offsetValue = (double) Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, mTimeZone.getTZCode()) / 60;
            if (offsetValue < 0) {
                offset = "" + offsetValue;
            } else {
                offset = "+" + offsetValue;
            }
            if (offset.endsWith(".0")) offset = offset.replace(".0", "");
            itemAirfieldTimeZone.setFootNote("UTC " + offset);
        }
    }

    private TimeZone getTimeZone(ZTimeZone zTimeZone) {
        return TimeZone.getTimeZone(zTimeZone.getTimeZone());
    }

    private LocationManager locationManager;
    private LocationListener listener;

    private void checkLocation() {
        locationManager = (LocationManager) mActivity.getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    edtItemAirfieldLatitude.setText(LocationUtils.getLatLongStringFromDouble(location.getLatitude(), false));
                    edtItemAirfieldLongitude.setText(LocationUtils.getLatLongStringFromDouble(location.getLongitude(), true));
                    edtItemAirfieldElevation.setText( String.valueOf((int) Math.round(location.getAltitude() * 3.280839895)));
                }
                if (locationManager != null) {
                    locationManager.removeUpdates(listener);
                    locationManager.removeUpdates(listener);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
                } else {
                }

                break;
            default:
                break;
        }
    }

    void configure_button() {
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        ivGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first check for permissions
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                                , 10);
                    }
                    return;
                }
                //noinspection MissingPermission
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (locationManager != null) {
            locationManager.removeUpdates(listener);
            locationManager.removeUpdates(listener);
        }

        super.onDestroy();
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
        if ((isChanged || isSelectValueChanged()) && isVisible()) {
            MccDialog.getOkCancelAlertDialog(mActivity, mAirfield == null ? R.string.add_airfield : R.string.edit_airfield
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
        return itemAirfieldCountry.isValueChanged ||
                itemAirfieldTimeZone.isValueChanged ||
                itemAirfieldCategory.isValueChanged;
    }
}