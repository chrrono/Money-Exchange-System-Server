package com.money.exchange.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InappropriatePathValueException extends RuntimeException {
	public InappropriatePathValueException(String exception) {
		super(exception);
	}
}
