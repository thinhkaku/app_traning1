package aero.pilotlog.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.utilities.KeyboardUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by tuan.pv on 2015/07/31.
 * Delay tail search
 */
public class DelayTailsSearchFragment extends BaseMCCFragment {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;

    @Bind(R.id.ivBelowSearch)
    ImageView mIvBelowSearch;
    @Bind(R.id.searchView)
    SearchView searchView;

    private Fragment mFragment;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar_none;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_delaytails_search;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mFragment = getLeftFragment();
        initView();

        searchView.setQueryHint("Search...");
        searchView.setBackgroundColor(getResources().getColor(R.color.background_white));
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(getResources().getColor(R.color.background_white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mFragment != null && mFragment instanceof TailListFragment) {
                    ((TailListFragment) mFragment).doSearch(newText);
                }else  if (mFragment != null && mFragment instanceof DelayListFragment) {
                    ((DelayListFragment) mFragment).doSearch(newText);
                }
                return false;
            }
        });
    }

    /**
     * Init view
     */
    public void initView() {
        mIbMenu.setVisibility(View.GONE);
        /*if (mFragment != null && mFragment instanceof TailListFragment ) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            mIvBelowSearch.setLayoutParams(lp);
        }*/
        mIvBelowSearch.setImageResource((mFragment != null && mFragment instanceof TailListFragment) ? R.drawable.ic_tails : R.drawable.ic_chrono);
    }

    @OnClick({R.id.ibMenu})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                toggleMenu();
                break;
        }
    }
}