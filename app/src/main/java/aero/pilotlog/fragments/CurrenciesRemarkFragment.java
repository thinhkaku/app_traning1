package aero.pilotlog.fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.StorageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tuan.pv on 2015/09/23.
 * Currencies remark
 */
public class CurrenciesRemarkFragment extends BaseMCCFragment {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvLastUpdate)
    TextView mTvLastUpdate;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_currencies_remark;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);

        mIbMenu.setVisibility(View.GONE);
        mTvTitle.setVisibility(View.GONE);
        String lastUpdate = DateTimeUtils.getDateLastSync(StorageUtils.getLongFromSharedPref(mActivity, MCCPilotLogConst.PREF_TIME_LAST_UPDATE, 0));
        mTvLastUpdate.setText(getResources().getString(R.string.text_currency_last_update) + "  " + lastUpdate);
    }
}
