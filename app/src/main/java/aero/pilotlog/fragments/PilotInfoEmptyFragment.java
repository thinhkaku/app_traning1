package aero.pilotlog.fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tuan.pv on 2015/09/04.
 * Pilot detail empty
 */
public class PilotInfoEmptyFragment extends BaseMCCFragment {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.title)
    TextView mTitle;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_pilot_info_empty;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mTitle.setVisibility(View.GONE);
        mTvTitle.setText("Pilot Info");
        //mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.size_text_title));
        mIbMenu.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int)getResources().getDimension(R.dimen.padding_text_title), 0, 0, 0);
        mTvTitle.setLayoutParams(params);
    }
}