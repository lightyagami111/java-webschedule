/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.services;

import ch.lambdaj.Lambda;
import com.github.lightyagami111.webschedule.design.datastructure.CalendarUIEventDTO;
import com.github.lightyagami111.webschedule.design.datastructure.EventException;
import com.github.lightyagami111.webschedule.design.datastructure.TaskEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author ivaylo
 */
@Service
public class RepeatableService {

    private static final Map<String, Integer> weekDays;
    
    static {
        Map<String, Integer> aMap = new HashMap();
        aMap.put("MO", 1);
        aMap.put("TU", 2);
        aMap.put("WE", 3);
        aMap.put("TH", 4);
        aMap.put("FR", 5);
        aMap.put("SA", 6);
        aMap.put("SU", 7);
        weekDays = Collections.unmodifiableMap(aMap);
    }

    public List<CalendarUIEventDTO> computeFireTimesBetween(TaskRepeatDataEntity rep, Date start, Date end) {
        List<CalendarUIEventDTO> list1 = new ArrayList<>();

        TaskEntity f = rep.getTask();
        List<Date> eventExceptions = Lambda.extract(rep.getEventsEx(), Lambda.on(EventException.class).getDateException());


        Date now = calculateNextInitial(rep.getMode_start(), rep);
        int count = 0;

        while (haveNext(now, count, rep)) {
            if (afterOrEqual(now,start) && beforeOrEqual(now,end) && !eventExceptions.contains(now)) {
                list1.add(createCalendarUIEventDTO(now, rep, f));
            }
            else if (! beforeOrEqual(now,end)) {
                break;
            }
            count++;
            now = calculateNext(now, rep);            
        }

        return list1;
    }

    public CalendarUIEventDTO computeNextFire(TaskRepeatDataEntity rep) {
        TaskEntity f = rep.getTask();
        List<Date> eventExceptions = Lambda.extract(rep.getEventsEx(), Lambda.on(EventException.class).getDateException());

        Date now = calculateNextInitial(rep.getMode_start(), rep);
        int count = 0;
        CalendarUIEventDTO ev = null;
        while (haveNext(now, count, rep)) {
            if (!eventExceptions.contains(now)) {
                ev = createCalendarUIEventDTO(now, rep, f);
                break;
            }            
            count++;
            now = calculateNext(now, rep);
        }
        
        return ev;
    }

    private CalendarUIEventDTO createCalendarUIEventDTO(Date now, TaskRepeatDataEntity rep, TaskEntity f) {
        CalendarUIEventDTO d = new CalendarUIEventDTO();
        d.setId(f.getId());
        d.setTitle("® " + f.getText());
        d.setColor(f.getCalendar().getBckgColor());

        d.setAllDay(rep.getAllDay());
        if (rep.getAllDay()) {
            d.setStart(Utils.format(now));
        } else {
            d.setStart(Utils.format(setMinutesAndHours(now, rep.getMode_start())));
            if (rep.getMode_end() != null) {
                d.setEnd(Utils.format(setMinutesAndHours(now, rep.getMode_end())));
            }
        }

        return d;
    }

    private boolean haveNext(Date now, Integer count, TaskRepeatDataEntity rep) {
        boolean res = false;

        if (rep.getEndson().equals("on_date")) {
            res = afterOrEqual(rep.getEndson_until(), now);
        } else if (rep.getEndson().equals("never")) {
            res = afterOrEqual(Utils.parse("3333-12-31"), now);
        } else { //count
            res = count < rep.getEndson_count();
        }

        return res;
    }

    private boolean afterOrEqual(Date date1, Date date2) {
        return date1.after(date2) || (date1.getTime() == date2.getTime());
    }

    private boolean beforeOrEqual(Date date1, Date date2) {
        return date1.before(date2) || (date1.getTime() == date2.getTime());
    }

    private Date setMinutesAndHours(Date now, Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);

        c.setTime(now);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);

        return c.getTime();
    }

    private Date calculateNextInitial(Date now, TaskRepeatDataEntity rep) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);

        if (rep.getMode().equals("w")) {
            int dayOfWeek = getDayOfWeek(c);
            List<Integer> repeatDays = getRepeatDays(rep);

            if (repeatDays.get(0) > dayOfWeek) {
                c.add(Calendar.DATE, repeatDays.get(0) - dayOfWeek);
            } else if (repeatDays.get(repeatDays.size() - 1) < dayOfWeek) {
                c.add(Calendar.DATE, 7 - dayOfWeek + repeatDays.get(0));
            } else {
                Integer isin = contains(repeatDays, dayOfWeek);
                if (isin == null) {
                    Integer next = findNext(repeatDays, dayOfWeek);
                    c.add(Calendar.DATE, repeatDays.get(next) - dayOfWeek);
                }
            }
        }

        return c.getTime();
    }

    private Date calculateNext(Date now, TaskRepeatDataEntity rep) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);

        if (rep.getMode().equals("d")) {
            c.add(Calendar.DATE, rep.getRepeat_days());
        } else if (rep.getMode().equals("w")) {
            getNextFromWeekdaysRepeat(now, c, rep);
        } else { //rep.mode = months
            if (rep.getRepeatby().equals("dm")) {
                c.add(Calendar.MONTH, rep.getRepeat_months());
            } else { //dw
                int a = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                c.add(Calendar.MONTH, rep.getRepeat_months());
                c.set(Calendar.DAY_OF_WEEK_IN_MONTH, a);
            }
        }

        return c.getTime();
    }

    private void getNextFromWeekdaysRepeat(Date now, Calendar c, TaskRepeatDataEntity rep) {
        int dayOfWeek = getDayOfWeek(c);
        List<Integer> repeatDays = getRepeatDays(rep);

        Integer isin = contains(repeatDays, dayOfWeek);
        if (repeatDays.size() - 1 == isin) {
            c.setTime(getNextWeekSpecDay(now, rep.getRepeat_weeks(), repeatDays.get(0)));
        } else {
            c.add(Calendar.DATE, repeatDays.get(isin + 1) - dayOfWeek);
        }
    }

    private List<Integer> getRepeatDays(TaskRepeatDataEntity rep) {
        List<Integer> repeatDays = new ArrayList<>();
        for (String ws : rep.getRepeat_wdays_asArray()) {
            repeatDays.add(weekDays.get(ws));
        }
        Collections.sort(repeatDays);
        return repeatDays;
    }

    private Date getNextWeekSpecDay(Date now, int addWeeks, int day) {
        Calendar date1 = Calendar.getInstance();
        date1.setTime(now);
        int add = 7 * addWeeks;
        while (getDayOfWeek(date1) != day) {
            date1.add(Calendar.DATE, 1);
            add = 7 * (addWeeks - 1);
        }
        date1.add(Calendar.DATE, add);
        return date1.getTime();
    }

    private Integer contains(List<Integer> repeatDays, int dayOfWeek) {
        Integer res = null;
        for (int i = 0; i < repeatDays.size(); i++) {
            Integer get = repeatDays.get(i);
            if (dayOfWeek == get) {
                res = i;
                break;
            }
        }
        return res;
    }

    private Integer findNext(List<Integer> repeatDays, int dayOfWeek) {
        Integer res = null;
        for (int i = 0; i < repeatDays.size(); i++) {
            Integer get = repeatDays.get(i);
            if (dayOfWeek < get) {
                res = i;
                break;
            }
        }
        return res;
    }

    private int getDayOfWeek(Calendar c) {
        int res = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (res == 0) {
            res = 7;
        }
        return res;
    }

}
