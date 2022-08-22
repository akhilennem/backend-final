package com.attendance.login.OtpSender.Controller;

import com.attendance.login.OtpSender.OtpGenerator.OtpGenarator;
import com.attendance.login.OtpSender.OtpRepository.OtpRepository;
import com.attendance.login.OtpSender.OtpVerifier.OtpVerifier;
import com.attendance.login.OtpSender.Otpmodel.Mail;
import com.attendance.login.OtpSender.service.MailService;
import com.attendance.login.UserPackage.models.User;
import com.attendance.login.UserPackage.repository.RoleRepository;
import com.attendance.login.UserPackage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequestMapping("/api/mail")
@CrossOrigin
public class OtpController {


    @Autowired
    public OtpGenarator genarator;

    @Autowired
    public User user1;
    @Autowired
    public OtpRepository otpRepository;
    @Autowired
    private MailService mailService;
    int i;
    @Autowired
    public RoleRepository roleRepository;

@Autowired
public UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;


@GetMapping("/send")
    public ResponseEntity<?> run(@RequestParam String email) throws Exception

    {
        if
        (userRepository.existsByEmail(email)) {

            String verify = genarator.generateRandom(4);
            System.out.println(verify);
            i = Integer.parseInt(verify);

            OtpVerifier otpVerifier=new OtpVerifier();
//            String cpy="email";
            otpVerifier.setEmail(email);
            otpVerifier.setOtp(i);
            otpRepository.save(otpVerifier);

            Mail mail = new Mail();
            mail.setMailFrom("akhilennem@gmail.com");
            mail.setMailTo(email);
            mail.setMailSubject("PTF ATTENDANCE APP Password Reset OTP");
            mail.setMailContent("\uD835\uDC80\uD835\uDC90\uD835\uDC96\uD835\uDC93 \uD835\uDC76\uD835\uDC8F\uD835\uDC86 \uD835\uDC7B\uD835\uDC8A\uD835\uDC8E\uD835\uDC86 \uD835\uDC77\uD835\uDC82\uD835\uDC94\uD835\uDC94\uD835\uDC98\uD835\uDC90\uD835\uDC93\uD835\uDC85 (\uD835\uDC76\uD835\uDC7B\uD835\uDC77) \uD835\uDC70\uD835\uDC94 : "+i);
//            mail.setMailContent(String.valueOf(i));
            mailService.sendEmail(mail);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/forget-password")
    public ResponseEntity<?> passwordReset(@RequestParam int otp, String email) {
        if (otpRepository.existsByOtpAndEmail(otp, email)) {

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }
        @GetMapping("/reset-password")
    public String updatePassword(@RequestParam String email, String newpassword) {

   User user1;
   user1=userRepository.getByEmail(email);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(newpassword);
        user1.setPassword(password);
userRepository.save(user1);

return "password updated successfully";
    }

    @PostMapping("/delete-otp")
    public String Delete(@RequestParam String email) {
    otpRepository.deleteByEmail(email);
    return "deleted";
    }
}