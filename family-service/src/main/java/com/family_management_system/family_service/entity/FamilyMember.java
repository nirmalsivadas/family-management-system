package com.family_management_system.family_service.entity;

import com.family_management_system.family_service.enums.Status;
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

    @Column(length = 10,nullable = false)
    private Long mobileNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String occupation;

    @Column(nullable = false)
    private String employment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
