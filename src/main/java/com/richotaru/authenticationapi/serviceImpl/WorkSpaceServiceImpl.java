package com.richotaru.authenticationapi.serviceImpl;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.dao.WorkSpaceRepository;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceCreationDto;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceUpdateDto;
import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.service.WorkSpaceService;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.WorkSpaceCodeSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WorkSpaceRepository workSpaceRepository;
    private final ClientSystemService clientSystemService;
    private final ClientSystemRepository clientSystemRepository;
    private final SequenceGenerator sequenceGenerator;



    public WorkSpaceServiceImpl(WorkSpaceRepository workSpaceRepository,
                                ClientSystemService clientSystemService,
                                ClientSystemRepository clientSystemRepository,
                                @WorkSpaceCodeSequence SequenceGenerator sequenceGenerator) {
        this.workSpaceRepository = workSpaceRepository;
        this.clientSystemService = clientSystemService;
        this.clientSystemRepository = clientSystemRepository;
        this.sequenceGenerator = sequenceGenerator;


    }
    @Transactional
    @Override
    public WorkSpace createWorkSpace(WorkSpaceCreationDto dto) {

        if(!clientSystemService.isValidKey(dto.getClientKey())){
            throw  new IllegalArgumentException(" Client Key is Invalid!");
        }

        ClientSystem clientSystem = clientSystemRepository.findByClientCodeAndStatus(dto.getSystemCode(), GenericStatusConstant.ACTIVE).orElse(null);

        if(clientSystem == null){
            throw  new IllegalArgumentException(" Client System not found!");
        }
        WorkSpace workSpace = new WorkSpace();
        workSpace.setName(dto.getDisplayName());
        workSpace.setCode(sequenceGenerator.getNext());
        workSpace.setType(dto.getAccountType());
        workSpace.setClientSystem(clientSystem);

//        workSpace.set(dto.getUsername());
//        portalAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
//        portalAccount.setJwtToken(jwtUtils.generateToken(portalAccount.getUsername()));
        workSpace.setCreatedAt(LocalDateTime.now());
        workSpace.setLastUpdatedAt(LocalDateTime.now());
        workSpace.setStatus(GenericStatusConstant.ACTIVE);
       return workSpaceRepository.save(workSpace);
    }

    @Transactional
    @Override
    public WorkSpace updateWorkSpace(Long id, WorkSpaceUpdateDto dto) {
        if(clientSystemService.isValidKey(dto.getClientKey())){
            throw  new IllegalArgumentException(" Client Key is Invalid!");
        }
        WorkSpace workSpace = workSpaceRepository.findById(id)
                .orElseThrow((()->new IllegalArgumentException("Work Space not found!")));

        if(dto.getDisplayName() != null){
            workSpace.setName(dto.getDisplayName());
        }

        if(dto.getAccountType() != null){
            workSpace.setType(dto.getAccountType());
        }
        if(dto.getStatus() != null){
            workSpace.setStatus(GenericStatusConstant.ACTIVE);
        }

//        workSpace.set(dto.getUsername());
//        portalAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
//        portalAccount.setJwtToken(jwtUtils.generateToken(portalAccount.getUsername()));
//        workSpace.setCreatedAt(LocalDateTime.now());
        workSpace.setLastUpdatedAt(LocalDateTime.now());
        return workSpaceRepository.save(workSpace);
    }

}
