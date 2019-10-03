package com.lambda.controller;

import com.lambda.model.entity.Role;
import com.lambda.model.entity.User;
import com.lambda.model.form.UserForm;
import com.lambda.model.util.CustomUserDetails;
import com.lambda.model.util.LoginRequest;
import com.lambda.model.util.LoginResponse;
import com.lambda.model.util.RandomStuff;
import com.lambda.repository.RoleRepository;
import com.lambda.service.UserService;
import com.lambda.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class UserRestController {
    private static final String DEFAULT_ROLE = "ROLE_USER";

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AvatarStorageService avatarStorageService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    FormConvertService formConvertService;

    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUser() {
        User user = userDetailService.getCurrentUser();
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/profile")
    public ResponseEntity<Long> updateProfile(@RequestBody UserForm userForm, @RequestParam("id") Long id) {
        User newUserInfo = formConvertService.convertToUser(userForm, false);
        Optional<User> oldUser = userService.findById(id);
        if (oldUser.isPresent()) {
            userService.setFields(newUserInfo, oldUser.get());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestPart("avatar") MultipartFile avatar, @RequestPart("id") String id) {
        Optional<User> user = userService.findById(Long.parseLong(id));
        if (user.isPresent()) {
            String fileName = avatarStorageService.storeFile(avatar, user.get());
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/avatar/")
                        .path(fileName)
                        .toUriString();
                user.get().setAvatarUrl(fileDownloadUri);
                userService.save(user.get());
            return new ResponseEntity<>("User's avatar uploaded successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("Not found user with the given id in database!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/avatar/{fileName:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("fileName") String fileName, HttpServletRequest request) {
        return downloadService.generateUrl(fileName, request, avatarStorageService);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@Valid @RequestBody UserForm userForm) {
        User user = formConvertService.convertToUser(userForm, true);
        if (user == null) return new ResponseEntity<>("Username existed in database!", HttpStatus.BAD_REQUEST);
        Role role = roleRepository.findByName(DEFAULT_ROLE);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>("Registered successfully!", HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        User currentUser = userDetailService.getCurrentUser();
        LoginResponse loginResponse = new LoginResponse(currentUser.getId(), currentUser.getUsername(), currentUser.getRoles(), jwt);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/random")
    public RandomStuff randomStuff(){
        // Creating object of Set
        Set<String> arrset1 = new HashSet<String>();

        // Populating arrset1
        arrset1.add("A");
        arrset1.add("B");
        arrset1.add("C");
        arrset1.add("D");
        arrset1.add("E");

        // print arrset1
        System.out.println("First Set: "
                + arrset1);

        // Creating another object of Set
        Set<String> arrset2 = new HashSet<String>();

        // Populating arrset2
        arrset2.add("C");
        arrset2.add("D");
        arrset2.add("A");
        arrset2.add("B");
        arrset2.add("E");

        // print arrset2
        System.out.println("Second Set: "
                + arrset2);
        boolean value = arrset1.equals(arrset2);
        return new RandomStuff(Boolean.toString(value));
//        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
    }
}