package com.example.daycare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Integer idParent;

    private String phone;
    private String email;

    @OneToMany(mappedBy = "parent")
    private List<Child> lstChild;
}
