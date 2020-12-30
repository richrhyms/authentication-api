package com.richotaru.authenticationapi.configuration.filters;


import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.model.RequestPrincipal;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Named;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ClientSystemJwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ClientSystemService clientSystemService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Authenticating Client System...");
        logger.info("REQUEST URL = " + request.getRequestURL());
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String clientName = jwtUtils.extractUsername(jwt);
            logger.info("USER NAME = " + clientName);
            RequestPrincipal requestPrincipal = new RequestPrincipal(clientSystemService.getAuthenticatedClient(clientName));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(requestPrincipal, jwt,
                    resolveRoles(requestPrincipal.getPortalAccount())));
        }
        logger.info("Finished Authenticating Client System...");
        filterChain.doFilter(request, response);
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
