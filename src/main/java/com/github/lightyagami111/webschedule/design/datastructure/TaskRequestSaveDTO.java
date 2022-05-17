/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ivaylo
 */
public class TaskRequestSaveDTO implements Serializable {
    
    private Long id;
    private String text;
    private String parent;
    private String parent_text;
    private Long project_id;
    private Long calendar_id;
    private String project_text;
    private String notes;
    private String start;
    private String end;
    private Long[] labels;
    private List<LinkEntity> links;
    private Integer priority;
    private Boolean allDay;
    private Long fromRepeatTaskId;
    private String fromRepeatTaskStart;
    private TaskRepeatDataDTO repeatData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent_text() {
        return parent_text;
    }

    public void setParent_text(String parent_text) {
        this.parent_text = parent_text;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getProject_text() {
        return project_text;
    }

    public void setProject_text(String project_text) {
        this.project_text = project_text;
    }

    public Long getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(Long calendar_id) {
        this.calendar_id = calendar_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Long[] getLabels() {
        return labels;
    }

    public void setLabels(Long[] labels) {
        this.labels = labels;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Long getFromRepeatTaskId() {
        return fromRepeatTaskId;
    }

    public void setFromRepeatTaskId(Long fromRepeatTaskId) {
        this.fromRepeatTaskId = fromRepeatTaskId;
    }

    public String getFromRepeatTaskStart() {
        return fromRepeatTaskStart;
    }

    public void setFromRepeatTaskStart(String fromRepeatTaskStart) {
        this.fromRepeatTaskStart = fromRepeatTaskStart;
    }

    public List<LinkEntity> getLinks() {
        return links;
    }

    public void setLinks(List<LinkEntity> links) {
        this.links = links;
    }

    public TaskRepeatDataDTO getRepeatData() {
        return repeatData;
    }

    public void setRepeatData(TaskRepeatDataDTO repeatData) {
        this.repeatData = repeatData;
    }
    
    
}
