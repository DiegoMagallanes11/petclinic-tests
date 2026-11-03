package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "types")
@Data
@NoArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    private String description;
    private Boolean active;
    private String sizeCategory;
    private Integer averageLifespan;
    private String careLevel;
}
