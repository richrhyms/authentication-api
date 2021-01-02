package com.richotaru.authenticationapi.controller;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.richotaru.authenticationapi.dao.AppRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.entity.*;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserPojo;
import com.richotaru.authenticationapi.service.ClientUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@RestController
@RequestMapping("client-user")
public class ClientUserController {
    @Autowired
    private ClientUserService clientUserService;
    @Autowired
    private AppRepository appRepository;

    @Public
    @GetMapping
    @Operation(summary = "Search for Client Users", description = "Call this API to search for Client Users")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Client User List"))
    public QueryResults<ClientUserPojo> searchClientUser(@RequestParam("limit") Optional<Integer> optionalLimit,
                                                               @RequestParam("offset") Optional<Integer> optionalOffset,
                                                               @RequestParam("username")Optional<String> optionalUsername){
        JPAQuery<ClientUser> jpaQuery = appRepository.startJPAQuery(QClientUser.clientUser);

        jpaQuery.limit(optionalLimit.orElse(100));
        jpaQuery.offset(optionalOffset.orElse(0));

        optionalUsername.ifPresent(username->{
            jpaQuery.where(QClientSystem.clientSystem.clientName.equalsIgnoreCase(username));
        });
        QueryResults<ClientUser> result = jpaQuery.fetchResults();
        return  new QueryResults<>(getClientUserPojos(result.getResults()),result.getLimit(),result.getOffset(),result.getTotal());
    }
    @Public
    @PostMapping
    public ResponseEntity<ClientUserPojo> createClientUser(@RequestBody ClientUserDto dto) throws Exception {
        return ResponseEntity.ok(clientUserService.createClientUser(dto));
    }
    private List<ClientUserPojo> getClientUserPojos(List<ClientUser> clientUsers){
        return clientUsers.stream().map(ClientUserPojo::new).collect(Collectors.toList());
    }
}
