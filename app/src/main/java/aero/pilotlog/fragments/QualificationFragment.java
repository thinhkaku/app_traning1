package aero.pilotlog.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.QualificationAdapter;
import aero.pilotlog.databases.entities.Qualification;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class QualificationFragment extends BaseMCCFragment implements IAsyncTaskCallback {
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mHeaderTvTitle;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;

    @Bind(R.id.lvCertificate)
    ListView lvCertificate;
    @Bind(R.id.lvProficiency)
    ListView lvProficiency;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;

    private List<Qualification> mCertificateList;
    private List<Qualification> mProficiencyList;

    private QualificationAdapter mCertificateAdapter;
    private QualificationAdapter mProficiencyAdapter;

    private DatabaseManager mDatabaseManager;
    private LoadDataTask mLoadDataTask;



    public QualificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mCertificateList = mDatabaseManager.getCertificateRules();
        mProficiencyList = mDatabaseManager.getProficiencyRules();
    }

    @Override
    public void updateUI() {
        mTvNumber.setText(String.valueOf(mCertificateList.size() + mProficiencyList.size()));
    }

    @Override
    public void end() {
        if (mCertificateList != null) {
            mCertificateAdapter = new QualificationAdapter(mActivity, mCertificateList);
            mCertificateAdapter.setQualificationType(QualificationAdapter.QualificationType.CERTIFICATE);
            lvCertificate.setAdapter(mCertificateAdapter);
            lvCertificate.setFastScrollEnabled(true);
        }
        if (mProficiencyList != null) {
            mProficiencyAdapter = new QualificationAdapter(mActivity, mProficiencyList);
            mProficiencyAdapter.setQualificationType(QualificationAdapter.QualificationType.PROFICIENCY);
            lvProficiency.setAdapter(mProficiencyAdapter);
            lvProficiency.setFastScrollEnabled(true);
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListViewHeightBasedOnChildren(lvProficiency);
                setListViewHeightBasedOnChildren(lvCertificate);
            }
        });
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_qualification;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);

        initView();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void initView() {
        /*Set header view*/
        if (mHeaderTvTitle != null) {
            mHeaderTvTitle.setText("Qualifications");
        }
    }

    @Nullable
    @OnClick({R.id.ibMenu,R.id.btn_insertCertificate,R.id.btn_insertProficiency})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu: // Open sliding menu
                toggleMenu();
                break;
            case R.id.btn_insertCertificate:
                mDatabaseManager.insertQualificationRulesSample(true);
                mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
                mLoadDataTask.execute();
                break;
            case R.id.btn_insertProficiency:
                mDatabaseManager.insertProficiencyRulesSample();
                mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
                mLoadDataTask.execute();
                break;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
          /*  TextView tvDescription2 = (TextView) listItem.findViewById(R.id.tv_description_2);
            if(tvDescription2!=null)*/
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
