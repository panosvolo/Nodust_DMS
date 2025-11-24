package com.mts.mea.nodust_app.common;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Mahmoud on 9/26/2018.
 */

public class InputFilterMinMax implements InputFilter {

    private double min, max;

    public InputFilterMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.length()>=1&&source.charAt(source.length()-1)=='.')
        {
            source=source+"0";
        }
        try {
            String dest_val=dest.toString();
            if(dest_val.isEmpty())
                dest_val="0";
            if (dest_val.length()>=1&&dest_val.charAt(dest_val.length()-1)=='.')
            {
                dest_val=dest_val+"0";
            }
            Double input = Double.parseDouble(dest_val.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();

        }
        return "";
    }

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}