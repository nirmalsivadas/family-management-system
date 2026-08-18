package com.family_management_system.master_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "qualifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 11)
    private Long id;

    @Column(length = 100,nullable = false)
    private String name;
}
