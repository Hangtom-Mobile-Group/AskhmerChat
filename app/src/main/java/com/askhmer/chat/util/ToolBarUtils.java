package com.askhmer.chat.util;

import android.content.Context;
import android.content.res.TypedArray;

import com.askhmer.chat.R;

/**
 * Created by Longdy on 10/7/2016.
 */
public class ToolBarUtils {
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }
}
