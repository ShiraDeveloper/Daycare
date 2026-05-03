package com.example.daycare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Child {
    @Id
    @GeneratedValue
    private int idChild;
    private String name;
    private String imageChild;
    private boolean sensitivity;
    @Enumerated(EnumType.STRING)
    private eLevel level;
    private LocalDate birthDate;
    private boolean Arrived;
@OneToMany(mappedBy = "child")
private List<Dairy1> dairy1List;

    @ManyToOne
    @JoinColumn(name="id_Parent")
    private Parent parent;


//    public int getIdChild() {
//        return idChild;
//    }
//
//    public void setIdChild(int idChild) {
//        this.idChild = idChild;
//    }
//    public boolean isArrived() {
//        return Arrived;
//    }
//
//    public void setArrived(boolean arrived) {
//        Arrived = arrived;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getImageChild() {
//        return imageChild;
//    }
//
//    public void setImageChild(String imageChild) {
//        this.imageChild = imageChild;
//    }
//
//    public boolean isSensitivity() {
//        return sensitivity;
//    }
//
//    public void setSensitivity(boolean sensitivity) {
//        this.sensitivity = sensitivity;
//    }
//
//    public eLevel getLevel() {
//        return level;
//    }
//
//    public void setLevel(eLevel level) {
//        this.level = level;
//    }
//
//    public Date getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(Date birthDate) {
//        this.birthDate = birthDate;
//    }
//
//    public Parent getParent() {
//        return parent;
//    }
//
//    public void setParent(Parent parent) {
//        this.parent = parent;
//    }
//

    public boolean getArrived(){
        return Arrived;
    }
}
