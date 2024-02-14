package com.rodrigo.helpdesk.errorhandling;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rodrigo.helpdesk.exceptions.DataIntegrityViolationException;
import com.rodrigo.helpdesk.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<StandardError> handleObjectNotFoundException(ObjectNotFoundException ex, HttpServletRequest request) {
    StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
        "NOT_FOUND", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); //404
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
    ValidationError errors = new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(),
        "UNPROCESSABLE_ENTITY", "Erro na validação dos campos", request.getRequestURI());

    for (FieldError e : ex.getBindingResult().getFieldErrors())
      errors.addError(e.getField(), e.getDefaultMessage());

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors); //422
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<StandardError> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
    StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.CONFLICT.value(),
        "CONFLICT", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);  //409
  }

}