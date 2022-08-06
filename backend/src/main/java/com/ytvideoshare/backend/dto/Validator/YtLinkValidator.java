package com.ytvideoshare.backend.dto.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class YtLinkValidator implements ConstraintValidator<YtLinkValidation, String> {
    @Override
    public void initialize(YtLinkValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(String linkURL, ConstraintValidatorContext context) {
        return linkURL.matches("http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?\u200C\u200B[\\w\\?\u200C\u200B=]*)?");
    }
}
