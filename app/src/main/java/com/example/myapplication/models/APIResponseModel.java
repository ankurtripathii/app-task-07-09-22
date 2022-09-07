
package com.example.myapplication.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponseModel {
    private Boolean error;
    private Integer code;
    private String message;
    private List<MyChildItems> response = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MyChildItems> getResponse() {
        return response;
    }

    public void setResponse(List<MyChildItems> response) {
        this.response = response;
    }


}


