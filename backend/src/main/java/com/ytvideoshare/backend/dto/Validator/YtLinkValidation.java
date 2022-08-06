package com.ytvideoshare.backend.dto.Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = YtLinkValidator.class)
@Documented
public @interface YtLinkValidation {
    String message() default "Enter a valid YouTube Video URL.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}