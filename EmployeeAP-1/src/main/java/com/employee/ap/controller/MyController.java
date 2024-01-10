package com.employee.ap.controller;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.employee.ap.entity.Employee;
import com.employee.ap.expection.Exceptionsv;
import com.employee.ap.response.Response;
import com.employee.ap.service.EmployeeService;

import jakarta.validation.Valid;



@RestController
public class MyController {
	
	@Autowired
	private EmployeeService empService;
	
	@GetMapping("/get")
	public List<Employee> getAllEmployee(Employee emp) {
		return this.empService.getAllEmployee(emp);
	
	}	
	@PostMapping("/emp")
	public Employee saveEmployee (@RequestBody @Valid Employee  emp)
	{
		return this.empService.saveEmployee(emp);
		
	}
	

	@GetMapping("/get/{id}")
	public Employee getById(@Valid @PathVariable("id") int id)
	{
	 return this.empService.getById(id)	;
	}
	
	@DeleteMapping("/del/{id}")
	public String deleteEmployee(@PathVariable int id)
	{
		return "Data deleted sucessufully ";
	}
	
	
	@PutMapping("/upd/{id}")
	public Employee updateEmployee( @PathVariable int id,@Valid @RequestBody Employee emp  )
	{
		emp.setId(id);
		return this.empService.updateEmployee(emp);
	}

	
}
