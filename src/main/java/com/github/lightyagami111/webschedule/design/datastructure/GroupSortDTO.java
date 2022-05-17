/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import java.util.List;

/**
 *
 * @author asd
 */
public class GroupSortDTO {
    
    private String groupBy;
    private List<TaskTreeDTO> tasks;

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public List<TaskTreeDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskTreeDTO> tasks) {
        this.tasks = tasks;
    }
    
    
}
