package com.attendance.login.OtpSender.OtpVerifier;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="otp")
public class OtpVerifier {
//   @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//   public int id;

   @Id
   public String email;
   public int otp;
}
