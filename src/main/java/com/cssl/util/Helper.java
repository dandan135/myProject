package com.cssl.util;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Helper {
    public static Double accuracy(double num, double total, int scale){
        DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return Double.valueOf(df.format(accuracy_num));
    }
}
