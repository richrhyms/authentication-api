///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.richotaru.authenticationapi.configuration.interceptors;
//
//import com.richotaru.authenticationapi.domain.annotations.Public;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springdoc.api.OpenApiResource;
//import org.springdoc.ui.SwaggerWelcome;
//import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
//import org.springframework.context.ApplicationContext;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class AccessConstraintHandlerInterceptor extends HandlerInterceptorAdapter {
//
//    final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final ApplicationContext applicationContext;
//
//    public AccessConstraintHandlerInterceptor(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        try {
//
//            if (handlerMethod.hasMethodAnnotation(Public.class) || handlerMethod.getMethod().getDeclaringClass().isAnnotationPresent(Public.class)
//                    || (BasicErrorController.class.isAssignableFrom(handlerMethod.getBeanType()))
//                    || (OpenApiResource.class.isAssignableFrom(handlerMethod.getBeanType()))
//                    || (SwaggerWelcome.class.isAssignableFrom(handlerMethod.getBeanType()))) {
//                    return true;
//                }else {
//                    response.setStatus(401);
//                    response.getWriter().append("Unauthorized");
//                    return false;
//                }
//        } catch (IllegalStateException e) {
//            logger.error(e.getMessage(), e);
//        }
//        return false;
//    }
//
//
//}
