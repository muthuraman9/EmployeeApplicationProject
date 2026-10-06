package com.example.employeeapp.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeeapp.model.Employee;
import com.example.employeeapp.service.EmployeeService;
import com.example.employeeapp.exception.AgeNotGreatException;
import com.example.employeeapp.model.Department;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
	private int val2;

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
                               @RequestParam("photoFile") MultipartFile photoFile,
                               Model model) throws AgeNotGreatException {

        
        if (result.hasErrors()) {
            model.addAttribute("departments", Department.values());
            
            Integer idToExclude = employee.getId() != null ? employee.getId() : null;
            model.addAttribute("managers", employeeService.getPossibleManagers(idToExclude));
            model.addAttribute("pageTitle",
                    employee.getId() == null ? "Add Employee" : "Edit Employee");
            return "employees/form";
        }
       if(!photoFile.isEmpty()) {
    	   try {
    	    	  String savedPath=saveUploadedFile(photoFile);
    	    	  employee.setPhotoPath(savedPath);
    	      }catch (Exception e) {
				// TODO: handle exception
			}
    	   
    	   
       }
      
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }
    
    @Value("${app.upload.dir}")
    private String uploadDir;

    private String saveUploadedFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);              // "uploads/"
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String original = file.getOriginalFilename();
        String extension = "";
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf("."));
        }
        String uniqueName = UUID.randomUUID() + extension;

        Files.copy(file.getInputStream(),
                   uploadPath.resolve(uniqueName),
                   StandardCopyOption.REPLACE_EXISTING);

        return uniqueName;   // ← return JUST the filename, not "uploads/" + name
    }

	
}