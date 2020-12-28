package com.richotaru.authenticationapi.controller;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.model.dto.AuthenticationRequestDto;
import com.richotaru.authenticationapi.domain.model.pojo.AuthenticationResponsePojo;
import com.richotaru.authenticationapi.serviceImpl.ClientSystemServiceImpl;
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
@RequestMapping("client")
public class ClientSystemController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientSystemServiceImpl clientSystemService;

    @Autowired
    private ClientSystemRepository clientSystemRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Public
    @GetMapping
    public String HelloClient(){
        return "Hello Client Works";
    }


    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponsePojo> AuthenticateClientSystem(@RequestBody AuthenticationRequestDto dto) throws Exception {
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword())
           );

       }catch (BadCredentialsException be){
           be.printStackTrace();
           throw  new Exception("Incorrect Username or Password", be);
       }
        UserDetails clientSystem = clientSystemService.loadUserByUsername(dto.getUsername());
        String jwtToken = jwtUtils.generateToken(clientSystem);
        Date expirationDate = jwtUtils.extractExpiration(jwtToken);

        AuthenticationResponsePojo response = new AuthenticationResponsePojo();
        response.setJwtToken(jwtToken);
        response.setExpirationDate(expirationDate);

        return ResponseEntity.ok(response);
    }

}
