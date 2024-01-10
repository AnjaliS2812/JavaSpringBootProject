package com.employee.apis.impl;

import org.springframework.http.ResponseEntity;

import com.employee.apis.Entity.Employee;

public interface EmployeeService {
	public Employee saveEmployee(Employee employee);

}
