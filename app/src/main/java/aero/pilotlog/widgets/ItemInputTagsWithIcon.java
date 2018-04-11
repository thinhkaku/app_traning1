package aero.pilotlog.widgets;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.fragments.BaseFragment;
import aero.pilotlog.fragments.SettingFlightLoggingFragment;
import aero.pilotlog.interfaces.IEditTagClear;
import aero.pilotlog.interfaces.ISequenceAction;
import aero.pilotlog.interfaces.ISequenceMultiChoiceAction;
import aero.pilotlog.interfaces.ItemSettingInterface;
import aero.pilotlog.widgets.tagsedittext.TagsEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by phuc.dd on 2/16/2017.
 */
public class ItemInputTagsWithIcon extends ItemBase implements TagsEditText.TagsEditListener {
    private TagsEditText tagsEditText;
    private TextView tvFootNote;
    private ArrayList<CharSequence> listStringDialog = null;
    private BaseFragment parentFragment;
    private int layoutResId = 0;
    private Class<? extends BaseFragment> navigationToFragment;
    public boolean isValueChanged = false;

    public void setNavigationToFragment(Class<? extends BaseFragment> navigationToFragment) {
        this.navigationToFragment = navigationToFragment;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public void setParentFragment(BaseFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    public void setTagsEditText(String text) {

        tagsEditText.setTags(text);
    }

    public void setTagsEditText(List<String> listTags) {
        String[] tagArr = new String[listTags.size()];
        tagArr = listTags.toArray(tagArr);
        tagsEditText.setTags(tagArr);
    }


    public ItemInputTagsWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onCreateLayout() {
        inflate(getContext(), R.layout.item_tags_with_icon, this);
        //tagsEditText = null;
        if (tagsEditText == null) {
            tagsEditText = (TagsEditText) findViewById(R.id.edtTags);
        }
        tvFootNote = (TextView) findViewById(R.id.txt_setting_footNote);
        tagsEditText.setTagsListener(this);
    }

    @Override
    public String getValueFromSharePref(String value) {
        return value;
    }

    public void setIcon(int id) {
        ImageView imageView = (ImageView) findViewById(R.id.ib_setting_icon);
        imageView.setBackgroundResource(id);
    }

    @Override
    public void onItemClick(ItemSettingInterface.ItemSettingListener obItemSettingListener, View view) {
        if (!isValueChanged) isValueChanged = true;
        if (listStringDialog != null && isMultiSelect)
            displayDialogMultiSelect();
        else if (listStringDialog != null)
            displayDialogSingleSelect();
        else
        navigationToListSelect();
    }

    private void displayDialogSingleSelect() {
        android.support.v7.app.AlertDialog alertDialog;
        if (tagsEditText == null) {
            tagsEditText = (TagsEditText) findViewById(R.id.edtTags);
        }
        String text = tagsEditText.getText().toString().trim();
        int index = listStringDialog.indexOf(text);
        alertDialog = new MccDialog().getSingleSelectLisAlertDialog(mContext, mTvTitle.getText().toString(),
                listStringDialog.toArray(new CharSequence[listStringDialog.size()]), index, new ISequenceAction() {
                    @Override
                    public void sequenceAction(String pName, int pIndex) {
                        mValue = pName;
                        itemId = pIndex;
                        //if (pIndex > 0)
                        tagsEditText.setTags(pName);
                        //else
                        //tagsEditText.setText("");
                    }
                });
        if (alertDialog != null) {
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }
    }

    private void displayDialogMultiSelect() {
        android.support.v7.app.AlertDialog alertDialog;
        if (tagsEditText == null) {
            tagsEditText = (TagsEditText) findViewById(R.id.edtTags);
        }
        boolean[] selectedItems = null;
        if (TextUtils.isEmpty(tagsEditText.getText())) {
            selectedItems = new boolean[listStringDialog.size()];
        } else if (!TextUtils.isEmpty(tagsEditText.getText())) {
            selectedItems = new boolean[listStringDialog.size()];
            for (int i = 0; i < tagsEditText.getTags().size(); i++) {
                String tag = tagsEditText.getTags().get(i);
                int index = listStringDialog.indexOf(tag);
                if (index >= 0) {
                    selectedItems[index] = true;
                }
            }
        }

        alertDialog = new MccDialog().getMultiSelectListAlertDialog(mContext, mTvTitle.getText().toString(),
                listStringDialog.toArray(new CharSequence[listStringDialog.size()]), selectedItems, new ISequenceMultiChoiceAction() {
                    @Override
                    public void sequenceAction(boolean[] selected) {
                        List<String> tagsValueList = new ArrayList<>();
                        //selectedItems = selected;
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i])
                                tagsValueList.add(listStringDialog.get(i).toString());
                        }
                        String[] tagsValueArray = new String[tagsValueList.size()];
                        tagsValueList.toArray(tagsValueArray);
                        tagsEditText.setTags(tagsValueArray);
                    }
                });
        if (alertDialog != null) {
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }
    }

    @Override
    public void onCustomUpdateUi(String value, View view) {


    }

    public void setListStringDialog(ArrayList<CharSequence> listStringDialog, boolean multiSelect) {
        this.listStringDialog = listStringDialog;
        isMultiSelect = multiSelect;
    }

    public TagsEditText getTagsEditText() {
        return tagsEditText;
    }

    boolean isMultiSelect = false;

    public void navigationToListSelect() {
        if (parentFragment != null && layoutResId != 0 && navigationToFragment != null) {
            if (parentFragment.getTag() == SettingFlightLoggingFragment.class.getName()) {
                Bundle bundle = new Bundle();
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
                bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_LOGGING_HOME_BASE);
                parentFragment.replaceFragment(layoutResId, navigationToFragment, bundle, true);
            } else {
                parentFragment.replaceFragment(layoutResId, navigationToFragment, true);
            }

        }
    }


    public void setFootNote(String footNote) {
        tvFootNote.setText(footNote);
        if (!TextUtils.isEmpty(footNote)) {
            tvFootNote.setVisibility(View.VISIBLE);
        } else {
            tvFootNote.setVisibility(View.GONE);

        }
    }


    @Override
    public void onTagsChanged(Collection<String> tags) {
        if (tagsEditText.getTags() != null && tagsEditText.getTags().size() == 0) {
            if (tvFootNote.getVisibility() == View.VISIBLE) {
                setFootNote("");
                if (iEditTagClear != null) {
                    iEditTagClear.onTagsClear(this);
                }
            }
        }
        if(tagsEditText.getTags()!=null){
            if (iEditTagClear != null) {
                iEditTagClear.onTagsChanged(this);
            }
        }

    }

    @Override
    public void onEditingFinished() {

    }

    public void setIEditTagClear(IEditTagClear iEditTagClear) {
        this.iEditTagClear = iEditTagClear;
    }

    private IEditTagClear iEditTagClear;
}
