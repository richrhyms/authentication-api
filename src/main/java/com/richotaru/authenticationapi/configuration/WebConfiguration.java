package com.richotaru.authenticationapi.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.richotaru.authenticationapi.configuration.interceptors.AccessConstraintHandlerInterceptor;
import com.richotaru.authenticationapi.configuration.interceptors.RequestPrincipalHandlerInterceptor;
import com.richotaru.authenticationapi.domain.enums.TimeFormatConstants;
import com.richotaru.authenticationapi.domain.model.RequestPrincipal;
import com.richotaru.authenticationapi.service.WorkSpaceUserService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.validation.ConstraintValidatorFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */
@Configuration
//@ComponentScan({
//        "com.richotaru.authenticationapi",
//})
public class WebConfiguration implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    private final ConstraintValidatorFactory constraintValidatorFactory;
    private final JwtUtils jwtUtils;
    private final WorkSpaceUserService workSpaceUserService;

    public WebConfiguration(ApplicationContext applicationContext,
                            ConstraintValidatorFactory constraintValidatorFactory,
                            JwtUtils jwtUtils, WorkSpaceUserService workSpaceUserService) {
        this.applicationContext = applicationContext;
        this.constraintValidatorFactory = constraintValidatorFactory;

        this.jwtUtils = jwtUtils;
        this.workSpaceUserService = workSpaceUserService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestPrincipalHandlerInterceptor(applicationContext,jwtUtils, workSpaceUserService));
        registry.addInterceptor(new AccessConstraintHandlerInterceptor(applicationContext));
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public FactoryBean<RequestPrincipal> requestPrincipal() {
        return RequestPrincipalHandlerInterceptor.requestPrincipal();
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter());
    }

    @Bean
    @Override
    public LocalValidatorFactoryBean getValidator() {
        final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean
                .setConstraintValidatorFactory(constraintValidatorFactory);
        return localValidatorFactoryBean;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(new StdSerializer<JavassistLazyInitializer>(JavassistLazyInitializer.class) {

            @Override
            public void serialize(JavassistLazyInitializer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeObject(Collections.singletonMap("id", value.getIdentifier()));
                gen.writeNull();
            }
        });
        simpleModule.addSerializer(new StdSerializer<HibernateProxy>(HibernateProxy.class) {

            @Override
            public void serialize(HibernateProxy value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeObject(Collections.singletonMap("id", value.getHibernateLazyInitializer().getIdentifier()));
                gen.writeNull();
            }
        });
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat(TimeFormatConstants.DEFAULT_DATE_TIME_FORMAT));
        objectMapper.registerModule(simpleModule);

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());

        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper());
        return jsonConverter;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
}
