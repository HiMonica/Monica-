package com.monica.seckilldemo.vo;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.monica.seckilldemo.utils.ValidatorUtils;
import com.monica.seckilldemo.validator.IsMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required){
            return ValidatorUtils.isMobile(value);
        }else {
            if (StringUtils.isEmpty(value)){
                return true;
            }else {
                return ValidatorUtils.isMobile(value);
            }
        }
    }
}
