/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ivaylo
 */
@Entity
public class TaskRepeatDataEntity implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne
    @JsonIgnore
    private TaskEntity task;
    
    private String mode;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date mode_start;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date mode_end;
    private Boolean allDay;
    
    
    private String endson;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endson_until;    
    private Integer endson_count;
    
    
    private Integer repeat_days;
    private Integer repeat_weeks;
    private String repeat_wdays;
    private Integer repeat_months;
    private String repeatby;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<EventException> eventsEx;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Date getMode_start() {
        return mode_start;
    }

    public void setMode_start(Date mode_start) {
        this.mode_start = mode_start;
    }

    public String getEndson() {
        return endson;
    }

    public void setEndson(String endson) {
        this.endson = endson;
    }

    public Date getEndson_until() {
        return endson_until;
    }

    public void setEndson_until(Date endson_until) {
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
    
    public List<String> getRepeat_wdays_asList() {
        List<String> res = new ArrayList<>();
        if (StringUtils.isNotBlank(repeat_wdays)) {
            res.addAll(Arrays.asList(repeat_wdays.split(",")));
        }
        return res;
    }
    
    public String[] getRepeat_wdays_asArray() {
        String[] res = {};
        if (StringUtils.isNotBlank(repeat_wdays)) {
            res = repeat_wdays.split(",");
        }
        return res;
    }

    public String getRepeat_wdays() {
        return repeat_wdays;
    }

    public void setRepeat_wdays(String repeat_wdays) {
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

    public Date getMode_end() {
        return mode_end;
    }

    public void setMode_end(Date mode_end) {
        this.mode_end = mode_end;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public List<EventException> getEventsEx() {
        return eventsEx;
    }

    public void setEventsEx(List<EventException> eventsEx) {
        this.eventsEx = eventsEx;
    }
    
    
    
}
