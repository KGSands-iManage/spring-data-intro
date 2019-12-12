package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "You have entered a finishing date that is before the start date")
public class IllegalDateException extends RuntimeException {
}