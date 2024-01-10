package com.employee.ap.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.employee.ap.entity.Employee;

@EnableJpaRepositories
public interface EmployeeDao  extends JpaRepository<Employee, Integer>{

	// saveAll(Optional<Employee> emp);

}
