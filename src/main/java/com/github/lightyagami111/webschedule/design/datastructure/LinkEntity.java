/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.datastructure;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author asd
 */
@Entity
public class LinkEntity implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(columnDefinition="VARCHAR(255)")
    private String title;
    
    @Column(columnDefinition="TEXT")
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) { //id form JSON post save task ?
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
