package com.employee.apis.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.apis.Entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	public interface EmployeeService {
	    List<Employee> getAllEmployees();
	    Employee getEmployeeById(Long id);
	    Employee addEmployee(Employee employee);
	    Employee updateEmployee(Long id, Employee employee);
	    void deleteEmployee(Long id);
	}
}
