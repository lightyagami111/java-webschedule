/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import com.github.lightyagami111.webschedule.design.services.Utils;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ivaylo
 */

public class TaskTreeDTO {
    
    private Long id;
    
    private ProjectEntity project;
    
    private String text;
    private String parent;
    private String parent_text;
    private String notes;
    private Date start;
    private Date end;
    private Boolean allDay;
    private Set<LabelEntity> labels = new HashSet<>();
    private Set<LinkEntity> links = new HashSet<>();
    private Integer priority;
    private TaskRepeatDataEntity rep;
    private CalendarUIEventDTO repNextFire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
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

    public TaskRepeatDataEntity getRep() {
        return rep;
    }

    public void setRep(TaskRepeatDataEntity rep) {
        this.rep = rep;
    }

    public CalendarUIEventDTO getRepNextFire() {
        return repNextFire;
    }

    public void setRepNextFire(CalendarUIEventDTO repNextFire) {
        this.repNextFire = repNextFire;
    }
    
    
    public Date getStartComputed() {
        Date res = start;
        if (repNextFire != null) {
            res = Utils.parse(repNextFire.getStart());
        }
        return res;
    }
    
    
}
