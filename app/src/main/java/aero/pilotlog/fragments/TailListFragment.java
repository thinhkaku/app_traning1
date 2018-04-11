package aero.pilotlog.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
//import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

import java.util.ArrayList;
import java.util.List;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.TailsAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import me.yokeyword.indexablerv.EntityWrapper;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

public class TailListFragment extends BaseMCCFragment {
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    /*   @Nullable
       @Bind(R.id.edtSearch)
       EditText mEdtSearch;*/
    @Bind(R.id.indexableLayout)
    IndexableLayout indexableLayout;
    @Bind(R.id.progress)
    FrameLayout mProgressBar;
    @Bind(R.id.searchView)
    SearchView searchView;

    private boolean mIsSearch = true;
    public ItemAdapter adapter;
    private List<ZCountry> mItemList;
    private List<ZCountry> mItemListCopy;
    private DatabaseManager databaseManager;


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.base_list;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        databaseManager = DatabaseManager.getInstance(mActivity);
        initView();
    }

    private void initView() {
        mTvTitle.setText("Tails");
        indexableLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(mActivity)));
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        if(isTablet()){
            searchView.setVisibility(View.GONE);
        }else {
            searchView.setQueryHint("Search...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //adapter.setDatas(doSearch(newText));
                    doSearch(newText);
                    return false;
                }
            });
        }


        // setAdapter
        adapter = new ItemAdapter(mActivity);
        indexableLayout.setAdapter(adapter);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mItemList = databaseManager.getAllCountry();
                        if (mItemList != null && mItemList.size() > 0 && mItemList.get(0).getCountryCode() == 0) {
                            mItemList.remove(0);
                        }
                        adapter.setDatas(mItemList, new IndexableAdapter.IndexCallback<ZCountry>() {
                            @Override
                            public void onFinished(List<EntityWrapper<ZCountry>> datas) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });

        // set Material Design OverlayView
        indexableLayout.setOverlayStyle_MaterialDesign(getResources().getColor(R.color.mcc_cyan));
        //indexableLayout.setIndexBarVisibility(false);
        indexableLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
    }

    public void doSearch(String pText) {
        String strSearch = pText.trim();
        if (strSearch.length() > 0) {
            List<ZCountry> listClone = new ArrayList<>();
            for (ZCountry zCountry : mItemList) {
                if (zCountry.getRegAC().toLowerCase().replace("-", "").contains(pText.toLowerCase())
                        || zCountry.getCountryName().substring(0, pText.length()).equalsIgnoreCase(pText)) {
                    listClone.add(zCountry);
                }
            }
            adapter.setDatas(listClone);
        } else {
            adapter.setDatas(mItemList);
        }
    }

    @Nullable
    @OnClick({R.id.ibMenu})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                toggleMenu();
                break;
        }
    }


    @Nullable
    @OnTextChanged(R.id.edtSearch)
    public void onTextChanged(CharSequence pText) {
        if (mIsSearch) {
            //doSearch(pText.toString());
        }
        mIsSearch = true;
    }

    @Override
    public void onKeyBackPress() {
        finishFragment();
        super.onKeyBackPress();
    }

    private String getRegAC(String oldRegAC) {
        String newRegAC = "";
        if (!TextUtils.isEmpty(oldRegAC)) {
            String[] arrayRegAC = oldRegAC.split(MCCPilotLogConst.SPLIT_KEY);
            if (arrayRegAC != null) {
                for (int i = 0; i < arrayRegAC.length; i++) {
                    newRegAC += arrayRegAC[i] + MCCPilotLogConst.SPACE;
                }
            }
        }
        return newRegAC;
    }

    private String getContinent(String pAcronym) {
        switch (pAcronym) {
            case MCCPilotLogConst.CONTINENT_ASIA:
                return getString(R.string.text_asia);
            case MCCPilotLogConst.CONTINENT_AFRICA:
                return getString(R.string.text_africa);
            case MCCPilotLogConst.CONTINENT_ANTARCTICA:
                return getString(R.string.text_antarctica);
            case MCCPilotLogConst.CONTINENT_EUROPE:
                return getString(R.string.text_europe);
            case MCCPilotLogConst.CONTINENT_MORTH_AMERICA:
                return getString(R.string.text_north_america);
            case MCCPilotLogConst.CONTINENT_OCEANIA:
                return getString(R.string.text_oceania);
            case MCCPilotLogConst.CONTINENT_SOUTH_AMERICA:
                return getString(R.string.text_south_america);
            default:
                return "";
        }
    }

    public class ItemAdapter extends IndexableAdapter<ZCountry> {
        private LayoutInflater mInflater;
        private int mSortType = 0;


        public ItemAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.item_index_title_recycler_view, parent, false);
            return new TailListFragment.ItemAdapter.IndexViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.row_item_standar, parent, false);
            return new TailListFragment.ItemAdapter.ContentViewHolder(view);
        }

        @Override
        public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
            TailListFragment.ItemAdapter.IndexViewHolder vh = (TailListFragment.ItemAdapter.IndexViewHolder) holder;
            vh.tv.setText(indexTitle);
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder viewHolder, final ZCountry entity) {
            final TailListFragment.ItemAdapter.ContentViewHolder holder = (TailListFragment.ItemAdapter.ContentViewHolder) viewHolder;
            holder.mItem = entity;
            holder.mTvTitle.setText(getRegAC(entity.getRegAC()));
            holder.mTvDescription.setText(entity.getCountryName());
            holder.mTvFootNote.setText(getContinent(entity.getContinent()));
        }


        private class IndexViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public IndexViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv_index);
            }
        }

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTvTitle;
            public final TextView mTvDescription;
            public final TextView mTvFootNote;

            public ZCountry mItem;

            public ContentViewHolder(View view) {
                super(view);
                mView = view;
                mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
                mTvDescription = (TextView) view.findViewById(R.id.tvDescription);
                mTvFootNote = (TextView) view.findViewById(R.id.tvFootNote);
            }
        }
    }

}
