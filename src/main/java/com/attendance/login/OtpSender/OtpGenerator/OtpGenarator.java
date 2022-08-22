package com.attendance.login.OtpSender.OtpGenerator;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpGenarator {

    public String generateRandom(int l) {
            String chars = "0123456789";


        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        int randomWithRandomDataGenerator = randomDataGenerator.nextInt(1000, 9999);
            String ran=String.valueOf(randomWithRandomDataGenerator);
//        Random rand = new Random();
//        int random=rand. nextInt(10000);
//        String ran= String.valueOf(random);
//        System.out.println("random num: "+random);

//        String chars="1234567890"+"!@#$%&";
//            Random rnd = new Random();
//            StringBuilder sb = new StringBuilder(l);
//            for (int i = 0; i < 4; i++)
//                sb.append(chars.charAt(rnd.nextInt(chars.length())));
        System.out.println("random number is:: "+ ran);
            return ran;

        }
    }

