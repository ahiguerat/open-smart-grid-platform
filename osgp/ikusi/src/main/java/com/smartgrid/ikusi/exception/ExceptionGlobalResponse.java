package com.smartgrid.ikusi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.smartgrid.ikusi.model.ResponseError;
import com.smartgrid.ikusi.model.Time;

@ControllerAdvice
public class ExceptionGlobalResponse {
	ResponseError result;
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseError> runtimeException(RuntimeException e) {
		result = new ResponseError(Time.getTime(), "Exception: " + e.getMessage(), 500, "Error", e.getLocalizedMessage());
		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseError> exception(Exception e) {		
		result = new ResponseError(Time.getTime(), "Exception: " + e.getMessage(), 500, "Error", e.getLocalizedMessage());
		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
