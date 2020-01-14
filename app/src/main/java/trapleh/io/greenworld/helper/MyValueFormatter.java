package trapleh.io.greenworld.helper;


import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }}
