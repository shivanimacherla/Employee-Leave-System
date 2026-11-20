package com.practice.leavesystem.service;

import com.practice.leavesystem.dto.EmployeeRequestDTO;
import com.practice.leavesystem.entity.Department;
import com.practice.leavesystem.entity.Employee;
import com.practice.leavesystem.exception.ResourceNotFoundException;
import com.practice.leavesystem.repository.DepartmentRepository;
import com.practice.leavesystem.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Employee createEmployee(EmployeeRequestDTO dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        Employee employee = new Employee(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getDateOfJoining(),
                department
        );
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee existing = getEmployeeById(id);
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setDateOfJoining(dto.getDateOfJoining());
        existing.setDepartment(department);

        return employeeRepository.save(existing);
    }

    public void deleteEmployee(Long id) {
        Employee existing = getEmployeeById(id);
        employeeRepository.delete(existing);
    }
}
