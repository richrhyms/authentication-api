package com.richotaru.authenticationapi.controller;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserAuthDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserAuthPojo;
import com.richotaru.authenticationapi.serviceImpl.ClientSystemServiceImpl;
import com.richotaru.authenticationapi.serviceImpl.UserDetailServiceImpl;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@RestController
@RequestMapping("client-user")
public class ClientSystemUserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl clientSystemService;

    @Autowired
    private ClientSystemRepository clientSystemRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Public
    @GetMapping
    public String HelloClient(){
        return "Hello Client User Works";
    }


    @PostMapping("authenticate")
    public ResponseEntity<ClientUserAuthPojo> AuthenticateClientSystemUser(@RequestBody ClientUserAuthDto dto) throws Exception {
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword())
           );

       }catch (BadCredentialsException be){
           be.printStackTrace();
           throw  new Exception("Incorrect Username or Password", be);
       }
        UserDetails clientSystem = clientSystemService.loadUserByUsername(dto.getUsername());
        String jwtToken = jwtUtils.generateToken(clientSystem.getUsername(),false);
        Date expirationDate = jwtUtils.extractExpiration(jwtToken);

        ClientUserAuthPojo response = new ClientUserAuthPojo();
        response.setJwtToken(jwtToken);
        response.setExpirationDate(expirationDate);

        return ResponseEntity.ok(response);
    }

}
