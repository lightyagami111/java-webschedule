/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.services;

import com.github.lightyagami111.webschedule.design.datastructure.CalendarUIEventDTO;
import com.github.lightyagami111.webschedule.design.datastructure.TaskRepeatDataEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author ivaylo
 */
public class RepeatableWorkThread implements Callable<List<CalendarUIEventDTO>> {
    

    private final RepeatableService repeatableService;
    private final TaskRepeatDataEntity rep;
    private final Date start;
    private final Date end;

    public RepeatableWorkThread(TaskRepeatDataEntity rep, Date start, Date end, RepeatableService repeatableService) {
        this.rep = rep;
        this.start = start;
        this.end = end;
        this.repeatableService = repeatableService;
    }

    @Override
    public List<CalendarUIEventDTO> call() throws Exception {
        return repeatableService.computeFireTimesBetween(rep, start, end);
    }
    
}
