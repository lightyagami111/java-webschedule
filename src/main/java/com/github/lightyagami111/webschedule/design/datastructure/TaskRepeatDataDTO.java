/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import com.github.lightyagami111.webschedule.design.services.Utils;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ivaylo
 */
public class TaskRepeatDataDTO {
    
    @NotNull
    private Long task_id;
    
    @NotNull
    private String mode;
    
    @NotNull
    private String mode_start;
    private String mode_end;
    private Boolean allDay;
    
    private String endson;
    private String endson_until;
    private Integer endson_count;
    
    private Integer repeat_days;
    private Integer repeat_weeks;
    private String[] repeat_wdays;
    private Integer repeat_months;
    private String repeatby;
    
    private Boolean dbExist;

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Date getMode_start() {
        return Utils.parse(mode_start);
    }

    public void setMode_start(String mode_start) {
        this.mode_start = mode_start;
    }

    public String getEndson() {
        return endson;
    }

    public void setEndson(String endson) {
        this.endson = endson;
    }

    public Date getEndson_until() {
        return Utils.parse(endson_until);
    }

    public void setEndson_until(String endson_until) {
        this.endson_until = endson_until;
    }

    public Integer getEndson_count() {
        return endson_count;
    }

    public void setEndson_count(Integer endson_count) {
        this.endson_count = endson_count;
    }

    public Integer getRepeat_days() {
        return repeat_days;
    }

    public void setRepeat_days(Integer repeat_days) {
        this.repeat_days = repeat_days;
    }

    public Integer getRepeat_weeks() {
        return repeat_weeks;
    }

    public void setRepeat_weeks(Integer repeat_weeks) {
        this.repeat_weeks = repeat_weeks;
    }

    public String[] getRepeat_wdays() {
        return repeat_wdays;
    }

    public void setRepeat_wdays(String[] repeat_wdays) {
        this.repeat_wdays = repeat_wdays;
    }

    public Integer getRepeat_months() {
        return repeat_months;
    }

    public void setRepeat_months(Integer repeat_months) {
        this.repeat_months = repeat_months;
    }

    public String getRepeatby() {
        return repeatby;
    }

    public void setRepeatby(String repeatby) {
        this.repeatby = repeatby;
    }

    public Boolean getDbExist() {
        return dbExist;
    }

    public void setDbExist(Boolean dbExist) {
        this.dbExist = dbExist;
    }

    public Date getMode_end() {
        return Utils.parse(mode_end);
    }

    public void setMode_end(String mode_end) {
        this.mode_end = mode_end;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    
}
