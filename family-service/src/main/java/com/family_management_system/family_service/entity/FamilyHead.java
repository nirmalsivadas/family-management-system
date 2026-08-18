package com.family_management_system.family_service.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "family_heads")
public class FamilyHead {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 11)
    private Long id;

    @Column(length = 50,nullable = false)
    private String firstName;
    @Column(length = 50,nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Date dateOfBirth;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private String maritalStatus;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String profession;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false,length = 5)
    private String bloodGroup;

    @Column(nullable = false,length = 150)
    private String addressLine1;

    @Column(length = 150)
    private String addressLine2;

    @Column(length = 100,nullable = false)
    private String area;

    @Column(length = 50,nullable = false)
    private String city;

    @Column(length = 50,nullable = false)
    private String state;

    @Column(length = 10,nullable = false)
    private String pincode;

    @Column(nullable = false)
    private Date joinDate;

    private byte[] photo;

    @Column(nullable = false)
    private String status;

}
