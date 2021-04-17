package com.richotaru.authenticationapi.controller;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.richotaru.authenticationapi.dao.AppRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.model.dto.AccountAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserPojo;
import com.richotaru.authenticationapi.entity.QWorkSpaceUser;
import com.richotaru.authenticationapi.entity.WorkSpaceUser;
import com.richotaru.authenticationapi.enumeration.GenderConstant;
import com.richotaru.authenticationapi.service.WorkSpaceUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@RestController
@RequestMapping("work-space-user")
public class WorkSpaceUserController {
    @Autowired
    private WorkSpaceUserService workSpaceUserService;
    @Autowired
    private AppRepository appRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Public
    @GetMapping
    @Operation(summary = "Search for Client Users", description = "Call this API to search for Client Users")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Client User List"))
    public QueryResults<WorkSpaceUser> searchWorkSpaceUser(@RequestParam("limit") Optional<Integer> optionalLimit,
                                                           @RequestParam("offset") Optional<Integer> optionalOffset,
                                                           @RequestParam("firstName") Optional<String> optionalFirstName,
                                                           @RequestParam("lastName") Optional<String> optionalLastName,
                                                           @RequestParam("otherNames") Optional<String> optionalOtherNames,
                                                           @RequestParam("email") Optional<String> optionalEmail,
                                                           @RequestParam("phoneNumber") Optional<String> optionalPhoneNumber,
                                                           @RequestParam("userId") Optional<String> optionalUserId,
                                                           @RequestParam("username") Optional<String> optionalUsername,
                                                           @RequestParam("dateOfBirth") Optional<String> optionalDateOfBirth,
                                                           @RequestParam("displayName") Optional<String> optionalDisplayName,
                                                           @RequestParam("gender") Optional<GenderConstant> optionalGender,
                                                           @RequestParam("setupComplete") Optional<Boolean> optionalSetupComplete,
                                                           @RequestParam("emailVerified") Optional<Boolean> optionalEmailVerified){
        QWorkSpaceUser qworkSpaceUser = QWorkSpaceUser.workSpaceUser;
        JPAQuery<WorkSpaceUser> jpaQuery = appRepository.startJPAQuery(qworkSpaceUser);

        optionalFirstName.ifPresent(firstName->{
            jpaQuery.where(qworkSpaceUser.firstName.equalsIgnoreCase(firstName));
        });
        optionalLastName.ifPresent(lastName->{
            jpaQuery.where(qworkSpaceUser.lastName.equalsIgnoreCase(lastName));
        });
        optionalOtherNames.ifPresent(otherName->{
            jpaQuery.where(qworkSpaceUser.otherNames.equalsIgnoreCase(otherName));
        });
        optionalEmail.ifPresent(email->{
            jpaQuery.where(qworkSpaceUser.email.equalsIgnoreCase(email));
        });
        optionalPhoneNumber.ifPresent(phoneNumber->{
            jpaQuery.where(qworkSpaceUser.phoneNumber.equalsIgnoreCase(phoneNumber));
        });
        optionalUserId.ifPresent(userId->{
            jpaQuery.where(qworkSpaceUser.userId.equalsIgnoreCase(userId));
        });
        optionalUsername.ifPresent(username->{
            jpaQuery.where(qworkSpaceUser.username.equalsIgnoreCase(username));
        });
        optionalDateOfBirth.ifPresent(dob->{
            jpaQuery.where(qworkSpaceUser.dateOfBirth.eq(Timestamp.valueOf(dob)));
        });
        optionalDisplayName.ifPresent(displayName->{
            jpaQuery.where(qworkSpaceUser.displayName.equalsIgnoreCase(displayName));
        });
        optionalGender.ifPresent(gender->{
            jpaQuery.where(qworkSpaceUser.gender.eq(gender));
        });
        optionalSetupComplete.ifPresent(setupComplete->{
            jpaQuery.where(qworkSpaceUser.setupComplete.eq(setupComplete));
        });
        optionalEmailVerified.ifPresent(emailVerified->{
            jpaQuery.where(qworkSpaceUser.emailVerified.eq(emailVerified));
        });
        jpaQuery.limit(optionalLimit.orElse(100));
        jpaQuery.offset(optionalOffset.orElse(0));

        return  jpaQuery.fetchResults();
    }
    @Public
    @PostMapping
    public ResponseEntity<WorkSpaceUserPojo> createWorkSpaceUser(@RequestBody WorkSpaceUserDto dto) throws Exception {
        return ResponseEntity.ok(workSpaceUserService.createWorkSpaceUser(dto));
    }

    @Public
    @PostMapping("authenticate")
    public ResponseEntity<WorkSpaceUserAuthPojo> authenticateClientUser(@RequestBody AccountAuthDto dto) throws Exception {
        return ResponseEntity.ok(workSpaceUserService.authenticate(dto));
    }
}
