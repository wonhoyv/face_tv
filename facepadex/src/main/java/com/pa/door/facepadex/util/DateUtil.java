package com.pa.door.facepadex.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shaojie on 2018/5/17.
 */

public class DateUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static String getTime() {
        String time = sdf.format(new Date());
        return time;
    }

    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }
}
