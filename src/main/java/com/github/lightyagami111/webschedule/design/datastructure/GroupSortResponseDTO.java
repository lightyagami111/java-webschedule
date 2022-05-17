/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import java.util.List;

/**
 *
 * @author ivaylo
 */
public class GroupSortResponseDTO {
    
    private List<GroupSortDTO> groups;
    private String view;
    private boolean allowNewTaskAction;

    public List<GroupSortDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupSortDTO> groups) {
        this.groups = groups;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public boolean isAllowNewTaskAction() {
        return allowNewTaskAction;
    }

    public void setAllowNewTaskAction(boolean allowNewTaskAction) {
        this.allowNewTaskAction = allowNewTaskAction;
    }
    
}
