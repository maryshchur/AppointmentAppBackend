package org.example.app.constants;

public interface ValidationErrorConstants {
        String INVALID_FIRSTNAME="FirstName field is not valid";
        String INVALID_LASTNAME="LastName field is not valid";
        String INVALID_EMAIL="Email is invalid";
        String INVALID_PHONE="Phone number is invalid";
        String INVALID_PASSWORD="Password must contain at least eight characters and at least one character of "
                + " uppercase letter, lowercase letter, digit, special character";
        String EMPTY_LASTNAME="Last name may not be blank";
        String EMPTY_FIRSTNAME="First name may not be blank";
        String EMPTY_EMAIL="Email may not be blank";
        String EMPTY_PHONE="Phone may not be blank";
        String EMPTY_PASSWORD="Password may not be blank";
        String USER_WITH_EMAIL_EXISTS ="Account with this email already exists ";

    }
