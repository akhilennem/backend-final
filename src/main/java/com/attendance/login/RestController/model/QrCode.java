package com.attendance.login.RestController.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "qrcode")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String para;
}
