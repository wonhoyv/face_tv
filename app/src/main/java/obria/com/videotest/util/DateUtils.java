package obria.com.videotest.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shaojie on 2018/2/28.
 */

public class DateUtils {

    static String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String getWeekOfDate(Date dt) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
