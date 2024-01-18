/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

import lombok.ToString;

/**
 *
 * @author simone
 */
@ToString
public class UsemRel {
    
    private String idUsem;
    private String relation;
    private String idUsemTarget;
    private String weigthing;
    private String template;

    public UsemRel() {
    }

    public String getIdUsem() {
        return idUsem;
    }

    public void setIdUsem(String idUsem) {
        this.idUsem = idUsem;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getIdUsemTarget() {
        return idUsemTarget;
    }

    public void setIdUsemTarget(String idUsemTarget) {
        this.idUsemTarget = idUsemTarget;
    }
    
    
}
