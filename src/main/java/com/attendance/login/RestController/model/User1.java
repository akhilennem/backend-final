package com.attendance.login.RestController.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "attendance")

public class User1 {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    public String first_In="Not Scanned";
    public String first_out="Not Scanned";
    public String second_In="Not Scanned";
    public String second_out="Not Scanned";

    private String name;

    private String email;
    public int count=0;
    public String para;
    public String last;

    private LocalDate date=LocalDate.now();
    public String time=LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));



    public String month=LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM"));
//    public String time;
}
