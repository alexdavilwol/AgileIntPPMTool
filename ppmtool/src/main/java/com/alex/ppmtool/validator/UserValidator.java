package com.alex.ppmtool.validator;

import com.alex.ppmtool.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClazz) {
        return User.class.equals(aClazz);
    }

    @Override
    public void validate(Object object, Errors errors) {

        User user = (User) object;

        if(user.getPassword().length()<6){
            //must equal the name of the password instance variable in user domain
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }

        //confirmPassword
        if(!user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }

    }
}
