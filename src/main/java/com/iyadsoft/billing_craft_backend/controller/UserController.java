package com.iyadsoft.billing_craft_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.entity.AuthRequest;
import com.iyadsoft.billing_craft_backend.entity.UserInfo;
import com.iyadsoft.billing_craft_backend.repository.UserInfoRepository;
import com.iyadsoft.billing_craft_backend.service.JwtService;
import com.iyadsoft.billing_craft_backend.service.UserInfoService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoRepository userInfoRepository;

    // @GetMapping("/home")
    // public String index() {
    //     return "Greetings from Spring Boot!";
    // }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/user/userList")
    public List<UserInfo> getUsers() {
        return userInfoRepository.findAll();
    }

    @GetMapping("/user/userRole")
    public Optional<UserInfo> getUserRole(@RequestParam String name) {
        return userInfoRepository.findByName(name);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        // public ResponseEntity <String> authenticateAndGetToken(@RequestBody
        // AuthRequest authRequest) {
        // public String authenticateAndGetToken(@RequestBody AuthRequest authRequest,
        // HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
            // return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
            // String token = jwtService.generateToken(authRequest.getUsername());

            // Add JWT token to response cookie
            // addJwtTokenToCookie(response, token);

            // return token;
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    };

    // private void addJwtTokenToCookie(HttpServletResponse response, String token)
    // {
    // Cookie cookie = new Cookie("JWT_TOKEN", token);

    // // Set the cookie path, domain, and other attributes if needed
    // // cookie.setPath("/");
    // // cookie.setDomain("http://localhost:3000");
    // cookie.setSecure(true);
    // cookie.setHttpOnly(true);
    // response.addCookie(cookie);
    // };

}
