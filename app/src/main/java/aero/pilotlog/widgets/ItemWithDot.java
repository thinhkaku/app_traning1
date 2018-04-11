package aero.pilotlog.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;

/**
 * Created by phuc.dd on 2/24/2017.
 */

public class ItemWithDot extends LinearLayout {
    private TextView tvTitle;

    public ItemWithDot(Context context) {
        super(context);

    }
    public ItemWithDot(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.item_with_dot, this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text))
            tvTitle.setText(text);
    }

}
