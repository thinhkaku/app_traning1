package aero.pilotlog.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.ItemSettingList;
import aero.pilotlog.widgets.MccDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccountFragment extends BaseMCCFragment {
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.expandable_layout)
    ExpandableLayout expandableLayout;
    @Bind(R.id.tvLicense)
    TextView tvLicense;
    @Bind(R.id.tvAccountId)
    TextView tvAccountId;
    @Bind(R.id.cbShowPassword)
    AppCompatCheckBox cbShowPassword;
    @Bind(R.id.edtOldPassword)
    TextView edtOldPassword;
    @Bind(R.id.edtNewPassword)
    TextView edtNewPassword;
    @Bind(R.id.edtConfirmPassword)
    TextView edtConfirmPassword;
    @Bind(R.id.tvDescriptionAccount)
    TextView tvDescriptionAccount;
    private ProfileUtils profileUtils;
    private String becomeVerifiedUser = "You become a <b>Verified User</b> after a successful login to your airline company web portal";

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_user_account;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        init();

    }
    public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);

    private void init(){
        mTvTitle.setText(getString(R.string.setting_title) + " - " + getString(R.string.setting_user_account));
        mIbLeft.setVisibility(View.VISIBLE);
        mIbMenu.setVisibility(View.GONE);
        profileUtils = ProfileUtils.newInstance();
        tvAccountId.setText(profileUtils.getUserProfileRespond().getUserProfile().getAccountEmail());
        tvLicense.setText("Valid until "+ profileUtils.getUserProfileRespond().getUserProfile().getLicenseDate());
        tvDescriptionAccount.setText(Html.fromHtml(becomeVerifiedUser));
       /* Date convertedDate = new Date();
        try {
            convertedDate = CONVERT_DATE_FORMAT.parse(profileUtils.getUserProfileRespond().getUserProfile().getLicenseDate());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tvLicense.setText("Valid until "+ DISPLAY_DATE_FORMAT.format(convertedDate));*/

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    edtOldPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }else {
                    edtOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void changePassword(){
        if(TextUtils.isEmpty(edtOldPassword.getText().toString())){
            MccDialog.createDialog(this.getContext(),R.string.error_old_password_empty).show();
            return;
        }
        if(TextUtils.isEmpty(edtNewPassword.getText().toString())){
            MccDialog.createDialog(this.getContext(),R.string.error_new_password_empty).show();
            return;
        }
        if(TextUtils.isEmpty(edtConfirmPassword.getText().toString())){
            MccDialog.createDialog(this.getContext(),R.string.error_confirm_password_empty).show();
            return;
        }
        if(!edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
            MccDialog.createDialog(this.getContext(),R.string.error_confirm_password_not_match).show();
            return;
        }
    }


    @Nullable
    @OnClick({R.id.layoutChangePassword,R.id.btnChangePassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutChangePassword:
                if(expandableLayout.isExpanded()){
                    expandableLayout.collapse(true);
                }else {
                    expandableLayout.expand(true);
                }
                break;
            case R.id.btnChangePassword:
                changePassword();
                break;

        }
    }
}
