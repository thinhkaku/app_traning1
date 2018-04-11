package aero.pilotlog.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.DelayTailsAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.entities.ZDelay;
import aero.pilotlog.databases.entities.ZDelayGroup;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.tagsedittext.TagsEditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.indexablerv.EntityWrapper;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.SimpleHeaderAdapter;

public class DelayListFragment extends BaseMCCFragment {
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
 /*   @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Nullable
    @Bind(R.id.edtSearch)
    EditText mEdtSearch;
    @Bind(R.id.indexableListView)
    IndexableListView mIndexableListView;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;*/
    /* @Bind(R.id.ln_clear)
     LinearLayout mLnClear;*/
    @Bind(R.id.edtTags)
    TagsEditText tagsEditText;
    @Bind(R.id.ln_tag_bar)
    LinearLayout lnTagBar;

    @Bind(R.id.indexableLayout)
    IndexableLayout indexableLayout;
    @Bind(R.id.progress)
    FrameLayout mProgressBar;
    @Bind(R.id.searchView)
    SearchView searchView;

    private ItemAdapter adapter;
    private List<ZDelay> mItemList;
    private List<ZDelay> mItemListCopy;

    private StringBuilder mZCodes;
    private List<String> mZShorts;

    private DelayTailsAdapter mDelayTailsAdapter;
    private int mViewType = MCCPilotLogConst.LIST_MODE; //mTypeAdapter = MCCPilotLogConst.DELAY_ADAPTER;
    private boolean mIsSearch = true;

    private DatabaseManager databaseManager;

    public DelayListFragment() {
        // Required empty public constructor
    }

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
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mViewType = bundle.getInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE);

        }
        databaseManager = DatabaseManager.getInstance(mActivity);
        initView();
    }

    public void initView() {

        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            mTvTitle.setText("Select Delay Codes");
            mIbMenu.setImageResource(R.drawable.ic_back);
        }else {
            mTvTitle.setText( R.string.text_delay_codes );
        }

        indexableLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(mActivity)));
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        if(isTablet()){
            searchView.setVisibility(View.GONE);
        }else {
            searchView.setQueryHint("Search...");
            View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            v.setBackgroundColor(getResources().getColor(R.color.background_white));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    doSearch(newText);
                    return false;
                }
            });
        }
        adapter = new ItemAdapter(mActivity);
        indexableLayout.setAdapter(adapter);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //mItemList = new ArrayList<ZDelay>();
                        mItemList = databaseManager.getAllZDelay("");
                       /* if (mItemList != null && mItemList.size() > 0 && mItemList.get(0).getCountryCode() == 0) {
                            mItemList.remove(0);
                        }*/
                        adapter.setDatas(mItemList, new IndexableAdapter.IndexCallback<ZDelay>() {
                            @Override
                            public void onFinished(List<EntityWrapper<ZDelay>> datas) {
                                mProgressBar.setVisibility(View.GONE);
                                if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                                    initTagsView();
                                }
                                //initListWithGroup();
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

        //setTextSizeDelayTail();
        adapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<ZDelay>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, ZDelay entity) {
                if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                    //ZDelay zDelay = mDelays.get(pPosition);
                    entity.getDelayCode();
                    mZShorts = new ArrayList<>();
                    if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                        for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                            mZShorts.add(tagsEditText.getTags().get(i));
                        }
                    }
                    if(mZShorts.size()<MCCPilotLogConst.MAX_LENGTH_TAGS){
                        mZShorts.add(entity.getDelayCode() + " (" + entity.getDelayDD() + ")");
                    }
                    String[] mZShortArr = new String[mZShorts.size()];
                    mZShorts.toArray(mZShortArr);
                    tagsEditText.setTags(mZShortArr);
                }
            }
        });
    }

    public void doSearch(String pText) {
        String strSearch = pText.trim();
        if (strSearch.length() > 0) {
            List<ZDelay> listClone = new ArrayList<>();
            for (ZDelay zDelay : mItemList) {
                if (zDelay.getDelayDD().toLowerCase().contains(pText.toLowerCase())
                        || zDelay.getDelayShort().contains(pText)
                        || zDelay.getDelayLong().contains(pText)
                        || zDelay.getDelayGroup().contains(pText)
                        || zDelay.getDelayCode().toString().contains(pText)) {
                    listClone.add(zDelay);
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
                if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                    closeFragment();
                    return;
                }
                toggleMenu();
                break;

        }
    }

    public void refreshAdapter() {
      /*  if (mDelayTailsAdapter != null) {
            mDelayTailsAdapter.refreshDelayAdapter(mDelays);
            setTextSizeDelayTail();
        }*/
    }

    public void setTextSizeDelayTail() {
      /*  int size = 0;
        if (mDelays != null && !mDelays.isEmpty()) {
            size = mDelays.size();
        }
        mTvNumber.setText("" + size);*/
    }

    private void initTagsView() {
        lnTagBar.setVisibility(View.VISIBLE);
        List<String> listZShort = new ArrayList<>();
        for (int i = 0; i < mItemList.size(); i++) {
            listZShort.add(mItemList.get(i).getDelayCode() + " (" + mItemList.get(i).getDelayDD() + ")");
        }
        String[] arrayZShort = new String[listZShort.size()];
        arrayZShort = listZShort.toArray(arrayZShort);
        //tagsEditText.setArrayAutoComplete(arrayZShort);
        FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
        if (fragment != null) {
            String zCodes = fragment.getmZDelayCodes();
            if (!TextUtils.isEmpty(zCodes)) {
                String[] zCodeArray;
                zCodeArray = zCodes.split(MCCPilotLogConst.SPLIT_KEY);
                mZShorts = new ArrayList<>();
                for (int i = 0; i < zCodeArray.length; i++) {
                    ZDelay zDelay = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity).getZDelay(zCodeArray[i]);
                    if (zDelay != null) {
                        mZShorts.add(zDelay.getDelayCode() + " (" + zDelay.getDelayDD() + ")");
                    }
                }
                String[] mZShortArr = new String[mZShorts.size()];
                mZShorts.toArray(mZShortArr);
                tagsEditText.setTags(mZShortArr);
            }
        }
    }

    private void closeFragment() {
       /* AsyncTask.execute(new Runnable() {
            @Override
            public void run() {*/
        FlightAddsFragment fragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
        if (fragment != null) {

            if (!TextUtils.isEmpty(tagsEditText.getText().toString())) {
                mZCodes = new StringBuilder();
                for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                    if (!TextUtils.isEmpty(tagsEditText.getTags().get(i))) {
                        ZDelay zDelay = databaseManager.getInstance(mActivity)
                                .getZDelay(tagsEditText.getTags().get(i).split(" \\(")[0]);
                        if (zDelay != null) {
                            mZCodes.append(zDelay.getDelayCode());
                            mZCodes.append(MCCPilotLogConst.SPLIT_KEY_APPEND);
                        }
                    }
                }
                if (mZCodes.length() > 2) {
                    fragment.setmZDelayCodes(mZCodes.toString().substring(0, mZCodes.length() - 2));
                }
            } else {
                fragment.setmZDelayCodes("");
            }
        }
       /*     }
        });*/

        finishFragment();
    }


    private String getFullSectionNameByStartWord(String startWord, List<ZDelayGroup> zDelayGroups){
        //List<ZDelayGroup> zDelayGroups = databaseManager.getAllZDelayGroup();
        for(int i=0;i<zDelayGroups.size();i++){
            if(zDelayGroups.get(i).getDelayGroupName()!=null && zDelayGroups.get(i).getDelayGroupName().startsWith(startWord))
                return zDelayGroups.get(i).getDelayGroupName();
        }
        return "#";
    }


    @Override
    public void onKeyBackPress() {
        closeFragment();
        super.onKeyBackPress();
    }

    public class ItemAdapter extends IndexableAdapter<ZDelay> {
        private LayoutInflater mInflater;
        private int mSortType = 0;
        List<ZDelayGroup> zDelayGroups = DatabaseManager.getInstance(mActivity).getAllZDelayGroup();

        public ItemAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.item_index_title_recycler_view, parent, false);
            return new DelayListFragment.ItemAdapter.IndexViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.row_item_standar, parent, false);
            return new DelayListFragment.ItemAdapter.ContentViewHolder(view);
        }

        @Override
        public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
            DelayListFragment.ItemAdapter.IndexViewHolder vh = (DelayListFragment.ItemAdapter.IndexViewHolder) holder;
            vh.tv.setText(getFullSectionNameByStartWord(indexTitle,zDelayGroups));
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder viewHolder, final ZDelay entity) {
            final DelayListFragment.ItemAdapter.ContentViewHolder holder = (DelayListFragment.ItemAdapter.ContentViewHolder) viewHolder;
            holder.mItem = entity;
            holder.mTvTitle.setText(entity.getDelayCode() + " (" + entity.getDelayDD() + ")");
            holder.mTvDescription.setText(entity.getDelayShort());
            holder.mTvFootNote.setText(entity.getDelayLong());

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

            public ZDelay mItem;

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
