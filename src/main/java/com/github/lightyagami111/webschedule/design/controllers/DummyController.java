/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.controllers;

import com.github.lightyagami111.webschedule.design.datastructure.LabelEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataEntity;
import com.google.common.collect.Lists;
import com.github.lightyagami111.webschedule.design.datastructure.CalendarUIEventDTO;
import com.github.lightyagami111.webschedule.design.datastructure.ProjectEntity;
import com.github.lightyagami111.webschedule.design.datastructure.TaskEntity;
import com.github.lightyagami111.webschedule.design.services.DaoService;
import com.github.lightyagami111.webschedule.design.services.RepeatableService;
import com.github.lightyagami111.webschedule.design.services.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ivaylo
 */
@Controller
public class DummyController {

    @Autowired
    private DaoService daoService;

    @Autowired
    private RepeatableService repeatableService;

    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public @ResponseBody
    String generateTasks(@RequestParam Long project_id, @RequestParam Long count) {
        ProjectEntity findProjectById = daoService.findProjectById(project_id);

        List<TaskEntity> parents = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            TaskEntity t = new TaskEntity();
            t.setProject(findProjectById);
            t.setParent("#");
            t.setAllDay(Boolean.TRUE);
            t.setPriority(1);
            t.setId(daoService.save(t));
            t.setText("task " + t.getId());

            if (i % 2 == 0 && parents.size() > 0) {
                TaskEntity randomElement = parents.get(rand.nextInt(parents.size()));
                t.setParent(String.valueOf(randomElement.getId()));
                t.setParent_text(randomElement.getText());
            }
            daoService.saveOrUpdate(t);

            parents.add(t);
        }

        return "OK";
    }

    @RequestMapping(value = "/update/time", method = RequestMethod.GET)
    public @ResponseBody
    String updateTasksTime(@RequestParam Long project_id) {
        Random rand = new Random();
        List<LabelEntity> findAllLabels = daoService.findAllLabels();

        List<TaskEntity> tasks = daoService.loadTasksByProject(project_id);
        for (int i = 0; i < tasks.size(); i++) {
            TaskEntity t = tasks.get(i);

            Boolean[] allDayArr = {true, false};
            boolean allDay = allDayArr[rand.nextInt(allDayArr.length)];
            t.setAllDay(allDay);

            t.setPriority(rand.nextInt(10) + 1);

            if (i % 3 == 0) {
                TaskRepeatDataEntity rep = daoService.findRepeatDataByTaskId(t.getId());
                if (rep == null) {
                    rep = generateRandomRep(rand);
                    rep.setTask(t);
                    daoService.saveOrUpdate(rep);
                }
            }
            if (i % 5 == 0) {
                TaskRepeatDataEntity rep = daoService.findRepeatDataByTaskId(t.getId());
                if (rep == null) {
                    Date start = getRandomDate(t.getAllDay());
                    Date end = null;
                    if (rand.nextInt(4) == 1) {
                        if (!t.getAllDay()) {
                            GregorianCalendar gc = new GregorianCalendar();
                            gc.setTime(start);
                            gc.set(GregorianCalendar.MONTH, gc.get(GregorianCalendar.MONTH) + 3);
                            end = gc.getTime();
                        } else {
                            end = getRandomDate(t.getAllDay());
                        }
                    }
                    t.setStart(start);
                    t.setEnd(end);
                }
            }

            if (i % 8 == 0) {
                if (t.getLabels() != null) {
                    if (t.getLabels().isEmpty()) {
                        int s = rand.nextInt(findAllLabels.size());
                        for (int j = 0; j < s + 1; j++) {
                            t.getLabels().add(findAllLabels.get(j));
                        }
                    }
                }

            }

            daoService.saveOrUpdate(t);

        }

        return "OK";
    }

    @RequestMapping(value = "/update/rep/exceptions", method = RequestMethod.GET)
    public @ResponseBody
    String updateTasksTimeRepExceptions(@RequestParam Long project_id) {
        List<TaskEntity> tasks = daoService.loadTasksByProject(project_id);
        for (TaskEntity task : tasks) {
            TaskRepeatDataEntity rep = daoService.findRepeatDataByTaskId(task.getId());
            if (rep != null) {
                List<CalendarUIEventDTO> fireTimesBetween = repeatableService.computeFireTimesBetween(rep, rep.getMode_start(), Utils.parse("3333-12-31"));
                if (fireTimesBetween.size() > 1000) {
                    List<List<CalendarUIEventDTO>> partition = Lists.partition(fireTimesBetween, 1000);
                    List<CalendarUIEventDTO> exceptions_to_persist = partition.get(0);
                    for (CalendarUIEventDTO ep : exceptions_to_persist) {
                        daoService.deleteCurrentEvent(ep.getId(), Utils.parse(ep.getStart()));
                    }
                }

            }
        }
        return "OK";
    }

    public TaskRepeatDataEntity generateRandomRep(Random rand) {
        TaskRepeatDataEntity rep = new TaskRepeatDataEntity();

        String[] modeArr = {"d", "w", "m"};
        String mode = modeArr[rand.nextInt(modeArr.length)];

        Boolean[] allDayArr = {true, false};
        boolean allDay = allDayArr[rand.nextInt(allDayArr.length)];

        Date mode_start = getRandomDate(allDay);
        Date mode_end = null;
        if (allDay == false) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(mode_start);
            gc.set(GregorianCalendar.HOUR_OF_DAY, gc.get(GregorianCalendar.HOUR_OF_DAY) + 2);
            mode_end = gc.getTime();
        }

        String[] endsonArr = {"never", "after", "on_date"};
        String endson = endsonArr[rand.nextInt(endsonArr.length)];
        Integer endson_count = null;
        Date endson_until = null;
        if (endson.equals("after")) {
            endson_count = rand.nextInt(15);
        }
        if (endson.equals("on_date")) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(mode_start);
            gc.set(GregorianCalendar.MONTH, gc.get(GregorianCalendar.MONTH) + 3);
            endson_until = gc.getTime();
        }

        Integer repeat_days = null;
        Integer repeat_weeks = null;
        String repeat_wdays = null;
        Integer repeat_months = null;
        String repeatby = null;
        if (mode.equals("d")) {
            repeat_days = rand.nextInt(30) + 1;
        }
        if (mode.equals("w")) {
            repeat_weeks = rand.nextInt(30) + 1;
            String[] repeat_wdays_arr = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
            repeat_wdays = repeat_wdays_arr[rand.nextInt(repeat_wdays_arr.length)];
        }
        if (mode.equals("m")) {
            repeat_months = rand.nextInt(30) + 1;
            String[] repeatby_arr = {"dm", "dw"};
            repeatby = repeatby_arr[rand.nextInt(repeatby_arr.length)];
        }

        rep.setMode(mode);

        rep.setAllDay(allDay);
        rep.setMode_start(mode_start);
        rep.setMode_end(mode_end);

        rep.setEndson(endson);
        rep.setEndson_count(endson_count);
        rep.setEndson_until(endson_until);

        rep.setRepeat_days(repeat_days);
        rep.setRepeat_weeks(repeat_weeks);
        rep.setRepeat_wdays(repeat_wdays);
        rep.setRepeat_months(repeat_months);
        rep.setRepeatby(repeatby);

        return rep;
    }

    public int nextInt(Random rand, List<Integer> ints) {
        int r = 0;
        do {
            r = rand.nextInt(297);
        } while (ints.contains(r));
        ints.add(r);
        return r;
    }

    public static Date getRandomDate(boolean allDay) {

        GregorianCalendar gc = new GregorianCalendar();

        gc.set(GregorianCalendar.YEAR, 2015);

        int dayOfYear = randBetween(140, 340);

        gc.set(GregorianCalendar.DAY_OF_YEAR, dayOfYear);

        int hour = 0;
        int minutes = 0;
        if (!allDay) {
            hour = randBetween(0, 24);
            minutes = randBetween(0, 6) * 10;
        }
        gc.set(GregorianCalendar.HOUR_OF_DAY, hour);
        gc.set(GregorianCalendar.MINUTE, minutes);

        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

}
