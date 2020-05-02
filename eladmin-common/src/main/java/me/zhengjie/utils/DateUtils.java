package me.zhengjie.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    public static String timeStampToDate(Timestamp timeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(timeStamp);
        return dateStr;
    }

    public static String getYearAndMonthByTimeStamp(Timestamp timeStamp) {
        String date = timeStampToDate(timeStamp);
        return date.substring(0, 7);
    }

    public static String getYearAndMonthAndDayByTimeStamp(Timestamp timeStamp){
        String date = timeStampToDate(timeStamp);
        return date.substring(0, 10);
    }

    public static String getYearByTimeStamp(Timestamp timeStamp) {
        String date = timeStampToDate(timeStamp);
        return date.substring(0, 4);
    }

    public static int getMonthByTimeStamp(Timestamp timeStamp) {
        String date = timeStampToDate(timeStamp);
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }

    public static int getDayByTimeStamp(Timestamp timeStamp) {
        String date = timeStampToDate(timeStamp);
        String day = date.substring(8, 10);
        return Integer.parseInt(day);
    }

    public static int getHourByTimeStamp(Timestamp timeStamp) {
        String date = timeStampToDate(timeStamp);
        String hour = date.substring(11, 13);
        return Integer.parseInt(hour);
    }

    public static int getMinuteByTimeStamp(Timestamp timeStamp) {
        String date = timeStampToDate(timeStamp);
        String minute = date.substring(14, 16);
        return Integer.parseInt(minute);
    }

    public static int getSecondByTimeStamp(Timestamp timeStamp) {
        String date = timeStampToDate(timeStamp);
        String second = date.substring(17, 19);
        return Integer.parseInt(second);
    }

}
