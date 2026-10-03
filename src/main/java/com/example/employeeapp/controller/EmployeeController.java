package com.example.employeeapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.employeeapp.model.Employee;
import com.example.employeeapp.service.EmployeeService;


import com.example.employeeapp.model.Department;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees/list";
    }

    
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", Department.values());
        model.addAttribute("managers", employeeService.getPossibleManagers(null));
        model.addAttribute("pageTitle", "Add Employee");
        return "employees/form";
    }

   
    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employees/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("departments", Department.values());
        model.addAttribute("managers", employeeService.getPossibleManagers(id));
        model.addAttribute("pageTitle", "Edit Employee");
        return "employees/form";
    }

    
    @PostMapping
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result,
                               Model model) {

        // If validation failed, re-render the form with error messages.
        if (result.hasErrors()) {
            model.addAttribute("departments", Department.values());
            // Exclude self when editing
            Integer idToExclude = employee.getId() != null ? employee.getId() : null;
            model.addAttribute("managers", employeeService.getPossibleManagers(idToExclude));
            model.addAttribute("pageTitle",
                    employee.getId() == null ? "Add Employee" : "Edit Employee");
            return "employees/form";
        }

        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }
}