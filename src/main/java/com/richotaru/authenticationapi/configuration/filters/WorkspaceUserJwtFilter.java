package com.richotaru.authenticationapi.configuration.filters;


import com.richotaru.authenticationapi.domain.model.RequestPrincipal;
import com.richotaru.authenticationapi.service.WorkSpaceUserService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


public class WorkspaceUserJwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String WORK_SPACE_CODE_HEADER = "WorkspaceCode";
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private WorkSpaceUserService workSpaceUserService;
    @Autowired
    private ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.info("Authenticating Work Space User...");
        logger.info("REQUEST URL = " + request.getRequestURL());
        String jwt = resolveToken(request);
        String currentWorkSpaceCode = currentWorkSpaceCode(request);
        if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt) && StringUtils.hasText(currentWorkSpaceCode) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtUtils.extractUsername(jwt);
            logger.info("USER NAME = " + username);
            logger.info("CURRENT WORK SPACE CODE " + currentWorkSpaceCode);
            RequestPrincipal requestPrincipal = new RequestPrincipal(workSpaceUserService.getAuthenticatedUser(username, currentWorkSpaceCode));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(requestPrincipal, jwt,
                    new ArrayList<>()));
            applicationContext.getAutowireCapableBeanFactory().autowireBean(requestPrincipal);
            RequestContextHolder.currentRequestAttributes().setAttribute(RequestPrincipal.class.getName(),
                    requestPrincipal,
                    RequestAttributes.SCOPE_REQUEST);
        }
        logger.info("Finished Authenticating User...");
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    private String currentWorkSpaceCode(HttpServletRequest request) {
        String workspaceCode = request.getHeader(WORK_SPACE_CODE_HEADER);
        if (StringUtils.hasText(workspaceCode)) {
            return workspaceCode;
        }
        return null;
    }
//    private List<SimpleGrantedAuthority> resolveRoles(PortalAccountPojo account) {
//        return Arrays.stream(account.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }
}
