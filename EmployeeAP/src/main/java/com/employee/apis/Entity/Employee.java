package com.employee.apis.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Employee_Details")
public class Employee {
	
	@Id
	private int id;
	
	@Column(name="Emp_Name", nullable=false)
	private String name;
	
	@Column(name="Emp_Email",nullable=false)
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Employee(int id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", email=" + email + "]";
	}

}
