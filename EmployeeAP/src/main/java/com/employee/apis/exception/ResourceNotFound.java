package com.employee.apis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFound  extends RuntimeException{
 private String resourcename;
 private String fieldName;
 private Object fieldValue;
 
public ResourceNotFound(String resourcename, String fieldName, Object fieldValue) {
	super(String.format("%s not found with %s: '%s'",resourcename,fieldName,fieldValue));
	this.resourcename = resourcename;
	this.fieldName = fieldName;
	this.fieldValue = fieldValue;
}
public String getResourcename() {
	return resourcename;
}
public void setResourcename(String resourcename) {
	this.resourcename = resourcename;
}
public String getFieldName() {
	return fieldName;
}
public void setFieldName(String fieldName) {
	this.fieldName = fieldName;
}
public Object getFieldValue() {
	return fieldValue;
}
public void setFieldValue(Object fieldValue) {
	this.fieldValue = fieldValue;
}
 
}
