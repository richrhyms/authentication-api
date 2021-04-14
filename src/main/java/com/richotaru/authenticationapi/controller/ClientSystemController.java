package com.richotaru.authenticationapi.controller;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.richotaru.authenticationapi.dao.AppRepository;
import com.richotaru.authenticationapi.domain.annotations.Public;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemUpdateDto;
import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.entity.QClientSystem;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.enumeration.AccessModeConstant;
import com.richotaru.authenticationapi.enumeration.ClientSystemTypeConstant;
import com.richotaru.authenticationapi.enumeration.WorkSpaceAccountTypeConstant;
import com.richotaru.authenticationapi.serviceImpl.ClientSystemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@RestController
@RequestMapping("client-system")
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
    public QueryResults<ClientSystem> searchClientSystem(@RequestParam("limit") Optional<Integer> optionalLimit,
                                                             @RequestParam("offset") Optional<Integer> optionalOffset,
                                                             @RequestParam("systemType")Optional<String> optionalSystemType,
                                                             @RequestParam("accessMode")Optional<String> optionalAccessMode,
                                                             @RequestParam("workSpaceAccountType")Optional<String> optionalWorkSpaceAccountType,
                                                             @RequestParam("displayName")Optional<String> optionalDisplayName,
                                                             @RequestParam("clientCode")Optional<String> optionalClientCode){
        JPAQuery<ClientSystem> jpaQuery = appRepository.startJPAQuery(QClientSystem.clientSystem);

        optionalSystemType.ifPresent(type->{
            jpaQuery.where(QClientSystem.clientSystem.systemType.eq(ClientSystemTypeConstant.valueOf(type)));
        });
        optionalAccessMode.ifPresent(mode->{
            jpaQuery.where(QClientSystem.clientSystem.accessMode.eq(AccessModeConstant.valueOf(mode)));
        });
        optionalWorkSpaceAccountType.ifPresent(type->{
            jpaQuery.where(QClientSystem.clientSystem.workSpaceAccountType.eq(WorkSpaceAccountTypeConstant.valueOf(type)));
        });
        optionalDisplayName.ifPresent(name->{
            jpaQuery.where(QClientSystem.clientSystem.displayName.equalsIgnoreCase(name));
        });
        optionalClientCode.ifPresent(code->{
            jpaQuery.where(QClientSystem.clientSystem.code.equalsIgnoreCase(code));
        });
        jpaQuery.limit(optionalLimit.orElse(100));
        jpaQuery.offset(optionalOffset.orElse(0));
        return  jpaQuery.fetchResults();
    }
    @PostMapping()
    @Operation(summary = "Create a Client System", description = "Call this API to create a Client System")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Client System created"))
    public ResponseEntity<ClientSystemPojo> createClientSystem(@RequestBody @Valid ClientSystemDto dto) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(clientSystemService.createClientSystem(dto));
        }catch (Exception e){
            e.printStackTrace();
            throw  new Exception("Unable to create Client System at this time", e);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Client System", description = "Call this API to update a Client System")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Client System updated"))
    public ResponseEntity<ClientSystemPojo> updatedClientSystem(@PathVariable("id") Long id,
                                                                @RequestBody @Valid ClientSystemUpdateDto dto) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clientSystemService.updateClientSystem(id,dto));
        }catch (Exception e){
            e.printStackTrace();
            throw  new Exception("Unable to create Client System at this time", e);
        }
    }
}
