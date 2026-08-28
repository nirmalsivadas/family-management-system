package com.family_management_system.family_service.entity;

import com.family_management_system.family_service.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "family_heads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyHead {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 11)
    private Long id;

    @Column(nullable = false)
    private String memberShipId;

    @Column(length = 50,nullable = false)
    private String familyName;

    @Column(nullable = false)
    private Long numberOfFamilyMembers;

    @Column(length = 50,nullable = false)
    private String memberShipType;

    @Column(length = 50,nullable = false)
    private String registrationCategory;

    @Column(length = 10,nullable = false)
    private Long mobileNumber;

    @Column(length = 10)
    private Long alternateMobile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private String maritalStatus;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String occupation;

    @Column(length = 50)
    private String employment;

    @Column(nullable = false)
    private String profession;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false,length = 5)
    private String bloodGroup;

    @Column(nullable = false)
    private Long annualIncome;

    @Column(nullable = false,length = 150)
    private String addressLine1;

    @Column(length = 150)
    private String addressLine2;

    @Column(length = 50,nullable = false)
    private String city;

    @Column(length = 50,nullable = false)
    private String state;

    @Column(length = 10,nullable = false)
    private String pincode;

    @Column(length = 50,nullable = false)
    private String country;

    @Column(nullable = false)
    private Date joinDate;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
