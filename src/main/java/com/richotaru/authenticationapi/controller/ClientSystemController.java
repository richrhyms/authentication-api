package com.richotaru.authenticationapi.controller;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.richotaru.authenticationapi.dao.AppRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.QClientSystem;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.serviceImpl.ClientSystemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@RestController
@RequestMapping("client")
public class ClientSystemController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final ClientSystemServiceImpl clientSystemService;

    private final AppRepository appRepository;

    public ClientSystemController(ClientSystemServiceImpl clientSystemService,
                                  AppRepository appRepository) {
        this.clientSystemService = clientSystemService;
        this.appRepository = appRepository;
    }


    @Public
    @GetMapping
    @Operation(summary = "Search for Client Systems", description = "Call this API to search for Client Systems")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Client System List"))
    public QueryResults<ClientSystemPojo> searchClientSystem(@RequestParam("limit") Optional<Integer> optionalLimit,
                                                             @RequestParam("offset") Optional<Integer> optionalOffset,
                                                             @RequestParam("username")Optional<String> optionalUserName){
        JPAQuery<ClientSystem> jpaQuery = appRepository.startJPAQuery(QClientSystem.clientSystem);

        jpaQuery.limit(optionalLimit.orElse(100));
        jpaQuery.offset(optionalOffset.orElse(0));

        optionalUserName.ifPresent(username->{
            jpaQuery.where(QClientSystem.clientSystem.clientName.equalsIgnoreCase(username));
        });
        QueryResults<ClientSystem> result = jpaQuery.fetchResults();
        return  new QueryResults<>(getClientSystemPojos(result.getResults()),result.getLimit(),result.getOffset(),result.getTotal());
    }
    @PostMapping("create")
    public ResponseEntity<ClientSystemPojo> createClientSystem(@RequestBody @Valid ClientSystemDto dto) throws Exception {
        try {
            return ResponseEntity.ok(clientSystemService.createClientSystem(dto));
        }catch (Exception e){
            e.printStackTrace();
            throw  new Exception("Unable to create Client System at this time", e);
        }
    }
    @Public
    @PostMapping("authenticate")
    public ResponseEntity<ClientSystemAuthPojo> authenticateClientSystem(@RequestBody ClientSystemAuthDto dto) throws Exception {
        return ResponseEntity.ok(clientSystemService.authenticateClient(dto));
    }


    private List<ClientSystemPojo> getClientSystemPojos(List<ClientSystem> clientSystems){
        return clientSystems.stream().map(ClientSystemPojo::new).collect(Collectors.toList());
    }
}
