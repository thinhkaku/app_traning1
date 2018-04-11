package aero.pilotlog.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.utilities.KeyboardUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 2/25/2016.
 * Flight Empty Screen on Tablet
 */
public class FlightAddEmptyFragment extends BaseMCCFragment {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.image_empty)
    ImageView imageEmpty;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_logbook_flight_view_empty;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_add_flight_footer_empty;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mTitle.setVisibility(View.GONE);
        Fragment fragment = getLeftFragment();
        int padding = (int)getResources().getDimension(R.dimen.padding_icon_watermark);
        switch (fragment.getClass().getName()){
            case "aero.pilotlog.fragments.FlightListFragment":
                mTvTitle.setText(R.string.text_flight_add);
                imageEmpty.setImageResource(android.R.color.transparent);
                imageEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_flights_large));
                break;
            case "aero.pilotlog.fragments.DutyFragment":
                mTvTitle.setText(R.string.duty_add_title);

                imageEmpty.setPadding(padding,padding,padding,padding);
                imageEmpty.setImageResource(android.R.color.transparent);
                imageEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_duty_large));
                break;
            case "aero.pilotlog.fragments.ExpenseListFragment":
                mTvTitle.setText(R.string.expense_add_title);
                imageEmpty.setPadding(padding,padding,padding,padding);
                imageEmpty.setImageResource(android.R.color.transparent);
                imageEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_expense_large));
                break;

        }

        //mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.size_text_title));
        mIbMenu.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int)getResources().getDimension(R.dimen.padding_text_title), 0, 0, 0);
        mTvTitle.setLayoutParams(params);
    }

    @Nullable
    @OnClick({R.id.ll_flight_configure})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ll_flight_configure:
                KeyboardUtils.hideKeyboard(mActivity);
                replaceFragment(R.id.rightContainerFragment, FlightConfigsFragment.class, FLAG_ADD_STACK);
                break;
            default:
                break;
        }
    }
}