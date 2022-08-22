package com.attendance.login.RestController.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Transactional
@Data
@Entity
@Table(name = "details")
@NoArgsConstructor
public class Details {

    @Id
    public String email;
    @NotBlank
    @Size(max = 300)
    public String name;
    @Size(max = 200)
    public String phone;
    @Size(max = 600)
    public String address;
    @Size(max = 200)
    public String designation;

    public Details(String email,  String phone,String name, String address, String designation) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.designation = designation;
    }


//    public Details(int id, String name, String email, String phone, String address, String designation) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//        this.address = address;
//        this.designation = designation;
    }

