package com.richotaru.authenticationapi.serviceImpl;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class ClientSystemServiceImpl implements UserDetailsService {
    @Autowired
    private ClientSystemRepository clientSystemRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientSystem user = clientSystemRepository.findClientSystemByClientName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user.getClientName(), user.getClientKey(), new ArrayList<>());
    }
}
