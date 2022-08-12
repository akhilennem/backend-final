package com.attendance.login.RestController.UserControl;

import com.attendance.login.RestController.Genarator;
import com.attendance.login.RestController.model.Details;
import com.attendance.login.RestController.model.QrCode;
import com.attendance.login.RestController.model.User1;
import com.attendance.login.RestController.repository.DetailRepository;
import com.attendance.login.RestController.repository.QrRepo;
import com.attendance.login.RestController.repository.UserRepository1;
import com.attendance.login.RestController.services.DetailsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

@Transactional
@CrossOrigin
@RestController
@RequestMapping("/api/rest")
public class Controller {

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    public UserRepository1 userRepository1;

    @Autowired
    public QrRepo qrRepo;

    @Autowired
    public Genarator genarator;

    public String i;
    public int rsp;

    public String in="in";
    public String out="out";



    @Autowired
    public DetailsServices detailService;

    @GetMapping("/qr-generator")

    public String Test() {
        rsp = 0;
        String verify = genarator.generateRandom(20);
        QrCode qrCode=new QrCode();

        qrCode.para = verify;
        qrRepo.save(qrCode);
        return verify;
    }

        @PostMapping("/save")
    public ResponseEntity AddUser(@RequestBody User1 user2,String time){
        System.out.println("time is "+time);

        LocalDate date = LocalDate.now();

//        if (i.equals(user2.para))
          if(qrRepo.existsByPara(user2.para))     {

            if (userRepository1.existsByPara(user2.para)) {


                return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);

            }
            if (userRepository1.existsByDateAndEmail(date, user2.getEmail())) {
//                return new ResponseEntity(HttpStatus.CONFLICT);

                User1 user3;
                user3 = userRepository1.getByDateAndEmail(date, user2.getEmail());

                if (user3.count == 1) {
                    if (user3.last.equals(user2.last)) {


                        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
                    }
                    else {

                        user3.count = 2;
                        user3.first_out = user2.time;
                        user3.para = user2.para;
                        user3.last=user2.last;
                        userRepository1.save(user3);
                        rsp = 100;
                        User1 user4;
                        user4 = userRepository1.getByDateAndEmail(date, user2.getEmail());
                        if (user4.first_out.contains("AM") || user4.first_out.contains("PM")) {
                            return new ResponseEntity<>(HttpStatus.GONE);
                        } else {

                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                    }
                }

                else if (user3.count == 2) {
                    if (user3.last.equals(user2.last)) {
                        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
                    }
                    else {

                        user3.count = 3;
                        user3.second_In = user2.time;
                        user3.para = user2.para;
                        user3.last=user2.last;
                        userRepository1.save(user3);
                        rsp = 100;
                        User1 user4;
                        user4 = userRepository1.getByDateAndEmail(date, user2.getEmail());
                        if (user4.second_In.contains("AM") || user4.second_In.contains("PM")) {
                            return new ResponseEntity<>(HttpStatus.OK);
                        } else {

                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                    }
                }else if (user3.count == 3) {
                    if (user3.last.equals(user2.last)) {
                        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);

                    }else {

                        user3.count = 4;
                        user3.second_out = user2.time;
                        user3.para = user2.para;
                        user3.last=user2.last;
                        userRepository1.save(user3);
                        rsp = 100;
                        User1 user4;
                        user4 = userRepository1.getByDateAndEmail(date, user2.getEmail());
                        if (user4.second_out.contains("AM") || user4.second_out.contains("PM")) {
                            return new ResponseEntity<>(HttpStatus.GONE);
                        } else {

                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                    }
                }else if (user3.count == 4) {


                    return new ResponseEntity<>(HttpStatus.LOCKED);

                }

            } else {
if(user2.last.equals("out")) {


    return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
}
    else {
        user2.count = 1;
        System.out.println("time is " + time);
        user2.first_In = user2.time;
        userRepository1.save(user2);
        rsp = 100;

        User1 user4;
        user4 = userRepository1.getByDateAndEmail(date, user2.getEmail());
        if (user4.first_In.contains("AM") || user2.first_In.contains("PM")) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


            }

        } else {

            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        return null;
    }


    @PostMapping("/response")
    public int respond()
    {

        return rsp;
    }

    @GetMapping("/get-user-details")
    public Iterable<User1> take( @RequestParam String email) {
        return userRepository1.findByEmail(email);
    }

    @GetMapping("/get-by-date")
    public Iterable<User1> findByDate(@RequestParam String date) {
        return userRepository1.getByDate(LocalDate.parse(date));
    }

    @PutMapping("/update-profile")
    public Details profile(@RequestBody Details details) {
        return detailRepository.save(details);
    }

    @GetMapping("/get-profile")
    public Details getProfileByEmail(@RequestParam String email) {
        return detailService.Find(email);
    }


    @GetMapping("/get-all")
    public Iterable<Details> getAll() {
        return (Iterable<Details>) detailRepository.findAll();
    }

    @GetMapping("/recent")
    public Iterable<User1> find() {
        return userRepository1.getByDate(LocalDate.now());
    }

    @GetMapping("/get-by-month")
    public Iterable<User1> findByMonth(@RequestParam String month,String email) {
        return userRepository1.getByMonthAndEmail(month,email);
    }


    @GetMapping("/delete-by-time")
    public Iterable<User1> delete( @RequestParam String date) {
        return userRepository1.deleteByDate(date);
    }

    @PostMapping("/delete-attendance")
    public ResponseEntity deleteAttendance()
    {

        userRepository1.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
