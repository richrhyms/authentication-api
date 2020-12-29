package com.richotaru.authenticationapi.configuration.filters;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.model.RequestPrincipal;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemAuthDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientSystemJwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtUtils jwtUtils;
    private final ClientSystemService clientSystemService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ClientSystemJwtFilter(JwtUtils jwtUtils, ClientSystemService clientSystemService) {
        this.jwtUtils = jwtUtils;
        this.clientSystemService = clientSystemService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        logger.info("Authenticating Client System...");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        logger.info("REQUEST URL = " + httpServletRequest.getRequestURL());
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String clientName = jwtUtils.extractUsername(jwt);
            RequestPrincipal requestPrincipal = new RequestPrincipal(clientSystemService.getAuthenticatedClient(clientName));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(requestPrincipal, jwt,
                    resolveRoles(requestPrincipal.getClient().getPortalAccount())));
        }
        logger.info("Finished Authenticating Client System...");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private List<SimpleGrantedAuthority> resolveRoles(PortalAccount account) {
        return Arrays.stream(account.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
