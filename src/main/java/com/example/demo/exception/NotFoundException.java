package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Resource not found. There is no entity of that type with that ID.")
public class NotFoundException extends RuntimeException {
}
