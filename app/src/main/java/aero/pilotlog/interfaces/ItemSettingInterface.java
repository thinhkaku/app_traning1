package aero.pilotlog.interfaces;

import android.view.View;

/**
 * Created by binh.pd on 1/27/2015.
 * asd
 */
public class ItemSettingInterface {

    /**
     * Interface to definiton for a callback to be invoked when use custom check,
     * bind data
     */
    public interface OnCustomText {
        /**
         * process text before is used to display in description
         * @param text input from dialog
         * @return text is processed
         */
        public String onCustomText(String text, View view);

        /**
         * method check text is inputed from dialog to decided sequence is valid or not valid
         * @param inputText text is input from dialog
         * @param view  view of item
         * @return true if @inputText is valid ortherwise return false
         */
        public boolean validInputText(String inputText, View view);

        /**
         * method call if use set OnCustomText for item
         * method is called if method validInputText return true
         * @param view view of item
         */
        public void doInvalid(View view);

        //TuanPV add new

        /**
         * when change sync id success then notify to change sync id text at MainMenuFragment
         */
       // void changeSynIdSuccess();
    }

    /**
     * Interface definition for a callback to be invoked a view update description, click to item
     */
    public interface ItemSettingListener {

        /**
         * listener update title
         * @param value
         */
        public void onUpdateDescription(String value, View view);

        /**
         * listener when click item
         */
        public void onItemClick(ItemSettingListener obItemSettingListenner, View view);
    }

    /**
     * Interface definition for a callback to be invoked when use want to custom create dialog
     */
    public interface OnInitDialog {
        public void onCreateDialog(View view);
    }

}


