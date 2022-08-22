package com.attendance.login.UserPackage.payload.request;

import lombok.Data;

import java.util.Set;

import javax.persistence.Id;
import javax.validation.constraints.*;

@Data

public class SignupRequest {
    @NotBlank
    @Size(max = 50)
    @Email
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    public String email;
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;




    public String name;

    public String phone;

    public String address;

    public String designation;
  
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public Set<String> getRole() {
//      return this.role;
//    }
//
//    public void setRole(Set<String> role) {
//      this.role = role;
//    }
}
