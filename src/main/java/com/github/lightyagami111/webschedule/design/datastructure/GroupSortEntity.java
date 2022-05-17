/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author asd
 */
@Entity
public class GroupSortEntity implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String group_;
    private String sort_;
    private String selectedView;
    private String selectedId;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup_() {
        return group_;
    }

    public void setGroup_(String group_) {
        this.group_ = group_;
    }

    public String getSort_() {
        return sort_;
    }

    public void setSort_(String sort_) {
        this.sort_ = sort_;
    }

    public String getSelectedView() {
        return selectedView;
    }

    public void setSelectedView(String selectedView) {
        this.selectedView = selectedView;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }
    
}
