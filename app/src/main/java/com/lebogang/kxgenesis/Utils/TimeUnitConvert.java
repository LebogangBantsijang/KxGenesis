package com.lebogang.kxgenesis.Utils;

import java.util.concurrent.TimeUnit;

public class TimeUnitConvert {

    private static final int MINUTE = 60000;

    public static String toMinutes(long time){
        String duration;
        long min = TimeUnit.MILLISECONDS.toMinutes(time);
        if (min < 10)
            duration = "0" + min;
        else
            duration = "" + min;
        long temp = time - (min * MINUTE);
        long sec = TimeUnit.MILLISECONDS.toSeconds(temp);
        if (sec < 10)
            duration += ":0" + sec;
        else
            duration += ":" + sec;
        return duration;
    }
}
