package com.kglory.tms.web.model.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.kglory.tms.web.model.dto.LoginFormDto;

@Component
public class LoginFormDtoValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return LoginFormDto.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		LoginFormDto dto = (LoginFormDto) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required.password");
	}
	
}
