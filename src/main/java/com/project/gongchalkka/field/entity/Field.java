package com.project.gongchalkka.field.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "fields")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String fieldName;

    @Column(nullable = false)
    private String location;

    @Column
    private int pricePerMatch;

    @Column
    private String phoneNumber;


    // 생성자
    public Field(String fieldName, String location, Integer pricePerMatch, String phoneNumber) {
        this.fieldName = fieldName;
        this.location = location;
        this.pricePerMatch = pricePerMatch;
        this.phoneNumber = phoneNumber;
    }
}
