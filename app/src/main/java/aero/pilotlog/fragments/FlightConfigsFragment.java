package aero.pilotlog.fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.ItemFlightConfigure;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by phuc.dd on 1/25/2017.
 * Flight Config
 */
public class FlightConfigsFragment extends BaseMCCFragment implements IAsyncTaskCallback {

    @Bind(R.id.item_configure_arrival_airfield)
    ItemFlightConfigure mItemConfigureArrivalAirfield;
    @Bind(R.id.item_configure_departure_airfield)
    ItemFlightConfigure mItemConfigureDepartureAirfield;
    @Bind(R.id.item_configure_hobbs)
    ItemFlightConfigure mItemConfigurehobbs;
    @Bind(R.id.item_configure_off_block)
    ItemFlightConfigure mItemConfigureOffBlock;
    @Bind(R.id.item_configure_take_off)
    ItemFlightConfigure mItemConfigureTakeOff;
    @Bind(R.id.item_configure_ldg_day)
    ItemFlightConfigure mItemConfigureLdgDay;
    @Bind(R.id.item_configure_ldg_night)
    ItemFlightConfigure mItemConfigureLdgNight;
    @Bind(R.id.item_configure_today)
    ItemFlightConfigure mItemConfigureToday;
    @Bind(R.id.item_configure_tonight)
    ItemFlightConfigure mItemConfigureTonight;

    @Bind(R.id.item_configure_date)
    ItemFlightConfigure mItemConfigureDate;
    @Bind(R.id.item_configure_aircraft)
    ItemFlightConfigure mItemConfigureAircraft;
    @Bind(R.id.item_configure_flight_number)
    ItemFlightConfigure mItemConfigureFlightNumber;
    @Bind(R.id.item_configure_pairing)
    ItemFlightConfigure mItemConfigurePairing;
    @Bind(R.id.item_configure_user_1)
    ItemFlightConfigure mItemConfigureUser1;
    @Bind(R.id.item_configure_user_2)
    ItemFlightConfigure mItemConfigureUser2;
    @Bind(R.id.item_configure_user_3)
    ItemFlightConfigure mItemConfigureUser3;
    @Bind(R.id.item_configure_user_4)
    ItemFlightConfigure mItemConfigureUser4;
    @Bind(R.id.item_configure_user_numeric)
    ItemFlightConfigure mItemConfigureUserNumeric;
    @Bind(R.id.item_configure_user_text)
    ItemFlightConfigure mItemConfigureUserText;
    @Bind(R.id.item_configure_user_boolean)
    ItemFlightConfigure mItemConfigureUserBoolean;

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;

    private LoadDataTask mLoadDataTask;

    private DatabaseManager mDatabaseManager;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_flight_configure;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        mIbMenu.setImageResource(R.drawable.ic_back);
        mTvTitle.setText("Flight Setup");
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void initView() {
        mItemConfigureUser1.setVisibilityImageSettingEdit(View.VISIBLE);
        mItemConfigureUser2.setVisibilityImageSettingEdit(View.VISIBLE);
        mItemConfigureUser3.setVisibilityImageSettingEdit(View.VISIBLE);
        mItemConfigureUser4.setVisibilityImageSettingEdit(View.VISIBLE);
        mItemConfigureUserNumeric.setVisibilityImageSettingEdit(View.VISIBLE);
        mItemConfigureUserText.setVisibilityImageSettingEdit(View.VISIBLE);
        mItemConfigureUserBoolean.setVisibilityImageSettingEdit(View.VISIBLE);
        mItemConfigureDepartureAirfield.setBackgroundForCheckboxMandatory();
        mItemConfigureArrivalAirfield.setBackgroundForCheckboxMandatory();
        mItemConfigureDate.setBackgroundForCheckboxMandatory();
        mItemConfigureAircraft.setBackgroundForCheckboxMandatory();
        if (mItemConfigureFlightNumber.isCheck()) {
            mItemConfigurePairing.setBackgroundForCheckboxUnMandatory();
        } else {
            mItemConfigurePairing.setBackgroundForCheckboxMandatoryUnCheck();
        }
    }

    private void updateConfig(ItemFlightConfigure item) {
        boolean check = item.isCheck();
        item.setChecked(!check);
        mDatabaseManager.updateSetting(item.getTag().toString(), item.isCheck() ? "1" : "0");
    }

    @OnClick({R.id.ibMenu, R.id.item_configure_hobbs, R.id.item_configure_off_block,
            R.id.item_configure_today, R.id.item_configure_tonight, R.id.item_configure_ldg_day,
            R.id.item_configure_ldg_night, R.id.item_configure_take_off,
            R.id.item_configure_fuel, R.id.item_configure_fuel_used, R.id.item_configure_sign_box, R.id.item_configure_sign_pen,
            R.id.item_configure_flight_number, R.id.item_configure_pairing, R.id.item_configure_total_time, R.id.item_configure_pic,
            R.id.item_configure_picus, R.id.item_configure_copilot, R.id.item_configure_dual, R.id.item_configure_instructor,
            R.id.item_configure_examiner, R.id.item_configure_relief, R.id.item_configure_night, R.id.item_configure_IFR,
            R.id.item_configure_actual_instrument, R.id.item_configure_simulated_instrument, R.id.item_configure_cross_country,
            R.id.item_configure_user_1, R.id.item_configure_user_2, R.id.item_configure_user_3, R.id.item_configure_user_4,
            R.id.item_configure_user_boolean, R.id.item_configure_user_numeric, R.id.item_configure_user_text,
            R.id.item_configure_task, R.id.item_configure_holding, R.id.item_configure_fuel_planned,
            R.id.item_configure_pax, R.id.item_configure_deicing, R.id.item_configure_delay, R.id.item_configure_glider, R.id.item_configure_lift, R.id.item_configure_pilot_1,
            R.id.item_configure_pilot_2, R.id.item_configure_pilot_3, R.id.item_configure_pilot_4, R.id.item_configure_crew_list,
            R.id.item_configure_remarks, R.id.item_configure_instruction, R.id.item_configure_report,
            R.id.item_configure_type_ops, R.id.item_configure_app_type, R.id.item_configure_schedule_hours, R.id.item_configure_arrival_airfield_runway,
            R.id.item_configure_departure_airfield_runway})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                KeyboardUtils.hideKeyboard(mActivity);
                finishFragment();
                break;
            case R.id.item_configure_hobbs:
                updateConfig(mItemConfigurehobbs);
                break;
            case R.id.item_configure_off_block:
                updateConfig(mItemConfigureOffBlock);
                break;
            case R.id.item_configure_today:
                updateConfig(mItemConfigureToday);
                break;
            case R.id.item_configure_tonight:
                updateConfig(mItemConfigureTonight);
                break;
            case R.id.item_configure_ldg_day:
                updateConfig(mItemConfigureLdgDay);
                break;
            case R.id.item_configure_ldg_night:
                updateConfig(mItemConfigureLdgNight);
                break;
            case R.id.item_configure_take_off:
                updateConfig(mItemConfigureTakeOff);
                break;
            case R.id.item_configure_flight_number:
                updateConfig(mItemConfigureFlightNumber);
                if (mItemConfigureFlightNumber.isCheck()) {
                    mItemConfigurePairing.setBackgroundForCheckboxUnMandatory();
                } else {
                    mItemConfigurePairing.setBackgroundForCheckboxMandatoryUnCheck();
                }
                break;
            default:
                updateConfig((ItemFlightConfigure) pView);
                break;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {

    }

    @Override
    public void updateUI() {
        initView();
        KeyboardUtils.hideKeyboard(mActivity);
    }

    @Override
    public void end() {

    }
}
