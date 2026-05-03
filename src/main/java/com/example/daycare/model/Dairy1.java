package com.example.daycare.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
@Entity
public class Dairy1 {

    @Id
    @GeneratedValue
    private int id_dairy;
    private LocalDate date1;
    private LocalTime hour1;
    @Enumerated(EnumType.STRING)
    private eDo doing;
    private String remark;
    private String images;
    public int getIdDairy() {
        return id_dairy;
    }
    @ManyToOne
    @JoinColumn(name = "child_id")
private  Child child;

    public int getId_dairy() {
        return id_dairy;
    }

    public void setId_dairy(int id_dairy) {
        this.id_dairy = id_dairy;
    }

    public LocalDate getDate1() {
        return date1;
    }

    public void setDate1(LocalDate date1) {
        this.date1 = date1;
    }

    public LocalTime getHour1() {
        return hour1;
    }

    public void setHour1(LocalTime hour1) {
        this.hour1 = hour1;
    }

    public int getChild() {
        return child.getIdChild();
    }

    public void setChild(int child) {
        this.child = new Child();
        this.child.setIdChild(child);
    }

    public void setIdDairy(int idDairy) {
        this.id_dairy = idDairy;
    }

    public LocalDate getDate() {
        return date1;
    }

    public void setDate(LocalDate date) {
        this.date1 = date;
    }

    public LocalTime getHour() {
        return hour1;
    }

    public void setHour(LocalTime hour) {
        this.hour1 = hour;
    }

    public eDo getDoing() {
        return doing;
    }

    public void setDoing(eDo doing) {
        this.doing = doing;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
