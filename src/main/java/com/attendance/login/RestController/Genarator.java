package com.attendance.login.RestController;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


import java.util.Random;
@Service
public class Genarator {

    public String generateRandom(int len) {
            String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                    +"lmnopqrstuvwxyz";

//        String chars="1234567890"+"!@#$%&";
            Random rnd = new Random();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++)
                sb.append(chars.charAt(rnd.nextInt(chars.length())));
            return sb.toString();
        }
    }

