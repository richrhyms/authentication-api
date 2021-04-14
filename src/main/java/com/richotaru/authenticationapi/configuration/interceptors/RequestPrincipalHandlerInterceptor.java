/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.richotaru.authenticationapi.configuration.interceptors;

import com.richotaru.authenticationapi.domain.model.RequestPrincipal;
import com.richotaru.authenticationapi.service.WorkSpaceService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.richotaru.authenticationapi.configuration.filters.ClientSystemJwtFilter.AUTHORIZATION_HEADER;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */
public class RequestPrincipalHandlerInterceptor extends HandlerInterceptorAdapter {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ApplicationContext applicationContext;
    private final JwtUtils jwtUtils;
    private final WorkSpaceService workSpaceService;

    public RequestPrincipalHandlerInterceptor(ApplicationContext applicationContext,
                                              JwtUtils jwtUtils,
                                              WorkSpaceService workSpaceService) {
        this.applicationContext = applicationContext;
        this.jwtUtils = jwtUtils;
        this.workSpaceService = workSpaceService;
    }

    public static FactoryBean<RequestPrincipal> requestPrincipal() {
        return new FactoryBean<RequestPrincipal>() {

            @Override
            public RequestPrincipal getObject() {
                if (RequestContextHolder.getRequestAttributes() == null) {
                    return null;
                }
                return (RequestPrincipal) RequestContextHolder.currentRequestAttributes().getAttribute(RequestPrincipal.class.getName(),
                        RequestAttributes.SCOPE_REQUEST);
            }

            @Override
            public Class<?> getObjectType() {
                return RequestPrincipal.class;
            }

            @Override
            public boolean isSingleton() {
                return false;
            }
        };
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        logger.info("request.getServletPath(): {}",request.getServletPath());
        try {
            String authHeader = StringUtils.defaultString(request.getHeader(HttpHeaders.AUTHORIZATION), "");
            if (StringUtils.isNotBlank(authHeader)) {
                RequestPrincipal user = getUserFromRequest(request);
                RequestAttributes currentRequestAttributes = RequestContextHolder.currentRequestAttributes();

                String ipAddress = request.getRemoteAddr();
                if (request.getRemoteAddr().equals("127.0.0.1") || request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
                    ipAddress = StringUtils.defaultIfBlank(request.getHeader("X-FORWARDED-FOR"),
                            request.getRemoteAddr());
                }

                if(user !=null){
                    user.setIpAddress(ipAddress);
                    applicationContext.getAutowireCapableBeanFactory().autowireBean(user);
                    currentRequestAttributes.setAttribute(RequestPrincipal.class.getName(),
                            user,
                            RequestAttributes.SCOPE_REQUEST);
                }
            }

            try {
                Cookie cookie = new Cookie(RequestPrincipal.AUTH_TOKEN_NAME, authHeader.replace("Bearer ", ""));
                cookie.setMaxAge(60 * 30);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                response.addCookie(cookie);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    private RequestPrincipal getUserFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            String clientName = jwtUtils.extractUsername(jwt);
            return new RequestPrincipal(workSpaceService.getAuthenticatedAccount(clientName));
        }
        return null;
    }

}
