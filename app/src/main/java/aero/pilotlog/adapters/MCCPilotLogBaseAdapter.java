package aero.pilotlog.adapters;

import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import aero.pilotlog.utilities.StringMatcher;

/**
 * Created by tuan.pv on 2015/07/29.
 */
public abstract class MCCPilotLogBaseAdapter extends BaseAdapter implements SectionIndexer {

    /**
     * List section index
     */
    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    protected abstract int getCountList();

    protected abstract String getStringItem(int pPosition);

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0, size = mSections.length(); i < size; i++) {
            sections[i] = String.valueOf(mSections.charAt(i));
        }

        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        // If there is no item for current section, previous section will be selected
        try {
            for (int i = sectionIndex; i >= 0; i--) {
                for (int j = 0; j < getCountList(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) { // remember to check this
                            if (StringMatcher.match("" + String.valueOf(getStringItem(j)).charAt(0), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match("" + String.valueOf(getStringItem(j)).charAt(0), String.valueOf(mSections.charAt(i))))
                            return j;
                    }
                }
            }
        }catch (Exception ignored){}
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
