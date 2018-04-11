package aero.pilotlog.fragments;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Totals;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.ItemTotal;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tuan.pv on 2015/09/17.
 * Total
 */
public class TotalsFragment extends BaseMCCFragment implements IAsyncTaskCallback {

    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Nullable
    @Bind(R.id.tv_title)
    TextView mSubTitle;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.itemTotalAircraft)
    ItemTotal itemTotalAircraft;
    @Bind(R.id.itemTotalSimulator)
    ItemTotal itemTotalSimulator;
    @Bind(R.id.itemTotalDrone)
    ItemTotal itemTotalDrone;
    @Bind(R.id.itemGrandTotal)
    ItemTotal itemTotalGrand;

    @Bind(R.id.itemTotalPic)
    ItemTotal itemTotalPic;
    @Bind(R.id.itemTotalPicUs)
    ItemTotal itemTotalPicUs;
    @Bind(R.id.itemTotalCoPilot)
    ItemTotal itemTotalCoPilot;
    @Bind(R.id.itemTotalDual)
    ItemTotal itemTotalDual;
    @Bind(R.id.itemTotalInstructor)
    ItemTotal itemTotalInstructor;
    @Bind(R.id.itemTotalExaminer)
    ItemTotal itemTotalExaminer;

    @Bind(R.id.itemTotalRelief)
    ItemTotal itemTotalRelief;
    @Bind(R.id.itemTotalNight)
    ItemTotal itemTotalNight;
    @Bind(R.id.itemTotalIFR)
    ItemTotal itemTotalIFR;
    @Bind(R.id.itemTotalActualInstrument)
    ItemTotal itemTotalActualInstrument;
    @Bind(R.id.itemTotalSimulatedInstrument)
    ItemTotal itemTotalSimulatedInstrument;
    @Bind(R.id.itemTotalXC)
    ItemTotal itemTotalXC;
    @Bind(R.id.itemTotalUser1)
    ItemTotal itemTotalUser1;
    @Bind(R.id.itemTotalUser2)
    ItemTotal itemTotalUser2;
    @Bind(R.id.itemTotalUser3)
    ItemTotal itemTotalUser3;
    @Bind(R.id.itemTotalUser4)
    ItemTotal itemTotalUser4;

    @Bind(R.id.itemTotalMicroLight)
    ItemTotal itemTotalMicroLight;
    @Bind(R.id.itemTotalGlider)
    ItemTotal itemTotalGlider;
    @Bind(R.id.itemTotalLighterThanAir)
    ItemTotal itemTotalLighterThanAir;
    @Bind(R.id.itemTotalRotorCraft)
    ItemTotal itemTotalRotorCraft;
    @Bind(R.id.itemTotalAeroplane)
    ItemTotal itemTotalAeroplane;
    @Bind(R.id.itemTotalUnmannedAircraft)
    ItemTotal itemTotalUnmannedAircraft;

    @Bind(R.id.itemTotalUnPower)
    ItemTotal itemTotalUnPower;
    @Bind(R.id.itemTotalSePiston)
    ItemTotal itemTotalSePiston;
    @Bind(R.id.itemTotalSeTurboprop)
    ItemTotal itemTotalSeTurboprop;
    @Bind(R.id.itemTotalSeTurbofan)
    ItemTotal itemTotalSeTurbofan;
    @Bind(R.id.itemTotalMePiston)
    ItemTotal itemTotalMePiston;
    @Bind(R.id.itemTotalMeTurboprop)
    ItemTotal itemTotalMeTurboprop;
    @Bind(R.id.itemTotalMeTurbofan)
    ItemTotal itemTotalMeTurbofan;

    @Bind(R.id.itemTotalToday)
    ItemTotal itemTotalToday;
    @Bind(R.id.itemTotalLdgDay)
    ItemTotal itemTotalLdgDay;
    @Bind(R.id.itemTotalToNight)
    ItemTotal itemTotalToNight;
    @Bind(R.id.itemTotalLdgNight)
    ItemTotal itemTotalLdgNight;
    @Bind(R.id.itemTotalLift)
    ItemTotal itemTotalLift;
    @Bind(R.id.itemTotalHolding)
    ItemTotal itemTotalHolding;

    @Bind(R.id.cbTotalAircraft)
    AppCompatCheckBox cbTotalAircraft;
    @Bind(R.id.cbTotalSimulator)
    AppCompatCheckBox cbTotalSimulator;
    @Bind(R.id.cbTotalDrone)
    AppCompatCheckBox cbTotalDrone;
    @Bind(R.id.ln_message_deduct)
    LinearLayout lnMessageDeduct;
    LoadDataTask mLoadDataTask = null;

    private DatabaseManager mDatabaseManager;
    private Totals mTotalsFromTotals, mTotalsFromFlight;
    private String strTimeInDecimal;
    private boolean isSelectAircraft = true, isSelectDrone = false, isSelectSimulator = false;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_totals;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        initView();
        initSetting();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void setEnableThreeCheckBox(boolean isEnable) {
        cbTotalAircraft.setEnabled(isEnable);
        cbTotalSimulator.setEnabled(isEnable);
        cbTotalDrone.setEnabled(isEnable);
    }

    private void initView() {
       /* if (isTablet()) {
            updateWidthLayoutTablet();
        }*/
       if(mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_DEDUCT_RELIEF).getData().equals("1"))
           lnMessageDeduct.setVisibility(View.VISIBLE);
        cbTotalAircraft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setEnableThreeCheckBox(false);
                if (b) {
                    isSelectAircraft = true;
                    cbTotalAircraft.setTextColor(getResources().getColor(R.color.background_white));
                } else {
                    isSelectAircraft = false;
                    cbTotalAircraft.setTextColor(getResources().getColor(R.color.mcc_cyan));
                }
                loadData();
                end();
                setEnableThreeCheckBox(true);
            }
        });
        cbTotalSimulator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setEnableThreeCheckBox(false);
                if (b) {
                    isSelectSimulator = true;
                    cbTotalSimulator.setTextColor(getResources().getColor(R.color.background_white));
                } else {
                    isSelectSimulator = false;
                    cbTotalSimulator.setTextColor(getResources().getColor(R.color.mcc_cyan));
                }
                loadData();
                end();
                setEnableThreeCheckBox(true);
            }
        });
        cbTotalDrone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setEnableThreeCheckBox(false);
                if (b) {
                    isSelectDrone = true;
                    cbTotalDrone.setTextColor(getResources().getColor(R.color.background_white));
                } else {
                    isSelectDrone = false;
                    cbTotalDrone.setTextColor(getResources().getColor(R.color.mcc_cyan));
                }
                loadData();
                end();
                setEnableThreeCheckBox(true);
            }
        });
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(R.string.text_title_totals);
        if (mSubTitle != null) {
            mSubTitle.setText(R.string.text_title_totals);
        }

    }

    private void initSetting() {
        String logDecimal = mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_IS_LOG_DECIMAL).getData();
        if (logDecimal.equals("2") || logDecimal.equals("1")) strTimeInDecimal = "1";
        else strTimeInDecimal = "0";
        String userDefineCaption1 = mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION1).getData();
        itemTotalUser1.setTextTitle(userDefineCaption1);
        String userDefineCaption2 = mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION2).getData();
        itemTotalUser2.setTextTitle(userDefineCaption2);
        String userDefineCaption3 = mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION3).getData();
        itemTotalUser3.setTextTitle(userDefineCaption3);
        String userDefineCaption4 = mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION4).getData();
        itemTotalUser4.setTextTitle(userDefineCaption4);
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        loadData();
    }

    private void loadData() {
        mTotalsFromFlight = mDatabaseManager.getTotalsFromFlight(isSelectAircraft, isSelectSimulator, isSelectDrone);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        if (mTotalsFromFlight == null) {
            return;
        }

        int sumAircraft = mTotalsFromFlight.getSumACFT();
        itemTotalAircraft.setTextValueBold(sumAircraft > 0 ? Utils.getHourTotals(strTimeInDecimal, sumAircraft) : STRING_EMPTY);

        int sumSimulator = mTotalsFromFlight.getSumSIM();
        itemTotalSimulator.setTextValueBold(sumSimulator > 0 ? Utils.getHourTotals(strTimeInDecimal, sumSimulator) : STRING_EMPTY);

        int sumDrone = mTotalsFromFlight.getSumDRONE();
        itemTotalDrone.setTextValueBold(sumDrone > 0 ? Utils.getHourTotals(strTimeInDecimal, sumDrone) : STRING_EMPTY);

        int totalGrand = sumAircraft + sumSimulator + sumDrone;
        itemTotalGrand.setTextValueBold(totalGrand > 0 ? Utils.getHourTotals(strTimeInDecimal, totalGrand) : STRING_EMPTY);

        int sumPic = mTotalsFromFlight.getSumPIC();
        itemTotalPic.setTextValue(sumPic > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPic) : STRING_EMPTY);

        int sumPiCus = mTotalsFromFlight.getSumPICus();
        itemTotalPicUs.setTextValue(sumPiCus > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPiCus) : STRING_EMPTY);

        int sumCopilot = mTotalsFromFlight.getSumCoP();
        itemTotalCoPilot.setTextValue(sumCopilot > 0 ? Utils.getHourTotals(strTimeInDecimal, sumCopilot) : STRING_EMPTY);

        int sumDual = mTotalsFromFlight.getSumDual();
        itemTotalDual.setTextValue(sumDual > 0 ? Utils.getHourTotals(strTimeInDecimal, sumDual) : STRING_EMPTY);

        int sumInstructor = mTotalsFromFlight.getSumInstr();
        itemTotalInstructor.setTextValue(sumInstructor > 0 ? Utils.getHourTotals(strTimeInDecimal, sumInstructor) : STRING_EMPTY);

        int sumExaminer = mTotalsFromFlight.getSumExam();
        itemTotalExaminer.setTextValue(sumExaminer > 0 ? Utils.getHourTotals(strTimeInDecimal, sumExaminer) : STRING_EMPTY);

        int sumRelief = mTotalsFromFlight.getSumREL();
        itemTotalRelief.setTextValue(sumRelief > 0 ? Utils.getHourTotals(strTimeInDecimal, sumRelief) : STRING_EMPTY);

        int sumNight = mTotalsFromFlight.getSumNight();
        itemTotalNight.setTextValue(sumNight > 0 ? Utils.getHourTotals(strTimeInDecimal, sumNight) : STRING_EMPTY);

        int sumIfr = mTotalsFromFlight.getSumIFR();
        itemTotalIFR.setTextValue(sumIfr > 0 ? Utils.getHourTotals(strTimeInDecimal, sumIfr) : STRING_EMPTY);

        int sumActualInstrument = mTotalsFromFlight.getSumIMT();
        itemTotalActualInstrument.setTextValue(sumActualInstrument > 0 ? Utils.getHourTotals(strTimeInDecimal, sumActualInstrument) : STRING_EMPTY);

        int sumSimulatedInstrument = mTotalsFromFlight.getSumSFR();
        itemTotalSimulatedInstrument.setTextValue(sumSimulatedInstrument > 0 ? Utils.getHourTotals(strTimeInDecimal, sumSimulatedInstrument) : STRING_EMPTY);

        int sumXC = mTotalsFromFlight.getSumXC();
        itemTotalXC.setTextValue(sumXC > 0 ? Utils.getHourTotals(strTimeInDecimal, sumXC) : STRING_EMPTY);

        int sumUser1 = mTotalsFromFlight.getSumU1();
        itemTotalUser1.setTextValue(sumUser1 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumUser1) : STRING_EMPTY);
        int sumUser2 = mTotalsFromFlight.getSumU2();
        itemTotalUser2.setTextValue(sumUser2 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumUser2) : STRING_EMPTY);
        int sumUser3 = mTotalsFromFlight.getSumU3();
        itemTotalUser3.setTextValue(sumUser3 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumUser3) : STRING_EMPTY);
        int sumUser4 = mTotalsFromFlight.getSumU4();
        itemTotalUser4.setTextValue(sumUser4 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumUser4) : STRING_EMPTY);

        int sumClass1 = mTotalsFromFlight.getSumClass1();
        itemTotalMicroLight.setTextValue(sumClass1 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumClass1) : STRING_EMPTY);
        int sumClass2 = mTotalsFromFlight.getSumClass2();
        itemTotalGlider.setTextValue(sumClass2 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumClass2) : STRING_EMPTY);
        int sumClass3 = mTotalsFromFlight.getSumClass3();
        itemTotalLighterThanAir.setTextValue(sumClass3 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumClass3) : STRING_EMPTY);
        int sumClass4 = mTotalsFromFlight.getSumClass4();
        itemTotalRotorCraft.setTextValue(sumClass4 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumClass4) : STRING_EMPTY);
        int sumClass5 = mTotalsFromFlight.getSumClass5();
        itemTotalAeroplane.setTextValue(sumClass5 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumClass5) : STRING_EMPTY);
        int sumClass6 = mTotalsFromFlight.getSumClass6();
        itemTotalUnmannedAircraft.setTextValue(sumClass6 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumClass6) : STRING_EMPTY);

        int sumPower0 = mTotalsFromFlight.getSumPower0();
        itemTotalUnPower.setTextValue(sumPower0 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPower0) : STRING_EMPTY);
        int sumPower1 = mTotalsFromFlight.getSumPower1();
        itemTotalSePiston.setTextValue(sumPower1 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPower1) : STRING_EMPTY);
        int sumPower2 = mTotalsFromFlight.getSumPower2();
        itemTotalSeTurboprop.setTextValue(sumPower2 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPower2) : STRING_EMPTY);
        int sumPower3 = mTotalsFromFlight.getSumPower3();
        itemTotalSeTurbofan.setTextValue(sumPower3 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPower3) : STRING_EMPTY);
        int sumPower4 = mTotalsFromFlight.getSumPower4();
        itemTotalMePiston.setTextValue(sumPower4 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPower4) : STRING_EMPTY);
        int sumPower5 = mTotalsFromFlight.getSumPower5();
        itemTotalMeTurboprop.setTextValue(sumPower5 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPower5) : STRING_EMPTY);
        int sumPower6 = mTotalsFromFlight.getSumPower6();
        itemTotalMeTurbofan.setTextValue(sumPower6 > 0 ? Utils.getHourTotals(strTimeInDecimal, sumPower6) : STRING_EMPTY);

        int sumToday = mTotalsFromFlight.getSumTODay();
        itemTotalToday.setTextValue(sumToday > 0 ? sumToday + STRING_EMPTY : STRING_EMPTY);
        int sumLdgDay = mTotalsFromFlight.getSumLdgDay();
        itemTotalLdgDay.setTextValue(sumLdgDay > 0 ? sumLdgDay + STRING_EMPTY : STRING_EMPTY);
        int sumToNight = mTotalsFromFlight.getSumTONight();
        itemTotalToNight.setTextValue(sumToNight > 0 ? sumToNight + STRING_EMPTY : STRING_EMPTY);
        int sumLdgNight = mTotalsFromFlight.getSumLdgNight();
        itemTotalLdgNight.setTextValue(sumLdgNight > 0 ? sumLdgNight + STRING_EMPTY : STRING_EMPTY);

        int sumLift = mTotalsFromFlight.getSumLift();
        itemTotalLift.setTextValue(sumLift > 0 ? sumLift + STRING_EMPTY : STRING_EMPTY);
        int sumHolding = mTotalsFromFlight.getSumHolding();
        itemTotalHolding.setTextValue(sumHolding > 0 ? sumHolding + STRING_EMPTY : STRING_EMPTY);
    }

    @OnClick(R.id.ibMenu)
    public void onClick(View pView) {
        toggleMenu();
    }


    /**
     * update again width layout tablet when rotate screen.
     */
   /* private void updateWidthLayoutTablet() {
        if (mLlLeftTotals != null && mLlRightTotals != null && isTablet()) {
            LinearLayout.LayoutParams lpLeft, lpRight;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lpLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lpRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            } else {
                lpLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lpRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            }
            mLlLeftTotals.setLayoutParams(lpLeft);
            mLlRightTotals.setLayoutParams(lpRight);
        }
    }*/

  /*  @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isTablet()) {
            updateWidthLayoutTablet();
        }
    }*/
}