//package com.attendance.login.RestController.services;
//
//import com.attendance.login.RestController.model.User1;
//import com.attendance.login.RestController.repository.UserRepository1;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserServices {
//    String verify = "confirm";
//    @Autowired
//    private UserRepository1 userRepository1;
//
//    public User1 Saving(User1 user1) {
//        if (verify.equals(user1.para)) {
//            System.out.println("authentication success");
//            return userRepository1.save(user1);
//
//
//        }
//        else
//        {
//            System.out.println("authentication failed!!!");
//
//        }
//
//        return null;
//
//
//    }
//
//
//    public Iterable<User1> getByEmail(String email)
//    {
//        return userRepository1.findByEmail(email);
//
//    }
//
//    public Iterable<User1> getByName(String name)
//    {
//        return userRepository1.findByName(name);
//
//    }
//
////    public Iterable<User> getByDate(String date) {
////        return  userRepository.findByDate.parse(date);
////    }
//
//
//
//}
