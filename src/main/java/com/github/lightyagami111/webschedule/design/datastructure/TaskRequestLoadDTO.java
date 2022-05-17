/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author asd
 */
public class TaskRequestLoadDTO {
    
    private Long id;
    private ProjectEntity project;
    private CalendarEntity calendar;
    private String text;
    private String parent;
    private String parent_text;
    private String notes;    
    private Integer priority;
    private Date start; 
    private Date end;
    private Boolean allDay;
    private Set<LabelEntity> labels = new HashSet<>();
    private Set<LinkEntity> links = new HashSet<>();    
    private Set<TaskEntity> subTasks = new HashSet<>();
    private TaskRepeatDataDTO repeatData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectEntity getProject() {
        return project;
    }
    
    public Long getProject_id() {
        return project.getId();
    }

    public String getProject_text() {
        return project.getText();
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public CalendarEntity getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarEntity calendar) {
        this.calendar = calendar;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Set<LabelEntity> getLabels() {
        return labels;
    }

    public void setLabels(Set<LabelEntity> labels) {
        this.labels = labels;
    }

    public Set<LinkEntity> getLinks() {
        return links;
    }

    public void setLinks(Set<LinkEntity> links) {
        this.links = links;
    }

    public Set<TaskEntity> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Set<TaskEntity> subTasks) {
        this.subTasks = subTasks;
    }

    public TaskRepeatDataDTO getRepeatData() {
        return repeatData;
    }

    public void setRepeatData(TaskRepeatDataDTO repeatData) {
        this.repeatData = repeatData;
    }
    
    
}
