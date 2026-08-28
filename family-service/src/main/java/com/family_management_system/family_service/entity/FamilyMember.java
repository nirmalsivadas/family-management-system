package com.family_management_system.family_service.entity;

import com.family_management_system.family_service.enums.Relation;
import com.family_management_system.family_service.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "family_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 11)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "family_head_id")
    private FamilyHead familyHead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Relation relationShipWithFamilyHead;

    @Column(length = 50,nullable = false)
    private String firstName;
    @Column(length = 50)
    private String middleName;
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

    @Column(nullable = false)
    private Long mobileNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String occupation;

    @Column(nullable = false)
    private String employment;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
