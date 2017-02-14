package com.kush.app.stayput;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Constants for the app. Duh.
 */

public class Consts {

    //all periods in ms
    public static final String STR_END_FINISH_TIME = " Uhr";
    public static final String NOTIFICATION_WORKTIME_STRING = "Arbeitszeit verbleibend: ";
    public static final String NOTIFICATION_OVERTIME_STRING = "Überstunden: ";
    public static final String WORKTIME = "Arbeitszeit";
    public static final int WORKTIME_MAX = 28080000; //7h 48min; 28080000 ms
    public static final int OVERTIME_MAX = 7920000;  //2h 12min 7920000 ms
    public static final int TIMER_COLOR_GREEN = android.graphics.Color.parseColor("#00CC00");
    public static final int TIMER_COLOR_RED = android.graphics.Color.parseColor("#F03333");

    //HTML-Strings for building the notification
    public static final String CONTEXT_COLOR_HTML_RED_START = "<font color='red'>";
    public static final String CONTEXT_COLOR_HTML_GREEN_START = "<font color='green'>";
    public static final String CONTEXT_COLOR_HTML_END = "</font>";
}
