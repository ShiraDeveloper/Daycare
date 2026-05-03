package com.example.daycare.Dto;

import com.example.daycare.model.eDo;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalTime;
public class DairyDto {
    private int idDairy;
    private LocalDate date;
    private LocalTime hour;
    @Enumerated(EnumType.STRING)
    private eDo doing;
    private String remark;
    private String images;

    public int getIdDairy() {
        return idDairy;
    }

    public void setIdDairy(int idDairy) {
        this.idDairy = idDairy;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

//    public eDo getDoing() {
//        return doing;
//    }
//
//    public void setDoing(eDo doing) {
//        this.doing = doing;
//    }

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
