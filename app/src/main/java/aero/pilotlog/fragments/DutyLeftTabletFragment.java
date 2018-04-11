package aero.pilotlog.fragments;

import android.view.View;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.models.DutyModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tuan.pv on 10/30/2015.
 * Duty left tablet
 */
public class DutyLeftTabletFragment extends BaseMCCFragment {

    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tv_title)
    TextView mTvSubTitle;
    //IcaoIata
    @Bind(R.id.tvIcaoIata1)
    TextView mTvIcaoIata1;
    @Bind(R.id.tvIcaoIata2)
    TextView mTvIcaoIata2;
    @Bind(R.id.tvIcaoIata3)
    TextView mTvIcaoIata3;
    @Bind(R.id.tvIcaoIata4)
    TextView mTvIcaoIata4;
    @Bind(R.id.tvIcaoIata5)
    TextView mTvIcaoIata5;
    @Bind(R.id.tvIcaoIata6)
    TextView mTvIcaoIata6;
    //Flight Time
    @Bind(R.id.tvFlightTime1)
    TextView mTvFlightTime1;
    @Bind(R.id.tvFlightTime2)
    TextView mTvFlightTime2;
    @Bind(R.id.tvFlightTime3)
    TextView mTvFlightTime3;
    @Bind(R.id.tvFlightTime4)
    TextView mTvFlightTime4;
    @Bind(R.id.tvFlightTime5)
    TextView mTvFlightTime5;
    @Bind(R.id.tvFlightTime6)
    TextView mTvFlightTime6;
    //Total item time
    @Bind(R.id.tvTotalFlightTime1)
    TextView mTvTotalFlightTime1;
    @Bind(R.id.tvTotalFlightTime2)
    TextView mTvTotalFlightTime2;
    @Bind(R.id.tvTotalFlightTime3)
    TextView mTvTotalFlightTime3;
    @Bind(R.id.tvTotalFlightTime4)
    TextView mTvTotalFlightTime4;
    @Bind(R.id.tvTotalFlightTime5)
    TextView mTvTotalFlightTime5;
    @Bind(R.id.tvTotalFlightTime6)
    TextView mTvTotalFlightTime6;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_duty_left_tablet;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        initView();
    }

    private void initView() {
        mTvTitle.setVisibility(View.GONE);
        mTvSubTitle.setText(R.string.duty_time);
    }

    public void initDataListDuty(List<DutyModel> pDutyModels) {
        //Set data for list flight
        DutyModel flight1, flight2, flight3, flight4, flight5, flight6;
        flight1 = pDutyModels.get(0);
        flight2 = pDutyModels.get(1);
        flight3 = pDutyModels.get(2);
        flight4 = pDutyModels.get(3);
        flight5 = pDutyModels.get(4);
        flight6 = pDutyModels.get(5);

        mTvIcaoIata1.setText(flight1.getIcaoIata());
        mTvIcaoIata2.setText(flight2.getIcaoIata());
        mTvIcaoIata3.setText(flight3.getIcaoIata());
        mTvIcaoIata4.setText(flight4.getIcaoIata());
        mTvIcaoIata5.setText(flight5.getIcaoIata());
        mTvIcaoIata6.setText(flight6.getIcaoIata());

        mTvFlightTime1.setText(flight1.getFromToHours());
        mTvFlightTime2.setText(flight2.getFromToHours());
        mTvFlightTime3.setText(flight3.getFromToHours());
        mTvFlightTime4.setText(flight4.getFromToHours());
        mTvFlightTime5.setText(flight5.getFromToHours());
        mTvFlightTime6.setText(flight6.getFromToHours());

        mTvTotalFlightTime1.setText(flight1.getTotalHours());
        mTvTotalFlightTime2.setText(flight2.getTotalHours());
        mTvTotalFlightTime3.setText(flight3.getTotalHours());
        mTvTotalFlightTime4.setText(flight4.getTotalHours());
        mTvTotalFlightTime5.setText(flight5.getTotalHours());
        mTvTotalFlightTime6.setText(flight6.getTotalHours());
    }

    @OnClick(R.id.ibMenu)
    public void onClickMenu() {
        toggleMenu();
    }
}
