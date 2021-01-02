package com.richotaru.authenticationapi.controller;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.richotaru.authenticationapi.dao.AppRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.entity.*;
import com.richotaru.authenticationapi.domain.model.dto.AccountAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserPojo;
import com.richotaru.authenticationapi.domain.model.pojo.PortalAccountAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.PortalAccountPojo;
import com.richotaru.authenticationapi.service.ClientUserService;
import com.richotaru.authenticationapi.service.PortalAccountService;
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
@RequestMapping("accounts")
public class PortalAccountController {
    @Autowired
    private PortalAccountService portalAccountService;
    @Autowired
    private AppRepository appRepository;

    @Public
    @GetMapping
    @Operation(summary = "Search for Portal Accounts", description = "Call this API to search for Portal Account")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Portal Account"))
    public QueryResults<PortalAccountPojo> searchPortalAccount(@RequestParam("limit") Optional<Integer> optionalLimit,
                                                               @RequestParam("offset") Optional<Integer> optionalOffset,
                                                               @RequestParam("username")Optional<String> optionalUsername){
        JPAQuery<PortalAccount> jpaQuery = appRepository.startJPAQuery(QPortalAccount.portalAccount);

        jpaQuery.limit(optionalLimit.orElse(100));
        jpaQuery.offset(optionalOffset.orElse(0));

        optionalUsername.ifPresent(username->{
            jpaQuery.where(QPortalAccount.portalAccount.username.equalsIgnoreCase(username));
        });
        QueryResults<PortalAccount> result = jpaQuery.fetchResults();
        return  new QueryResults<>(getPortalAccountPojos(result.getResults()),result.getLimit(),result.getOffset(),result.getTotal());
    }
//    @Public
//    @PostMapping
//    public ResponseEntity<ClientUserPojo> createClientUser(@RequestBody ClientUserDto dto) throws Exception {
//        return ResponseEntity.ok(portalAccountService.createClientUser(dto));
//    }
    @Public
    @PostMapping("authenticate")
    public ResponseEntity<PortalAccountAuthPojo> authenticateClientUser(@RequestBody AccountAuthDto dto) throws Exception {
        return ResponseEntity.ok(portalAccountService.authenticate(dto));
    }
    private List<PortalAccountPojo> getPortalAccountPojos(List<PortalAccount> portalAccounts){
        return portalAccounts.stream().map(PortalAccountPojo::new).collect(Collectors.toList());
    }
}
