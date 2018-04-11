package aero.pilotlog.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.LocationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tuan.pv on 11/3/2015.
 * Airfield globe
 */
public class AirfieldGlobeFragment extends BaseMCCFragment {

    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvAirfieldGlobeName)
    TextView mTvAirfieldGlobeName;
    @Bind(R.id.tvAirfieldGlobeCountry)
    TextView mTvAirfieldGlobeCountry;
    @Bind(R.id.tvAirfieldGlobeLongLat)
    TextView mTvAirfieldGlobeLongLat;
    @Bind(R.id.ivGlobe)
    ImageView mIvGlobe;

    private RelativeLayout mRlGlobe;
    private ImageView mIvRedDot;

    enum GlobePart {
        GLOBE_EUROPE, GLOBE_SOUTH_AMERICA, GLOBE_NORTH_AMERICA, GLOBE_AUSTRALIA,
        GLOBE_ASIA, GLOBE_AFRICA
    }

    private GlobePart mGlobeUsed;
    private int width;


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfield_globe;
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

    @OnClick(R.id.rlBackIcon)
    public void onClick() {
        finishFragment();
    }

    private void initView() {
        //Only calculate position follow only one size screen. It's 720x1280, and then capture view to use for all screen (480X800, 540X960, 1080X1920...)
        width = 720;
        mRlGlobe = new RelativeLayout(mActivity);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.width = 720;
        rlp.height = (int) (720 * 1.422);
        rlp.setMargins(0, 30, 0, 0);
        mRlGlobe.setLayoutParams(rlp);
        mIvRedDot = new ImageView(mActivity);
        mIvRedDot.setImageResource(R.drawable.red_dot);

        mIbLeft.setVisibility(View.VISIBLE);
        mIbMenu.setVisibility(View.GONE);
        DatabaseManager mDatabaseManager = new DatabaseManager(mActivity);
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] airfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
            if(airfieldCode!=null){
                Airfield airfield = mDatabaseManager.getAirfieldByCode(airfieldCode);
                if (airfield != null) {
                    String strIcaoIata = airfield.getAFICAO() + (TextUtils.isEmpty(airfield.getAFIATA()) ?
                            MCCPilotLogConst.STRING_EMPTY : " - ") + airfield.getAFIATA();
                    String strLong, strLat, strLongLat;
                    int longitude = airfield.getLongitude();
                    int latitude = airfield.getLatitude();
                    strLong = LocationUtils.getLongitudeString(String.valueOf(airfield.getLongitude()));
                    strLat = LocationUtils.getLatitudeString(String.valueOf(airfield.getLatitude()));
                    if (TextUtils.isEmpty(strLat) && TextUtils.isEmpty(strLong)) {
                        strLongLat = "";
                    } else if (TextUtils.isEmpty(strLat)) {
                        strLongLat = strLong;
                    } else if (TextUtils.isEmpty(strLong)) {
                        strLongLat = strLat;
                    } else {
                        strLongLat = strLat + " - " + strLong;
                    }
                    mTvTitle.setText(strIcaoIata);
                    mTvAirfieldGlobeName.setText(airfield.getAFName());
                    mTvAirfieldGlobeLongLat.setText(strLongLat);
                    ZCountry country = mDatabaseManager.getCountryByCode(airfield.getAFCountry());
                    if (country != null) {
                        String countryName = country.getCountryName();
                        mTvAirfieldGlobeCountry.setText(countryName);

                        setGlobeImage(airfield.getLatitude(), country, airfield.getLongitude());
                        setRedDot(airfield);
                    }
                }
                //Convert view to bitmap and set image bitmap for image view
                if (mIvRedDot != null) {
                    mRlGlobe.addView(mIvRedDot);
                }
                mRlGlobe.setDrawingCacheEnabled(true);
                // this is the important code :)
                // Without it the view will have a dimension of 0,0 and the bitmap will be null
                mRlGlobe.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mRlGlobe.layout(0, 0, mRlGlobe.getMeasuredWidth(), mRlGlobe.getMeasuredHeight());

                mRlGlobe.buildDrawingCache(true);
                if (mRlGlobe.getDrawingCache() != null) {
                    Bitmap mBitmap = Bitmap.createBitmap(mRlGlobe.getDrawingCache());
                    mRlGlobe.setDrawingCacheEnabled(false); // clear drawing cache

                    if (mBitmap != null) {
                        mIvGlobe.setImageBitmap(mBitmap);
                        //keep part code bellow, maybe will use it later.
                        //scale if widthscreen > 720
//                int widthReal = isTablet() ? MCCPilotLogConst.sScreenWidth / 3 : MCCPilotLogConst.sScreenWidth;
//                if (widthReal > 720) {
//                    float scale = (float) widthReal / 720;
//                    mIvGlobe.setScaleX(scale);
//                    mIvGlobe.setScaleY(scale);
//                }
                    }
                }
            }

        }
    }

    /**
     * set globe image for each airfield
     *
     * @param latitude
     * @param country
     * @param longitude
     */
    private void setGlobeImage(int latitude, ZCountry country, int longitude) {
     /*   if (!TextUtils.isEmpty(latitude)) {
            latitude = latitude.replace(",", ".");
        }
        if (!TextUtils.isEmpty(longitude)) {
            longitude = longitude.replace(",", ".");
        }*/
        String code = country.getPrefixICAO();
        if (!TextUtils.isEmpty(code)) {
            code = code.toUpperCase();
            if (code.equals("G")) {
                mGlobeUsed = GlobePart.GLOBE_AFRICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_africa);
            } else if (code.equals("E") || code.equals("L")) {
                mGlobeUsed = GlobePart.GLOBE_EUROPE;
                mRlGlobe.setBackgroundResource(R.drawable.globe_europe);
            } else if (code.equals("C") || code.equals("K")) {
                mGlobeUsed = GlobePart.GLOBE_NORTH_AMERICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_north_america);
            } else if (code.equals("S")) {
                mGlobeUsed = GlobePart.GLOBE_SOUTH_AMERICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_south_america);
            } else if (code.equals("D") || code.equals("H") || code.equals("F")) {
                mGlobeUsed = GlobePart.GLOBE_AFRICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_africa);
            } else if (code.equals("O") || code.equals("U")) {
                int cCode = country.getCountryCode();
                switch (cCode) {
                    case 1:
                    case 165:
                        mGlobeUsed = GlobePart.GLOBE_ASIA;
                        mRlGlobe.setBackgroundResource(R.drawable.globe_asia);
                        break;
                    case 15:
                    case 20:
                    case 80:
                    case 228:
                        mGlobeUsed = GlobePart.GLOBE_EUROPE;
                        mRlGlobe.setBackgroundResource(R.drawable.globe_europe);
                        break;
                    case 164:
                    case 178:
                    //JIRA PL-623
                    case 189:
                    //End JIRA PL-623
                    case 229:
                    case 243:
                        mGlobeUsed = GlobePart.GLOBE_AFRICA;
                        mRlGlobe.setBackgroundResource(R.drawable.globe_africa);
                        break;
                    default:
                        if (Math.abs((float)(longitude) / 1000.0) > 60.0) {
                            mGlobeUsed = GlobePart.GLOBE_ASIA;
                            mRlGlobe.setBackgroundResource(R.drawable.globe_asia);
                        } else {
                            mGlobeUsed = GlobePart.GLOBE_EUROPE;
                            mRlGlobe.setBackgroundResource(R.drawable.globe_europe);
                        }
                        break;
                }
            } else if (code.equals("B")) {
                mGlobeUsed = GlobePart.GLOBE_EUROPE;
                mRlGlobe.setBackgroundResource(R.drawable.globe_europe);
                if (Math.abs((float)(longitude) / 1000.0) > 45.0) {
                    mGlobeUsed = GlobePart.GLOBE_NORTH_AMERICA;
                    mRlGlobe.setBackgroundResource(R.drawable.globe_north_america);
                }
            } else if (code.equals("M") || code.equals("T")) {
                mGlobeUsed = GlobePart.GLOBE_NORTH_AMERICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_north_america);
                if (Math.abs((float)(latitude) / 1000.0) < 10.0) {
                    mGlobeUsed = GlobePart.GLOBE_SOUTH_AMERICA;
                    mRlGlobe.setBackgroundResource(R.drawable.globe_south_america);
                }
                if (Math.abs((float)(longitude) / 1000.0) < 70.0) {
                    mGlobeUsed = GlobePart.GLOBE_SOUTH_AMERICA;
                    mRlGlobe.setBackgroundResource(R.drawable.globe_south_america);
                }
            } else if (code.equals("W") || code.equals("P")) {
                mGlobeUsed = GlobePart.GLOBE_NORTH_AMERICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_north_america);
                if (Math.abs((float)(latitude) / 1000.0) < 0.0 && Math.abs((float)(longitude) / 1000.0) > 110.0) {
                    mGlobeUsed = GlobePart.GLOBE_AUSTRALIA;
                    mRlGlobe.setBackgroundResource(R.drawable.globe_australia);
                }
            } else if (code.equals("Z") || code.equals("V") || code.equals("R")) {
                mGlobeUsed = GlobePart.GLOBE_ASIA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_asia);
            } else if (code.equals("Y") || code.equals("A") || code.equals("N")) {
                mGlobeUsed = GlobePart.GLOBE_AUSTRALIA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_australia);
            }
        } else {
            String codeContinent = country.getContinent();
            if (codeContinent.equalsIgnoreCase("AF")) {
                mGlobeUsed = GlobePart.GLOBE_AFRICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_africa);
            } else if (codeContinent.equalsIgnoreCase("EU")) {
                mGlobeUsed = GlobePart.GLOBE_EUROPE;
                mRlGlobe.setBackgroundResource(R.drawable.globe_europe);
            } else if (codeContinent.equalsIgnoreCase("NA")) {
                mGlobeUsed = GlobePart.GLOBE_NORTH_AMERICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_north_america);
            } else if (codeContinent.equalsIgnoreCase("SA")) {
                mGlobeUsed = GlobePart.GLOBE_SOUTH_AMERICA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_south_america);
            } else if (codeContinent.equalsIgnoreCase("AS")) {
                mGlobeUsed = GlobePart.GLOBE_ASIA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_asia);
            } else if (codeContinent.equalsIgnoreCase("OC")) {
                mGlobeUsed = GlobePart.GLOBE_AUSTRALIA;
                mRlGlobe.setBackgroundResource(R.drawable.globe_australia);
            } else {
                int cCode = country.getCountryCode();
                switch (cCode) {
                    case 1:
                    case 165:
                        mGlobeUsed = GlobePart.GLOBE_ASIA;
                        mRlGlobe.setBackgroundResource(R.drawable.globe_asia);
                        break;
                    case 15:
                    case 20:
                    case 80:
                    case 228:
                        mGlobeUsed = GlobePart.GLOBE_EUROPE;
                        mRlGlobe.setBackgroundResource(R.drawable.globe_europe);
                        break;
                    case 164:
                    case 178:
                    case 229:
                    case 243:
                        mGlobeUsed = GlobePart.GLOBE_AFRICA;
                        mRlGlobe.setBackgroundResource(R.drawable.globe_africa);
                        break;
                    default:
                        if (Math.abs((float)(longitude) / 1000.0) > 60.0) {
                            mGlobeUsed = GlobePart.GLOBE_ASIA;
                            mRlGlobe.setBackgroundResource(R.drawable.globe_asia);
                        } else {
                            mGlobeUsed = GlobePart.GLOBE_EUROPE;
                            mRlGlobe.setBackgroundResource(R.drawable.globe_europe);
                        }
                        break;
                }
            }
        }
    }

    /**
     * calculate position and set for red dot
     * default size standard: width= 720, height = 1280
     *
     * @param airfield
     */
    private void setRedDot(Airfield airfield) {
        float holdAFLong;
        String strAFIcao = airfield.getAFICAO();
        if (!TextUtils.isEmpty(strAFIcao) && strAFIcao.equalsIgnoreCase("PHIK") || strAFIcao.equalsIgnoreCase("PHNL")) {
            mIvRedDot = null;
            return;
        }
        int strLongitude, strLatitude;
        strLongitude = airfield.getLongitude();
        strLatitude = airfield.getLatitude();
        if (strLatitude==0 && strLongitude == 0) {
            mIvRedDot = null;
            return;
        }
        try {
            holdAFLong = (float) (18.0 - strLongitude / 10000.0);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            holdAFLong = 18f;
        }

        float holdAFLat;
        try {
            holdAFLat = (float) (9.0 - strLatitude / 10000.0);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            holdAFLat = 9f;
        }
        //left
        int avLeftUp;
        int avTopUp;
        int avLeftLow;
        int avTopLow;
        int p1Left, p1Top, p2Left, p2Top;
        int[] arr = getCoordinatesForLatRow((int) holdAFLat, (int) holdAFLong);

        if (arr != null) {
            p1Left = arr[0];
            p1Top = arr[1];
            p2Left = arr[2];
            p2Top = arr[3];
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(25, 25);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            mIvRedDot.setLayoutParams(lp);
            return;
        }
        avLeftUp = (int) (p1Left + (p2Left - p1Left) * (holdAFLong - (int) holdAFLong));
        avTopUp = (int) (p1Top + (p2Top - p1Top) * (holdAFLong - (int) holdAFLong));

        arr = getCoordinatesForLatRow((int) holdAFLat + 1, (int) holdAFLong);

        if (arr != null) {
            p1Left = arr[0];
            p1Top = arr[1];
            p2Left = arr[2];
            p2Top = arr[3];
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(25, 25);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            mIvRedDot.setLayoutParams(lp);
            return;
        }

        avLeftLow = (int) (p1Left + (p2Left - p1Left) * (holdAFLong - (int) holdAFLong));
        avTopLow = (int) (p1Top + (p2Top - p1Top) * (holdAFLong - (int) holdAFLong));

        int left = (int) (avLeftUp + (avLeftLow - avLeftUp) * (holdAFLat - (int) holdAFLat));
        int top = (int) (avTopUp + (avTopLow - avTopUp) * (holdAFLat - (int) holdAFLat));

        if (mGlobeUsed == GlobePart.GLOBE_AUSTRALIA) {
            left = left + 10;//
            top = top + 80;  //
        }

        if (!TextUtils.isEmpty(strAFIcao) && mGlobeUsed == GlobePart.GLOBE_EUROPE || strAFIcao.equalsIgnoreCase("GCLA")) {
            top = top + 80;
            left = left - 30;
        }

        if (mGlobeUsed == GlobePart.GLOBE_ASIA) {
            top = top + 70;
        }
        if (mGlobeUsed == GlobePart.GLOBE_SOUTH_AMERICA) {
            top = top + 90;
            left = left - 10;
        }

        if (mGlobeUsed == GlobePart.GLOBE_NORTH_AMERICA) {
            top = top + 120;
            left = left - 50;
        }
        //720x1280
        if (mGlobeUsed == GlobePart.GLOBE_AFRICA/* && height > 1150*/) {
            if (mGlobeUsed == GlobePart.GLOBE_AFRICA && top < 680) {
                top = top + 30;
                left = left + 10;
            } else if (mGlobeUsed == GlobePart.GLOBE_AFRICA && top > 680 && top < 1000) {
                top = top + 90;
                left = left - 30;
            } else if (mGlobeUsed == GlobePart.GLOBE_AFRICA && top > 1700 && top < 2400) {
                top = top + 50;
                left = left - 40;
            } else if (mGlobeUsed == GlobePart.GLOBE_AFRICA && top > 2400) {
                top = top + 40;
                left = left - 160;
            } else if (mGlobeUsed == GlobePart.GLOBE_AFRICA && top > 1500 && left > 1000) {
                top = top + 40;
                left = left - 10;
            } else if (mGlobeUsed == GlobePart.GLOBE_AFRICA && top < 1500) {
                top = top + 60;
                left = left - 1;
            }
        }
        if (!TextUtils.isEmpty(strAFIcao) && strAFIcao.equalsIgnoreCase("KMIA") || strAFIcao.equalsIgnoreCase("KJFK") || strAFIcao.equalsIgnoreCase("MMUN")) {
            left = left + 40;
        }
        if (!TextUtils.isEmpty(strAFIcao) && strAFIcao.equalsIgnoreCase("GCLP")) {
            left = left - 150;
        }
        //260 = globe width
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(25, 25);
        //25 + left 110+ top 320x480
        if (width == 540)
            lp.leftMargin = 25 + (left * 470 / 3000);//540
        else if (width == 480 || width == 600)
            lp.leftMargin = 7 + (left * 430 / 3000);//480
        else if (width > 700)
            lp.leftMargin = 25 + (left * 640 / 3000);//720
        else if (isTablet() && width == 640) {
            lp.leftMargin = 45 + (left * 550 / 3000);//
        } else //720
            lp.leftMargin = 25 + (left * 260 / 3000);//normal

        lp.topMargin = 200 + (top * 645 / 3000);//
        mIvRedDot.setLayoutParams(lp);
    }

    /**
     * Calculate array int coordinate
     *
     * @param pLatRow
     * @param pLongCol
     * @return
     */
    private int[] getCoordinatesForLatRow(int pLatRow, int pLongCol) {
        int matrixOffset = 0;

        int[] locLeft = null;
        int[] locTop = null;
        if (mGlobeUsed == GlobePart.GLOBE_EUROPE) {
            matrixOffset = 13;

            if (pLatRow == 1) {
                locLeft = new int[]{1215, 1265, 1285, 1305, 1315, 1355, 1405, 1480, 1550, 1615, 1670, 1730};
                locTop = new int[]{375, 430, 455, 465, 490, 500, 505, 525, 515, 535, 520, 490};
            }
            if (pLatRow == 2) {
                locLeft = new int[]{1035, 1080, 1105, 1140, 1195, 1240, 1325, 1430, 1535, 1660, 1775, 1895};
                locTop = new int[]{445, 510, 555, 580, 615, 655, 695, 715, 720, 705, 700, 685};

            }
            if (pLatRow == 3) {
                locLeft = new int[]{720, 780, 825, 895, 1005, 1105, 1255, 1395, 1550, 1730, 1890, 2035};
                locTop = new int[]{590, 660, 725, 770, 845, 900, 965, 995, 1005, 1005, 985, 930};
            }
            if (pLatRow == 4) {
                locLeft = new int[]{0, 0, 620, 710, 855, 990, 1190, 1360, 1565, 1770, 1965, 2150};
                locTop = new int[]{0, 0, 945, 1025, 1120, 1205, 1260, 1295, 1305, 1305, 1270, 1210};
            }
            if (pLatRow == 5) {
                locLeft = new int[]{0, 0, 450, 560, 730, 900, 1135, 1335, 1575, 1800, 2030, 2250};
                locTop = new int[]{0, 0, 1185, 1290, 1375, 1460, 1540, 1580, 1605, 1590, 1560, 1480};
            }
            if (pLatRow == 6) {
                locLeft = new int[]{0, 0, 310, 445, 600, 800, 1060, 1305, 1590, 1815, 2075, 2295};
                locTop = new int[]{0, 0, 1450, 1600, 1695, 1795, 1860, 1930, 1945, 1920, 1855, 1770};
            }
            if (pLatRow == 7) {
                locLeft = new int[]{0, 0, 0, 530, 815, 1000, 1225, 1465, 1705, 1885, 2100, 2300};
                locTop = new int[]{0, 0, 0, 548, 515, 548, 565, 565, 582, 582, 598, 615};
            }
            if (pLatRow == 8) {
                locLeft = new int[]{0, 0, 0, 540, 750, 960, 1205, 1465, 1730, 1910, 2160, 2400};
                locTop = new int[]{0, 0, 0, 720, 720, 720, 720, 720, 720, 720, 720, 720};
            }
        }

        if (mGlobeUsed == GlobePart.GLOBE_ASIA) {
            matrixOffset = 24;

            if (pLatRow == 2) {
                locLeft = new int[]{1155, 1275, 1390, 1445, 1505, 1585, 1665, 1760, 1845, 1920, 1945};
                locTop = new int[]{520, 545, 545, 545, 545, 555, 525, 515, 495, 475, 455};
            }
            if (pLatRow == 3) {
                locLeft = new int[]{1015, 1175, 1330, 1425, 1505, 1620, 1745, 1880, 2020, 2105, 2175};
                locTop = new int[]{645, 670, 675, 680, 680, 680, 665, 645, 610, 580, 555};
            }
            if (pLatRow == 4) {
                locLeft = new int[]{855, 1060, 1250, 1385, 1505, 1665, 1865, 2020, 2200, 2300, 2420};
                locTop = new int[]{800, 860, 875, 900, 895, 890, 860, 815, 775, 715, 665};
            }
            if (pLatRow == 5) {
                locLeft = new int[]{655, 900, 1130, 1320, 1505, 1730, 2005, 2165, 2335, 2495, 2575};
                locTop = new int[]{1065, 1140, 1175, 1195, 1205, 1180, 1115, 1035, 965, 880, 780};
            }
            if (pLatRow == 6) {
                locLeft = new int[]{500, 780, 1020, 1260, 1505, 1780, 2075, 2305, 2495, 2645};
                locTop = new int[]{1295, 1420, 1475, 1500, 1485, 1475, 1435, 1310, 1230, 1040};
            }
            if (pLatRow == 7) {
                locLeft = new int[]{415, 700, 960, 1230, 1505, 1810, 2095, 2375, 2575, 2650};
                locTop = new int[]{1505, 1645, 1710, 1750, 1745, 1720, 1675, 1535, 1400, 1300};
            }
            if (pLatRow == 8) {
                locLeft = new int[]{360, 635, 905, 1195, 1505, 1840, 2135, 2430, 2530, 2630};
                locTop = new int[]{1775, 1900, 1970, 2030, 2015, 1980, 1945, 1900, 1865, 1835};
            }
            if (pLatRow == 9) {
                locLeft = new int[]{330, 590, 870, 1165, 1505, 1850, 2165, 2490, 2710};
                locTop = new int[]{1995, 2165, 2245, 2305, 2315, 2285, 2215, 2110, 1670};
            }
            if (pLatRow == 10 || pLatRow == 11) {
                locLeft = new int[]{0, 0, 0, 0, 0, 1510, 1850, 2160};
                locTop = new int[]{0, 0, 0, 0, 0, 2565, 2560, 2470};
            }
        }

        if (mGlobeUsed == GlobePart.GLOBE_NORTH_AMERICA) {
            matrixOffset = 2;
            if (pLatRow == 1) {
                locLeft = new int[]{1275, 1320, 1360, 1380, 1440, 1500, 1560, 1585, 1630, 1660, 1730, 1815, 1860};
                locTop = new int[]{410, 445, 470, 480, 490, 490, 495, 490, 490, 485, 485, 480, 470};
            }
            if (pLatRow == 2) {
                locLeft = new int[]{1035, 1105, 1155, 1215, 1309, 1420, 1505, 1595, 1690, 1755, 1865, 1955, 2010};
                locTop = new int[]{480, 545, 600, 640, 675, 670, 680, 665, 670, 670, 630, 600, 555};
            }
            if (pLatRow == 3) {
                locLeft = new int[]{745, 825, 920, 1045, 1175, 1330, 1475, 1620, 1745, 1860, 2000, 2125, 2230};
                locTop = new int[]{645, 740, 830, 870, 910, 935, 950, 930, 915, 885, 815, 775, 720};
            }
            if (pLatRow == 4) {
                locLeft = new int[]{565, 650, 775, 910, 1080, 1255, 1435, 1645, 1820, 1985, 2165, 2315, 2430};
                locTop = new int[]{795, 905, 1035, 1120, 1195, 1235, 1245, 1245, 1210, 1170, 1100, 1035, 950};
            }
            if (pLatRow == 5) {
                locLeft = new int[]{0, 515, 650, 805, 1000, 1185, 1405, 1670, 1895, 2100, 2290, 2465, 2585};
                locTop = new int[]{0, 1055, 1210, 1340, 1475, 1550, 1595, 1590, 1560, 1485, 1410, 1315, 1160};
            }
            if (pLatRow == 6) {
                locLeft = new int[]{0, 380, 515, 690, 915, 1125, 1405, 1675, 1920, 2160, 2385};
                locTop = new int[]{0, 1240, 1420, 1600, 1775, 1860, 1905, 1880, 1845, 1750, 1660};
            }
            if (pLatRow == 7) {
                locLeft = new int[]{0, 0, 0, 585, 830, 1090, 1405, 1670, 1935, 2200, 2445};
                locTop = new int[]{0, 0, 0, 1850, 2050, 2145, 2165, 2165, 2115, 2040, 1920};
            }
            if (pLatRow == 8) {
                locLeft = new int[]{0, 0, 0, 510, 780, 1065, 1410, 1665, 1945, 2220, 2470};
                locTop = new int[]{0, 0, 0, 2110, 2295, 2405, 2450, 2450, 2415, 2320, 2185};
            }
            if (pLatRow == 9) {
                locLeft = new int[]{0, 0, 0, 510, 780, 1065, 1410, 1665, 1945, 2220, 2470};
                locTop = new int[]{0, 0, 0, 2110, 2295, 2405, 2450, 2450, 2415, 2320, 2185};
            }
        }

        if (mGlobeUsed == GlobePart.GLOBE_SOUTH_AMERICA) {
            matrixOffset = 9;
            if (pLatRow == 7) {
                locLeft = new int[]{660, 770, 1070, 1505, 1920, 2185, 0};
                locTop = new int[]{575, 495, 460, 405, 405, 430, 0};
            }
            if (pLatRow == 8) {
                locLeft = new int[]{620, 800, 1095, 1505, 1895, 2175, 2390};
                locTop = new int[]{755, 735, 705, 660, 650, 620, 645};
            }
            if (pLatRow == 9) {
                locLeft = new int[]{605, 840, 1135, 1505, 1875, 2155, 2395};
                locTop = new int[]{1055, 1040, 1010, 975, 945, 925, 910};
            }
            if (pLatRow == 10) {
                locLeft = new int[]{640, 885, 1170, 1505, 1855, 2125, 2370};
                locTop = new int[]{1295, 1295, 1295, 1295, 1295, 1295, 1295};
            }
            if (pLatRow == 11) {
                locLeft = new int[]{0, 935, 1190, 1505, 1830, 2100, 2335};
                locTop = new int[]{0, 1615, 1615, 1615, 1615, 1615, 1615};
            }
            if (pLatRow == 12) {
                locLeft = new int[]{0, 975, 1235, 1505, 1805, 2080, 2270};
                locTop = new int[]{0, 1910, 1910, 1910, 1910, 1910, 1910};
            }
            if (pLatRow == 13) {
                locLeft = new int[]{0, 1030, 1270, 1505, 1748, 2030, 2225};
                locTop = new int[]{0, 2255, 2255, 2255, 2255, 2255, 2255};
            }
            if (pLatRow == 14) {
                locLeft = new int[]{0, 1070, 1300, 1505, 1765, 1955, 2170};
                locTop = new int[]{0, 2510, 2510, 2510, 2510, 2510, 2510};
            }
            if (pLatRow == 15) {
                locLeft = new int[]{0, 1180, 1315, 1490, 1700};
                locTop = new int[]{0, 2675, 2670, 2670, 2675};
            }
        }

        if (mGlobeUsed == GlobePart.GLOBE_AFRICA) {
            matrixOffset = 15;
            if (pLatRow == 5) {
                locLeft = new int[]{0, 530, 815, 1000, 1225, 1465, 1705, 1900, 2085, 2275};
                locTop = new int[]{0, 548, 515, 548, 565, 565, 582, 510, 525, 530};
            }
            if (pLatRow == 6) {
                locLeft = new int[]{455, 540, 750, 960, 1205, 1465, 1730, 1965, 2220, 2425};
                locTop = new int[]{725, 720, 700, 690, 690, 690, 700, 705, 745, 775};
            }
            if (pLatRow == 7) {
                locLeft = new int[]{310, 400, 660, 900, 1175, 1465, 1750, 2055, 2295, 2490};
                locTop = new int[]{1005, 1008, 1008, 995, 985, 985, 995, 1008, 1008, 1035};
            }
            if (pLatRow == 8) {
                locLeft = new int[]{225, 330, 570, 825, 1145, 1465, 1765, 2085, 2355, 2560};
                locTop = new int[]{1320, 1320, 1320, 1320, 1320, 1320, 1320, 1320, 1320, 1320};
            }
            if (pLatRow == 9) {
                locLeft = new int[]{190, 305, 480, 755, 1110, 1465, 1795, 2115, 2370, 2600};
                locTop = new int[]{1685, 1685, 1685, 1685, 1685, 1685, 1685, 1685, 1685, 1685};
            }
            if (pLatRow == 10) {
                locLeft = new int[]{0, 345, 505, 760, 1110, 1465, 1805, 2060, 2355, 2555};
                locTop = new int[]{0, 1965, 1965, 1965, 1965, 1965, 1965, 1965, 1965, 1965};
            }
            if (pLatRow == 11) {
                locLeft = new int[]{0, 420, 540, 795, 1120, 1465, 1825, 2085, 2295, 2460};
                locTop = new int[]{0, 2265, 2265, 2265, 2265, 2265, 2265, 2265, 2265, 2265};
            }
            if (pLatRow == 12) {
                locLeft = new int[]{0, 545, 640, 865, 1140, 1465, 1835, 2040, 2220, 2315};
                locTop = new int[]{0, 2525, 2525, 2525, 2525, 2525, 2525, 2525, 2525, 2525};
            }
            if (pLatRow == 13) {
                locLeft = new int[]{0, 0, 1185, 1465, 1805, 2035};
                locTop = new int[]{0, 0, 2715, 2715, 2715, 2715};
            }
        }

        if (mGlobeUsed == GlobePart.GLOBE_AUSTRALIA) {
            matrixOffset = 29;
            if (pLongCol < 5) {
                pLongCol = pLongCol + 36;
            }
            if (pLatRow == 9) {
                locLeft = new int[]{610, 810, 1100, 1402, 1670, 1885, 2150, 2390, 2575, 2715};
                locTop = new int[]{795, 715, 640, 605, 605, 625, 680, 735, 780, 845};
            }
            if (pLatRow == 10) {
                locLeft = new int[]{600, 820, 1100, 1402, 1675, 1885, 2135, 2365, 2550, 2695};
                locTop = new int[]{1000, 940, 885, 855, 890, 925, 980, 1030, 1080, 1125};
            }
            if (pLatRow == 11) {
                locLeft = new int[]{605, 850, 1120, 1402, 1670, 1885, 2135, 2355, 2535, 2670};
                locTop = new int[]{1295, 1235, 1200, 1190, 1215, 1240, 1265, 1290, 1315, 1345};
            }
            if (pLatRow == 12) {
                locLeft = new int[]{640, 895, 1135, 1408, 1670, 1875, 2115, 2330, 2515, 2635};
                locTop = new int[]{1535, 1500, 1480, 1495, 1520, 1545, 1560, 1575, 1620, 1665};
            }
            if (pLatRow == 13) {
                locLeft = new int[]{770, 960, 1180, 1415, 1655, 1865, 2075, 2290, 2450, 2560};
                locTop = new int[]{1885, 1845, 1800, 1770, 1790, 1825, 1870, 1915, 1940, 1975};
            }
            if (pLatRow == 14) {
                locLeft = new int[]{0, 0, 0, 0, 1665, 1835, 2040, 2215};
                locTop = new int[]{0, 0, 0, 0, 2095, 2135, 2150, 2200};
            }
        }

        int diff = pLongCol - matrixOffset;
        int p1Left = 0;
        int p1Top = 0;
        int p2Left = 0;
        int p2Top = 0;

        if (locLeft != null && locTop != null) {
            if ((diff + 1) < locLeft.length) {
                p1Left = locLeft[diff];
                p1Top = locTop[diff];
                p2Left = locLeft[diff + 1];
                p2Top = locTop[diff + 1];
            } else {
                p1Left = locLeft[locLeft.length - 1];
                p1Top = locTop[locTop.length - 1];
                p2Left = locLeft[locLeft.length - 1];
                p2Top = locTop[locTop.length - 1];
            }
        }
        if (p1Left == 0 || p1Top == 0 || p2Left == 0 || p2Top == 0) {
            return null;
        }
        if (p1Left > 3000 || p1Top > 3000 || p2Left > 3000 || p2Top > 3000) {
            return null;
        }

        return new int[]{p1Left, p1Top, p2Left, p2Top};
    }
}