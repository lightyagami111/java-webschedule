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
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author ivaylo
 */
@Entity
public class ProjectEntity implements Serializable {    
        
    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    @Length(max = 255)
    private String text;
    
    @Length(max = 255)
    private String parent;
    
    private Boolean defaultProject = false;
    
    

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

    public Boolean getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(Boolean defaultProject) {
        this.defaultProject = defaultProject;
    }
    

    
}
