package com.mrrobot.overflow.common.exception;

public class AlreadyExitsException extends Exception{
    private String code;
    private String message;

    public AlreadyExitsException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
