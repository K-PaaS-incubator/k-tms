package com.kglory.tms.web.model.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SearchDtoValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDateInput", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDateInput", "field.required");
	}
	
}
