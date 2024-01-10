package com.employee.ap.service;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.employee.ap.entity.Employee;
import com.employee.ap.response.Response;


public interface EmployeeService  {

 public	List<Employee> getAllEmployee(Employee emp);
 public Employee saveEmployee(Employee emp);
 public Employee  getById(int id) ;
 public void deleteEmployee(int id);
 public Employee updateEmployee(Employee emp);
	
	
	
 
 
}
