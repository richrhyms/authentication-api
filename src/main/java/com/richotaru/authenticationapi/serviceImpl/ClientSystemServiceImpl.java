package com.richotaru.authenticationapi.serviceImpl;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemAuthDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class ClientSystemServiceImpl implements ClientSystemService {
    @Autowired
    private ClientSystemRepository clientSystemRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    @Override
    public ClientSystemAuthPojo authenticateClient(ClientSystemAuthDto dto) throws UsernameNotFoundException {
        ClientSystem user = clientSystemRepository.findClientSystemByClientName(dto.getUsername()).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));

        String jwtToken = user.getJwtToken();

        if(!jwtUtils.validateToken(user.getJwtToken(), user.getClientName())) {
            jwtToken = jwtUtils.generateToken(user.getClientName(), true);
            user.setJwtToken(jwtToken);
            clientSystemRepository.save(user);
        }

        Date expirationDate = jwtUtils.extractExpiration(jwtToken);

        ClientSystemAuthPojo response = new ClientSystemAuthPojo();
        response.setJwtToken(jwtToken);
        response.setExpirationDate(expirationDate);

        return response;
    }
    @Transactional
    @Override
    public ClientSystemPojo getAuthenticatedClient(String username) throws UsernameNotFoundException {
        ClientSystem user = clientSystemRepository.findClientSystemByClientName(username).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
        ClientSystemPojo pojo = new ClientSystemPojo(user);
        pojo.setExpirationDate(new Timestamp(jwtUtils.extractExpiration(pojo.getJwtToken()).getTime()));
        return pojo;
    }
}
