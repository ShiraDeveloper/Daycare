package com.example.daycare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
}
