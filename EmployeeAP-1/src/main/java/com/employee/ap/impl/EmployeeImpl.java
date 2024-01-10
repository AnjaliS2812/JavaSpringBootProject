package com.employee.ap.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.employee.ap.Dao.EmployeeDao;
import com.employee.ap.entity.Employee;
import com.employee.ap.expection.Exceptionsv;
import com.employee.ap.response.Response;
import com.employee.ap.service.EmployeeService;
@Service
public class EmployeeImpl implements EmployeeService {
	
	@Autowired
	private EmployeeDao empDao;

	@Override
	public List<Employee> getAllEmployee(Employee emp) {
		
		return empDao.findAll();
	}

	@Override
	public Employee saveEmployee(Employee emp) {
		
		return empDao.save(emp);
	}

	@Override
	public Employee getById(int id) {
		
		return empDao.getById(id);
	}

	@Override
	public void deleteEmployee(int id) {
		
		empDao.deleteById(id);
	}

	
	public Employee updateEmployee(Employee emp) {
		
		return empDao.save(emp);
	}
}

	