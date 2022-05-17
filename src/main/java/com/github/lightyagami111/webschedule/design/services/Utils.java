/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.services;

import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataDTO;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author asd
 */
public class Utils {
    
    static Logger log = Logger.getLogger(Utils.class.getName());

    public static Map mapOf(Object... objects) {
        Map m = new HashMap();
        if (objects.length >= 2) {
            for (int i = 0; i < objects.length; i = i + 2) {
                m.put(objects[i], objects[i + 1]);
            }
        }
        return m;
    }

    public static Date parse(String date) {
        Date res = null;

        if (StringUtils.isNotBlank(date)) {

            TimeZone aDefault = TimeZone.getDefault();

            SimpleDateFormat isoDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            isoDateTimeFormat.setTimeZone(aDefault);
            try {
                res = isoDateTimeFormat.parse(date);
            } catch (ParseException ex) {
                SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                isoDateFormat.setTimeZone(aDefault);
                try {
                    res = isoDateFormat.parse(date);
                } catch (ParseException ex2) {
                    log.error("", ex);
                }
            }

        }

        return res;
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd'T'HH:mm:ss");
    }

    public static String format(Date date, String format) {
        String res = null;

        if (date != null) {
            TimeZone aDefault = TimeZone.getDefault();

            SimpleDateFormat isoDateTimeFormat = new SimpleDateFormat(format);
            isoDateTimeFormat.setTimeZone(aDefault);
            res = isoDateTimeFormat.format(date);
        }

        return res;
    }

    public static boolean equalsDates(Date d1, Date d2) {
        int result = 0;
        if (d1 == null) {
            if (d2 != null) {
                result = -1;
            }
        } else if (d2 == null) {
            result = 1;
        } else {
            result = d1.compareTo(d2);
        }
        return result==0;
    }

    public static boolean checkForChangesToDeleteEventExceptions(TaskRepeatDataEntity te, TaskRepeatDataDTO dTO) {
        boolean res = false;

        if (!Objects.equals(te.getMode(), dTO.getMode())) {
            res = true;
        }
        if (!equalsDates(te.getMode_start(),dTO.getMode_start())) {
            res = true;
        }
        if (!equalsDates(te.getMode_end(), dTO.getMode_end())) {
            res = true;
        }
        if (!Objects.equals(te.getAllDay(), dTO.getAllDay())) {
            res = true;
        }

        if (!Objects.equals(te.getEndson(), dTO.getEndson())) {
            res = true;
        }
        if (!Objects.equals(te.getEndson_count(), dTO.getEndson_count())) {
            res = true;
        }
        if (!equalsDates(te.getEndson_until(), dTO.getEndson_until())) {
            res = true;
        }

        if (!Objects.equals(te.getRepeat_days(), dTO.getRepeat_days())) {
            res = true;
        }

        if (!Objects.equals(te.getRepeat_weeks(), dTO.getRepeat_weeks())) {
            res = true;
        }
        if (!Objects.equals(te.getRepeat_wdays(), String.join(",", dTO.getRepeat_wdays()))) {
            res = true;
        }

        if (!Objects.equals(te.getRepeat_months(), dTO.getRepeat_months())) {
            res = true;
        }
        if (!Objects.equals(te.getRepeatby(), dTO.getRepeatby())) {
            res = true;
        }

        return res;
    }

}
