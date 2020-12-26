/*
 * Copyright (c) 2020. Lebogang Bantsijang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lebogang.kxgenesis.AppUtils;

import java.util.concurrent.TimeUnit;

public class TimeUnitConvert {

    private static final int MINUTE = 60000;

    public static String toMinutes(long time){
        String duration;
        long min = TimeUnit.MILLISECONDS.toMinutes(time);
        duration = min < 10? "0"+ min : "" + min;
        long temp = time - (min * MINUTE);
        long sec = TimeUnit.MILLISECONDS.toSeconds(temp);
        duration += sec < 10? ":0" + sec : ":" + sec;
        return duration;
    }
}
