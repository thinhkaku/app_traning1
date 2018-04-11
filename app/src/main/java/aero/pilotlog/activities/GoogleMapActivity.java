package aero.pilotlog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.utilities.NetworkUtils;
import aero.pilotlog.widgets.AlertToast;
import aero.pilotlog.widgets.MccDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoogleMapActivity extends AppCompatActivity {
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    public static final String KEY_LAT = "mccPilotLOG.GoogleMapActivity.key.LAT";
    public static final String KEY_LNG = "mccPilotLOG.GoogleMapActivity.key.LNG";
    public static final String KEY_ADDRESS = "mccPilotLOG.GoogleMapActivity.key.ADDRESS";

    private Double latitude;
    private Double longitude;
    private String address;

    private ProgressBar mProgressBar;
    private AlertToast mToastLoading;

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar mActionbar = getSupportActionBar();

        if (mActionbar != null) {
            mActionbar.hide();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        ActionBar mActionbar = getSupportActionBar();
        ButterKnife.bind(this);
        mIbMenu.setVisibility(View.GONE);
        mIbLeft.setVisibility(View.VISIBLE);
        mTvNumber.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //mToastLoading = new AlertToast(GoogleMapActivity.this);
        //mToastLoading.showToast();
        // final AlertToast mToastLoading = new AlertToast(GoogleMapActivity.this);
        // mToastLoading.showToast();
        if (!NetworkUtils.isHavingNetwork(this)) {
            mProgressBar.setVisibility(View.INVISIBLE);
            MccDialog alertDialog = new MccDialog();
            alertDialog.createDialog(this,R.string.google_map, R.string.dialog_no_internet_content).show();
            // mToastLoading.cancelToast();
            return;
        }
        //setTitleToolbar(getResources().getString(R.string.google_map));
        Intent intent = getIntent();
        String lat = intent.getStringExtra(KEY_LAT);



        String lng = intent.getStringExtra(KEY_LNG);
        address = intent.getStringExtra(KEY_ADDRESS);

        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
            try {
                latitude = Double.parseDouble(lat);
                longitude = Double.parseDouble(lng);
                ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        googleMap.addMarker(new MarkerOptions().position(
                                new LatLng(latitude, longitude)).title(address));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(latitude, longitude), 10.0f));
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }
                });
                // mToastLoading.cancelToast();
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    @OnClick({R.id.ibLeft, R.id.tvTitle})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.tvTitle:
            case R.id.ibLeft: //Handle button back
                finish();
                break;
            default:
                break;
        }
    }

}
