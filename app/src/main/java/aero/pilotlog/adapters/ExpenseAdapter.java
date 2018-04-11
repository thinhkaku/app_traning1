package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.Expense;
import aero.pilotlog.databases.entities.ZCurrency;
import aero.pilotlog.databases.entities.ZExpense;
import aero.pilotlog.databases.entities.ZExpenseGroup;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ISwipeLayoutExpenseCallback;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.widgets.swipelayout.SimpleSwipeListener;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.adapters.BaseSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by phuc.dd on 8/2/2017.
 */

public class ExpenseAdapter extends BaseSwipeAdapter {
    private List<Expense> mList;
    private Context mContext;
    private boolean isEnableItemClick = true;
    private static final long DELAY_ENABLE_CLICK = 100;
    private String mTimeInDecimal;
    private DatabaseManager mDbManager;
    private SwipeLayout mSwipeLayout;
    public boolean isOpenSwipe() {
        return mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Open;
    }
    public void setSwipeLayoutCallBack(ISwipeLayoutExpenseCallback mSwipeLayoutCallBack) {
        this.mSwipeLayoutCallBack = mSwipeLayoutCallBack;
    }

    private ISwipeLayoutExpenseCallback mSwipeLayoutCallBack;


    public boolean isEnableClick() {
        return isEnableItemClick;
    }

    public ExpenseAdapter(Context pContext, List<Expense> pList) {
        this.mContext = pContext;
        this.mList = pList;
        DatabaseManager mDatabaseManager = DatabaseManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        if (mList.size() > 0)
            return mList.get(i);
        else return null;
    }

    @Override
    public long getItemId(int i) {
        return mList.get(i).hashCode();
    }

    @Override
    public void closeAllItems(boolean smooth) {

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    protected String getStringItem(int pPosition) {
        return "";
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    public void closeSwipe(boolean pSmooth) {
        if (mSwipeLayout != null) {
            mSwipeLayout.close(pSmooth);
            mSwipeLayout = null;
        }
    }

    public boolean getSwipeLayoutShown() {
        return mSwipeLayout != null && mSwipeLayout.isShown();
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_item_expense, parent, false);

        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        mDbManager = new DatabaseManager(mContext);
        Expense expense;
        expense = (Expense) getItem(position);
        if (expense != null) {
            final ViewHolder viewHolder = new ViewHolder(convertView, expense, position);
            String strCurrentDate = expense.getExpDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = new Date();
            try {
                newDate = format.parse(strCurrentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.tvDate.setText(DateTimeUtils.formatDateToString(newDate));
            int etCode = expense.getETCode();
            ZExpense zExpense = mDbManager.getZExpenseByCode(etCode);
            if(zExpense!=null){
                ZExpenseGroup zExpenseGroup = mDbManager.getZExpenseGroupByCode(zExpense.getExpGroupCode());
                if(zExpenseGroup!=null){
                    viewHolder.tvType.setText(zExpenseGroup.getExpGroupShort() + " - " + zExpense.getExpTypeShort());
                    viewHolder.tvExpenseTypeLong.setText(zExpense.getExpTypeLong());
                }
                int currencyCode = expense.getCurrCode();
                ZCurrency zCurrency = mDbManager.getZCurrencyByCode(currencyCode);
                if(zCurrency!=null){
                    if(zExpense.getDebit()!=null && zExpense.getDebit()){
                        viewHolder.tvAmount.setText("- " + (double)expense.getAmount()/100 + " " + zCurrency.getCurrShort());
                        viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.color_red));
                    }else {
                        viewHolder.tvAmount.setText("+ " + (double)expense.getAmount()/100 + " " + zCurrency.getCurrShort());
                        viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.mcc_green_credit));
                    }

                }

            }

            viewHolder.tvNote.setText(expense.getDescription());

            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.setSwipeEnabled(true);//In select mode then disable swipe layout

            swipeLayout.setClickToClose(true);
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    mSwipeLayout = layout;
                }

                @Override
                public void onHandRelease(final SwipeLayout layout, float xvel, float yvel) {
                    isEnableItemClick = false;
                    layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isEnableItemClick = true;
                        }
                    }, DELAY_ENABLE_CLICK);
                    super.onHandRelease(layout, xvel, yvel);
                }
            });
        }

    }

    public void refreshAdapter(List<Expense> pList) {
        this.mList = pList;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        @Bind(R.id.tv_expense_date)
        TextView tvDate;
        @Bind(R.id.tv_expense_type)
        TextView tvType;
        @Bind(R.id.tv_expense_amount)
        TextView tvAmount;
        @Bind(R.id.tv_expense_note)
        TextView tvNote;
        @Bind(R.id.tv_expense_type_long)
        TextView tvExpenseTypeLong;
        @Bind(R.id.rlFront)
        LinearLayout rlFront;
        @Bind(R.id.lnFlight)
        LinearLayout lnFlight;

        private final Expense mExpense;
        private int mIndex;


        public ViewHolder(View pView, Expense expense, int pPosition) {
            ButterKnife.bind(this, pView);
            this.mExpense = expense;
            this.mIndex = pPosition;
           /* if (mTypeScreen == MCCPilotLogConst.BUTTON_DUTY) {
                mBtnItemSelect.setVisibility(View.VISIBLE);
            }*/
        }

        @OnClick({R.id.btnDelete})
        public void onClick(View pView) {
            switch (pView.getId()) {
                case R.id.btnDelete:
                    if (mSwipeLayoutCallBack != null) {
                        mSwipeLayoutCallBack.onDeleteRecord(pView, mExpense, mIndex);
                    }
                    break;
            }
        }

    }

}
