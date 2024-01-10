package com.employee.ap.expection;

import java.util.HashMap;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.employee.ap.response.Response;

@RestControllerAdvice
public class Exceptionsv{
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?>handleMethodArgsNotValidException(MethodArgumentNotValidException ex)
	{
		Response<?> res=new Response<>();
	
		res.setMessage("your data can't be updated");
		res.setStatus("000");
		
	Map<String,String> resMap=new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error)->{
	    	String fieldName=((FieldError)error).getField();
	    	String message=error.getDefaultMessage();
	    	resMap.put(fieldName, message);
	    });
	 res.setResM(resMap);
	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String>handleIntegrityEcxeption()
	{
		String msg="Data Already present";
		return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST);
	}
}


	


