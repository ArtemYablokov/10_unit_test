package com.yabloko.models.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;


// response JSON  object

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDtoNew {
    private boolean success;
    @JsonAlias("error-codes") // тк java не понимает названия переменных через дефис
    private Set<String> errorCodes;

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }
    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
