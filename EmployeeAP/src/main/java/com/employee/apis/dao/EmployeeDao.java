package com.employee.apis.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.apis.Entity.Employee;

public interface EmployeeDao extends JpaRepository<Employee, Integer> {

}
