package com.family_management_system.master_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "marital_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaritalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 11)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;
}
