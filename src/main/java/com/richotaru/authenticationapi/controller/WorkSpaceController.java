package com.richotaru.authenticationapi.controller;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.richotaru.authenticationapi.dao.AppRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceCreationDto;
import com.richotaru.authenticationapi.entity.QWorkSpace;
import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.enumeration.WorkSpaceTypeConstant;
import com.richotaru.authenticationapi.service.WorkSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@RestController
@RequestMapping("portal-account")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;
    @Autowired
    private AppRepository appRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Public
    @GetMapping
    @Operation(summary = "Search for Portal Accounts", description = "Call this API to search for Portal Account")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Receive Search Result for Portal Account"))
    public QueryResults<WorkSpace> searchPortalAccount(@RequestParam("limit") Optional<Integer> optionalLimit,
                                                               @RequestParam("offset") Optional<Integer> optionalOffset,
                                                               @RequestParam("acountName")Optional<String> optionalAccountName,
                                                               @RequestParam("accountCode")Optional<String> optionalAccountCode,
                                                               @RequestParam("acountType")Optional<String> optionalAccountType,
                                                               @RequestParam("clientSystemCode")Optional<String> optionalClientSystemCode){
        JPAQuery<WorkSpace> jpaQuery = appRepository.startJPAQuery(QWorkSpace.workSpace);

        optionalAccountName.ifPresent(name->{
            jpaQuery.where(QWorkSpace.workSpace.name.equalsIgnoreCase(name));
        });
        optionalAccountCode.ifPresent(code->{
            jpaQuery.where(QWorkSpace.workSpace.code.equalsIgnoreCase(code));
        });
        optionalAccountType.ifPresent(type->{
            jpaQuery.where(QWorkSpace.workSpace.type.eq(WorkSpaceTypeConstant.valueOf(type)));
        });
        optionalClientSystemCode.ifPresent(code->{
            jpaQuery.join(QWorkSpace.workSpace.clientSystem).fetchJoin()
            .where(QWorkSpace.workSpace.clientSystem.code.equalsIgnoreCase(code));
        });

        jpaQuery.limit(optionalLimit.orElse(100));
        jpaQuery.offset(optionalOffset.orElse(0));
        return  jpaQuery.fetchResults();
    }
    @PostMapping()
    @Operation(summary = "Create a Work Space in a Specific Client System", description = "Call this API to create a Work Space account in a specific Client System")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Work Space created"))
    public ResponseEntity<WorkSpace> createWorkSpace(@RequestBody @Valid WorkSpaceCreationDto dto) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(workSpaceService.createWorkSpace(dto));
        }catch (Exception e){
            e.printStackTrace();
            throw  new Exception("Unable to create Portal Account at this time", e);
        }
    }
//    @Public
//    @PostMapping("authenticate")
//    public ResponseEntity<PortalAccountAuthPojo> authenticateClientUser(@RequestBody AccountAuthDto dto) throws Exception {
//        return ResponseEntity.ok(portalAccountService.authenticate(dto));
//    }
}
