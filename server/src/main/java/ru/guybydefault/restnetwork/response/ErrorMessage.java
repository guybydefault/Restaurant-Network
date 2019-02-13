package ru.guybydefault.restnetwork.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class ErrorMessage extends RestControllerResponseMessage{

    public ErrorMessage(int statusCode, String message) {
        super(statusCode, message);
    }
}
