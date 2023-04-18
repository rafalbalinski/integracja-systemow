package com.example.system.integration.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "laptop")
@NoArgsConstructor
@AllArgsConstructor
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;
    private String size;
    private String resolution;
    private String matrixType;
    private String isTouch;
    private String processor;
    private String physicalCores;
    private String clockSpeed;
    private String ram;
    private String discCapacity;
    private String discType;
    private String graphicCard;
    private String graphicCardMemory;
    private String operationSystem;
    private String discReader;
}
