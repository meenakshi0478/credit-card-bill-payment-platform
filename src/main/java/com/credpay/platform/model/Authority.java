package com.credpay.platform.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name="Authorities")
public class Authority implements Serializable {

    private static final long serialVersionUID = -3206518490338399368L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(nullable = false, length = 20)
    private String name;


    @ManyToMany(mappedBy = "authorities")
    private Collection<Role>roles;

    public Authority(){

    }

    public Authority(String name) {
        this.name=name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}