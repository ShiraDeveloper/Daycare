package com.example.daycare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Parent {
    @Id
    @GeneratedValue
    private int idParent;
    private String phone;
    private String email;
@OneToMany
    private List<Child> lstChild;

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Child> getLstChild() {
        return lstChild;
    }

    public void setLstChild(List<Child> lstChild) {
        this.lstChild = lstChild;
    }
}
