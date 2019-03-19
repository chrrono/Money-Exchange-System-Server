package com.money.exchange.exceptions;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler
		extends ResponseEntityExceptionHandler {
	
	
	@ExceptionHandler({TransactionBadParametersException.class, InappropriatePathValueException.class,ObjectNotExistException.class})
	  public final ResponseEntity<ErrorInfo> handleUserNotFoundException(TransactionBadParametersException ex, WebRequest request) {
	    ErrorInfo errorResponse = new ErrorInfo(LocalDateTime.now(), ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	  }

	@ExceptionHandler(Exception.class)
	  public final ResponseEntity<ErrorInfo> handleAllException(Exception ex, WebRequest request) {
	    ErrorInfo errorResponse = new ErrorInfo(LocalDateTime.now(), ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	  }
}
