package com.money.exchange.exceptions;

import java.time.LocalDateTime;
import java.util.Date;

public class ErrorInfo {
	private LocalDateTime time;
	private String message;
	private String details;

	public ErrorInfo(LocalDateTime time, String message, String details) {
	    super();
	    this.time = time;
	    this.message = message;
	    this.details = details;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
}
