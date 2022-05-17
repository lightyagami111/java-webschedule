/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.services;

import com.github.lightyagami111.webschedule.design.datastructure.GroupSortEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author asd
 */
@Service
public class GroupAndSortService {
    
    @Autowired
    private DaoService daoService;
    
    
    public GroupSortEntity saveOrUpdate(String view, String id, String group, String sort) {
        GroupSortEntity selectedGS = daoService.findGSBySelectedViewAndId(view, id);
        if (selectedGS == null) {
            selectedGS = new GroupSortEntity();
        }
        selectedGS.setSelectedView(view);
        selectedGS.setSelectedId(id);
        selectedGS.setGroup_(group);
        selectedGS.setSort_(sort);

        daoService.saveOrUpdate(selectedGS);
        
        return selectedGS;
    }
    
    public GroupSortEntity getExistingOrDefault(String view, String id) {
        GroupSortEntity selectedGS = daoService.findGSBySelectedViewAndId(view, id);
        if (selectedGS == null) {
            selectedGS = new GroupSortEntity();
            selectedGS.setSelectedView(view);
            selectedGS.setSelectedId(id);
            selectedGS.setGroup_("dont-group");
            selectedGS.setSort_("dont-sort");
        }
        return selectedGS;
    }
    
    public GroupSortEntity getSearchGroupSort() {
        GroupSortEntity selectedGS = new GroupSortEntity();
        selectedGS.setSelectedView("search");
        selectedGS.setSelectedId("search-0");
        selectedGS.setGroup_("");
        selectedGS.setSort_("");
        return selectedGS;
    }
        
    public boolean allowNewTaskAction(GroupSortEntity gs) {
        return gs.getGroup_().equals("dont-group") && gs.getSort_().equals("dont-sort");
    }
    
    public boolean disableParent(GroupSortEntity gs) {
        return !gs.getSort_().equals("dont-sort") || !gs.getGroup_().equals("dont-group") || !gs.getSelectedView().equals("projects");
    }
    
    
}
