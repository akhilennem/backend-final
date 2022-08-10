package com.attendance.login.UserPackage.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.attendance.login.OtpSender.Otpmodel.Mail;
import com.attendance.login.OtpSender.service.MailService;
import com.attendance.login.RestController.model.Details;
import com.attendance.login.RestController.repository.DetailRepository;
import com.attendance.login.RestController.repository.UserRepository1;
import com.attendance.login.UserPackage.models.Role;
import com.attendance.login.UserPackage.models.User;
import com.attendance.login.UserPackage.payload.response.UserInfoResponse;
import com.attendance.login.UserPackage.repository.UserRepository;
import com.attendance.login.UserPackage.repository.UserRoleRepo;
import com.attendance.login.UserPackage.security.jwt.JwtUtils;
import com.attendance.login.UserPackage.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.attendance.login.UserPackage.models.ERole;
import com.attendance.login.UserPackage.payload.request.LoginRequest;
import com.attendance.login.UserPackage.payload.request.SignupRequest;
import com.attendance.login.UserPackage.payload.response.MessageResponse;
import com.attendance.login.UserPackage.repository.RoleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  DetailRepository detailRepository;

//  @Autowired
//  Details details;

  @Autowired
  private MailService mailService;
  @Autowired
  UserRepository userRepository;

  @Autowired
  UserRoleRepo userRoleRepo;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  public String stats;

  @PostMapping("/signin")
  public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {


    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    if (roles.toString().equals("[ROLE_USER]")){
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    } else if (roles.toString().equals("[ROLE_ADMIN]")) {
      return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);

    }


    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString(), String.valueOf(HttpStatus.OK))
            .body(new UserInfoResponse(
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.unprocessableEntity().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.unprocessableEntity().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));
    Details details=new Details(signUpRequest.getEmail(),signUpRequest.getPhone(),
            signUpRequest.getName(),signUpRequest.getAddress(),signUpRequest.getDesignation());


    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);

            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);
    detailRepository.save(details);
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new MessageResponse("You've been signed out!"));
  }

  @PostMapping("/add-role")
  public Role addrole(Role role) {
    return roleRepository.save(role);
  }


  @PostMapping("/admin-signup")
  public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.unprocessableEntity().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.unprocessableEntity().body(new MessageResponse("Error: Email is already in use!"));
    }
String psw=signUpRequest.getPassword();
    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(adminRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "user":
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);

            break;
          default:
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
        }
      });
    }
    user.setRoles(roles);
    userRepository.save(user);


String email=signUpRequest.getEmail();
    Mail mail = new Mail();
    mail.setMailFrom("akhilennem@gmail.com");
    mail.setMailTo(email);
    mail.setMailSubject("Your PTF ATTENDANCE APP Password Is");
    mail.setMailContent("\uD835\uDC18\uD835\uDC28\uD835\uDC2E\uD835\uDC2B \uD835\uDC0F\uD835\uDC1A\uD835\uDC2C\uD835\uDC2C\uD835\uDC30\uD835\uDC28\uD835\uDC2B\uD835\uDC1D \uD835\uDC05\uD835\uDC28\uD835\uDC2B \uD835\uDC0F\uD835\uDC13\uD835\uDC05 \uD835\uDC00\uD835\uDC2D\uD835\uDC2D\uD835\uDC1E\uD835\uDC27\uD835\uDC1D\uD835\uDC1A\uD835\uDC27\uD835\uDC1C\uD835\uDC1E \uD835\uDC1A\uD835\uDC29\uD835\uDC29 \uD835\uDC22\uD835\uDC2C : "+psw +"   \uD835\uDDEC\uD835\uDDFC\uD835\uDE02 \uD835\uDDD6\uD835\uDDEE\uD835\uDDFB \uD835\uDDE5\uD835\uDDF2\uD835\uDE00\uD835\uDDF2\uD835\uDE01 \uD835\uDDEC\uD835\uDDFC\uD835\uDE02\uD835\uDDFF \uD835\uDDE3\uD835\uDDEE\uD835\uDE00\uD835\uDE00\uD835\uDE04\uD835\uDDFC\uD835\uDDFF\uD835\uDDF1 \uD835\uDDDC\uD835\uDDFB \uD835\uDDD9\uD835\uDDFC\uD835\uDDFF\uD835\uDDF4\uD835\uDDF2\uD835\uDE01 \uD835\uDDE3\uD835\uDDEE\uD835\uDE00\uD835\uDE00\uD835\uDE04\uD835\uDDFC\uD835\uDDFF\uD835\uDDF1 \uD835\uDDE6\uD835\uDDF2\uD835\uDDF0\uD835\uDE01\uD835\uDDF6\uD835\uDDFC\uD835\uDDFB");
//            mail.setMailContent(String.valueOf(i));
    mailService.sendEmail(mail);

    return ResponseEntity.ok(new MessageResponse("Registered successfully!"));
  }


  @PostMapping("/delete-account")
  public ResponseEntity<UserInfoResponse> password(@Valid @RequestBody LoginRequest loginRequest) {


    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());


    String email=loginRequest.getUsername();
    detailRepository.deleteByEmail(email);
    userRoleRepo.deleteByUserid(email);
    userRepository.deleteByUsername(loginRequest.getUsername());

    return new ResponseEntity(HttpStatus.FORBIDDEN);

  }


  @RequestMapping("/delete-user")
public ResponseEntity dlt(@RequestBody String email) {
  String username = email;
  detailRepository.deleteByEmail(email);
  userRoleRepo.deleteByUserid(email);
  userRepository.deleteByUsername(username);

  return new ResponseEntity(HttpStatus.OK);
}
    @RequestMapping("/delete-admin")
  public ResponseEntity dltadmin(@RequestBody String email) {
    String username = email;
//    detailRepository.deleteByEmail(email);
    userRoleRepo.deleteByUserid(email);
    userRepository.deleteByUsername(username);

    return new ResponseEntity(HttpStatus.CONTINUE);
  }
}
  
}
