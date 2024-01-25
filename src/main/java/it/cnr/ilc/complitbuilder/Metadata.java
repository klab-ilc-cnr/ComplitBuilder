/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

import java.util.ArrayList;
import lombok.ToString;

/**
 *
 * @author Simone Marchi
 */
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = false)
public abstract class Metadata {

    @ToString.Include
    private String id; //id generato dai metodi di creazione invocati su LexO

    private String created; //timestamp

    private ArrayList<String> creator = new ArrayList<>();

    private String modified; //timestamp

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreator() {
        return String.join(";", creator);
    }

    public void addCreator(String creator) {
        if (!this.creator.contains(creator)) {
            this.creator.add(creator);
        }
    }

    public String getModified() {
        return modified.toString();
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Metadata() {
        this.created = Utils.getTimestamp();
        this.modified = this.created;
    }

}
