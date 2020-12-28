package com.richotaru.authenticationapi.domain.annotations;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Neme Iloeje niloeje@byteworks.com.ng
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Public {
}
