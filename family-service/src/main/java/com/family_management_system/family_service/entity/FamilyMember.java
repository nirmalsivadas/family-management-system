package com.family_management_system.family_service.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "family_members")
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 11)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "family_head_id")
    private FamilyHead familyHead;

    @ManyToOne
    @JoinColumn(name = "relation_id")
    private Relation relation;

    @Column(length = 50,nullable = false)
    private String firstName;
    @Column(length = 50,nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Date dateOfBirth;

    @Column(nullable = false)
    private String maritalStatus;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String profession;

    @Column(nullable = false,length = 5)
    private String bloodGroup;
}
