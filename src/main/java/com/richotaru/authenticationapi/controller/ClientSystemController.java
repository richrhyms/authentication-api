package com.richotaru.authenticationapi.controller;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.richotaru.authenticationapi.dao.AppRepository;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.QClientSystem;
import com.richotaru.authenticationapi.domain.model.dto.AuthenticationRequestDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.pojo.AuthenticationResponsePojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.serviceImpl.ClientSystemServiceImpl;
import com.richotaru.authenticationapi.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private AppRepository appRepository;


    @Public
    @GetMapping
    @Operation(summary = "Search for Client Systems", description = "Call this API to search for Client Systems")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Client System List"))
    public QueryResults<ClientSystemPojo> searchClientSystem(@RequestParam("limit") Optional<Integer> optionalLimit,
                                                             @RequestParam("offset") Optional<Integer> optionalOffset,
                                                             @RequestParam("displayName")Optional<String> optionalDisplayName){
        JPAQuery<ClientSystem> jpaQuery = appRepository.startJPAQuery(QClientSystem.clientSystem);

        jpaQuery.limit(optionalLimit.orElse(100));
        jpaQuery.offset(optionalOffset.orElse(0));

        optionalDisplayName.ifPresent(display->{
            jpaQuery.where(QClientSystem.clientSystem.displayName.equalsIgnoreCase(display));
        });
        QueryResults<ClientSystem> result = jpaQuery.fetchResults();
        return  new QueryResults<>(getClientSytemPojos(result.getResults()),result.getLimit(),result.getOffset(),result.getTotal());
    }
    @PostMapping("create")
    public ResponseEntity<ClientSystemPojo> creeteClientSystem(@RequestBody @Valid ClientSystemDto dto) throws Exception {
        try {

            ClientSystem clientSystem = new ClientSystem();
            clientSystem.setClientKey(dto.getClientKey());
            clientSystem.setClientName(dto.getClientName());
            clientSystem.setDateRegistered(new Timestamp(new Date().getTime()));
            clientSystem.setDisplayName(dto.getDisplayName());
            ClientSystemPojo savedClient = new ClientSystemPojo(clientSystemRepository.save(clientSystem));
            UserDetails userDetails = clientSystemService.loadUserByUsername(savedClient.getClientName());
            String jwtToken = jwtUtils.generateToken(userDetails);
            savedClient.setJwtToken(jwtToken);
            savedClient.setExpirationDate(new Timestamp(jwtUtils.extractExpiration(jwtToken).getTime()));

            return ResponseEntity.ok(savedClient);
        }catch (Exception e){
            e.printStackTrace();
            throw  new Exception("Unable to create Client System at this time", e);
        }
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponsePojo> authenticateClientSystem(@RequestBody AuthenticationRequestDto dto) throws Exception {
       try {
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


    private List<ClientSystemPojo> getClientSytemPojos(List<ClientSystem> clientSystems){
        return clientSystems.stream().map(ClientSystemPojo::new).collect(Collectors.toList());
    }
}
