/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ivaylo
 */
@Entity
@Indexed
public class TaskEntity implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private ProjectEntity project;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private CalendarEntity calendar;
    
    @Field
    private String text;
    private String parent;
    private String parent_text;
    
    @Field
    @Column(columnDefinition="TEXT")
    private String notes;
    
    private Integer priority;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date start;    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date end;
    private Boolean allDay;
    
    
    @ManyToMany(fetch = FetchType.EAGER)    
    private Set<LabelEntity> labels = new HashSet<>();
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<LinkEntity> links = new HashSet<>();
    

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

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public Long getProject_id() {
        return project.getId();
    }

    public String getProject_text() {
        return project.getText();
    }

    public CalendarEntity getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarEntity calendar) {
        this.calendar = calendar;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }    
    
}
