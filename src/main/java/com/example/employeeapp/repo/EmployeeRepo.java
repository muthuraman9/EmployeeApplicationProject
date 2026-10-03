package com.example.employeeapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employeeapp.model.Employee;

@Repository
public interface EmployeeRepo  extends JpaRepository<Employee, Integer>{

}
