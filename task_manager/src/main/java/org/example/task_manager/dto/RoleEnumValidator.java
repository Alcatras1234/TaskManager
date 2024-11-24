package org.example.task_manager.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.task_manager.enums.RoleEnum;

public class RoleEnumValidator implements ConstraintValidator<ValidRoleEnum, String> {
    @Override
    public boolean isValid(String roleEnum, ConstraintValidatorContext constraintValidatorContext) {
        if (roleEnum == null) {
            return false;
        }
        try {
            RoleEnum.valueOf(roleEnum);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
