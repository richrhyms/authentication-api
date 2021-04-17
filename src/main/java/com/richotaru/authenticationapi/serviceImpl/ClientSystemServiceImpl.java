package com.richotaru.authenticationapi.serviceImpl;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemUpdateDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.service.SettingService;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.WorkSpaceCodeSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class ClientSystemServiceImpl implements ClientSystemService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ClientSystemRepository clientSystemRepository;
    private final SequenceGenerator sequenceGenerator;
    private final SettingService settingService;

    public ClientSystemServiceImpl(ClientSystemRepository clientSystemRepository,
                                   SettingService settingService,
                                   @WorkSpaceCodeSequence SequenceGenerator sequenceGenerator) {
        this.clientSystemRepository = clientSystemRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.settingService = settingService;
    }

    @Transactional
    @Override
    public ClientSystemPojo createClientSystem(ClientSystemDto dto) {
        if(!isValidKey(dto.getClientKey())){
            throw  new IllegalArgumentException(" Client Key is Invalid");
        }

        ClientSystem clientSystem = new ClientSystem();
        clientSystem.setCode(sequenceGenerator.getNext());
        clientSystem.setDisplayName(dto.getDisplayName());
        clientSystem.setCreatedAt(LocalDateTime.now());
        clientSystem.setLastUpdatedAt(LocalDateTime.now());
        clientSystem.setWorkSpaceAccountType(dto.getWorkSpaceAccountType());
        clientSystem.setAccessMode(dto.getAccessMode());
        clientSystem.setSystemType(dto.getSystemType());
        clientSystem.setStatus(GenericStatusConstant.ACTIVE);

        ClientSystem created = clientSystemRepository.save(clientSystem);

        return new ClientSystemPojo(created);
    }
    @Transactional
    @Override
    public ClientSystemPojo updateClientSystem(Long clientSystemId, ClientSystemUpdateDto dto) {
        if(!isValidKey(dto.getClientKey())){
            throw  new IllegalArgumentException(" Client Key is Invalid");
        }

        ClientSystem clientSystem = clientSystemRepository.findById(clientSystemId)
                .orElseThrow(() -> new IllegalArgumentException("Client System not found!"));

        if(dto.getDisplayName() != null){
            clientSystem.setDisplayName(dto.getDisplayName());
        }
        if(dto.getAccessMode() != null){
            clientSystem.setAccessMode(dto.getAccessMode());
        }
        if(dto.getWorkSpaceAccountType() != null){
            clientSystem.setWorkSpaceAccountType(dto.getWorkSpaceAccountType());
        }
        if(dto.getSystemType() != null){
            clientSystem.setSystemType(dto.getSystemType());
        }

        if(dto.getStatus() != null){
            clientSystem.setStatus(dto.getStatus());

            if(dto.getStatus() == GenericStatusConstant.INACTIVE){
                clientSystem.setDateDeactivated(new Date());
            }
        }
        clientSystem.setLastUpdatedAt(LocalDateTime.now());

        return new ClientSystemPojo(clientSystemRepository.save(clientSystem));
    }

    @Override
    public boolean isValidKey(String clientKey) {
        String valid_client_keys = settingService.getString("VALID_CLIENT_KEYS", "ABCXYZ123789,123789ABCXYZ");
        Set<String> validKeyList = new HashSet<>(Arrays.asList(valid_client_keys.split(",")));
        return validKeyList.contains(clientKey);
    }
}
