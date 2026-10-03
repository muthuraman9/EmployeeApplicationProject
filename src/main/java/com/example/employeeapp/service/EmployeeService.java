package com.example.employeeapp.service;

import com.example.employeeapp.model.Employee;
import com.example.employeeapp.repo.EmployeeRepo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepository;

    public EmployeeService(EmployeeRepo employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

  
    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + id));
    }

    
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

   
    public List<Employee> getPossibleManagers(Integer excludeEmployeeId) {
        List<Employee> all = employeeRepository.findAll();
        if (excludeEmployeeId == null) {
            return all;
        }
        return all.stream()
                .filter(e -> !e.getId().equals(excludeEmployeeId))
                .toList();
    }
}